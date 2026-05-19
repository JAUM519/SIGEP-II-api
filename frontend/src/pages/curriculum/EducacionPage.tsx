import React, { useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import { curriculumService, getApiError, toInstant } from "../../services/api";
import {
  AreaConocimiento,
  AreaConocimientoLabels,
  EstadoEstudio,
  EstadoEstudioLabels,
  IdiomaNivel,
  IdiomaNivelLabels,
  MedioCapacitacion,
  ModalidadEducacionTrabajo,
  NivelAcademico,
  NivelAcademicoLabels,
  NivelFormacion,
  NivelFormacionLabels,
  type EducacionTrabajo,
  type FormacionAcademica,
  type Idioma,
} from "../../types";

const medioCapacitacionLabels: Record<MedioCapacitacion, string> = {
  [MedioCapacitacion.ADistancia]: "A distancia",
  [MedioCapacitacion.Mixta]: "Mixta",
  [MedioCapacitacion.Multimedia]: "Multimedia",
  [MedioCapacitacion.Otro]: "Otro",
  [MedioCapacitacion.Presencial]: "Presencial",
  [MedioCapacitacion.Virtual]: "Virtual",
};

const modalidadLabels: Record<ModalidadEducacionTrabajo, string> = {
  [ModalidadEducacionTrabajo.EducacionInformal]: "Educación informal",
  [ModalidadEducacionTrabajo.EducacionTrabajoDesarrolloHumano]: "Educación para el trabajo y desarrollo humano",
};

const EducacionPage: React.FC = () => {
  const [tab, setTab] = useState(0);
  const [saved, setSaved] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  const emptyFormacion = (): FormacionAcademica => ({
    nivelAcademico: NivelAcademico.Pregrado,
    nivelFormacion: NivelFormacion.Profesional,
    areaConocimiento: AreaConocimiento.NoAplica,
    pais: "Colombia",
    institucionFormacionAcademica: "",
    programaAcademico: "",
    tituloObtenido: "",
    semestresAprobados: undefined,
    estadoEstudio: EstadoEstudio.Finalizado,
    fechaTerminacionMaterias: "",
    fechaGrado: "",
    estudioConvalidado: false,
    fechaConvalidacion: "",
  });

  const emptyIdioma = (): Idioma => ({
    idioma: "",
    fechaCertificado: "",
    conversacion: IdiomaNivel.Regular,
    lectura: IdiomaNivel.Regular,
    redaccion: IdiomaNivel.Regular,
    lenguaNativa: false,
    certificado: "",
  });

  const emptyEducacionTrabajo = (): EducacionTrabajo => ({
    fechaFinalizacion: "",
    numeroTotalHoras: 1,
    pais: "Colombia",
    nombre: "",
    institucion: "",
    medioCapacitacion: MedioCapacitacion.Presencial,
    modalidad: ModalidadEducacionTrabajo.EducacionTrabajoDesarrolloHumano,
    diplomaActaCertificadoEstudio: "",
  });

  const [formaciones, setFormaciones] = useState<FormacionAcademica[]>([emptyFormacion()]);
  const [idiomas, setIdiomas] = useState<Idioma[]>([]);
  const [trabajos, setTrabajos] = useState<EducacionTrabajo[]>([]);

  const updateFormacion = (i: number, field: keyof FormacionAcademica, value: string | boolean | number | undefined) => {
    setFormaciones(prev => prev.map((f, idx) => idx === i ? { ...f, [field]: value } : f));
  };

  const updateIdioma = (i: number, field: keyof Idioma, value: string | boolean) => {
    setIdiomas(prev => prev.map((item, idx) => idx === i ? { ...item, [field]: value } : item));
  };

  const updateTrabajo = (i: number, field: keyof EducacionTrabajo, value: string | number) => {
    setTrabajos(prev => prev.map((item, idx) => idx === i ? { ...item, [field]: value } : item));
  };

  const showSuccess = () => {
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError("");
    setSaved(false);

    try {
      if (tab === 0) {
        await Promise.all(formaciones.map(f => curriculumService.registrarFormacionAcademica({
          nivelAcademico: f.nivelAcademico as NivelAcademico,
          nivelFormacion: f.nivelFormacion as NivelFormacion,
          areaConocimiento: f.areaConocimiento as AreaConocimiento,
          pais: f.pais.trim(),
          institucion: f.institucionFormacionAcademica.trim(),
          programaAcademico: f.programaAcademico.trim() || undefined,
          tituloObtenido: f.tituloObtenido.trim(),
          semestresAprobados: f.semestresAprobados,
          estadoEstudio: f.estadoEstudio,
          fechaTerminacionMaterias: toInstant(f.fechaTerminacionMaterias),
          fechaGrado: toInstant(f.fechaGrado),
          estudioConvalidado: f.estudioConvalidado,
          fechaConvalidacion: toInstant(f.fechaConvalidacion),
        })));
      }

      if (tab === 1) {
        await Promise.all(idiomas.map(idioma => curriculumService.registrarIdioma({
          idioma: idioma.idioma.trim(),
          fechaCertificado: toInstant(idioma.fechaCertificado) ?? "",
          conversacion: idioma.conversacion as IdiomaNivel,
          lectura: idioma.lectura as IdiomaNivel,
          redaccion: idioma.redaccion as IdiomaNivel,
          lenguaNativa: idioma.lenguaNativa,
          certificado: idioma.certificado?.trim() || undefined,
        })));
      }

      if (tab === 2) {
        await Promise.all(trabajos.map(trabajo => curriculumService.registrarEducacionTrabajo({
          ...trabajo,
          fechaFinalizacion: toInstant(trabajo.fechaFinalizacion) ?? "",
          nombre: trabajo.nombre.trim(),
          institucion: trabajo.institucion.trim(),
          pais: trabajo.pais.trim(),
          diplomaActaCertificadoEstudio: trabajo.diplomaActaCertificadoEstudio.trim(),
        })));
      }

      showSuccess();
    } catch (err) {
      setError(getApiError(err));
    } finally {
      setSaving(false);
    }
  };

  return (
    <AppLayout title="Educación">
      <div className="page-header animate-in">
        <h2>Educación</h2>
        <p>Registre formación académica, idiomas y educación para el trabajo.</p>
      </div>

      {saved && (
        <div className="alert alert-success animate-in" style={{ marginBottom: 20 }}>
          ✅ Información de educación guardada correctamente.
        </div>
      )}

      {error && (
        <div className="alert alert-danger animate-in" style={{ marginBottom: 20 }}>
          {error}
        </div>
      )}

      <div className="tabs animate-in">
        {["Formación Académica", "Idiomas", "Educación para el Trabajo"].map((t, i) => (
          <button key={t} type="button" className={`tab ${tab === i ? "active" : ""}`} onClick={() => setTab(i)}>{t}</button>
        ))}
      </div>

      <form onSubmit={handleSave}>
        {tab === 0 && (
          <div className="animate-in">
            {formaciones.map((f, i) => (
              <div key={i} className="form-section" style={{ marginBottom: 16 }}>
                <div className="form-section-header" style={{ cursor: "default" }}>
                  <div className="section-icon">🎓</div>
                  <h3>Estudio #{i + 1}</h3>
                  {formaciones.length > 1 && (
                    <button type="button" className="btn btn-danger btn-sm" style={{ marginLeft: "auto" }} onClick={() => setFormaciones(prev => prev.filter((_, idx) => idx !== i))}>
                      Eliminar
                    </button>
                  )}
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Nivel académico <span className="required">*</span></label>
                      <select className="form-select" required value={f.nivelAcademico} onChange={e => updateFormacion(i, "nivelAcademico", e.target.value)}>
                        {Object.entries(NivelAcademicoLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Nivel de formación <span className="required">*</span></label>
                      <select className="form-select" required value={f.nivelFormacion} onChange={e => updateFormacion(i, "nivelFormacion", e.target.value)}>
                        {Object.entries(NivelFormacionLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Área de conocimiento</label>
                      <select className="form-select" value={f.areaConocimiento} onChange={e => updateFormacion(i, "areaConocimiento", e.target.value)}>
                        {Object.entries(AreaConocimientoLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">País <span className="required">*</span></label>
                      <input className="form-input" required value={f.pais} onChange={e => updateFormacion(i, "pais", e.target.value)} />
                    </div>

                    <div className="form-group span-2">
                      <label className="form-label">Institución <span className="required">*</span></label>
                      <input className="form-input" required value={f.institucionFormacionAcademica} onChange={e => updateFormacion(i, "institucionFormacionAcademica", e.target.value)} placeholder="Ej: Universidad Nacional de Colombia" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Estado del estudio <span className="required">*</span></label>
                      <select className="form-select" required value={f.estadoEstudio} onChange={e => updateFormacion(i, "estadoEstudio", e.target.value)}>
                        {Object.entries(EstadoEstudioLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Semestres aprobados</label>
                      <input type="number" min={0} max={12} className="form-input" value={f.semestresAprobados ?? ""} onChange={e => updateFormacion(i, "semestresAprobados", e.target.value ? Number(e.target.value) : undefined)} />
                    </div>

                    <div className="form-group span-2">
                      <label className="form-label">Programa académico</label>
                      <input className="form-input" value={f.programaAcademico} onChange={e => updateFormacion(i, "programaAcademico", e.target.value)} placeholder="Ej: Ingeniería de Sistemas" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Título obtenido <span className="required">*</span></label>
                      <input className="form-input" required value={f.tituloObtenido} onChange={e => updateFormacion(i, "tituloObtenido", e.target.value)} placeholder="Ej: Ingeniero de Sistemas" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha terminación materias</label>
                      <input type="date" className="form-input" value={f.fechaTerminacionMaterias ?? ""} onChange={e => updateFormacion(i, "fechaTerminacionMaterias", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha de grado</label>
                      <input type="date" className="form-input" value={f.fechaGrado ?? ""} onChange={e => updateFormacion(i, "fechaGrado", e.target.value)} />
                    </div>
                  </div>

                  <div className="form-checkbox-group" style={{ marginTop: 12 }}>
                    <input type="checkbox" id={`conv-${i}`} checked={f.estudioConvalidado} onChange={e => updateFormacion(i, "estudioConvalidado", e.target.checked)} />
                    <label htmlFor={`conv-${i}`}>¿Estudio convalidado?</label>
                  </div>
                </div>
              </div>
            ))}

            <button type="button" className="btn btn-secondary" onClick={() => setFormaciones(prev => [...prev, emptyFormacion()])}>
              + Agregar otra formación
            </button>
          </div>
        )}

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
                  <button type="button" className="btn btn-danger btn-sm" style={{ marginLeft: "auto" }} onClick={() => setIdiomas(prev => prev.filter((_, idx) => idx !== i))}>
                    Eliminar
                  </button>
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Idioma <span className="required">*</span></label>
                      <input className="form-input" required value={id.idioma} onChange={e => updateIdioma(i, "idioma", e.target.value)} placeholder="Ej: Inglés, Francés" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Conversación <span className="required">*</span></label>
                      <select className="form-select" required value={id.conversacion} onChange={e => updateIdioma(i, "conversacion", e.target.value)}>
                        {Object.entries(IdiomaNivelLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Lectura <span className="required">*</span></label>
                      <select className="form-select" required value={id.lectura} onChange={e => updateIdioma(i, "lectura", e.target.value)}>
                        {Object.entries(IdiomaNivelLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Redacción <span className="required">*</span></label>
                      <select className="form-select" required value={id.redaccion} onChange={e => updateIdioma(i, "redaccion", e.target.value)}>
                        {Object.entries(IdiomaNivelLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha del certificado <span className="required">*</span></label>
                      <input type="date" className="form-input" required value={id.fechaCertificado} onChange={e => updateIdioma(i, "fechaCertificado", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Certificado</label>
                      <input className="form-input" value={id.certificado ?? ""} onChange={e => updateIdioma(i, "certificado", e.target.value)} placeholder="Nombre o URL del certificado" />
                    </div>
                  </div>

                  <div className="form-checkbox-group" style={{ marginTop: 12 }}>
                    <input type="checkbox" id={`native-${i}`} checked={id.lenguaNativa} onChange={e => updateIdioma(i, "lenguaNativa", e.target.checked)} />
                    <label htmlFor={`native-${i}`}>¿Es lengua nativa?</label>
                  </div>
                </div>
              </div>
            ))}

            <button type="button" className="btn btn-secondary" onClick={() => setIdiomas(prev => [...prev, emptyIdioma()])}>
              + Agregar idioma
            </button>
          </div>
        )}

        {tab === 2 && (
          <div className="animate-in">
            {trabajos.length === 0 && (
              <div className="empty-state card" style={{ padding: "40px" }}>
                <div style={{ fontSize: "2.5rem", marginBottom: 12 }}>📚</div>
                <p className="text-muted">No ha registrado educación para el trabajo.</p>
              </div>
            )}

            {trabajos.map((trabajo, i) => (
              <div key={i} className="form-section" style={{ marginBottom: 16 }}>
                <div className="form-section-header" style={{ cursor: "default" }}>
                  <div className="section-icon">📚</div>
                  <h3>Capacitación #{i + 1}</h3>
                  <button type="button" className="btn btn-danger btn-sm" style={{ marginLeft: "auto" }} onClick={() => setTrabajos(prev => prev.filter((_, idx) => idx !== i))}>
                    Eliminar
                  </button>
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Nombre <span className="required">*</span></label>
                      <input className="form-input" required value={trabajo.nombre} onChange={e => updateTrabajo(i, "nombre", e.target.value)} placeholder="Ej: Diplomado en gestión pública" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Institución <span className="required">*</span></label>
                      <input className="form-input" required value={trabajo.institucion} onChange={e => updateTrabajo(i, "institucion", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">País <span className="required">*</span></label>
                      <input className="form-input" required value={trabajo.pais} onChange={e => updateTrabajo(i, "pais", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha finalización <span className="required">*</span></label>
                      <input type="date" className="form-input" required value={trabajo.fechaFinalizacion} onChange={e => updateTrabajo(i, "fechaFinalizacion", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Total horas <span className="required">*</span></label>
                      <input type="number" min={1} className="form-input" required value={trabajo.numeroTotalHoras} onChange={e => updateTrabajo(i, "numeroTotalHoras", Number(e.target.value))} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Medio de capacitación <span className="required">*</span></label>
                      <select className="form-select" required value={trabajo.medioCapacitacion} onChange={e => updateTrabajo(i, "medioCapacitacion", e.target.value)}>
                        {Object.entries(medioCapacitacionLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Modalidad <span className="required">*</span></label>
                      <select className="form-select" required value={trabajo.modalidad} onChange={e => updateTrabajo(i, "modalidad", e.target.value)}>
                        {Object.entries(modalidadLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group span-2">
                      <label className="form-label">Diploma / acta / certificado <span className="required">*</span></label>
                      <input className="form-input" required value={trabajo.diplomaActaCertificadoEstudio} onChange={e => updateTrabajo(i, "diplomaActaCertificadoEstudio", e.target.value)} placeholder="Nombre o URL del archivo" />
                    </div>
                  </div>
                </div>
              </div>
            ))}

            <button type="button" className="btn btn-secondary" onClick={() => setTrabajos(prev => [...prev, emptyEducacionTrabajo()])}>
              + Agregar educación para el trabajo
            </button>
          </div>
        )}

        <div className="flex justify-between items-center mt-4">
          <span />
          <div className="flex gap-2">
            <button type="submit" className="btn btn-primary" disabled={saving}>
              {saving ? "Guardando..." : "✓ Guardar sección"}
            </button>
          </div>
        </div>
      </form>
    </AppLayout>
  );
};

export default EducacionPage;
