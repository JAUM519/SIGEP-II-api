import axios from "axios";
import type {
  ArchivoResponse,
  ActualizarDatosBasicosRequest,
  ActualizarDatosContactoRequest,
  ActualizarDatosDemograficosRequest,
  ActualizarEducacionTrabajoRequest,
  ActualizarExperienciaLaboralDocenteRequest,
  ActualizarExperienciaLaboralRequest,
  ActualizarFormacionAcademicaRequest,
  ActualizarIdiomaRequest,
  CambiarContrasenaRequest,
  InhabilitarUsuarioRequest,
  LoginRequest,
  LoginResponse,
  NuevoUsuarioRequest,
  PedirEnlaceEmailRequest,
  RegistrarDatosBasicosRequest,
  RegistrarDatosContactoRequest,
  RegistrarDatosDemograficosRequest,
  RegistrarEducacionTrabajoRequest,
  RegistrarExperienciaLaboralDocenteRequest,
  RegistrarExperienciaLaboralRequest,
  RegistrarFormacionAcademicaRequest,
  RegistrarIdiomaRequest,
  RegistrarParticipacionCorporacionEntidadRequest,
  RegistrarParticipacionProyectoRequest,
  RegistrarPremioReconocimientoRequest,
  RegistrarPublicacionRequest,
} from "../types";

const BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";
const STORAGE_KEY = "sigep_user";

const decodeJwtPayload = (token: string): { exp?: number } | null => {
  try {
    const payload = token.split(".")[1];
    if (!payload) return null;

    const normalized = payload.replace(/-/g, "+").replace(/_/g, "/");
    const padded = normalized.padEnd(normalized.length + ((4 - (normalized.length % 4)) % 4), "=");

    return JSON.parse(window.atob(padded));
  } catch {
    return null;
  }
};

const isJwtExpired = (token: string): boolean => {
  const payload = decodeJwtPayload(token);

  if (!payload?.exp) return true;

  // Margen de 30 segundos para evitar llamadas con token vencido o por vencer.
  return Date.now() >= payload.exp * 1000 - 30_000;
};

const redirectToLogin = () => {
  localStorage.removeItem(STORAGE_KEY);

  if (window.location.pathname !== "/login") {
    window.location.href = "/login";
  }
};

const api = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  const publicRoutes = [
    "/api/auth/login",
    "/api/auth/pedirEnlace",
    "/api/auth/recuperarContraseña",
  ];

  const isPublicRoute = publicRoutes.some((route) => config.url?.includes(route));

  if (isPublicRoute) {
    delete config.headers.Authorization;
    return config;
  }

  const stored = localStorage.getItem(STORAGE_KEY);

  if (stored) {
    try {
      const user = JSON.parse(stored);

      if (user?.token) {
        if (isJwtExpired(user.token)) {
          redirectToLogin();
          throw new axios.CanceledError("Sesión expirada. Inicia sesión nuevamente.");
        }

        config.headers.Authorization = `Bearer ${user.token}`;
      }
    } catch {
      localStorage.removeItem(STORAGE_KEY);
    }
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      redirectToLogin();
    }

    return Promise.reject(error);
  }
);

const cleanErrorMessage = (message: string): string => {
  const normalized = message.toLowerCase();

  if (
    normalized.includes("no se ha encontrado un curriculum") ||
    normalized.includes("curriculum registrado")
  ) {
    return "Primero guarda los datos básicos para poder continuar con esta sección.";
  }

  if (normalized.includes("ya tiene datos basicos") || normalized.includes("ya tiene datos básicos")) {
    return "Esta información ya estaba guardada. Puedes actualizarla desde esta misma sección.";
  }

  if (normalized.includes("formato inválido") || normalized.includes("json parse") || normalized.includes("not valid")) {
    return "Revisa los campos del formulario. Hay información incompleta o con un formato no válido.";
  }

  if (normalized.includes("sesión expirada") || normalized.includes("token")) {
    return "Tu sesión venció. Inicia sesión nuevamente para continuar.";
  }

  if (
    normalized.includes("backend") ||
    normalized.includes("spring") ||
    normalized.includes("cors") ||
    normalized.includes("endpoint") ||
    normalized.includes("http") ||
    normalized.includes("api") ||
    normalized.includes("exception") ||
    normalized.includes("stack")
  ) {
    return "No fue posible completar la acción. Inténtalo nuevamente o vuelve a iniciar sesión.";
  }

  return message;
};

