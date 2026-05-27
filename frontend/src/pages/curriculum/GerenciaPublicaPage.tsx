import React, { useCallback, useEffect, useState } from "react";
import AppLayout from "../../components/layout/AppLayout";
import { curriculumService, getApiError } from "../../services/api";
import {
  addDateNotFutureError,
  addDateOrderError,
  addRequiredDateError,
  addRequiredTextError,
  joinValidationErrors,
} from "../../utils/curriculumValidation";
import {
  ArticuloPublicacion,
  LibroResultadoInvestigacion,
  TipoPremioReconocimiento,
  TipoProduccionBibliografica,
} from "../../types";

const articuloLabels: Record<ArticuloPublicacion, string> = {
  [ArticuloPublicacion.Libro]: "Libro",
  [ArticuloPublicacion.RevistaIndexada]: "Revista indexada",
  [ArticuloPublicacion.RevistaNoIndexada]: "Revista no indexada",
};

const libroLabels: Record<LibroResultadoInvestigacion, string> = {
  [LibroResultadoInvestigacion.ArticuloRevista]: "Artículo de revista",
  [LibroResultadoInvestigacion.CapituloLibro]: "Capítulo en libro resultado de investigación",
  [LibroResultadoInvestigacion.LibroCompleto]: "Libro completo resultado de investigación",
};

const produccionLabels: Record<TipoProduccionBibliografica, string> = {
  [TipoProduccionBibliografica.DocumentoTrabajo]: "Documento de trabajo",
  [TipoProduccionBibliografica.Otro]: "Otro",
  [TipoProduccionBibliografica.Traduccion]: "Traducción",
};

const premioLabels: Record<TipoPremioReconocimiento, string> = {
  [TipoPremioReconocimiento.Premio]: "Premio",
  [TipoPremioReconocimiento.Reconocimiento]: "Reconocimiento",
};

const tabs = ["Publicación", "Premio / Reconocimiento", "Proyecto", "Corporación / Entidad"];

type ListItem = Record<string, unknown>;

const getListTitle = (item: ListItem, fallback: string) => {
  const value = item.nombrePublicacion ?? item.nombreArticulo ?? item.nombre ?? item.nombreCorporacion ?? item.nombreEntidadOrganizacion;
  return typeof value === "string" && value.trim() ? value : fallback;
};

