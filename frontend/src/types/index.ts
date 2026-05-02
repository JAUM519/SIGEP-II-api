// ─── Enums ────────────────────────────────────────────────────────────────────

export enum TipoIdentificacion {
  CedulaDeCiudadania = "CedulaDeCiudadania",
  CedulaDeExtranjeria = "CedulaDeExtranjeria",
  CedulaMilitar = "CedulaMilitar",
  Pasaporte = "Pasaporte",
  TarjetaDeIdentidad = "TarjetaDeIdentidad",
  TarjetaExtranjeria = "TarjetaExtranjeria",
  PermisoDeProteccionTemporal = "PermisoDeProteccionTemporal",
  RegistroCivil = "RegistroCivil",
  Nuip = "Nuip",
  Otro = "Otro",
}

export const TipoIdentificacionLabels: Record<TipoIdentificacion, string> = {
  [TipoIdentificacion.CedulaDeCiudadania]: "Cédula de Ciudadanía",
  [TipoIdentificacion.CedulaDeExtranjeria]: "Cédula de Extranjería",
  [TipoIdentificacion.CedulaMilitar]: "Cédula Militar",
  [TipoIdentificacion.Pasaporte]: "Pasaporte",
  [TipoIdentificacion.TarjetaDeIdentidad]: "Tarjeta de Identidad",
  [TipoIdentificacion.TarjetaExtranjeria]: "Tarjeta Extranjería",
  [TipoIdentificacion.PermisoDeProteccionTemporal]: "Permiso de Protección Temporal",
  [TipoIdentificacion.RegistroCivil]: "Registro Civil",
  [TipoIdentificacion.Nuip]: "NUIP",
  [TipoIdentificacion.Otro]: "Otro",
};

export enum RolUsuario {
  ServidorPublico = "servidorPublico",
  JefeDeTalentoHumano = "jefeDeTalentoHumano",
}

export enum Genero {
  Masculino = "Masculino",
  Femenino = "Femenino",
  NoConforme = "NoConforme",
  Otro = "Otro",
}

export enum EstadoCivil {
  Soltero = "Soltero",
  Casado = "Casado",
  UnionLibre = "UnionLibre",
  Divorciado = "Divorciado",
  Viudo = "Viudo",
}

export enum PreferenciaEtnica {
  Ninguna = "Ninguna",
  Indigena = "Indigena",
  Afrocolombiano = "Afrocolombiano",
  Raizal = "Raizal",
  Palenquero = "Palenquero",
  ROM = "ROM",
}

export enum ClaseLibretaMilitar {
  Primera = "Primera",
  Segunda = "Segunda",
}

// ─── Auth ─────────────────────────────────────────────────────────────────────

export interface LoginRequest {
  tipoDocumento: TipoIdentificacion;
  numeroIdentificacion: string;
  contraseña: string;
}

export interface LoginResponse {
  tipoIdentificacion: TipoIdentificacion;
  numeroIdentificacion: string;
  token: string;
}

export interface NuevoUsuarioRequest {
  tipoIdentificacion: TipoIdentificacion;
  numeroIdentificacion: string;
  email: string;
  contraseña: string;
}

// ─── Curriculum ───────────────────────────────────────────────────────────────

export interface DatosBasicos {
  nombre: string;
  tipoIdentificacion: TipoIdentificacion;
  numeroIdentificacion: string;
  fechaNacimiento: string;
  email: string;
  genero: Genero;
  claseLibretaMilitar?: ClaseLibretaMilitar;
  numeroLibretaMilitar?: string;
  distritoMilitar?: number;
  documentoIdentificacion?: string;
  documentoVerificado: boolean;
  libretaMilitar?: string;
  libretaVerificada: boolean;
  personaExpuestaPoliticamente: boolean;
}

export interface DatosDemograficos {
  nacionalidad: string;
  estadoCivil: EstadoCivil;
  preferenciaEtnica: PreferenciaEtnica;
  paisNacimiento: string;
  departamentoNacimiento: string;
  municipioNacimiento: string;
  discapacidad: boolean;
}

export interface DatosContacto {
  paisResidencia: string;
  departamentoResidencia: string;
  municipioResidencia: string;
  zona: string;
  direccionResidencia: string;
  telefonoResidencia: string;
  celular: string;
  telefonoOficina?: string;
  extension?: string;
  emailPersonalPrincipal: string;
  emailOficina?: string;
}

export interface DatosPersonales {
  datosBasicos: DatosBasicos;
  datosDemograficos: DatosDemograficos;
  datosContacto: DatosContacto;
}

export interface FormacionAcademica {
  nivelAcademico: string;
  nivelFormacion: string;
  areaConocimiento: string;
  pais: string;
  institucionFormacionAcademica: string;
  programaAcademico: string;
  tituloObtenido: string;
  estadoEstudio: string;
  fechaGrado?: string;
  estudioConvalidado: boolean;
}

export interface Idioma {
  idioma: string;
  fechaCertificado: string;
  conversacion: string;
}

export interface EducacionTrabajo {
  nivelAcademico: string;
  areaConocimiento: string;
}

export interface Educacion {
  formacionesAcademicas: FormacionAcademica[];
  educacionTrabajos: EducacionTrabajo[];
  idiomas: Idioma[];
}

export interface ExperienciaLaboral {
  tipoEntidad: string;
  nombreEntidad: string;
  pais: string;
  departamento: string;
  municipio: string;
  direccionEntidad: string;
  dependencia: string;
  nivelJerarquiaEmpleo: string;
  cargo: string;
  telefono: string;
  trabajoActual: string;
  fechaIngreso: string;
  fechaRetiro?: string;
  jornadaLaboral: string;
  horasPromedioMes: number;
  tiempoExperiencia: number;
  motivoRetiro?: string;
}

export interface ExperienciaLaboralDocente {
  tipoInstitucion: string;
  nombreInstitucion: string;
  pais: string;
  departamento: string;
  municipio: string;
  nivelAcademico: string;
  areaConocimiento: string;
  tipoZona: string;
  trabajoActual: string;
  fechaIngreso: string;
  fechaTerminacion?: string;
  jornadaLaboral: string;
  horasPromedioMes: number;
  materiaImpartida: string;
  tiempoExperiencia: number;
}

export interface Curriculum {
  id?: string;
  usuarioId: string;
  datosPersonales: DatosPersonales;
  educacion: Educacion;
  experienciasLaborales: ExperienciaLaboral[];
  experienciasLaboralesDocente: ExperienciaLaboralDocente[];
}

// ─── Auth Context ─────────────────────────────────────────────────────────────

export interface AuthUser {
  tipoIdentificacion: TipoIdentificacion;
  numeroIdentificacion: string;
  token: string;
}