export const getApiError = (error: unknown): string => {
  if (axios.isCancel(error)) {
    return cleanErrorMessage(error.message || "La acción fue cancelada.");
  }

  if (axios.isAxiosError(error)) {
    if (error.code === "ERR_NETWORK" || !error.response) {
      return "No fue posible conectar con el sistema. Verifica tu conexión e inténtalo nuevamente.";
    }

    const data = error.response?.data;

    if (typeof data === "string") return cleanErrorMessage(data);
    if (typeof data?.message === "string") return cleanErrorMessage(data.message);
    if (typeof data?.error === "string") return cleanErrorMessage(data.error);

    if (data && typeof data === "object") {
      const details = Object.entries(data)
        .map(([key, value]) => `${key}: ${Array.isArray(value) ? value.join(", ") : String(value)}`)
        .join(" | ");

      return cleanErrorMessage(details) || "Revisa los campos del formulario e inténtalo nuevamente.";
    }

    return "No fue posible completar la acción. Inténtalo nuevamente.";
  }

  return "No fue posible completar la acción. Inténtalo nuevamente.";
};

export const toInstant = (date?: string): string | undefined => {
  if (!date) return undefined;
  return new Date(`${date}T00:00:00.000Z`).toISOString();
};

export const removeEmpty = <T extends object>(data: T): Partial<T> => {
  return Object.fromEntries(
    Object.entries(data).filter(([, value]) => value !== "" && value !== undefined && value !== null)
  ) as Partial<T>;
};

// Auth
export const authService = {
  login: (data: LoginRequest) =>
    api.post<LoginResponse>("/api/auth/login", data).then((r) => r.data),

  crearUsuario: (data: NuevoUsuarioRequest) =>
    api.post("/api/auth/registro", data).then((r) => r.data),

  pedirEnlace: (data: PedirEnlaceEmailRequest) =>
    api.post<string>("/api/auth/pedirEnlace", data).then((r) => r.data),

  recuperarContrasena: (token: string, data: CambiarContrasenaRequest) =>
    api.post<string>("/api/auth/recuperarContraseña", data, { params: { token } }).then((r) => r.data),

  cambiarContraseña: (data: CambiarContrasenaRequest) =>
    api.put("/api/auth/cambiarContraseña", data).then((r) => r.data),

  inhabilitarUsuario: (data: InhabilitarUsuarioRequest) =>
    api.put("/api/auth/inhabilitarUsuario", data).then((r) => r.data),
};


