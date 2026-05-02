import React, { useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import type { FormacionAcademica, Idioma } from "../../types";

const EducacionPage: React.FC = () => {
  const [tab, setTab] = useState(0);
  const [saved, setSaved] = useState(false);

  // ─── Formación Académica ──────────────────────────────────────────────────
  const emptyFormacion = (): FormacionAcademica => ({
    nivelAcademico: "", nivelFormacion: "", areaConocimiento: "",
    pais: "Colombia", institucionFormacionAcademica: "", programaAcademico: "",
    tituloObtenido: "", estadoEstudio: "Graduado", estudioConvalidado: false,
  });

  const [formaciones, setFormaciones] = useState<FormacionAcademica[]>([emptyFormacion()]);

  const updateFormacion = (i: number, field: keyof FormacionAcademica, value: string | boolean) => {
    setFormaciones(prev => prev.map((f, idx) => idx === i ? { ...f, [field]: value } : f));
  };

  // ─── Idiomas ──────────────────────────────────────────────────────────────
  const emptyIdioma = (): Idioma => ({ idioma: "", fechaCertificado: "", conversacion: "" });
  const [idiomas, setIdiomas] = useState<Idioma[]>([]);

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  const nivelesAcademicos = ["Bachillerato", "Técnico", "Tecnológico", "Profesional", "Especialización", "Maestría", "Doctorado", "Postdoctorado"];
  const estadosEstudio = ["En curso", "Graduado", "Sin graduar", "Convalidado"];
  const nivelesConversacion = ["Básico", "Intermedio", "Avanzado", "Nativo"];

  return (
    <AppLayout title="Educación">
      <div className="page-header animate-in">
        <h2>Educación</h2>
        <p>Registre su formación académica e idiomas.</p>
      </div>

      {saved && (
        <div className="alert alert-success animate-in" style={{ marginBottom: 20 }}>
          ✅ Información de educación guardada correctamente.
        </div>
      )}

      <div className="tabs animate-in">
        {["Formación Académica", "Idiomas"].map((t, i) => (
          <button key={t} className={`tab ${tab === i ? "active" : ""}`} onClick={() => setTab(i)}>{t}</button>
        ))}
      </div>

      <form onSubmit={handleSave}>
        {/* ── Formación Académica ── */}
        {tab === 0 && (
          <div className="animate-in">
            {formaciones.map((f, i) => (
              <div key={i} className="form-section" style={{ marginBottom: 16 }}>
                <div className="form-section-header" style={{ cursor: "default" }}>
                  <div className="section-icon">🎓</div>
                  <h3>Estudio #{i + 1}</h3>
                  {formaciones.length > 1 && (
                    <button type="button" className="btn btn-danger btn-sm"
                      style={{ marginLeft: "auto" }}
                      onClick={() => setFormaciones(prev => prev.filter((_, idx) => idx !== i))}>
                      Eliminar
                    </button>
                  )}
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Nivel académico <span className="required">*</span></label>
                      <select className="form-select" value={f.nivelAcademico}
                        onChange={e => updateFormacion(i, "nivelAcademico", e.target.value)}>
                        <option value="">Seleccione...</option>
                        {nivelesAcademicos.map(n => <option key={n} value={n}>{n}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Área de conocimiento</label>
                      <input className="form-input" value={f.areaConocimiento}
                        onChange={e => updateFormacion(i, "areaConocimiento", e.target.value)}
                        placeholder="Ej: Ingeniería de Sistemas" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">País</label>
                      <input className="form-input" value={f.pais}
                        onChange={e => updateFormacion(i, "pais", e.target.value)} />
                    </div>

                    <div className="form-group span-2">
                      <label className="form-label">Institución <span className="required">*</span></label>
                      <input className="form-input" value={f.institucionFormacionAcademica}
                        onChange={e => updateFormacion(i, "institucionFormacionAcademica", e.target.value)}
                        placeholder="Ej: Universidad Nacional de Colombia" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Estado del estudio</label>
                      <select className="form-select" value={f.estadoEstudio}
                        onChange={e => updateFormacion(i, "estadoEstudio", e.target.value)}>
                        {estadosEstudio.map(e => <option key={e} value={e}>{e}</option>)}
                      </select>
                    </div>

                    <div className="form-group span-2">
                      <label className="form-label">Programa académico <span className="required">*</span></label>
                      <input className="form-input" value={f.programaAcademico}
                        onChange={e => updateFormacion(i, "programaAcademico", e.target.value)}
                        placeholder="Ej: Ingeniería de Sistemas y Computación" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Título obtenido</label>
                      <input className="form-input" value={f.tituloObtenido}
                        onChange={e => updateFormacion(i, "tituloObtenido", e.target.value)}
                        placeholder="Ej: Ingeniero de Sistemas" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha de grado</label>
                      <input type="date" className="form-input" value={f.fechaGrado ?? ""}
                        onChange={e => updateFormacion(i, "fechaGrado", e.target.value)} />
                    </div>
                  </div>

                  <div className="form-checkbox-group" style={{ marginTop: 12 }}>
                    <input type="checkbox" id={`conv-${i}`} checked={f.estudioConvalidado}
                      onChange={e => updateFormacion(i, "estudioConvalidado", e.target.checked)} />
                    <label htmlFor={`conv-${i}`}>¿Estudio convalidado?</label>
                  </div>
                </div>
              </div>
            ))}

            <button type="button" className="btn btn-secondary"
              onClick={() => setFormaciones(prev => [...prev, emptyFormacion()])}>
              + Agregar otra formación
            </button>
          </div>
        )}

        {/* ── Idiomas ── */}
        {tab === 1 && (
          <div className="animate-in">
            {idiomas.length === 0 && (
              <div className="empty-state card" style={{ padding: "40px" }}>
                <div style={{ fontSize: "2.5rem", marginBottom: 12 }}>🌐</div>
                <p className="text-muted">No ha registrado idiomas. Agregue uno a continuación.</p>
              </div>
            )}

            {idiomas.map((id, i) => (
              <div key={i} className="form-section" style={{ marginBottom: 16 }}>
                <div className="form-section-header" style={{ cursor: "default" }}>
                  <div className="section-icon">🌐</div>
                  <h3>Idioma #{i + 1}</h3>
                  <button type="button" className="btn btn-danger btn-sm"
                    style={{ marginLeft: "auto" }}
                    onClick={() => setIdiomas(prev => prev.filter((_, idx) => idx !== i))}>
                    Eliminar
                  </button>
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Idioma <span className="required">*</span></label>
                      <input className="form-input" value={id.idioma}
                        onChange={e => setIdiomas(prev => prev.map((x, idx) => idx === i ? { ...x, idioma: e.target.value } : x))}
                        placeholder="Ej: Inglés, Francés" />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Nivel de conversación</label>
                      <select className="form-select" value={id.conversacion}
                        onChange={e => setIdiomas(prev => prev.map((x, idx) => idx === i ? { ...x, conversacion: e.target.value } : x))}>
                        <option value="">Seleccione...</option>
                        {nivelesConversacion.map(n => <option key={n} value={n}>{n}</option>)}
                      </select>
                    </div>
                    <div className="form-group">
                      <label className="form-label">Fecha del certificado</label>
                      <input type="date" className="form-input" value={id.fechaCertificado}
                        onChange={e => setIdiomas(prev => prev.map((x, idx) => idx === i ? { ...x, fechaCertificado: e.target.value } : x))} />
                    </div>
                  </div>
                </div>
              </div>
            ))}

            <button type="button" className="btn btn-secondary"
              onClick={() => setIdiomas(prev => [...prev, emptyIdioma()])}>
              + Agregar idioma
            </button>
          </div>
        )}

        <div className="flex justify-between items-center mt-4">
          <span />
          <div className="flex gap-2">
            <button type="submit" className="btn btn-secondary">Guardar borrador</button>
            <button type="submit" className="btn btn-primary">✓ Guardar educación</button>
          </div>
        </div>
      </form>
    </AppLayout>
  );
};

export default EducacionPage;
