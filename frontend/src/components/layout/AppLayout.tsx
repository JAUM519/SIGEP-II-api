import React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

interface NavItem {
  label: string;
  path: string;
  icon: React.ReactNode;
}

const IconDashboard = () => (
  <svg className="nav-icon" fill="none" stroke="currentColor" strokeWidth={1.8} viewBox="0 0 24 24">
    <rect x="3" y="3" width="7" height="7" rx="1" /><rect x="14" y="3" width="7" height="7" rx="1" />
    <rect x="3" y="14" width="7" height="7" rx="1" /><rect x="14" y="14" width="7" height="7" rx="1" />
  </svg>
);

const IconHojaVida = () => (
  <svg className="nav-icon" fill="none" stroke="currentColor" strokeWidth={1.8} viewBox="0 0 24 24">
    <path d="M9 12h6M9 16h6M17 21H7a2 2 0 01-2-2V5a2 2 0 012-2h5l5 5v11a2 2 0 01-2 2z" />
  </svg>
);

const IconEducacion = () => (
  <svg className="nav-icon" fill="none" stroke="currentColor" strokeWidth={1.8} viewBox="0 0 24 24">
    <path d="M12 14l9-5-9-5-9 5 9 5z" />
    <path d="M12 14l6.16-3.422A12 12 0 0122 21H2a12 12 0 013.84-10.422L12 14z" />
  </svg>
);

const IconExperiencia = () => (
  <svg className="nav-icon" fill="none" stroke="currentColor" strokeWidth={1.8} viewBox="0 0 24 24">
    <rect x="2" y="7" width="20" height="14" rx="2" />
    <path d="M16 7V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v2" />
  </svg>
);

const IconLogout = () => (
  <svg width="16" height="16" fill="none" stroke="currentColor" strokeWidth={2} viewBox="0 0 24 24">
    <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4M16 17l5-5-5-5M21 12H9" />
  </svg>
);

const navItems: NavItem[] = [
  { label: "Dashboard",         path: "/dashboard",                icon: <IconDashboard /> },
  { label: "Datos Personales",  path: "/curriculum/datos-personales", icon: <IconHojaVida /> },
  { label: "Educación",         path: "/curriculum/educacion",    icon: <IconEducacion /> },
  { label: "Experiencia Laboral", path: "/curriculum/experiencia", icon: <IconExperiencia /> },
];

const AppLayout: React.FC<{ children: React.ReactNode; title?: string }> = ({ children, title }) => {
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const initials = user?.numeroIdentificacion?.slice(-2).toUpperCase() ?? "US";

  return (
    <div className="app-shell">
      <div className="flag-bar" />
      <div className="app-body">
        {/* Sidebar */}
        <aside className="sidebar">
          <div className="sidebar-logo">
            <div className="logo-sigep">SIGEP II</div>
            <div className="logo-sub">Sistema de Gestión del Empleo Público</div>
          </div>

          <nav className="sidebar-nav">
            <div className="nav-section-label">Principal</div>
            {navItems.map((item) => (
              <Link
                key={item.path}
                to={item.path}
                className={`nav-item ${location.pathname === item.path ? "active" : ""}`}
              >
                {item.icon}
                {item.label}
              </Link>
            ))}
          </nav>

          <div className="sidebar-footer">
            <div className="sidebar-user">
              <div className="avatar">{initials}</div>
              <div className="user-info">
                <div className="user-name">{user?.numeroIdentificacion ?? "Usuario"}</div>
                <div className="user-role">Servidor Público</div>
              </div>
            </div>
          </div>
        </aside>

        {/* Main */}
        <div className="main-content">
          <header className="topbar">
            <span className="topbar-title">{title ?? "SIGEP II"}</span>
            <div className="topbar-right">
              <span className="text-sm text-muted">
                {user?.tipoIdentificacion} · {user?.numeroIdentificacion}
              </span>
              <button className="btn-logout" onClick={handleLogout}>
                <IconLogout /> Salir
              </button>
            </div>
          </header>

          <main className="page-content">
            {children}
          </main>
        </div>
      </div>
    </div>
  );
};

export default AppLayout;
