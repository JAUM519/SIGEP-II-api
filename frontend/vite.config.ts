import { defineConfig, type Plugin } from 'vite'
import react from '@vitejs/plugin-react'

const SECURITY_HEADERS: Record<string, string> = {
  'Content-Security-Policy': [
    "default-src 'self'",
    "base-uri 'self'",
    "object-src 'none'",
    "frame-ancestors 'none'",
    "img-src 'self' data:",
    "font-src 'self' data:",
    "style-src 'self' 'unsafe-inline'",
    "script-src 'self' 'unsafe-inline'",
    "connect-src 'self' http://localhost:* http://127.0.0.1:* ws://localhost:* ws://127.0.0.1:*",
  ].join('; '),
  'X-Frame-Options': 'DENY',
  'X-Content-Type-Options': 'nosniff',
  'Referrer-Policy': 'strict-origin-when-cross-origin',
  'Permissions-Policy': 'camera=(), microphone=(), geolocation=(), payment=()',
  'Strict-Transport-Security': 'max-age=31536000; includeSubDomains',
}

function decodeRepeatedly(value: string): string {
  let decoded = value

  for (let i = 0; i < 3; i += 1) {
    try {
      const next = decodeURIComponent(decoded)
      if (next === decoded) break
      decoded = next
    } catch {
      break
    }
  }

  return decoded.toLowerCase()
}

function securityDevMiddleware(): Plugin {
  return {
    name: 'sigep-security-dev-middleware',
    configureServer(server) {
      server.middlewares.use((req, res, next) => {
        for (const [header, value] of Object.entries(SECURITY_HEADERS)) {
          res.setHeader(header, value)
        }

        const requestUrl = new URL(req.url ?? '/', 'http://localhost')
        const normalizedPath = decodeRepeatedly(requestUrl.pathname).replace(/\\/g, '/')
        const hasTraversalInPath = normalizedPath.includes('../') || normalizedPath.includes('..\\')

        const hasTraversalInQuery = [...requestUrl.searchParams.values()].some((rawValue) => {
          const value = decodeRepeatedly(rawValue).replace(/\\/g, '/')

          return (
            value.includes('../') ||
            value.includes('..\\') ||
            value.includes('%2e') ||
            value.includes('%2f') ||
            value.includes('%5c') ||
            value.includes('\u0000') ||
            value.startsWith('/etc/') ||
            value.match(/^\/[a-z0-9_.-]+/i)
          )
        })

        if (hasTraversalInPath || hasTraversalInQuery) {
          res.statusCode = 400
          res.end('Bad request')
          return
        }

        next()
      })
    },
  }
}

// https://vite.dev/config/
export default defineConfig(({ command }) => ({
  plugins: [react(), securityDevMiddleware()],
  server: {
    headers: SECURITY_HEADERS,
  },
  preview: {
    headers: SECURITY_HEADERS,
  },
  build: {
    sourcemap: false,
  },
  define: command === 'build' ? { 'process.env.NODE_ENV': '"production"' } : {},
}))
