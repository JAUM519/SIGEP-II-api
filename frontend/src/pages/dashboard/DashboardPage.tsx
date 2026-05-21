import React, { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import AppLayout from "../../components/layout/AppLayout";
import { useAuth } from "../../hooks/useAuth";
import { curriculumService, getApiError } from "../../services/api";

type SectionKey = "datosPersonales" | "educacion" | "experiencia" | "gerenciaPublica";

type SectionStatus = {
  loading: boolean;
  completed: boolean;
};

const initialSectionStatus: Record<SectionKey, SectionStatus> = {
  datosPersonales: { loading: true, completed: false },
  educacion: { loading: true, completed: false },
  experiencia: { loading: true, completed: false },
  gerenciaPublica: { loading: true, completed: false },
};

const isFulfilled = <T,>(result: PromiseSettledResult<T>): result is PromiseFulfilledResult<T> =>
  result.status === "fulfilled";

const hasItems = (value: unknown): boolean => Array.isArray(value) && value.length > 0;

const DashboardPage: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [sectionStatus, setSectionStatus] = useState<Record<SectionKey, SectionStatus>>(initialSectionStatus);
  const [downloadingPdf, setDownloadingPdf] = useState(false);
  const [pdfError, setPdfError] = useState<string | null>(null);

  useEffect(() => {
    let active = true;

    const loadSectionStatus = async () => {
      const [datosBasicos, datosDemograficos, datosContacto] = await Promise.allSettled([
        curriculumService.obtenerDatosBasicos(),
        curriculumService.obtenerDatosDemograficos(),
        curriculumService.obtenerDatosContacto(),
      ]);

      const [formaciones, educacionesTrabajo, idiomas] = await Promise.allSettled([
        curriculumService.obtenerFormacionesAcademicas(),
        curriculumService.obtenerEducacionesTrabajo(),
        curriculumService.obtenerIdiomas(),
      ]);

      const [experienciasLaborales, experienciasDocentes] = await Promise.allSettled([
        curriculumService.obtenerExperienciasLaborales(),
        curriculumService.obtenerExperienciasDocentes(),
      ]);

      const [publicaciones, premios, proyectos, corporaciones] = await Promise.allSettled([
        curriculumService.obtenerPublicaciones(),
        curriculumService.obtenerPremiosReconocimientos(),
        curriculumService.obtenerParticipacionesProyectos(),
        curriculumService.obtenerParticipacionesCorporaciones(),
      ]);

      if (!active) return;

      setSectionStatus({
        datosPersonales: {
          loading: false,
          completed: [datosBasicos, datosDemograficos, datosContacto].every(isFulfilled),
        },
        educacion: {
          loading: false,
          completed: [formaciones, educacionesTrabajo, idiomas]
            .filter(isFulfilled)
            .some((response) => hasItems(response.value)),
        },
        experiencia: {
          loading: false,
          completed: [experienciasLaborales, experienciasDocentes]
            .filter(isFulfilled)
            .some((response) => hasItems(response.value)),
        },
        gerenciaPublica: {
          loading: false,
          completed: [publicaciones, premios, proyectos, corporaciones]
            .filter(isFulfilled)
            .some((response) => hasItems(response.value)),
        },
      });
    };

    loadSectionStatus();

    return () => {
      active = false;
    };
  }, []);

  const sections = useMemo(
    () => [
      {
        key: "datosPersonales" as const,
        title: "Datos Personales",
        desc: "Información básica, demográfica y de contacto.",
        icon: "👤",
        path: "/curriculum/datos-personales",
      },
      {
        key: "educacion" as const,
        title: "Educación",
        desc: "Formación académica, estudios de posgrado e idiomas.",
        icon: "🎓",
        path: "/curriculum/educacion",
      },
      {
        key: "experiencia" as const,
        title: "Experiencia Laboral",
        desc: "Historial de empleos en sector público y privado.",
        icon: "💼",
        path: "/curriculum/experiencia",
      },
      {
        key: "gerenciaPublica" as const,
        title: "Gerencia Pública",
        desc: "Publicaciones, reconocimientos, proyectos y participación institucional.",
        icon: "🏛️",
        path: "/curriculum/gerencia-publica",
      },
    ],
    []
  );

  const completedSections = sections.filter((section) => sectionStatus[section.key].completed).length;
  const loadingSections = sections.some((section) => sectionStatus[section.key].loading);

  const stats = [
    {
      label: "Secciones Completadas",
      value: loadingSections ? "Cargando..." : `${completedSections} / ${sections.length}`,
      iconClass: "blue",
      icon: "📋",
    },
    { label: "Documentos Adjuntos", value: "0", iconClass: "green", icon: "📎" },
    { label: "Última actualización", value: "—", iconClass: "amber", icon: "🕐" },
    { label: "Estado del perfil", value: "Activo", iconClass: "green", icon: "✅" },
  ];

  const handleDownloadPdf = async () => {
    try {
      setPdfError(null);
      setDownloadingPdf(true);
      await curriculumService.descargarHojaVidaPdf();
    } catch (error) {
      setPdfError(getApiError(error));
    } finally {
      setDownloadingPdf(false);
    }
  };

  return (
    <AppLayout title="Inicio">
      <div className="page-header animate-in">
        <div style={{ display: "flex", justifyContent: "space-between", gap: 16, alignItems: "flex-start", flexWrap: "wrap" }}>
          <div>
            <h2>Hoja de Vida</h2>
            <p>Bienvenido al Sistema de Gestión del Empleo Público · {user?.numeroIdentificacion}</p>
          </div>
          <button
            type="button"
            className="btn btn-primary"
            onClick={handleDownloadPdf}
            disabled={downloadingPdf}
          >
            {downloadingPdf ? "Generando PDF..." : "Descargar hoja de vida"}
          </button>
        </div>
        {pdfError && (
          <div className="alert alert-info" style={{ marginTop: 16 }}>
            {pdfError}
          </div>
        )}
      </div>

      {/* Stats */}
      <div className="stats-grid">
        {stats.map((s, i) => (
          <div key={i} className={`stat-card animate-in animate-in-delay-${i + 1}`}>
            <div className={`stat-icon ${s.iconClass}`} style={{ fontSize: "1.3rem" }}>
              {s.icon}
            </div>
            <div>
              <div className="stat-value" style={{ fontSize: "1.3rem" }}>{s.value}</div>
              <div className="stat-label">{s.label}</div>
            </div>
          </div>
        ))}
      </div>

      {/* Hoja de vida */}
      <div className="page-header">
        <h3>Secciones de la Hoja de Vida</h3>
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))", gap: 16 }}>
        {sections.map((sec, i) => {
          const status = sectionStatus[sec.key];
          const label = status.loading ? "Verificando..." : status.completed ? "Completo" : "Incompleto";
          const statusClass = status.loading ? "badge-blue" : status.completed ? "badge-green" : "badge-amber";
          const buttonLabel = status.completed ? "Ver sección →" : "Completar sección →";

          return (
            <div
              key={sec.path}
              className={`card animate-in animate-in-delay-${i + 1}`}
              style={{ cursor: "pointer", transition: "box-shadow 180ms ease" }}
              onClick={() => navigate(sec.path)}
              onMouseEnter={e => (e.currentTarget.style.boxShadow = "var(--shadow-md)")}
              onMouseLeave={e => (e.currentTarget.style.boxShadow = "var(--shadow-sm)")}
            >
              <div className="card-body" style={{ padding: "24px" }}>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", marginBottom: 12 }}>
                  <span style={{ fontSize: "2rem" }}>{sec.icon}</span>
                  <span className={`badge ${statusClass}`}>{label}</span>
                </div>
                <h3 style={{ marginBottom: 6, fontSize: "1rem" }}>{sec.title}</h3>
                <p className="text-sm text-muted" style={{ marginBottom: 16 }}>{sec.desc}</p>
                <button className="btn btn-secondary btn-sm">
                  {buttonLabel}
                </button>
              </div>
            </div>
          );
        })}
      </div>

      {/* Info Alert */}
      <div className="alert alert-info animate-in" style={{ marginTop: 24 }}>
        <svg width="18" height="18" fill="currentColor" viewBox="0 0 20 20" style={{ flexShrink: 0, marginTop: 1 }}>
          <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
        </svg>
        <span>
          Complete todas las secciones de su hoja de vida para participar en convocatorias y procesos de selección del empleo público en Colombia.
        </span>
      </div>
    </AppLayout>
  );
};

export default DashboardPage;
