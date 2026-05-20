import React, { useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import { authService } from "../../services/api";
import { TipoIdentificacion, TipoIdentificacionLabels } from "../../types";

const CreateUserPage: React.FC = () => {
  const [form, setForm] = useState({
    tipoIdentificacion: TipoIdentificacion.CedulaDeCiudadania,
    numeroIdentificacion: "",
    email: "",
    contraseña: "",
    confirmarContraseña: "",
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

    if (!form.numeroIdentificacion.trim() || !form.email.trim() || !form.contraseña) {
      setError("Complete todos los campos obligatorios.");
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
      await authService.crearUsuario({
        tipoIdentificacion: form.tipoIdentificacion,
        numeroIdentificacion: form.numeroIdentificacion.trim(),
        email: form.email.trim(),
        contraseña: form.contraseña,
      });
      setSuccess("Usuario creado correctamente.");
      setForm({
        tipoIdentificacion: TipoIdentificacion.CedulaDeCiudadania,
        numeroIdentificacion: "",
        email: "",
        contraseña: "",
        confirmarContraseña: "",
      });
    } catch {
      setError("No fue posible crear el usuario. Verifique los datos o permisos.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppLayout title="Crear usuario">
      <div className="page-header">
        <h2>Crear usuario</h2>
        <p>Registro de usuarios autorizado para Talento Humano.</p>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form className="form-section" onSubmit={handleSubmit}>
        <div className="form-section-header">
          <div className="section-icon">US</div>
          <h3>Datos del usuario</h3>
        </div>

        <div className="form-section-body">
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label">Tipo de identificación <span className="required">*</span></label>
              <select name="tipoIdentificacion" className="form-select" value={form.tipoIdentificacion} onChange={handleChange}>
                {Object.entries(TipoIdentificacionLabels).map(([value, label]) => (
                  <option key={value} value={value}>{label}</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">Número de identificación <span className="required">*</span></label>
              <input name="numeroIdentificacion" className="form-input" value={form.numeroIdentificacion} onChange={handleChange} />
            </div>

            <div className="form-group span-2">
              <label className="form-label">Correo electrónico <span className="required">*</span></label>
              <input type="email" name="email" className="form-input" value={form.email} onChange={handleChange} />
            </div>

            <div className="form-group">
              <label className="form-label">Contraseña <span className="required">*</span></label>
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
              {loading ? "Creando..." : "Crear usuario"}
            </button>
          </div>
        </div>
      </form>
    </AppLayout>
  );
};

export default CreateUserPage;