// Archivos
export const fileService = {
  subirArchivo: async (archivo: File): Promise<ArchivoResponse> => {
    const formData = new FormData();
    formData.append("archivo", archivo);

    const response = await api.post<ArchivoResponse>("/api/archivos", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    return response.data;
  },

  verArchivo: async (url: string): Promise<void> => {
    const response = await api.get<Blob>(url, { responseType: "blob" });
    const objectUrl = URL.createObjectURL(response.data);
    window.open(objectUrl, "_blank", "noopener,noreferrer");

    window.setTimeout(() => URL.revokeObjectURL(objectUrl), 60_000);
  },
};

// Curriculum
export const curriculumService = {
  obtenerDatosBasicos: () =>
    api.get("/api/curriculum/datosPersonales/datosBasicos").then((r) => r.data),

  obtenerDatosDemograficos: () =>
    api.get("/api/curriculum/datosPersonales/datosDemograficos").then((r) => r.data),

  obtenerDatosContacto: () =>
    api.get("/api/curriculum/datosPersonales/datosContacto").then((r) => r.data),

  registrarDatosBasicos: (data: RegistrarDatosBasicosRequest) =>
    api.post("/api/curriculum/datosPersonales/datosBasicos", data).then((r) => r.data),

  actualizarDatosBasicos: (data: ActualizarDatosBasicosRequest) =>
    api.put("/api/curriculum/datosPersonales/datosBasicos", removeEmpty(data)).then((r) => r.data),

  registrarDatosDemograficos: (data: RegistrarDatosDemograficosRequest) =>
    api.post("/api/curriculum/datosPersonales/datosDemograficos", data).then((r) => r.data),

  actualizarDatosDemograficos: (data: ActualizarDatosDemograficosRequest) =>
    api.put("/api/curriculum/datosPersonales/datosDemograficos", removeEmpty(data)).then((r) => r.data),

  registrarDatosContacto: (data: RegistrarDatosContactoRequest) =>
    api.post("/api/curriculum/datosPersonales/datosContacto", data).then((r) => r.data),

  actualizarDatosContacto: (data: ActualizarDatosContactoRequest) =>
    api.put("/api/curriculum/datosPersonales/datosContacto", removeEmpty(data)).then((r) => r.data),

  obtenerFormacionesAcademicas: () =>
    api.get("/api/curriculum/educacion/formacionAcademica").then((r) => r.data),

  obtenerEducacionesTrabajo: () =>
    api.get("/api/curriculum/educacion/trabajo").then((r) => r.data),

  obtenerIdiomas: () =>
    api.get("/api/curriculum/educacion/idioma").then((r) => r.data),

  registrarFormacionAcademica: (data: RegistrarFormacionAcademicaRequest) =>
    api.post("/api/curriculum/educacion/formacionAcademica", removeEmpty(data)).then((r) => r.data),

  actualizarFormacionAcademica: (data: ActualizarFormacionAcademicaRequest) =>
    api.put("/api/curriculum/educacion/formacionAcademica", removeEmpty(data)).then((r) => r.data),

  registrarEducacionTrabajo: (data: RegistrarEducacionTrabajoRequest) =>
    api.post("/api/curriculum/educacion/trabajo", removeEmpty(data)).then((r) => r.data),

  actualizarEducacionTrabajo: (data: ActualizarEducacionTrabajoRequest) =>
    api.put("/api/curriculum/educacion/trabajo", removeEmpty(data)).then((r) => r.data),

  registrarIdioma: (data: RegistrarIdiomaRequest) =>
    api.post("/api/curriculum/educacion/idioma", removeEmpty(data)).then((r) => r.data),

  actualizarIdioma: (data: ActualizarIdiomaRequest) =>
    api.put("/api/curriculum/educacion/idioma", removeEmpty(data)).then((r) => r.data),

  obtenerExperienciasLaborales: () =>
    api.get("/api/curriculum/experienciaLaboral").then((r) => r.data),

  obtenerExperienciasDocentes: () =>
    api.get("/api/curriculum/experienciaLaboral/docente").then((r) => r.data),

  registrarExperienciaLaboral: (data: RegistrarExperienciaLaboralRequest) =>
    api.post("/api/curriculum/experienciaLaboral", removeEmpty(data)).then((r) => r.data),

  actualizarExperienciaLaboral: (data: ActualizarExperienciaLaboralRequest) =>
    api.put("/api/curriculum/experienciaLaboral", removeEmpty(data)).then((r) => r.data),

  registrarExperienciaLaboralDocente: (data: RegistrarExperienciaLaboralDocenteRequest) =>
    api.post("/api/curriculum/experienciaLaboral/docente", removeEmpty(data)).then((r) => r.data),

  actualizarExperienciaLaboralDocente: (data: ActualizarExperienciaLaboralDocenteRequest) =>
    api.put("/api/curriculum/experienciaLaboral/docente", removeEmpty(data)).then((r) => r.data),

  obtenerPublicaciones: () =>
    api.get("/api/curriculum/gerenciaPublica/publicacion").then((r) => r.data),

  obtenerPremiosReconocimientos: () =>
    api.get("/api/curriculum/gerenciaPublica/premioReconocimiento").then((r) => r.data),

  obtenerParticipacionesProyectos: () =>
    api.get("/api/curriculum/gerenciaPublica/participacionProyecto").then((r) => r.data),

  obtenerParticipacionesCorporaciones: () =>
    api.get("/api/curriculum/gerenciaPublica/participacionCorporacionEntidad").then((r) => r.data),

  registrarPublicacion: (data: RegistrarPublicacionRequest) =>
    api.post("/api/curriculum/gerenciaPublica/publicacion", data).then((r) => r.data),

  registrarPremioReconocimiento: (data: RegistrarPremioReconocimientoRequest) =>
    api.post("/api/curriculum/gerenciaPublica/premioReconocimiento", {
      ...data,
      fecha: toInstant(data.fecha),
    }).then((r) => r.data),

  registrarParticipacionProyecto: (data: RegistrarParticipacionProyectoRequest) =>
    api.post("/api/curriculum/gerenciaPublica/participacionProyecto", {
      ...data,
      fechaInicio: toInstant(data.fechaInicio),
      fechaTerminacion: toInstant(data.fechaTerminacion),
    }).then((r) => r.data),

  registrarParticipacionCorporacionEntidad: (data: RegistrarParticipacionCorporacionEntidadRequest) =>
    api.post("/api/curriculum/gerenciaPublica/participacionCorporacionEntidad", data).then((r) => r.data),

  descargarHojaVidaPdf: async (): Promise<void> => {
    const response = await api.get<Blob>("/api/curriculum/pdf", {
      responseType: "blob",
    });

    const objectUrl = URL.createObjectURL(response.data);
    const link = document.createElement("a");
    link.href = objectUrl;
    link.download = "hoja-de-vida.pdf";
    document.body.appendChild(link);
    link.click();
    link.remove();

    window.setTimeout(() => URL.revokeObjectURL(objectUrl), 60_000);
  },
};

export default api;
