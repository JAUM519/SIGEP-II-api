import React, { useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import type { ExperienciaLaboral, ExperienciaLaboralDocente } from "../../types";

const ExperienciaPage: React.FC = () => {
  const [tab, setTab] = useState(0);
  const [saved, setSaved] = useState(false);

  const emptyExp = (): ExperienciaLaboral => ({
    tipoEntidad: "", nombreEntidad: "", pais: "Colombia",
    departamento: "", municipio: "", direccionEntidad: "",
    dependencia: "", nivelJerarquiaEmpleo: "", cargo: "",
    telefono: "", trabajoActual: "No", fechaIngreso: "", jornadaLaboral: "Completa",
    horasPromedioMes: 0, tiempoExperiencia: 0,
  });

  const emptyDocente = (): ExperienciaLaboralDocente => ({
    tipoInstitucion: "", nombreInstitucion: "", pais: "Colombia",
    departamento: "", municipio: "", nivelAcademico: "",
    areaConocimiento: "", tipoZona: "Urbana", trabajoActual: "No",
    fechaIngreso: "", jornadaLaboral: "Completa", horasPromedioMes: 0,
    materiaImpartida: "", tiempoExperiencia: 0,
  });

  const [exps, setExps] = useState<ExperienciaLaboral[]>([emptyExp()]);
  const [docentes, setDocentes] = useState<ExperienciaLaboralDocente[]>([]);

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  const tiposEntidad = ["Entidad Pública", "Empresa Privada", "ONG", "Entidad Internacional", "Otro"];
  const nivelesJerarquia = ["Directivo", "Asesor", "Profesional", "Técnico", "Asistencial", "Operativo"];
  const jornadasLaborales = ["Completa", "Medio tiempo", "Por horas", "Por turnos"];

  return (
    <AppLayout title="Experiencia Laboral">
      <div className="page-header animate-in">
        <h2>Experiencia Laboral</h2>
        <p>Registre su historial de empleo en el sector público, privado y docente.</p>
      </div>

      {saved && (
        <div className="alert alert-success animate-in" style={{ marginBottom: 20 }}>
          ✅ Experiencia laboral guardada correctamente.
        </div>
      )}

      <div className="tabs animate-in">
        {["Experiencia General", "Experiencia Docente"].map((t, i) => (
          <button key={t} className={`tab ${tab === i ? "active" : ""}`} onClick={() => setTab(i)}>{t}</button>
        ))}
      </div>

      <form onSubmit={handleSave}>
        {/* ── Experiencia General ── */}
        {tab === 0 && (
          <div className="animate-in">
            {exps.map((exp, i) => (
              <div key={i} className="form-section" style={{ marginBottom: 16 }}>
                <div className="form-section-header" style={{ cursor: "default" }}>
                  <div className="section-icon">💼</div>
                  <h3>Empleo #{i + 1}{exp.cargo ? ` — ${exp.cargo}` : ""}</h3>
                  {exps.length > 1 && (
                    <button type="button" className="btn btn-danger btn-sm" style={{ marginLeft: "auto" }}
                      onClick={() => setExps(p => p.filter((_, idx) => idx !== i))}>
                      Eliminar
                    </button>
                  )}
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Tipo de entidad <span className="required">*</span></label>
                      <select className="form-select" value={exp.tipoEntidad}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, tipoEntidad: e.target.value } : x))}>
                        <option value="">Seleccione...</option>
                        {tiposEntidad.map(t => <option key={t} value={t}>{t}</option>)}
                      </select>
                    </div>
                    <div className="form-group span-2">
                      <label className="form-label">Nombre de la entidad / empresa <span className="required">*</span></label>
                      <input className="form-input" value={exp.nombreEntidad}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, nombreEntidad: e.target.value } : x))}
                        placeholder="Ej: Ministerio de Hacienda" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">País</label>
                      <input className="form-input" value={exp.pais}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, pais: e.target.value } : x))} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Departamento</label>
                      <input className="form-input" value={exp.departamento}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, departamento: e.target.value } : x))}
                        placeholder="Ej: Bogotá D.C." />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Municipio</label>
                      <input className="form-input" value={exp.municipio}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, municipio: e.target.value } : x))}
                        placeholder="Ej: Bogotá" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Cargo <span className="required">*</span></label>
                      <input className="form-input" value={exp.cargo}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, cargo: e.target.value } : x))}
                        placeholder="Ej: Analista de Sistemas" />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Nivel jerárquico</label>
                      <select className="form-select" value={exp.nivelJerarquiaEmpleo}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, nivelJerarquiaEmpleo: e.target.value } : x))}>
                        <option value="">Seleccione...</option>
                        {nivelesJerarquia.map(n => <option key={n} value={n}>{n}</option>)}
                      </select>
                    </div>
                    <div className="form-group">
                      <label className="form-label">Dependencia</label>
                      <input className="form-input" value={exp.dependencia}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, dependencia: e.target.value } : x))}
                        placeholder="Ej: Dirección de TI" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha de ingreso <span className="required">*</span></label>
                      <input type="date" className="form-input" value={exp.fechaIngreso}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, fechaIngreso: e.target.value } : x))} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Fecha de retiro</label>
                      <input type="date" className="form-input" value={exp.fechaRetiro ?? ""}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, fechaRetiro: e.target.value } : x))} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">¿Trabajo actual?</label>
                      <select className="form-select" value={exp.trabajoActual}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, trabajoActual: e.target.value } : x))}>
                        <option value="No">No</option>
                        <option value="Si">Sí</option>
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Jornada laboral</label>
                      <select className="form-select" value={exp.jornadaLaboral}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, jornadaLaboral: e.target.value } : x))}>
                        {jornadasLaborales.map(j => <option key={j} value={j}>{j}</option>)}
                      </select>
                    </div>
                    <div className="form-group">
                      <label className="form-label">Horas promedio / mes</label>
                      <input type="number" className="form-input" value={exp.horasPromedioMes}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, horasPromedioMes: +e.target.value } : x))}
                        min={0} max={300} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Teléfono entidad</label>
                      <input className="form-input" value={exp.telefono}
                        onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, telefono: e.target.value } : x))}
                        placeholder="Ej: 6012345678" />
                    </div>

                    {exp.trabajoActual === "No" && (
                      <div className="form-group span-3">
                        <label className="form-label">Motivo de retiro</label>
                        <input className="form-input" value={exp.motivoRetiro ?? ""}
                          onChange={e => setExps(p => p.map((x, idx) => idx === i ? { ...x, motivoRetiro: e.target.value } : x))}
                          placeholder="Opcional" />
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
            <button type="button" className="btn btn-secondary"
              onClick={() => setExps(p => [...p, emptyExp()])}>
              + Agregar otra experiencia
            </button>
          </div>
        )}

        {/* ── Experiencia Docente ── */}
        {tab === 1 && (
          <div className="animate-in">
            {docentes.length === 0 && (
              <div className="card" style={{ padding: "40px", textAlign: "center", marginBottom: 16 }}>
                <div style={{ fontSize: "2.5rem", marginBottom: 12 }}>🏫</div>
                <p className="text-muted">No ha registrado experiencia docente.</p>
              </div>
            )}

            {docentes.map((doc, i) => (
              <div key={i} className="form-section" style={{ marginBottom: 16 }}>
                <div className="form-section-header" style={{ cursor: "default" }}>
                  <div className="section-icon">🏫</div>
                  <h3>Docencia #{i + 1}{doc.nombreInstitucion ? ` — ${doc.nombreInstitucion}` : ""}</h3>
                  <button type="button" className="btn btn-danger btn-sm" style={{ marginLeft: "auto" }}
                    onClick={() => setDocentes(p => p.filter((_, idx) => idx !== i))}>
                    Eliminar
                  </button>
                </div>
                <div className="form-section-body">
                  <div className="form-grid cols-3">
                    <div className="form-group">
                      <label className="form-label">Tipo de institución</label>
                      <select className="form-select" value={doc.tipoInstitucion}
                        onChange={e => setDocentes(p => p.map((x, idx) => idx === i ? { ...x, tipoInstitucion: e.target.value } : x))}>
                        <option value="">Seleccione...</option>
                        <option value="Pública">Pública</option>
                        <option value="Privada">Privada</option>
                      </select>
                    </div>
                    <div className="form-group span-2">
                      <label className="form-label">Nombre de la institución <span className="required">*</span></label>
                      <input className="form-input" value={doc.nombreInstitucion}
                        onChange={e => setDocentes(p => p.map((x, idx) => idx === i ? { ...x, nombreInstitucion: e.target.value } : x))}
                        placeholder="Ej: Universidad Nacional de Colombia" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Nivel académico</label>
                      <input className="form-input" value={doc.nivelAcademico}
                        onChange={e => setDocentes(p => p.map((x, idx) => idx === i ? { ...x, nivelAcademico: e.target.value } : x))}
                        placeholder="Ej: Universitario" />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Área de conocimiento</label>
                      <input className="form-input" value={doc.areaConocimiento}
                        onChange={e => setDocentes(p => p.map((x, idx) => idx === i ? { ...x, areaConocimiento: e.target.value } : x))}
                        placeholder="Ej: Ingeniería" />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Materia impartida</label>
                      <input className="form-input" value={doc.materiaImpartida}
                        onChange={e => setDocentes(p => p.map((x, idx) => idx === i ? { ...x, materiaImpartida: e.target.value } : x))}
                        placeholder="Ej: Cálculo I" />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Fecha de ingreso</label>
                      <input type="date" className="form-input" value={doc.fechaIngreso}
                        onChange={e => setDocentes(p => p.map((x, idx) => idx === i ? { ...x, fechaIngreso: e.target.value } : x))} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Fecha de terminación</label>
                      <input type="date" className="form-input" value={doc.fechaTerminacion ?? ""}
                        onChange={e => setDocentes(p => p.map((x, idx) => idx === i ? { ...x, fechaTerminacion: e.target.value } : x))} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Tipo de zona</label>
                      <select className="form-select" value={doc.tipoZona}
                        onChange={e => setDocentes(p => p.map((x, idx) => idx === i ? { ...x, tipoZona: e.target.value } : x))}>
                        <option value="Urbana">Urbana</option>
                        <option value="Rural">Rural</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            ))}

            <button type="button" className="btn btn-secondary"
              onClick={() => setDocentes(p => [...p, emptyDocente()])}>
              + Agregar experiencia docente
            </button>
          </div>
        )}

        <div className="flex justify-between items-center mt-4">
          <span />
          <div className="flex gap-2">
            <button type="submit" className="btn btn-secondary">Guardar borrador</button>
            <button type="submit" className="btn btn-primary">✓ Guardar experiencia</button>
          </div>
        </div>
      </form>
    </AppLayout>
  );
};

export default ExperienciaPage;