const GerenciaPublicaPage: React.FC = () => {
  const [tab, setTab] = useState(0);
  const [saved, setSaved] = useState(false);
  const [saving, setSaving] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [listados, setListados] = useState({
    publicaciones: [] as ListItem[],
    premios: [] as ListItem[],
    proyectos: [] as ListItem[],
    corporaciones: [] as ListItem[],
  });

  const [publicacion, setPublicacion] = useState({
    articulo: ArticuloPublicacion.Libro,
    nombreArticulo: "",
    libroResultadoInvestigacion: LibroResultadoInvestigacion.LibroCompleto,
    nombreLibroRevista: "",
    tiposProduccionBibliografica: TipoProduccionBibliografica.DocumentoTrabajo,
    nombrePublicacion: "",
  });

  const [premio, setPremio] = useState({
    tipo: TipoPremioReconocimiento.Premio,
    nombreEntidadOrganizacion: "",
    fecha: "",
    pais: "Colombia",
    departamento: "",
    municipio: "",
  });

  const [proyecto, setProyecto] = useState({
    nombre: "",
    rolDesempeñado: "",
    nombreEntidadOrganizacion: "",
    pais: "Colombia",
    departamento: "",
    municipio: "",
    fechaInicio: "",
    fechaTerminacion: "",
  });

  const [corporacion, setCorporacion] = useState({
    nombreCorporacion: "",
    nombreRazonSocialInstitucion: "",
    nombreEntidadOrganizacion: "",
  });

  const cargarListados = useCallback(async (silent = false) => {
    if (!silent) {
      setLoading(true);
      setError("");
    }

    const [publicaciones, premios, proyectos, corporaciones] = await Promise.allSettled([
      curriculumService.obtenerPublicaciones(),
      curriculumService.obtenerPremiosReconocimientos(),
      curriculumService.obtenerParticipacionesProyectos(),
      curriculumService.obtenerParticipacionesCorporaciones(),
    ]);

    setListados({
      publicaciones: publicaciones.status === "fulfilled" && Array.isArray(publicaciones.value) ? publicaciones.value : [],
      premios: premios.status === "fulfilled" && Array.isArray(premios.value) ? premios.value : [],
      proyectos: proyectos.status === "fulfilled" && Array.isArray(proyectos.value) ? proyectos.value : [],
      corporaciones: corporaciones.status === "fulfilled" && Array.isArray(corporaciones.value) ? corporaciones.value : [],
    });

    if (!silent) setLoading(false);
  }, []);

  useEffect(() => {
    const timer = window.setTimeout(() => void cargarListados(), 0);
    return () => window.clearTimeout(timer);
  }, [cargarListados]);

  const showSuccess = () => {
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  const validateCurrentTab = () => {
    const errors: string[] = [];

    if (tab === 0) {
      addRequiredTextError(errors, publicacion.nombreArticulo, "Nombre del artículo");
      addRequiredTextError(errors, publicacion.nombreLibroRevista, "Libro o revista");
      addRequiredTextError(errors, publicacion.nombrePublicacion, "Nombre de la publicación");
    }

    if (tab === 1) {
      addRequiredTextError(errors, premio.nombreEntidadOrganizacion, "Entidad u organización");
      addRequiredDateError(errors, premio.fecha, "Fecha del premio o reconocimiento");
      addDateNotFutureError(errors, premio.fecha, "Fecha del premio o reconocimiento");
      addRequiredTextError(errors, premio.pais, "País");
      addRequiredTextError(errors, premio.departamento, "Departamento");
      addRequiredTextError(errors, premio.municipio, "Municipio");
    }

    if (tab === 2) {
      addRequiredTextError(errors, proyecto.nombre, "Nombre del proyecto");
      addRequiredTextError(errors, proyecto.rolDesempeñado, "Rol desempeñado");
      addRequiredTextError(errors, proyecto.nombreEntidadOrganizacion, "Entidad u organización");
      addRequiredTextError(errors, proyecto.pais, "País");
      addRequiredTextError(errors, proyecto.departamento, "Departamento");
      addRequiredTextError(errors, proyecto.municipio, "Municipio");
      addRequiredDateError(errors, proyecto.fechaInicio, "Fecha de inicio del proyecto");
      addRequiredDateError(errors, proyecto.fechaTerminacion, "Fecha de terminación del proyecto");
      addDateNotFutureError(errors, proyecto.fechaInicio, "Fecha de inicio del proyecto");
      addDateNotFutureError(errors, proyecto.fechaTerminacion, "Fecha de terminación del proyecto");
      addDateOrderError(errors, proyecto.fechaInicio, proyecto.fechaTerminacion, "La fecha de terminación del proyecto no puede ser anterior a la fecha de inicio.");
    }

    if (tab === 3) {
      addRequiredTextError(errors, corporacion.nombreCorporacion, "Nombre de la corporación");
      addRequiredTextError(errors, corporacion.nombreRazonSocialInstitucion, "Razón social de la institución");
      addRequiredTextError(errors, corporacion.nombreEntidadOrganizacion, "Entidad u organización");
    }

    return errors;
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError("");
    setSaved(false);

    const validationErrors = validateCurrentTab();
    if (validationErrors.length) {
      setError(joinValidationErrors(validationErrors));
      setSaving(false);
      return;
    }

    try {
      if (tab === 0) {
        await curriculumService.registrarPublicacion({
          ...publicacion,
          nombreArticulo: publicacion.nombreArticulo.trim(),
          nombreLibroRevista: publicacion.nombreLibroRevista.trim(),
          nombrePublicacion: publicacion.nombrePublicacion.trim(),
        });
      }

      if (tab === 1) {
        await curriculumService.registrarPremioReconocimiento({
          ...premio,
          nombreEntidadOrganizacion: premio.nombreEntidadOrganizacion.trim(),
          pais: premio.pais.trim(),
          departamento: premio.departamento.trim(),
          municipio: premio.municipio.trim(),
        });
      }

      if (tab === 2) {
        await curriculumService.registrarParticipacionProyecto({
          ...proyecto,
          nombre: proyecto.nombre.trim(),
          rolDesempeñado: proyecto.rolDesempeñado.trim(),
          nombreEntidadOrganizacion: proyecto.nombreEntidadOrganizacion.trim(),
          pais: proyecto.pais.trim(),
          departamento: proyecto.departamento.trim(),
          municipio: proyecto.municipio.trim(),
        });
      }

      if (tab === 3) {
        await curriculumService.registrarParticipacionCorporacionEntidad({
          nombreCorporacion: corporacion.nombreCorporacion.trim(),
          nombreRazonSocialInstitucion: corporacion.nombreRazonSocialInstitucion.trim(),
          nombreEntidadOrganizacion: corporacion.nombreEntidadOrganizacion.trim(),
        });
      }

      await cargarListados(true);
      showSuccess();
    } catch (err) {
      setError(getApiError(err));
    } finally {
      setSaving(false);
    }
  };

  const listaActual = [listados.publicaciones, listados.premios, listados.proyectos, listados.corporaciones][tab];

  return (
    <AppLayout title="Gerencia Pública">
      <div className="page-header animate-in">
        <h2>Gerencia Pública</h2>
        <p>Registre publicaciones, premios, proyectos y participación en corporaciones o entidades.</p>
      </div>

      {loading && <div className="alert alert-info animate-in" style={{ marginBottom: 20 }}>Cargando registros guardados...</div>}
      {saved && <div className="alert alert-success animate-in" style={{ marginBottom: 20 }}>✅ Información guardada correctamente.</div>}
      {error && <div className="alert alert-danger animate-in" style={{ marginBottom: 20 }}>{error}</div>}

      <div className="tabs animate-in">
        {tabs.map((item, i) => (
          <button key={item} type="button" className={`tab ${tab === i ? "active" : ""}`} onClick={() => setTab(i)}>{item}</button>
        ))}
      </div>

      <div className="card animate-in" style={{ padding: 16, marginBottom: 16 }}>
        <strong>Registros existentes:</strong>
        {listaActual.length === 0 ? (
          <p className="text-muted" style={{ marginTop: 8 }}>No hay registros guardados para esta sección.</p>
        ) : (
          <ul style={{ marginTop: 8, paddingLeft: 18 }}>
            {listaActual.map((item, index) => (
              <li key={`${String(item.id ?? index)}`}>{getListTitle(item, `${tabs[tab]} #${index + 1}`)}</li>
            ))}
          </ul>
        )}
      </div>

      <form onSubmit={handleSave}>
        {tab === 0 && (
          <div className="form-section animate-in">
            <div className="form-section-header"><div className="section-icon">📘</div><h3>Nueva publicación</h3></div>
            <div className="form-section-body">
              <div className="form-grid cols-3">
                <div className="form-group"><label className="form-label">Artículo <span className="required">*</span></label><select className="form-select" required value={publicacion.articulo} onChange={e => setPublicacion(p => ({ ...p, articulo: e.target.value as ArticuloPublicacion }))}>{Object.entries(articuloLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}</select></div>
                <div className="form-group"><label className="form-label">Nombre artículo <span className="required">*</span></label><input className="form-input" required value={publicacion.nombreArticulo} onChange={e => setPublicacion(p => ({ ...p, nombreArticulo: e.target.value }))} /></div>
                <div className="form-group"><label className="form-label">Resultado investigación <span className="required">*</span></label><select className="form-select" required value={publicacion.libroResultadoInvestigacion} onChange={e => setPublicacion(p => ({ ...p, libroResultadoInvestigacion: e.target.value as LibroResultadoInvestigacion }))}>{Object.entries(libroLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}</select></div>
                <div className="form-group"><label className="form-label">Libro / revista <span className="required">*</span></label><input className="form-input" required value={publicacion.nombreLibroRevista} onChange={e => setPublicacion(p => ({ ...p, nombreLibroRevista: e.target.value }))} /></div>
                <div className="form-group"><label className="form-label">Tipo producción <span className="required">*</span></label><select className="form-select" required value={publicacion.tiposProduccionBibliografica} onChange={e => setPublicacion(p => ({ ...p, tiposProduccionBibliografica: e.target.value as TipoProduccionBibliografica }))}>{Object.entries(produccionLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}</select></div>
                <div className="form-group"><label className="form-label">Nombre publicación <span className="required">*</span></label><input className="form-input" required value={publicacion.nombrePublicacion} onChange={e => setPublicacion(p => ({ ...p, nombrePublicacion: e.target.value }))} /></div>
              </div>
            </div>
          </div>
        )}

        {tab === 1 && (
          <div className="form-section animate-in">
            <div className="form-section-header"><div className="section-icon">🏅</div><h3>Nuevo premio o reconocimiento</h3></div>
            <div className="form-section-body"><div className="form-grid cols-3">
              <div className="form-group"><label className="form-label">Tipo <span className="required">*</span></label><select className="form-select" required value={premio.tipo} onChange={e => setPremio(p => ({ ...p, tipo: e.target.value as TipoPremioReconocimiento }))}>{Object.entries(premioLabels).map(([value, label]) => <option key={value} value={value}>{label}</option>)}</select></div>
              <div className="form-group"><label className="form-label">Entidad / organización <span className="required">*</span></label><input className="form-input" required value={premio.nombreEntidadOrganizacion} onChange={e => setPremio(p => ({ ...p, nombreEntidadOrganizacion: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Fecha <span className="required">*</span></label><input type="date" className="form-input" required value={premio.fecha} onChange={e => setPremio(p => ({ ...p, fecha: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">País <span className="required">*</span></label><input className="form-input" required value={premio.pais} onChange={e => setPremio(p => ({ ...p, pais: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Departamento <span className="required">*</span></label><input className="form-input" required value={premio.departamento} onChange={e => setPremio(p => ({ ...p, departamento: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Municipio <span className="required">*</span></label><input className="form-input" required value={premio.municipio} onChange={e => setPremio(p => ({ ...p, municipio: e.target.value }))} /></div>
            </div></div>
          </div>
        )}

        {tab === 2 && (
          <div className="form-section animate-in">
            <div className="form-section-header"><div className="section-icon">🧩</div><h3>Nueva participación en proyecto</h3></div>
            <div className="form-section-body"><div className="form-grid cols-3">
              <div className="form-group"><label className="form-label">Nombre <span className="required">*</span></label><input className="form-input" required value={proyecto.nombre} onChange={e => setProyecto(p => ({ ...p, nombre: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Rol desempeñado <span className="required">*</span></label><input className="form-input" required value={proyecto.rolDesempeñado} onChange={e => setProyecto(p => ({ ...p, rolDesempeñado: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Entidad / organización <span className="required">*</span></label><input className="form-input" required value={proyecto.nombreEntidadOrganizacion} onChange={e => setProyecto(p => ({ ...p, nombreEntidadOrganizacion: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">País <span className="required">*</span></label><input className="form-input" required value={proyecto.pais} onChange={e => setProyecto(p => ({ ...p, pais: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Departamento <span className="required">*</span></label><input className="form-input" required value={proyecto.departamento} onChange={e => setProyecto(p => ({ ...p, departamento: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Municipio <span className="required">*</span></label><input className="form-input" required value={proyecto.municipio} onChange={e => setProyecto(p => ({ ...p, municipio: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Fecha inicio <span className="required">*</span></label><input type="date" className="form-input" required value={proyecto.fechaInicio} onChange={e => setProyecto(p => ({ ...p, fechaInicio: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Fecha terminación <span className="required">*</span></label><input type="date" className="form-input" required min={proyecto.fechaInicio || undefined} value={proyecto.fechaTerminacion} onChange={e => setProyecto(p => ({ ...p, fechaTerminacion: e.target.value }))} /></div>
            </div></div>
          </div>
        )}

        {tab === 3 && (
          <div className="form-section animate-in">
            <div className="form-section-header"><div className="section-icon">🏛️</div><h3>Nueva corporación o entidad</h3></div>
            <div className="form-section-body"><div className="form-grid cols-3">
              <div className="form-group"><label className="form-label">Nombre corporación <span className="required">*</span></label><input className="form-input" required value={corporacion.nombreCorporacion} onChange={e => setCorporacion(p => ({ ...p, nombreCorporacion: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Razón social institución <span className="required">*</span></label><input className="form-input" required value={corporacion.nombreRazonSocialInstitucion} onChange={e => setCorporacion(p => ({ ...p, nombreRazonSocialInstitucion: e.target.value }))} /></div>
              <div className="form-group"><label className="form-label">Entidad / organización <span className="required">*</span></label><input className="form-input" required value={corporacion.nombreEntidadOrganizacion} onChange={e => setCorporacion(p => ({ ...p, nombreEntidadOrganizacion: e.target.value }))} /></div>
            </div></div>
          </div>
        )}

        <div className="flex justify-between items-center mt-4">
          <button type="button" className="btn btn-secondary" onClick={() => setTab(Math.max(0, tab - 1))} disabled={tab === 0 || saving}>← Anterior</button>
          <div className="flex gap-2">
            <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? "Guardando..." : "✓ Crear registro"}</button>
            {tab < tabs.length - 1 && <button type="button" className="btn btn-secondary" onClick={() => setTab(tab + 1)} disabled={saving}>Siguiente →</button>}
          </div>
        </div>
      </form>
    </AppLayout>
  );
};

export default GerenciaPublicaPage;
