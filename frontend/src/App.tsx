import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import { RolUsuario } from "./types";

import LoginPage           from "./pages/auth/LoginPage";
import ForgotPasswordPage  from "./pages/auth/ForgotPasswordPage";
import ResetPasswordPage   from "./pages/auth/ResetPasswordPage";
import DashboardPage       from "./pages/dashboard/DashboardPage";
import DatosPersonalesPage from "./pages/curriculum/DatosPersonalesPage";
import EducacionPage       from "./pages/curriculum/EducacionPage";
import ExperienciaPage     from "./pages/curriculum/ExperienciaPage";
import GerenciaPublicaPage from "./pages/curriculum/GerenciaPublicaPage";
import CreateUserPage      from "./pages/usuarios/CreateUserPage";
import DisableUserPage     from "./pages/usuarios/DisableUserPage";
import ChangePasswordPage  from "./pages/usuarios/ChangePasswordPage";

import "./styles/global.css";

const App: React.FC = () => (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/recuperar" element={<ForgotPasswordPage />} />
          <Route path="/recuperar-contraseña" element={<ResetPasswordPage />} />
          <Route path="/recuperar-contrasena" element={<ResetPasswordPage />} />
          <Route path="/dashboard" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
          <Route path="/curriculum/datos-personales" element={<ProtectedRoute><DatosPersonalesPage /></ProtectedRoute>} />
          <Route path="/curriculum/educacion" element={<ProtectedRoute><EducacionPage /></ProtectedRoute>} />
          <Route path="/curriculum/experiencia" element={<ProtectedRoute><ExperienciaPage /></ProtectedRoute>} />
          <Route path="/curriculum/gerencia-publica" element={<ProtectedRoute><GerenciaPublicaPage /></ProtectedRoute>} />
          <Route path="/usuarios/crear" element={<ProtectedRoute allowedRoles={[RolUsuario.JefeDeTalentoHumano]}><CreateUserPage /></ProtectedRoute>} />
          <Route path="/usuarios/inhabilitar" element={<ProtectedRoute allowedRoles={[RolUsuario.JefeDeTalentoHumano]}><DisableUserPage /></ProtectedRoute>} />
          <Route path="/perfil/cambiar-contrasena" element={<ProtectedRoute><ChangePasswordPage /></ProtectedRoute>} />
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
);

export default App;