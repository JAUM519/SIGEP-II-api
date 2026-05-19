import React, { useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import { curriculumService, getApiError, toInstant } from "../../services/api";
import {
  ClaseLibretaMilitar,
  EstadoCivil,
  EstadoCivilLabels,
  Genero,
  GeneroLabels,
  PreferenciaEtnica,
  PreferenciaEtnicaLabels,
  TipoIdentificacion,
  TipoIdentificacionLabels,
  Zona,
  ZonaLabels,
  type RegistrarDatosBasicosRequest,
} from "../../types";

const tabs = ["Datos Básicos", "Datos Demográficos", "Datos de Contacto"];

const DatosPersonalesPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState(0);
  const [saved, setSaved] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  const [basicos, setBasicos] = useState({
    nombre: "",
    tipoIdentificacion: TipoIdentificacion.CedulaDeCiudadania,
    numeroIdentificacion: "",
    fechaNacimiento: "",
    email: "",
    genero: Genero.Masculino,
    claseLibretaMilitar: ClaseLibretaMilitar.Primera,
    numeroLibretaMilitar: "",
    distritoMilitar: "",
    documentoIdentificacion: "",
    documentoVerificado: false,
    libretaMilitar: "",
    libretaVerificada: false,
    personaExpuestaPoliticamente: false,
  });

  const [demo, setDemo] = useState({
    nacionalidad: "Colombiana",
    estadoCivil: EstadoCivil.Soltero,
    preferenciaEtnica: PreferenciaEtnica.Ninguna,
    paisNacimiento: "Colombia",
    departamentoNacimiento: "",
    municipioNacimiento: "",
    discapacidad: false,
  });

  const [contacto, setContacto] = useState({
    paisResidencia: "Colombia",
    departamentoResidencia: "",
    municipioResidencia: "",
    zona: Zona.Urbana,
    direccionResidencia: "",
    telefonoResidencia: "",
    celular: "",
    telefonoOficina: "",
    extension: "",
    emailPersonalPrincipal: "",
    emailOficina: "",
  });

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
      if (activeTab === 0) {
        const payload: RegistrarDatosBasicosRequest = {
          nombre: basicos.nombre.trim(),
          tipoIdentificacion: basicos.tipoIdentificacion,
          numeroIdentificacion: basicos.numeroIdentificacion.trim(),
          fechaNacimiento: toInstant(basicos.fechaNacimiento) ?? "",
          email: basicos.email.trim(),
          genero: basicos.genero,
          claseLibretaMilitar: basicos.claseLibretaMilitar,
          numeroLibretaMilitar: basicos.numeroLibretaMilitar.trim() || undefined,
          distritoMilitar: basicos.distritoMilitar ? Number(basicos.distritoMilitar) : undefined,
          documentoIdentificacion: basicos.documentoIdentificacion.trim() || undefined,
          documentoVerificado: basicos.documentoVerificado,
          libretaMilitar: basicos.libretaMilitar.trim() || undefined,
          libretaVerificada: basicos.libretaVerificada,
          personaExpuestaPoliticamente: basicos.personaExpuestaPoliticamente,
        };

        await curriculumService.registrarDatosBasicos(payload);
      }

      if (activeTab === 1) {
        await curriculumService.registrarDatosDemograficos({
          nacionalidad: demo.nacionalidad.trim(),
          estadoCivil: demo.estadoCivil,
          preferenciaEtnica: demo.preferenciaEtnica,
          paisNacimiento: demo.paisNacimiento.trim(),
          departamentoNacimiento: demo.departamentoNacimiento.trim(),
          municipioNacimiento: demo.municipioNacimiento.trim(),
          discapacidad: demo.discapacidad,
        });
      }

      if (activeTab === 2) {
        await curriculumService.registrarDatosContacto({
          paisResidencia: contacto.paisResidencia.trim(),
          departamentoResidencia: contacto.departamentoResidencia.trim(),
          municipioResidencia: contacto.municipioResidencia.trim(),
          zona: contacto.zona,
          direccionResidencia: contacto.direccionResidencia.trim(),
          telefonoResidencia: contacto.telefonoResidencia.trim() || undefined,
          celular: contacto.celular.trim(),
          telefonoOficina: contacto.telefonoOficina.trim() || undefined,
          extension: contacto.extension.trim() || undefined,
          emailPersonalPrincipal: contacto.emailPersonalPrincipal.trim(),
          emailOficina: contacto.emailOficina.trim() || undefined,
        });
      }

      showSuccess();
    } catch (err) {
      setError(getApiError(err));
    } finally {
      setSaving(false);
    }
  };

  return (
    <AppLayout title="Datos Personales">
      <div className="page-header animate-in">
        <h2>Datos Personales</h2>
        <p>Información básica, demográfica y de contacto del servidor público.</p>
      </div>

      {saved && (
        <div className="alert alert-success animate-in" style={{ marginBottom: 20 }}>
          ✅ Información guardada correctamente.
        </div>
      )}

      {error && (
        <div className="alert alert-danger animate-in" style={{ marginBottom: 20 }}>
          {error}
        </div>
      )}

      <div className="tabs animate-in">
        {tabs.map((tab, i) => (
          <button key={tab} type="button" className={`tab ${activeTab === i ? "active" : ""}`} onClick={() => setActiveTab(i)}>
            {tab}
          </button>
        ))}
      </div>

      <form onSubmit={handleSave}>
        {activeTab === 0 && (
          <div className="animate-in">
            <div className="form-section">
              <div className="form-section-header">
                <div className="section-icon">👤</div>
                <h3>Identificación y datos básicos</h3>
              </div>
              <div className="form-section-body">
                <div className="form-grid">
                  <div className="form-group span-2">
                    <label className="form-label">Nombre completo <span className="required">*</span></label>
                    <input className="form-input" required value={basicos.nombre}
                      onChange={e => setBasicos(p => ({ ...p, nombre: e.target.value }))}
                      placeholder="Ej: Juan Carlos Pérez Gómez" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Tipo de identificación <span className="required">*</span></label>
                    <select className="form-select" required value={basicos.tipoIdentificacion}
                      onChange={e => setBasicos(p => ({ ...p, tipoIdentificacion: e.target.value as TipoIdentificacion }))}>
                      {Object.entries(TipoIdentificacionLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                    </select>
                  </div>

                  <div className="form-group">
                    <label className="form-label">Número de identificación <span className="required">*</span></label>
                    <input className="form-input" required value={basicos.numeroIdentificacion}
                      onChange={e => setBasicos(p => ({ ...p, numeroIdentificacion: e.target.value }))}
                      placeholder="Ej: 1234567890" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Fecha de nacimiento <span className="required">*</span></label>
                    <input type="date" className="form-input" required value={basicos.fechaNacimiento}
                      onChange={e => setBasicos(p => ({ ...p, fechaNacimiento: e.target.value }))} />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Género <span className="required">*</span></label>
                    <select className="form-select" required value={basicos.genero}
                      onChange={e => setBasicos(p => ({ ...p, genero: e.target.value as Genero }))}>
                      {Object.entries(GeneroLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                    </select>
                  </div>

                  <div className="form-group span-2">
                    <label className="form-label">Correo electrónico institucional <span className="required">*</span></label>
                    <input type="email" className="form-input" required value={basicos.email}
                      onChange={e => setBasicos(p => ({ ...p, email: e.target.value }))}
                      placeholder="servidor@entidad.gov.co" />
                  </div>
                </div>
              </div>
            </div>

            <div className="form-section">
              <div className="form-section-header">
                <div className="section-icon">🪖</div>
                <h3>Libreta militar y verificaciones</h3>
              </div>
              <div className="form-section-body">
                <div className="form-grid cols-3">
                  <div className="form-group">
                    <label className="form-label">Clase de libreta</label>
                    <select className="form-select" value={basicos.claseLibretaMilitar}
                      onChange={e => setBasicos(p => ({ ...p, claseLibretaMilitar: e.target.value as ClaseLibretaMilitar }))}>
                      <option value={ClaseLibretaMilitar.Primera}>Primera clase</option>
                      <option value={ClaseLibretaMilitar.Segunda}>Segunda clase</option>
                      <option value={ClaseLibretaMilitar.Provisional}>Provisional</option>
                    </select>
                  </div>

                  <div className="form-group">
                    <label className="form-label">Número de libreta</label>
                    <input className="form-input" value={basicos.numeroLibretaMilitar}
                      onChange={e => setBasicos(p => ({ ...p, numeroLibretaMilitar: e.target.value }))}
                      placeholder="Ej: 123456789" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Distrito militar</label>
                    <input type="number" className="form-input" value={basicos.distritoMilitar}
                      onChange={e => setBasicos(p => ({ ...p, distritoMilitar: e.target.value }))}
                      placeholder="Ej: 14" min={0} />
                  </div>
                </div>

                <div className="form-checkbox-group" style={{ marginTop: 12 }}>
                  <input type="checkbox" id="pep" checked={basicos.personaExpuestaPoliticamente}
                    onChange={e => setBasicos(p => ({ ...p, personaExpuestaPoliticamente: e.target.checked }))} />
                  <label htmlFor="pep">¿Es Persona Expuesta Políticamente (PEP)?</label>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 1 && (
          <div className="animate-in">
            <div className="form-section">
              <div className="form-section-header">
                <div className="section-icon">🌍</div>
                <h3>Información demográfica</h3>
              </div>
              <div className="form-section-body">
                <div className="form-grid">
                  <div className="form-group">
                    <label className="form-label">Nacionalidad <span className="required">*</span></label>
                    <input className="form-input" required value={demo.nacionalidad}
                      onChange={e => setDemo(p => ({ ...p, nacionalidad: e.target.value }))} />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Estado civil <span className="required">*</span></label>
                    <select className="form-select" required value={demo.estadoCivil}
                      onChange={e => setDemo(p => ({ ...p, estadoCivil: e.target.value as EstadoCivil }))}>
                      {Object.entries(EstadoCivilLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                    </select>
                  </div>

                  <div className="form-group">
                    <label className="form-label">Preferencia étnica <span className="required">*</span></label>
                    <select className="form-select" required value={demo.preferenciaEtnica}
                      onChange={e => setDemo(p => ({ ...p, preferenciaEtnica: e.target.value as PreferenciaEtnica }))}>
                      {Object.entries(PreferenciaEtnicaLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                    </select>
                  </div>

                  <div className="form-group">
                    <label className="form-label">País de nacimiento <span className="required">*</span></label>
                    <input className="form-input" required value={demo.paisNacimiento}
                      onChange={e => setDemo(p => ({ ...p, paisNacimiento: e.target.value }))} />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Departamento de nacimiento <span className="required">*</span></label>
                    <input className="form-input" required value={demo.departamentoNacimiento}
                      onChange={e => setDemo(p => ({ ...p, departamentoNacimiento: e.target.value }))}
                      placeholder="Ej: Cundinamarca" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Municipio de nacimiento <span className="required">*</span></label>
                    <input className="form-input" required value={demo.municipioNacimiento}
                      onChange={e => setDemo(p => ({ ...p, municipioNacimiento: e.target.value }))}
                      placeholder="Ej: Bogotá D.C." />
                  </div>
                </div>

                <div className="form-checkbox-group" style={{ marginTop: 12 }}>
                  <input type="checkbox" id="disc" checked={demo.discapacidad}
                    onChange={e => setDemo(p => ({ ...p, discapacidad: e.target.checked }))} />
                  <label htmlFor="disc">¿Tiene algún tipo de discapacidad?</label>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 2 && (
          <div className="animate-in">
            <div className="form-section">
              <div className="form-section-header">
                <div className="section-icon">📍</div>
                <h3>Dirección de residencia</h3>
              </div>
              <div className="form-section-body">
                <div className="form-grid cols-3">
                  <div className="form-group">
                    <label className="form-label">País <span className="required">*</span></label>
                    <input className="form-input" required value={contacto.paisResidencia}
                      onChange={e => setContacto(p => ({ ...p, paisResidencia: e.target.value }))} />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Departamento <span className="required">*</span></label>
                    <input className="form-input" required value={contacto.departamentoResidencia}
                      onChange={e => setContacto(p => ({ ...p, departamentoResidencia: e.target.value }))}
                      placeholder="Ej: Antioquia" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Municipio <span className="required">*</span></label>
                    <input className="form-input" required value={contacto.municipioResidencia}
                      onChange={e => setContacto(p => ({ ...p, municipioResidencia: e.target.value }))}
                      placeholder="Ej: Medellín" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Zona <span className="required">*</span></label>
                    <select className="form-select" required value={contacto.zona}
                      onChange={e => setContacto(p => ({ ...p, zona: e.target.value as Zona }))}>
                      {Object.entries(ZonaLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
                    </select>
                  </div>

                  <div className="form-group span-2">
                    <label className="form-label">Dirección <span className="required">*</span></label>
                    <input className="form-input" required value={contacto.direccionResidencia}
                      onChange={e => setContacto(p => ({ ...p, direccionResidencia: e.target.value }))}
                      placeholder="Ej: Calle 45 # 23-10 Apto 301" />
                  </div>
                </div>
              </div>
            </div>

            <div className="form-section">
              <div className="form-section-header">
                <div className="section-icon">📞</div>
                <h3>Teléfonos y correos electrónicos</h3>
              </div>
              <div className="form-section-body">
                <div className="form-grid cols-3">
                  <div className="form-group">
                    <label className="form-label">Celular <span className="required">*</span></label>
                    <input className="form-input" required value={contacto.celular}
                      onChange={e => setContacto(p => ({ ...p, celular: e.target.value }))}
                      placeholder="Ej: 3001234567" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Teléfono residencia</label>
                    <input className="form-input" value={contacto.telefonoResidencia}
                      onChange={e => setContacto(p => ({ ...p, telefonoResidencia: e.target.value }))}
                      placeholder="Ej: 6012345678" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Teléfono oficina</label>
                    <input className="form-input" value={contacto.telefonoOficina}
                      onChange={e => setContacto(p => ({ ...p, telefonoOficina: e.target.value }))} />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Extensión</label>
                    <input className="form-input" value={contacto.extension}
                      onChange={e => setContacto(p => ({ ...p, extension: e.target.value }))} />
                  </div>

                  <div className="form-group span-2">
                    <label className="form-label">Correo personal principal <span className="required">*</span></label>
                    <input type="email" className="form-input" required value={contacto.emailPersonalPrincipal}
                      onChange={e => setContacto(p => ({ ...p, emailPersonalPrincipal: e.target.value }))}
                      placeholder="correo@dominio.com" />
                  </div>

                  <div className="form-group">
                    <label className="form-label">Correo de oficina</label>
                    <input type="email" className="form-input" value={contacto.emailOficina}
                      onChange={e => setContacto(p => ({ ...p, emailOficina: e.target.value }))}
                      placeholder="servidor@entidad.gov.co" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        <div className="flex justify-between items-center mt-4">
          <button type="button" className="btn btn-secondary" onClick={() => setActiveTab(Math.max(0, activeTab - 1))} disabled={activeTab === 0 || saving}>
            ← Anterior
          </button>

          <div className="flex gap-2">
            <button type="submit" className="btn btn-secondary" disabled={saving}>
              {saving ? "Guardando..." : "Guardar sección"}
            </button>
            {activeTab < tabs.length - 1 ? (
              <button type="button" className="btn btn-primary" disabled={saving} onClick={() => setActiveTab(activeTab + 1)}>
                Siguiente →
              </button>
            ) : (
              <button type="submit" className="btn btn-primary" disabled={saving}>
                ✓ Guardar datos de contacto
              </button>
            )}
          </div>
        </div>
      </form>
    </AppLayout>
  );
};

export default DatosPersonalesPage;
