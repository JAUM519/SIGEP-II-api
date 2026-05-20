import React, { useState } from "react";
import { Link } from "react-router-dom";
import { authService } from "../../services/api";
import { TipoIdentificacion, TipoIdentificacionLabels } from "../../types";

const ForgotPasswordPage: React.FC = () => {
  const [form, setForm] = useState({
    tipoIdentificacion: TipoIdentificacion.CedulaDeCiudadania,
    numeroIdentificacion: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setError("");
    setSuccess("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.numeroIdentificacion.trim()) {
      setError("Ingrese el número de identificación.");
      return;
    }

    setLoading(true);
    try {
      const message = await authService.pedirEnlace(form);
      setSuccess(message || "Se ha enviado un enlace a su correo electrónico.");
    } catch {
      setError("No fue posible enviar el enlace. Verifique los datos e inténtelo nuevamente.");
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
          <h2>Recuperar contraseña</h2>
          <p className="login-sub">Ingrese sus datos para recibir el enlace de recuperación.</p>

          {error && <div className="alert alert-danger">{error}</div>}
          {success && <div className="alert alert-success">{success}</div>}

          <form className="login-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label">Tipo de documento</label>
              <select
                name="tipoIdentificacion"
                className="form-select"
                value={form.tipoIdentificacion}
                onChange={handleChange}
              >
                {Object.entries(TipoIdentificacionLabels).map(([value, label]) => (
                  <option key={value} value={value}>{label}</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">Número de identificación</label>
              <input
                type="text"
                name="numeroIdentificacion"
                className="form-input"
                placeholder="Ej: 1234567890"
                value={form.numeroIdentificacion}
                onChange={handleChange}
                autoComplete="username"
              />
            </div>

            <button type="submit" className="btn btn-primary w-full btn-lg" disabled={loading}>
              {loading ? <span className="spinner" /> : null}
              {loading ? "Enviando..." : "Enviar enlace"}
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

export default ForgotPasswordPage;
