import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";

import LoginPage           from "./pages/auth/LoginPage";
import DashboardPage       from "./pages/dashboard/DashboardPage";
import DatosPersonalesPage from "./pages/curriculum/DatosPersonalesPage";
import EducacionPage       from "./pages/curriculum/EducacionPage";
import ExperienciaPage     from "./pages/curriculum/ExperienciaPage";

import "./styles/global.css";

const App: React.FC = () => (
  <AuthProvider>
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/dashboard" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
        <Route path="/curriculum/datos-personales" element={<ProtectedRoute><DatosPersonalesPage /></ProtectedRoute>} />
        <Route path="/curriculum/educacion" element={<ProtectedRoute><EducacionPage /></ProtectedRoute>} />
        <Route path="/curriculum/experiencia" element={<ProtectedRoute><ExperienciaPage /></ProtectedRoute>} />
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  </AuthProvider>
);

export default App;
