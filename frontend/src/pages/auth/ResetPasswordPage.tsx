import React, { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { authService } from "../../services/api";

const ResetPasswordPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token") || "";

  const [form, setForm] = useState({ contraseña: "", confirmarContraseña: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setError("");
    setSuccess("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!token) {
      setError("El enlace de recuperación no es válido.");
      return;
    }
    if (form.contraseña.length < 6) {
      setError("La contraseña debe tener mínimo 6 caracteres.");
      return;
    }
    if (form.contraseña !== form.confirmarContraseña) {
      setError("Las contraseñas no coinciden.");
      return;
    }

    setLoading(true);
    try {
      const message = await authService.recuperarContrasena(token, { contraseña: form.contraseña });
      setSuccess(message || "Contraseña actualizada correctamente.");
      setForm({ contraseña: "", confirmarContraseña: "" });
    } catch {
      setError("No fue posible cambiar la contraseña. Solicita un nuevo enlace e inténtalo de nuevo.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-bg-pattern" />
      <div className="flag-bar" />

      <header className="login-header">
        <div className="escudo">
          <svg width="28" height="28" viewBox="0 0 28 28" fill="none">
            <circle cx="14" cy="14" r="12" stroke="rgba(255,255,255,.6)" strokeWidth="1.5" />
            <path d="M14 4 L18 10 L24 10 L19 15 L21 22 L14 18 L7 22 L9 15 L4 10 L10 10 Z" fill="rgba(255,255,255,.5)" />
          </svg>
        </div>
        <div className="gov-name">
          <h1>República de Colombia</h1>
          <p>Departamento Administrativo de la Función Pública</p>
        </div>
      </header>

      <div className="login-center">
        <div className="login-card animate-in">
          <h2>Nueva contraseña</h2>
          <p className="login-sub">Cree una nueva contraseña para acceder al sistema.</p>

          {error && <div className="alert alert-danger">{error}</div>}
          {success && <div className="alert alert-success">{success}</div>}

          <form className="login-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label">Nueva contraseña</label>
              <input
                type="password"
                name="contraseña"
                className="form-input"
                placeholder="Mínimo 6 caracteres"
                value={form.contraseña}
                onChange={handleChange}
                autoComplete="new-password"
              />
            </div>

            <div className="form-group">
              <label className="form-label">Confirmar contraseña</label>
              <input
                type="password"
                name="confirmarContraseña"
                className="form-input"
                placeholder="Repita la contraseña"
                value={form.confirmarContraseña}
                onChange={handleChange}
                autoComplete="new-password"
              />
            </div>

            <button type="submit" className="btn btn-primary w-full btn-lg" disabled={loading || !token}>
              {loading ? <span className="spinner" /> : null}
              {loading ? "Guardando..." : "Cambiar contraseña"}
            </button>
          </form>

          <div className="divider" />
          <p className="text-sm text-muted" style={{ textAlign: "center" }}>
            <Link to="/login">Volver al inicio de sesión</Link>
          </p>
        </div>
      </div>

      <footer className="login-footer">
        © 2025 Departamento Administrativo de la Función Pública · SIGEP II v2.0
      </footer>
    </div>
  );
};

export default ResetPasswordPage;
