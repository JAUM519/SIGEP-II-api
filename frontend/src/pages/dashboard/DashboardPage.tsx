import React from "react";
import { useNavigate } from "react-router-dom";
import AppLayout from "../../components/layout/AppLayout";
import { useAuth } from "../../hooks/useAuth";

const DashboardPage: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const sections = [
    {
      title: "Datos Personales",
      desc: "Información básica, demográfica y de contacto del servidor.",
      icon: "👤",
      path: "/curriculum/datos-personales",
      status: "Incompleto",
      statusClass: "badge-amber",
    },
    {
      title: "Educación",
      desc: "Formación académica, estudios de posgrado e idiomas.",
      icon: "🎓",
      path: "/curriculum/educacion",
      status: "Incompleto",
      statusClass: "badge-amber",
    },
    {
      title: "Experiencia Laboral",
      desc: "Historial de empleos en sector público y privado.",
      icon: "💼",
      path: "/curriculum/experiencia",
      status: "Incompleto",
      statusClass: "badge-amber",
    },
  ];

  return (
    <AppLayout title="Dashboard">
      <div className="page-header animate-in">
        <h2>Hoja de Vida</h2>
        <p>Bienvenido al Sistema de Gestión del Empleo Público · {user?.numeroIdentificacion}</p>
      </div>

      {/* Stats */}
      <div className="stats-grid">
        {[
          { label: "Secciones Completadas", value: "0 / 3", iconClass: "blue", icon: "📋" },
          { label: "Documentos Adjuntos",   value: "0",     iconClass: "green", icon: "📎" },
          { label: "Última actualización",  value: "—",     iconClass: "amber", icon: "🕐" },
          { label: "Estado del perfil",     value: "Activo",iconClass: "green", icon: "✅" },
        ].map((s, i) => (
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

      {/* Curriculum Sections */}
      <div className="page-header">
        <h3>Secciones de la Hoja de Vida</h3>
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))", gap: 16 }}>
        {sections.map((sec, i) => (
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
                <span className={`badge ${sec.statusClass}`}>{sec.status}</span>
              </div>
              <h3 style={{ marginBottom: 6, fontSize: "1rem" }}>{sec.title}</h3>
              <p className="text-sm text-muted" style={{ marginBottom: 16 }}>{sec.desc}</p>
              <button className="btn btn-secondary btn-sm">
                Completar sección →
              </button>
            </div>
          </div>
        ))}
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
