import axios from "axios";
import type { LoginRequest, LoginResponse, NuevoUsuarioRequest } from "../types";

const BASE_URL = import.meta.env.VITE_API_URL;

const api = axios.create({
  baseURL: BASE_URL,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  const stored = localStorage.getItem("sigep_user");
  if (stored) {
    const user = JSON.parse(stored);
    if (user.token) {
      config.headers.Authorization = `Bearer ${user.token}`;
    }
  }
  return config;
});

// Auth
export const authService = {
  login: (data: LoginRequest) =>
    api.post<LoginResponse>("/api/auth/login", data).then((r) => r.data),

  crearUsuario: (data: NuevoUsuarioRequest) =>
    api.post("/api/auth/registro", data).then((r) => r.data),
};

export default api;
