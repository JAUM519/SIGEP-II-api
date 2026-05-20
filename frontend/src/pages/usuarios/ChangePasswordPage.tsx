import React, { useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import { authService } from "../../services/api";

const ChangePasswordPage: React.FC = () => {
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
      await authService.cambiarContraseña({ contraseña: form.contraseña });
      setSuccess("Contraseña actualizada correctamente.");
      setForm({ contraseña: "", confirmarContraseña: "" });
    } catch {
      setError("No fue posible cambiar la contraseña.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppLayout title="Cambiar contraseña">
      <div className="page-header">
        <h2>Cambiar contraseña</h2>
        <p>Actualice la contraseña del usuario autenticado.</p>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form className="form-section" onSubmit={handleSubmit}>
        <div className="form-section-header">
          <div className="section-icon">PW</div>
          <h3>Nueva contraseña</h3>
        </div>

        <div className="form-section-body">
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label">Nueva contraseña <span className="required">*</span></label>
              <input type="password" name="contraseña" className="form-input" value={form.contraseña} onChange={handleChange} autoComplete="new-password" />
            </div>

            <div className="form-group">
              <label className="form-label">Confirmar contraseña <span className="required">*</span></label>
              <input type="password" name="confirmarContraseña" className="form-input" value={form.confirmarContraseña} onChange={handleChange} autoComplete="new-password" />
            </div>
          </div>

          <div className="flex justify-between mt-6">
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? <span className="spinner" /> : null}
              {loading ? "Guardando..." : "Guardar contraseña"}
            </button>
          </div>
        </div>
      </form>
    </AppLayout>
  );
};

export default ChangePasswordPage;
