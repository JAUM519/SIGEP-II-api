import React, { useCallback, useEffect, useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import FileUploadField from "../../components/common/FileUploadField";
import { curriculumService, getApiError, toInstant } from "../../services/api";
import {
  AreaConocimiento,
  AreaConocimientoLabels,
  JornadaLaboral,
  JornadaLaboralLabels,
  NivelAcademico,
  NivelAcademicoLabels,
  NivelJerarquicoEmpleo,
  NivelJerarquicoEmpleoLabels,
  TipoEntidad,
  TipoEntidadLabels,
  TipoInstitucion,
  TipoInstitucionLabels,
  TipoZona,
  TipoZonaLabels,
  type ExperienciaLaboral,
  type ExperienciaLaboralDocente,
} from "../../types";

const toDateInput = (value?: string) => value ? value.slice(0, 10) : "";
const createClientId = () => `tmp-${crypto.randomUUID()}`;


const emptyExp = (): ExperienciaLaboral => ({
  clientId: createClientId(),
  tipoEntidad: TipoEntidad.Publica,
  nombreEntidad: "",
  pais: "Colombia",
  departamento: "",
  municipio: "",
  direccionEntidad: "",
  dependencia: "",
  nivelJerarquiaEmpleo: NivelJerarquicoEmpleo.Profesional,
  cargo: "",
  telefono: "",
  trabajoActual: false,
  fechaIngreso: "",
  fechaRetiro: "",
  jornadaLaboral: JornadaLaboral.TiempoCompleto,
  horasPromedioMes: 1,
  tiempoExperiencia: 1,
  motivoRetiro: "",
  certificadoLaboral: "",
  documentoVerificado: false,
});

const emptyDocente = (): ExperienciaLaboralDocente => ({
  clientId: createClientId(),
  tipoInstitucion: TipoInstitucion.Publica,
  nombreInstitucion: "",
  pais: "Colombia",
  departamento: "",
  municipio: "",
  nivelAcademico: NivelAcademico.Pregrado,
  areaConocimiento: AreaConocimiento.CienciasEducacion,
  tipoZona: TipoZona.Urbana,
  trabajoActual: false,
  fechaIngreso: "",
  fechaTerminacion: "",
  jornadaLaboral: JornadaLaboral.TiempoCompleto,
  horasPromedioMes: 1,
  motivoRetiro: "",
  telefono: "",
  materiaImpartida: "",
  tiempoExperiencia: 1,
  certificadoLaboral: "",
  documentoVerificado: false,
});

const ExperienciaPage: React.FC = () => {
  const [tab, setTab] = useState(0);
  const [saved, setSaved] = useState(false);
  const [saving, setSaving] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [exps, setExps] = useState<ExperienciaLaboral[]>([emptyExp()]);
  const [docentes, setDocentes] = useState<ExperienciaLaboralDocente[]>([]);

  const cargarExperiencia = useCallback(async (silent = false) => {
    if (!silent) {
      setLoading(true);
      setError("");
    }

    const [experienciasResult, docentesResult] = await Promise.allSettled([
      curriculumService.obtenerExperienciasLaborales(),
      curriculumService.obtenerExperienciasDocentes(),
    ]);

    if (experienciasResult.status === "fulfilled" && Array.isArray(experienciasResult.value)) {
      setExps(experienciasResult.value.length ? experienciasResult.value.map((item) => ({
        id: item.id,
        clientId: item.id ?? createClientId(),
        tipoEntidad: item.tipoEntidad ?? TipoEntidad.Publica,
        nombreEntidad: item.nombreEntidad ?? "",
        pais: item.pais ?? "Colombia",
        departamento: item.departamento ?? "",
        municipio: item.municipio ?? "",
        direccionEntidad: item.direccionEntidad ?? "",
        dependencia: item.dependencia ?? "",
        nivelJerarquiaEmpleo: item.nivelJerarquiaEmpleo ?? NivelJerarquicoEmpleo.Profesional,
        cargo: item.cargo ?? "",
        telefono: item.telefono ?? "",
        trabajoActual: Boolean(item.trabajoActual),
        fechaIngreso: toDateInput(item.fechaIngreso),
        fechaRetiro: toDateInput(item.fechaRetiro),
        jornadaLaboral: item.jornadaLaboral ?? JornadaLaboral.TiempoCompleto,
        horasPromedioMes: item.horasPromedioMes ?? 1,
        tiempoExperiencia: item.tiempoExperiencia ?? 1,
        motivoRetiro: item.motivoRetiro ?? "",
        certificadoLaboral: item.certificadoLaboral ?? "",
        documentoVerificado: Boolean(item.documentoVerificado),
      })) : [emptyExp()]);
    }

    if (docentesResult.status === "fulfilled" && Array.isArray(docentesResult.value)) {
      setDocentes(docentesResult.value.map((item) => ({
        id: item.id,
        clientId: item.id ?? createClientId(),
        tipoInstitucion: item.tipoInstitucion ?? TipoInstitucion.Publica,
        nombreInstitucion: item.nombreInstitucion ?? "",
        pais: item.pais ?? "Colombia",
        departamento: item.departamento ?? "",
        municipio: item.municipio ?? "",
        nivelAcademico: item.nivelAcademico ?? NivelAcademico.Pregrado,
        areaConocimiento: item.areaConocimiento ?? AreaConocimiento.CienciasEducacion,
        tipoZona: item.tipoZona ?? TipoZona.Urbana,
        trabajoActual: Boolean(item.trabajoActual),
        fechaIngreso: toDateInput(item.fechaIngreso),
        fechaTerminacion: toDateInput(item.fechaTerminacion),
        jornadaLaboral: item.jornadaLaboral ?? JornadaLaboral.TiempoCompleto,
        horasPromedioMes: item.horasPromedioMes ?? 1,
        motivoRetiro: item.motivoRetiro ?? "",
        telefono: item.telefono ?? "",
        materiaImpartida: item.materiaImpartida ?? "",
        tiempoExperiencia: item.tiempoExperiencia ?? 1,
        certificadoLaboral: item.certificadoLaboral ?? "",
        documentoVerificado: Boolean(item.documentoVerificado),
      })));
    }

    if (!silent) setLoading(false);
  }, []);

  useEffect(() => {
    const timer = window.setTimeout(() => void cargarExperiencia(), 0);
    return () => window.clearTimeout(timer);
  }, [cargarExperiencia]);

  const updateExp = (i: number, field: keyof ExperienciaLaboral, value: string | boolean | number | undefined) => {
    setExps(prev => prev.map((item, idx) => idx === i ? { ...item, [field]: value } : item));
  };

  const updateDocente = (i: number, field: keyof ExperienciaLaboralDocente, value: string | boolean | number | undefined) => {
    setDocentes(prev => prev.map((item, idx) => idx === i ? { ...item, [field]: value } : item));
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
        await Promise.all(exps.map((exp) => {
          if (exp.id) {
            return curriculumService.actualizarExperienciaLaboral({
              experienciaLaboralId: exp.id,
              telefono: exp.telefono?.trim() || undefined,
              fechaRetiro: exp.trabajoActual ? undefined : toInstant(exp.fechaRetiro),
              horasPromedioMes: exp.horasPromedioMes,
              motivoRetiro: exp.trabajoActual ? undefined : exp.motivoRetiro?.trim() || undefined,
              certificadoLaboral: exp.certificadoLaboral?.trim() || undefined,
              documentoVerificado: exp.documentoVerificado,
            });
          }

          return curriculumService.registrarExperienciaLaboral({
            tipoEntidad: exp.tipoEntidad as TipoEntidad,
            nombreEntidad: exp.nombreEntidad.trim(),
            pais: exp.pais.trim(),
            departamento: exp.departamento.trim(),
            municipio: exp.municipio.trim(),
            direccionEntidad: exp.direccionEntidad.trim(),
            dependencia: exp.dependencia.trim(),
            nivelJerarquicoEmpleo: exp.nivelJerarquiaEmpleo as NivelJerarquicoEmpleo,
            cargo: exp.cargo.trim(),
            telefono: exp.telefono?.trim() || undefined,
            trabajoActual: exp.trabajoActual,
            fechaIngreso: toInstant(exp.fechaIngreso) ?? "",
            fechaRetiro: exp.trabajoActual ? undefined : toInstant(exp.fechaRetiro),
            jornadaLaboral: exp.jornadaLaboral as JornadaLaboral,
            horasPromedioMes: exp.horasPromedioMes,
            tiempoExperiencia: Math.max(1, Number(exp.tiempoExperiencia || 1)),
            motivoRetiro: exp.trabajoActual ? undefined : exp.motivoRetiro?.trim() || undefined,
            certificadoLaboral: exp.certificadoLaboral?.trim() || undefined,
            documentoVerificado: exp.documentoVerificado,
          });
        }));
      }

      if (tab === 1) {
        await Promise.all(docentes.map((doc) => {
          if (doc.id) {
            return curriculumService.actualizarExperienciaLaboralDocente({
              experienciaLaboralDocenteId: doc.id,
              fechaTerminacion: doc.trabajoActual ? undefined : toInstant(doc.fechaTerminacion),
              horasPromedioMes: doc.horasPromedioMes,
              motivoRetiro: doc.trabajoActual ? undefined : doc.motivoRetiro?.trim() || undefined,
              telefono: doc.telefono?.trim() || undefined,
              materiaImpartida: doc.materiaImpartida?.trim() || undefined,
              certificadoLaboral: doc.certificadoLaboral?.trim() || undefined,
              documentoVerificado: doc.documentoVerificado,
            });
          }

          return curriculumService.registrarExperienciaLaboralDocente({
            tipoInstitucion: doc.tipoInstitucion as TipoInstitucion,
            nombreInstitucion: doc.nombreInstitucion.trim(),
            pais: doc.pais.trim(),
            departamento: doc.departamento.trim(),
            municipio: doc.municipio.trim(),
            nivelAcademico: doc.nivelAcademico as NivelAcademico,
            areaConocimiento: doc.areaConocimiento as AreaConocimiento,
            tipoZona: doc.tipoZona as TipoZona,
            trabajoActual: doc.trabajoActual,
            fechaIngreso: toInstant(doc.fechaIngreso) ?? "",
            fechaTerminacion: doc.trabajoActual ? undefined : toInstant(doc.fechaTerminacion),
            jornadaLaboral: doc.jornadaLaboral as JornadaLaboral,
            horasPromedioMes: doc.horasPromedioMes,
            motivoRetiro: doc.trabajoActual ? undefined : doc.motivoRetiro?.trim() || undefined,
            telefono: doc.telefono?.trim() || undefined,
            materiaImpartida: doc.materiaImpartida?.trim() || undefined,
            tiempoExperiencia: Math.max(1, Number(doc.tiempoExperiencia || 1)),
            certificadoLaboral: doc.certificadoLaboral?.trim() || undefined,
            documentoVerificado: doc.documentoVerificado,
          });
        }));
      }

      await cargarExperiencia(true);
      showSuccess();
    } catch (err) {
      setError(getApiError(err));
    } finally {
      setSaving(false);
    }
  };

  return (
    <AppLayout title="Experiencia Laboral">
      <div className="page-header animate-in">
        <h2>Experiencia Laboral</h2>
        <p>Registre su historial de empleo en el sector público, privado y docente.</p>
      </div>

      {loading && (
        <div className="alert alert-info animate-in" style={{ marginBottom: 20 }}>
          Cargando experiencia guardada...
        </div>
      )}

      {saved && (
        <div className="alert alert-success animate-in" style={{ marginBottom: 20 }}>
          ✅ Experiencia laboral guardada correctamente.
        </div>
      )}

      {error && (
        <div className="alert alert-danger animate-in" style={{ marginBottom: 20 }}>
          {error}
        </div>
      )}

      <div className="tabs animate-in">
        {["Experiencia General", "Experiencia Docente"].map((t, i) => (
          <button key={t} type="button" className={`tab ${tab === i ? "active" : ""}`} onClick={() => setTab(i)}>{t}</button>
        ))}
      </div>

      <form onSubmit={handleSave}>
        {tab === 0 && (
          <div className="animate-in">
            <div className="alert alert-info" style={{ marginBottom: 16 }}>
              Puede agregar varias experiencias laborales. Para cambiar una experiencia ya guardada, actualice la información disponible y guarde los cambios.
            </div>
            {exps.map((exp, i) => (
              <div key={exp.id ?? exp.clientId ?? i} className="form-section" style={{ marginBottom: 16 }}> 
                <div className="form-section-header" style={{ cursor: "default" }}>
                  <div className="section-icon">💼</div>
                  <h3>Empleo #{i + 1}{exp.id ? " — existente" : " — nuevo"}{exp.cargo ? ` — ${exp.cargo}` : ""}</h3>
                  <div style={{ marginLeft: "auto" }}>
                    {exp.id ? (
                      <span className="badge badge-green">Registrado</span>
                    ) : (
                      exps.length > 1 && (
                        <button type="button" className="btn btn-danger btn-sm" onClick={() => setExps(p => p.filter((_, idx) => idx !== i))}>
                          Quitar borrador
                        </button>
                      )
                    )}
                  </div>
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Tipo de entidad <span className="required">*</span></label>
                      <select className="form-select" required value={exp.tipoEntidad} onChange={e => updateExp(i, "tipoEntidad", e.target.value)}>
                        {Object.entries(TipoEntidadLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group span-2">
                      <label className="form-label">Nombre de la entidad / empresa <span className="required">*</span></label>
                      <input className="form-input" required value={exp.nombreEntidad} onChange={e => updateExp(i, "nombreEntidad", e.target.value)} placeholder="Ej: Ministerio de Hacienda" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">País <span className="required">*</span></label>
                      <input className="form-input" required value={exp.pais} onChange={e => updateExp(i, "pais", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Departamento <span className="required">*</span></label>
                      <input className="form-input" required value={exp.departamento} onChange={e => updateExp(i, "departamento", e.target.value)} placeholder="Ej: Bogotá D.C." />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Municipio <span className="required">*</span></label>
                      <input className="form-input" required value={exp.municipio} onChange={e => updateExp(i, "municipio", e.target.value)} placeholder="Ej: Bogotá" />
                    </div>

                    <div className="form-group span-2">
                      <label className="form-label">Dirección entidad <span className="required">*</span></label>
                      <input className="form-input" required value={exp.direccionEntidad} onChange={e => updateExp(i, "direccionEntidad", e.target.value)} placeholder="Ej: Calle 26 # 13-19" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Teléfono entidad</label>
                      <input className="form-input" value={exp.telefono ?? ""} onChange={e => updateExp(i, "telefono", e.target.value)} placeholder="Ej: 6012345678" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Cargo <span className="required">*</span></label>
                      <input className="form-input" required value={exp.cargo} onChange={e => updateExp(i, "cargo", e.target.value)} placeholder="Ej: Analista de Sistemas" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Nivel jerárquico <span className="required">*</span></label>
                      <select className="form-select" required value={exp.nivelJerarquiaEmpleo} onChange={e => updateExp(i, "nivelJerarquiaEmpleo", e.target.value)}>
                        {Object.entries(NivelJerarquicoEmpleoLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Dependencia <span className="required">*</span></label>
                      <input className="form-input" required value={exp.dependencia} onChange={e => updateExp(i, "dependencia", e.target.value)} placeholder="Ej: Dirección de TI" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha de ingreso <span className="required">*</span></label>
                      <input type="date" className="form-input" required value={exp.fechaIngreso} onChange={e => updateExp(i, "fechaIngreso", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">¿Trabajo actual? <span className="required">*</span></label>
                      <select className="form-select" value={String(exp.trabajoActual)} onChange={e => updateExp(i, "trabajoActual", e.target.value === "true")}>
                        <option value="false">No</option>
                        <option value="true">Sí</option>
                      </select>
                    </div>

                    {!exp.trabajoActual && (
                      <div className="form-group">
                        <label className="form-label">Fecha de retiro</label>
                        <input type="date" className="form-input" value={exp.fechaRetiro ?? ""} onChange={e => updateExp(i, "fechaRetiro", e.target.value)} />
                      </div>
                    )}

                    <div className="form-group">
                      <label className="form-label">Jornada laboral <span className="required">*</span></label>
                      <select className="form-select" required value={exp.jornadaLaboral} onChange={e => updateExp(i, "jornadaLaboral", e.target.value)}>
                        {Object.entries(JornadaLaboralLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Horas promedio / mes</label>
                      <input type="number" className="form-input" value={exp.horasPromedioMes ?? 0} onChange={e => updateExp(i, "horasPromedioMes", Number(e.target.value))} min={0} max={1000000000} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Tiempo experiencia <span className="required">*</span></label>
                      <input type="number" className="form-input" required value={exp.tiempoExperiencia} onChange={e => updateExp(i, "tiempoExperiencia", Number(e.target.value))} min={1} max={1000000000} />
                    </div>

                    {!exp.trabajoActual && (
                      <div className="form-group span-3">
                        <label className="form-label">Motivo de retiro</label>
                        <input className="form-input" value={exp.motivoRetiro ?? ""} onChange={e => updateExp(i, "motivoRetiro", e.target.value)} placeholder="Opcional" />
                      </div>
                    )}

                    <div className="form-group span-2">
                      <FileUploadField
                        label="Certificado laboral"
                        value={exp.certificadoLaboral}
                        onChange={(url) => updateExp(i, "certificadoLaboral", url)}
                      />
                    </div>

                    <div className="form-checkbox-group" style={{ marginTop: 12 }}>
                      <input type="checkbox" id={`exp-cert-${i}`} checked={Boolean(exp.documentoVerificado)} onChange={e => updateExp(i, "documentoVerificado", e.target.checked)} />
                      <label htmlFor={`exp-cert-${i}`}>Certificado verificado</label>
                    </div>
                  </div>
                </div>
              </div>
            ))}
            <button type="button" className="btn btn-secondary" onClick={() => setExps(p => [...p, emptyExp()])}>
              + Agregar otra experiencia
            </button>
          </div>
        )}

        {tab === 1 && (
          <div className="animate-in">
            <div className="alert alert-info" style={{ marginBottom: 16 }}>
              Puede agregar varias experiencias docentes. Para cambiar una experiencia ya guardada, actualice la información disponible y guarde los cambios.
            </div>
            {docentes.length === 0 && (
              <div className="card" style={{ padding: "40px", textAlign: "center", marginBottom: 16 }}>
                <div style={{ fontSize: "2.5rem", marginBottom: 12 }}>🏫</div>
                <p className="text-muted">No ha registrado experiencia docente.</p>
              </div>
            )}

            {docentes.map((doc, i) => (
              <div key={doc.id ?? doc.clientId ?? i} className="form-section" style={{ marginBottom: 16 }}> 
                <div className="form-section-header" style={{ cursor: "default" }}>
                  <div className="section-icon">🏫</div>
                  <h3>Docencia #{i + 1}{doc.id ? " — existente" : " — nuevo"}{doc.nombreInstitucion ? ` — ${doc.nombreInstitucion}` : ""}</h3>
                  <div style={{ marginLeft: "auto" }}>
                    {doc.id ? (
                      <span className="badge badge-green">Registrado</span>
                    ) : (
                      <button type="button" className="btn btn-danger btn-sm" onClick={() => setDocentes(p => p.filter((_, idx) => idx !== i))}>
                        Quitar borrador
                      </button>
                    )}
                  </div>
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Tipo de institución <span className="required">*</span></label>
                      <select className="form-select" required value={doc.tipoInstitucion} onChange={e => updateDocente(i, "tipoInstitucion", e.target.value)}>
                        {Object.entries(TipoInstitucionLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group span-2">
                      <label className="form-label">Nombre de la institución <span className="required">*</span></label>
                      <input className="form-input" required value={doc.nombreInstitucion} onChange={e => updateDocente(i, "nombreInstitucion", e.target.value)} placeholder="Ej: Universidad Nacional de Colombia" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">País <span className="required">*</span></label>
                      <input className="form-input" required value={doc.pais} onChange={e => updateDocente(i, "pais", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Departamento <span className="required">*</span></label>
                      <input className="form-input" required value={doc.departamento} onChange={e => updateDocente(i, "departamento", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Municipio <span className="required">*</span></label>
                      <input className="form-input" required value={doc.municipio} onChange={e => updateDocente(i, "municipio", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Nivel académico <span className="required">*</span></label>
                      <select className="form-select" required value={doc.nivelAcademico} onChange={e => updateDocente(i, "nivelAcademico", e.target.value)}>
                        {Object.entries(NivelAcademicoLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Área de conocimiento <span className="required">*</span></label>
                      <select className="form-select" required value={doc.areaConocimiento} onChange={e => updateDocente(i, "areaConocimiento", e.target.value)}>
                        {Object.entries(AreaConocimientoLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Materia impartida</label>
                      <input className="form-input" value={doc.materiaImpartida ?? ""} onChange={e => updateDocente(i, "materiaImpartida", e.target.value)} placeholder="Ej: Cálculo I" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha de ingreso <span className="required">*</span></label>
                      <input type="date" className="form-input" required value={doc.fechaIngreso} onChange={e => updateDocente(i, "fechaIngreso", e.target.value)} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">¿Trabajo actual? <span className="required">*</span></label>
                      <select className="form-select" value={String(doc.trabajoActual)} onChange={e => updateDocente(i, "trabajoActual", e.target.value === "true")}>
                        <option value="false">No</option>
                        <option value="true">Sí</option>
                      </select>
                    </div>

                    {!doc.trabajoActual && (
                      <div className="form-group">
                        <label className="form-label">Fecha de terminación</label>
                        <input type="date" className="form-input" value={doc.fechaTerminacion ?? ""} onChange={e => updateDocente(i, "fechaTerminacion", e.target.value)} />
                      </div>
                    )}

                    <div className="form-group">
                      <label className="form-label">Tipo de zona <span className="required">*</span></label>
                      <select className="form-select" required value={doc.tipoZona} onChange={e => updateDocente(i, "tipoZona", e.target.value)}>
                        {Object.entries(TipoZonaLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Jornada laboral <span className="required">*</span></label>
                      <select className="form-select" required value={doc.jornadaLaboral} onChange={e => updateDocente(i, "jornadaLaboral", e.target.value)}>
                        {Object.entries(JornadaLaboralLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Horas promedio / mes</label>
                      <input type="number" className="form-input" value={doc.horasPromedioMes ?? 0} onChange={e => updateDocente(i, "horasPromedioMes", Number(e.target.value))} min={0} max={1000000000} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Tiempo experiencia <span className="required">*</span></label>
                      <input type="number" className="form-input" required value={doc.tiempoExperiencia} onChange={e => updateDocente(i, "tiempoExperiencia", Number(e.target.value))} min={1} max={1000000000} />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Teléfono</label>
                      <input className="form-input" value={doc.telefono ?? ""} onChange={e => updateDocente(i, "telefono", e.target.value)} />
                    </div>

                    {!doc.trabajoActual && (
                      <div className="form-group span-2">
                        <label className="form-label">Motivo de retiro</label>
                        <input className="form-input" value={doc.motivoRetiro ?? ""} onChange={e => updateDocente(i, "motivoRetiro", e.target.value)} />
                      </div>
                    )}

                    <div className="form-group span-2">
                      <FileUploadField
                        label="Certificado laboral"
                        value={doc.certificadoLaboral}
                        onChange={(url) => updateDocente(i, "certificadoLaboral", url)}
                      />
                    </div>

                    <div className="form-checkbox-group" style={{ marginTop: 12 }}>
                      <input type="checkbox" id={`doc-cert-${i}`} checked={Boolean(doc.documentoVerificado)} onChange={e => updateDocente(i, "documentoVerificado", e.target.checked)} />
                      <label htmlFor={`doc-cert-${i}`}>Certificado verificado</label>
                    </div>
                  </div>
                </div>
              </div>
            ))}

            <button type="button" className="btn btn-secondary" onClick={() => setDocentes(p => [...p, emptyDocente()])}>
              + Agregar experiencia docente
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

export default ExperienciaPage;
