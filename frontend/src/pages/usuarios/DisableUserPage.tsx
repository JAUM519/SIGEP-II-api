import React, { useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import { authService } from "../../services/api";
import { TipoIdentificacion, TipoIdentificacionLabels } from "../../types";

const DisableUserPage: React.FC = () => {
  const [form, setForm] = useState({
    tipoIdentificacion: TipoIdentificacion.CedulaDeCiudadania,
    numeroIdentificacion: "",
    fechaFin: "",
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

    if (!form.numeroIdentificacion.trim() || !form.fechaFin) {
      setError("Complete todos los campos obligatorios.");
      return;
    }

    const fechaFinISO = new Date(form.fechaFin).toISOString();

    setLoading(true);
    try {
      await authService.inhabilitarUsuario({
        tipoIdentificacion: form.tipoIdentificacion,
        numeroIdentificacion: form.numeroIdentificacion.trim(),
        fechaFin: fechaFinISO,
      });
      setSuccess("Usuario inhabilitado correctamente.");
      setForm({
        tipoIdentificacion: TipoIdentificacion.CedulaDeCiudadania,
        numeroIdentificacion: "",
        fechaFin: "",
      });
    } catch {
      setError("No fue posible inhabilitar el usuario. Verifique los datos o permisos.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppLayout title="Inhabilitar usuario">
      <div className="page-header">
        <h2>Inhabilitar usuario</h2>
        <p>Defina hasta qué fecha el usuario quedará inhabilitado.</p>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form className="form-section" onSubmit={handleSubmit}>
        <div className="form-section-header">
          <div className="section-icon">IN</div>
          <h3>Datos de inhabilitación</h3>
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

            <div className="form-group">
              <label className="form-label">Fecha final <span className="required">*</span></label>
              <input type="datetime-local" name="fechaFin" className="form-input" value={form.fechaFin} onChange={handleChange} />
            </div>
          </div>

          <div className="flex justify-between mt-6">
            <button type="submit" className="btn btn-danger" disabled={loading}>
              {loading ? <span className="spinner" /> : null}
              {loading ? "Procesando..." : "Inhabilitar usuario"}
            </button>
          </div>
        </div>
      </form>
    </AppLayout>
  );
};

export default DisableUserPage;
