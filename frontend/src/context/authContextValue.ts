import { createContext } from "react";
import type { AuthUser } from "../types";
import type { RolUsuario } from "../types";

export interface AuthContextType {
  user: AuthUser | null;
  login: (loginResponse: import("../types").LoginResponse) => void;
  logout: () => void;
  isAuthenticated: boolean;
  hasRole: (roles: RolUsuario[]) => boolean;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
