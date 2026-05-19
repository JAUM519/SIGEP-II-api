import axios from "axios";
import type {
  ActualizarDatosBasicosRequest,
  ActualizarDatosContactoRequest,
  ActualizarDatosDemograficosRequest,
  ActualizarEducacionTrabajoRequest,
  ActualizarExperienciaLaboralDocenteRequest,
  ActualizarExperienciaLaboralRequest,
  ActualizarFormacionAcademicaRequest,
  ActualizarIdiomaRequest,
  CambiarContrasenaRequest,
  Curriculum,
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
      localStorage.removeItem(STORAGE_KEY);

      if (window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
    }

    return Promise.reject(error);
  }
);

export const getApiError = (error: unknown): string => {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data;

    if (typeof data === "string") return data;
    if (typeof data?.message === "string") return data.message;
    if (typeof data?.error === "string") return data.error;

    if (data && typeof data === "object") {
      return Object.entries(data)
        .map(([key, value]) => `${key}: ${Array.isArray(value) ? value.join(", ") : String(value)}`)
        .join(" | ");
    }

    return error.message || "Error en la solicitud.";
  }

  return "Error inesperado.";
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

// Curriculum
export const curriculumService = {
  obtenerMiCurriculum: () =>
    api.get<Curriculum>("/api/curriculum/me").then((r) => r.data),

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

  registrarExperienciaLaboral: (data: RegistrarExperienciaLaboralRequest) =>
    api.post("/api/curriculum/experienciaLaboral", removeEmpty(data)).then((r) => r.data),

  actualizarExperienciaLaboral: (data: ActualizarExperienciaLaboralRequest) =>
    api.put("/api/curriculum/experienciaLaboral", removeEmpty(data)).then((r) => r.data),

  registrarExperienciaLaboralDocente: (data: RegistrarExperienciaLaboralDocenteRequest) =>
    api.post("/api/curriculum/experienciaLaboral/docente", removeEmpty(data)).then((r) => r.data),

  actualizarExperienciaLaboralDocente: (data: ActualizarExperienciaLaboralDocenteRequest) =>
    api.put("/api/curriculum/experienciaLaboral/docente", removeEmpty(data)).then((r) => r.data),

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
};

export default api;
