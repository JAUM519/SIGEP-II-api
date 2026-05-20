// ─── Enums ────────────────────────────────────────────────────────────────────

export enum TipoIdentificacion {
  CedulaDeCiudadania = "CedulaDeCiudadania",
  CedulaDeExtranjeria = "CedulaDeExtranjeria",
  CedulaMilitar = "CedulaMilitar",
  CertificadoAntecedentesAbogado = "CertificadoAntecedentesAbogado",
  CertificadoSobreAntecedentesDisciplinarios = "CertificadoSobreAntecedentesDisciplinarios",
  CertificadoSobreAntecedentesFiscales = "CertificadoSobreAntecedentesFiscales",
  EducacionComplementariaONoFormal = "EducacionComplementariaONoFormal",
  LibretaMilitar = "LibretaMilitar",
  LicenciaConduccionMotocicleta = "LicenciaConduccionMotocicleta",
  LicenciadeConduccion = "LicenciadeConduccion",
  MatriculaProfesional = "MatriculaProfesional",
  NumeroDeIdentificacionTributaria = "NumeroDeIdentificacionTributaria",
  NitEntidadPrivada = "NitEntidadPrivada",
  Nuip = "Nuip",
  Otro = "Otro",
  Pasaporte = "Pasaporte",
  PasadoJudicial = "PasadoJudicial",
  PermisoDeProteccionTemporal = "PermisoDeProteccionTemporal",
  RegistroCivil = "RegistroCivil",
  RegistroProfesional = "RegistroProfesional",
  TarjetaDeIdentidad = "TarjetaDeIdentidad",
  TarjetaExtranjeria = "TarjetaExtranjeria",
}

export const TipoIdentificacionLabels: Record<TipoIdentificacion, string> = {
  [TipoIdentificacion.CedulaDeCiudadania]: "Cédula de Ciudadanía",
  [TipoIdentificacion.CedulaDeExtranjeria]: "Cédula de Extranjería",
  [TipoIdentificacion.CedulaMilitar]: "Cédula Militar",
  [TipoIdentificacion.CertificadoAntecedentesAbogado]: "Certificado Antecedentes Abogado",
  [TipoIdentificacion.CertificadoSobreAntecedentesDisciplinarios]: "Certificado Antecedentes Disciplinarios",
  [TipoIdentificacion.CertificadoSobreAntecedentesFiscales]: "Certificado Antecedentes Fiscales",
  [TipoIdentificacion.EducacionComplementariaONoFormal]: "Educación Complementaria o No Formal",
  [TipoIdentificacion.LibretaMilitar]: "Libreta Militar",
  [TipoIdentificacion.LicenciaConduccionMotocicleta]: "Licencia de Conducción Motocicleta",
  [TipoIdentificacion.LicenciadeConduccion]: "Licencia de Conducción",
  [TipoIdentificacion.MatriculaProfesional]: "Matrícula Profesional",
  [TipoIdentificacion.NumeroDeIdentificacionTributaria]: "Número de Identificación Tributaria",
  [TipoIdentificacion.NitEntidadPrivada]: "NIT Entidad Privada",
  [TipoIdentificacion.Nuip]: "NUIP",
  [TipoIdentificacion.Otro]: "Otro",
  [TipoIdentificacion.Pasaporte]: "Pasaporte",
  [TipoIdentificacion.PasadoJudicial]: "Pasado Judicial",
  [TipoIdentificacion.PermisoDeProteccionTemporal]: "Permiso de Protección Temporal",
  [TipoIdentificacion.RegistroCivil]: "Registro Civil",
  [TipoIdentificacion.RegistroProfesional]: "Registro Profesional",
  [TipoIdentificacion.TarjetaDeIdentidad]: "Tarjeta de Identidad",
  [TipoIdentificacion.TarjetaExtranjeria]: "Tarjeta Extranjería",
};

export enum RolUsuario {
  ServidorPublico = "servidorPublico",
  JefeDeTalentoHumano = "jefeDeTalentoHumano",
}

export enum Genero {
  Masculino = "MASCULINO",
  Femenino = "FEMENINO",
}

export enum EstadoCivil {
  Soltero = "SOLTERO",
  Casado = "CASADO",
  UnionLibre = "UNIONLIBRE",
  Divorciado = "DIVORCIADO",
  Separado = "SEPARADO",
  Viudo = "VIUDO",
}

export enum PreferenciaEtnica {
  Ninguna = "NINGUNA",
  Afrocolombiano = "AFROCOLOMBIANO",
  Indigena = "INDIGENA",
  Palenquero = "PALENQUERO",
  Raizal = "RAIZAL",
  Rrom = "RROM",
}

export enum ClaseLibretaMilitar {
  Primera = "PRIMERA_CLASE",
  Segunda = "SEGUNDA_CLASE",
  Provisional = "PROVISIONAL",
}

export enum Zona {
  Urbana = "URBANA",
  Rural = "RURAL",
}

export enum NivelAcademico {
  Preescolar = "PREESCOLAR",
  BasicaPrimaria = "BASICA_PRIMARIA",
  BasicaSecundaria = "BASICA_SECUNDARIA",
  EducacionMedia = "EDUCACION_MEDIA",
  Pregrado = "PREGRADO",
  Postgrado = "POSTGRADO",
}

export enum NivelFormacion {
  Preescolar = "PREESCOLAR",
  Primero = "PRIMERO",
  Segundo = "SEGUNDO",
  Tercero = "TERCERO",
  Cuarto = "CUARTO",
  Quinto = "QUINTO",
  Sexto = "SEXTO",
  Septimo = "SEPTIMO",
  Octavo = "OCTAVO",
  Noveno = "NOVENO",
  Decimo = "DECIMO",
  Once = "ONCE",
  FormacionComplementaria = "FORMACION_COMPLEMENTARIA",
  Tecnico = "TECNICO",
  TecnicoProfesional = "TECNICO_PROFESIONAL",
  Tecnologo = "TECNOLOGO",
  Profesional = "PROFESIONAL",
  Especializacion = "ESPECIALIZACION",
  Maestria = "MAESTRIA",
  Doctorado = "DOCTORADO",
  Postdoctorado = "POSTDOCTORADO",
}

export enum AreaConocimiento {
  AgronomiaVeterinaria = "AGRONOMIA_VETERINARIA_Y_AFINES",
  BellasArtes = "BELLAS_ARTES",
  CienciasEducacion = "CIENCIAS_DE_LA_EDUCACION",
  CienciasSalud = "CIENCIAS_DE_LA_SALUD",
  CienciasSocialesHumanas = "CIENCIAS_SOCIALES_Y_HUMANAS",
  CienciasSocialesDerechoPoliticas = "CIENCIAS_SOCIALES_DERECHO_CIENCIAS_POLITICAS",
  CienciasHumanidadesPersonales = "CIENCIAS_Y_HUMANIDADES_PERSONALES",
  EconomiaAdministracionContaduria = "ECONOMIA_ADMINISTRACION_CONTADURIA_Y_AFINES",
  Generica = "GENERICA",
  HumanidadesCienciasReligiosas = "HUMANIDADES_Y_CIENCIAS_RELIGIOSAS",
  IngenieriaArquitecturaUrbanismo = "INGENIERIA_ARQUITECTURA_URBANISMO_Y_AFINES",
  MatematicasCienciasNaturales = "MATEMATICAS_Y_CIENCIAS_NATURALES",
  NoAplica = "NO_APLICA",
}

export enum EstadoEstudio {
  EnProceso = "En_proceso",
  Finalizado = "Finalizado",
}

export enum IdiomaNivel {
  Ninguno = "NINGUNO",
  Regular = "REGULAR",
  Bien = "BIEN",
  MuyBien = "MUY_BIEN",
}

export enum MedioCapacitacion {
  ADistancia = "A_DISTANCIA",
  Mixta = "MIXTA",
  Multimedia = "MULTIMEDIA",
  Otro = "OTRO",
  Presencial = "PRESENCIAL",
  Virtual = "VIRTUAL",
}

export enum ModalidadEducacionTrabajo {
  EducacionInformal = "EDUCACION_INFORMAL",
  EducacionTrabajoDesarrolloHumano = "EDUCACION_PARA_EL_TRABAJO_Y_DESARROLLO_HUMANO",
}

export enum TipoEntidad {
  Privada = "PRIVADA",
  PrivadaFuncionesPublicas = "PRIVADA_CON_FUNCIONES_PUBLICAS",
  Publica = "PUBLICA",
}

export enum TipoInstitucion {
  Privada = "PRIVADA",
  Publica = "PUBLICA",
}

export enum TipoZona {
  Urbana = "URBANA",
  Rural = "RURAL",
}

export enum JornadaLaboral {
  MedioTiempo = "MEDIO_TIEMPO",
  TiempoCompleto = "TIEMPO_COMPLETO",
  TiempoParcial = "TIEMPO_PARCIAL",
}

export enum NivelJerarquicoEmpleo {
  Administrativo = "ADMINISTRATIVO",
  Asesor = "ASESOR",
  Asistencial = "ASISTENCIAL",
  Directivo = "DIRECTIVO",
  Docente = "DOCENTE",
  Ejecutivo = "EJECUTIVO",
  NoAplica = "NO_APLICA",
  Operativo = "OPERATIVO",
  Profesional = "PROFESIONAL",
  Tecnico = "TÉCNICO",
  Otro = "OTRO",
}

export enum ArticuloPublicacion {
  Libro = "LIBRO",
  RevistaIndexada = "REVISTA_INDEXADA",
  RevistaNoIndexada = "REVISTA_NO_INDEXADA",
}

export enum LibroResultadoInvestigacion {
  ArticuloRevista = "ARTICULO_DE_REVISTA",
  CapituloLibro = "CAPITULO_EN_LIBRO_RESULTADO_DE_INVESTIGACION",
  LibroCompleto = "LIBRO_COMPLETO_RESULTADO_INVESTIGACION",
}

export enum TipoProduccionBibliografica {
  DocumentoTrabajo = "DOCUMENTO_TRABAJO",
  Otro = "OTRO",
  Traduccion = "TRADUCCION",
}

export enum TipoPremioReconocimiento {
  Premio = "Premio",
  Reconocimiento = "Reconocimiento",
}

// ─── Labels reutilizables ───────────────────────────────────────────────────

export const optionEntries = <T extends Record<string, string>>(labels: Record<T[keyof T], string>) =>
  Object.entries(labels) as Array<[T[keyof T], string]>;

export const GeneroLabels: Record<Genero, string> = {
  [Genero.Masculino]: "Masculino",
  [Genero.Femenino]: "Femenino",
};

export const EstadoCivilLabels: Record<EstadoCivil, string> = {
  [EstadoCivil.Soltero]: "Soltero(a)",
  [EstadoCivil.Casado]: "Casado(a)",
  [EstadoCivil.UnionLibre]: "Unión libre",
  [EstadoCivil.Divorciado]: "Divorciado(a)",
  [EstadoCivil.Separado]: "Separado(a)",
  [EstadoCivil.Viudo]: "Viudo(a)",
};

export const PreferenciaEtnicaLabels: Record<PreferenciaEtnica, string> = {
  [PreferenciaEtnica.Ninguna]: "Ninguna",
  [PreferenciaEtnica.Afrocolombiano]: "Afrocolombiano(a)",
  [PreferenciaEtnica.Indigena]: "Indígena",
  [PreferenciaEtnica.Palenquero]: "Palenquero(a)",
  [PreferenciaEtnica.Raizal]: "Raizal",
  [PreferenciaEtnica.Rrom]: "RROM / Gitano",
};

export const ZonaLabels: Record<Zona, string> = {
  [Zona.Urbana]: "Urbana",
  [Zona.Rural]: "Rural",
};

export const NivelAcademicoLabels: Record<NivelAcademico, string> = {
  [NivelAcademico.Preescolar]: "Preescolar",
  [NivelAcademico.BasicaPrimaria]: "Básica primaria",
  [NivelAcademico.BasicaSecundaria]: "Básica secundaria",
  [NivelAcademico.EducacionMedia]: "Educación media",
  [NivelAcademico.Pregrado]: "Pregrado",
  [NivelAcademico.Postgrado]: "Postgrado",
};

export const NivelFormacionLabels: Record<NivelFormacion, string> = {
  [NivelFormacion.Preescolar]: "Preescolar",
  [NivelFormacion.Primero]: "Primero",
  [NivelFormacion.Segundo]: "Segundo",
  [NivelFormacion.Tercero]: "Tercero",
  [NivelFormacion.Cuarto]: "Cuarto",
  [NivelFormacion.Quinto]: "Quinto",
  [NivelFormacion.Sexto]: "Sexto",
  [NivelFormacion.Septimo]: "Séptimo",
  [NivelFormacion.Octavo]: "Octavo",
  [NivelFormacion.Noveno]: "Noveno",
  [NivelFormacion.Decimo]: "Décimo",
  [NivelFormacion.Once]: "Once",
  [NivelFormacion.FormacionComplementaria]: "Formación complementaria",
  [NivelFormacion.Tecnico]: "Técnico",
  [NivelFormacion.TecnicoProfesional]: "Técnico profesional",
  [NivelFormacion.Tecnologo]: "Tecnólogo",
  [NivelFormacion.Profesional]: "Profesional",
  [NivelFormacion.Especializacion]: "Especialización",
  [NivelFormacion.Maestria]: "Maestría",
  [NivelFormacion.Doctorado]: "Doctorado",
  [NivelFormacion.Postdoctorado]: "Postdoctorado",
};

export const AreaConocimientoLabels: Record<AreaConocimiento, string> = {
  [AreaConocimiento.AgronomiaVeterinaria]: "Agronomía, veterinaria y afines",
  [AreaConocimiento.BellasArtes]: "Bellas artes",
  [AreaConocimiento.CienciasEducacion]: "Ciencias de la educación",
  [AreaConocimiento.CienciasSalud]: "Ciencias de la salud",
  [AreaConocimiento.CienciasSocialesHumanas]: "Ciencias sociales y humanas",
  [AreaConocimiento.CienciasSocialesDerechoPoliticas]: "Derecho y ciencias políticas",
  [AreaConocimiento.CienciasHumanidadesPersonales]: "Ciencias, humanidades y personales",
  [AreaConocimiento.EconomiaAdministracionContaduria]: "Economía, administración, contaduría y afines",
  [AreaConocimiento.Generica]: "Genérica",
  [AreaConocimiento.HumanidadesCienciasReligiosas]: "Humanidades y ciencias religiosas",
  [AreaConocimiento.IngenieriaArquitecturaUrbanismo]: "Ingeniería, arquitectura, urbanismo y afines",
  [AreaConocimiento.MatematicasCienciasNaturales]: "Matemáticas y ciencias naturales",
  [AreaConocimiento.NoAplica]: "No aplica",
};

export const EstadoEstudioLabels: Record<EstadoEstudio, string> = {
  [EstadoEstudio.EnProceso]: "En proceso",
  [EstadoEstudio.Finalizado]: "Finalizado",
};

export const IdiomaNivelLabels: Record<IdiomaNivel, string> = {
  [IdiomaNivel.Ninguno]: "Ninguno",
  [IdiomaNivel.Regular]: "Regular",
  [IdiomaNivel.Bien]: "Bien",
  [IdiomaNivel.MuyBien]: "Muy bien",
};

export const TipoEntidadLabels: Record<TipoEntidad, string> = {
  [TipoEntidad.Publica]: "Pública",
  [TipoEntidad.Privada]: "Privada",
  [TipoEntidad.PrivadaFuncionesPublicas]: "Privada con funciones públicas",
};

export const TipoInstitucionLabels: Record<TipoInstitucion, string> = {
  [TipoInstitucion.Publica]: "Pública",
  [TipoInstitucion.Privada]: "Privada",
};

export const TipoZonaLabels: Record<TipoZona, string> = {
  [TipoZona.Urbana]: "Urbana",
  [TipoZona.Rural]: "Rural",
};

export const JornadaLaboralLabels: Record<JornadaLaboral, string> = {
  [JornadaLaboral.TiempoCompleto]: "Tiempo completo",
  [JornadaLaboral.MedioTiempo]: "Medio tiempo",
  [JornadaLaboral.TiempoParcial]: "Tiempo parcial",
};

export const NivelJerarquicoEmpleoLabels: Record<NivelJerarquicoEmpleo, string> = {
  [NivelJerarquicoEmpleo.Administrativo]: "Administrativo",
  [NivelJerarquicoEmpleo.Asesor]: "Asesor",
  [NivelJerarquicoEmpleo.Asistencial]: "Asistencial",
  [NivelJerarquicoEmpleo.Directivo]: "Directivo",
  [NivelJerarquicoEmpleo.Docente]: "Docente",
  [NivelJerarquicoEmpleo.Ejecutivo]: "Ejecutivo",
  [NivelJerarquicoEmpleo.NoAplica]: "No aplica",
  [NivelJerarquicoEmpleo.Operativo]: "Operativo",
  [NivelJerarquicoEmpleo.Profesional]: "Profesional",
  [NivelJerarquicoEmpleo.Tecnico]: "Técnico",
  [NivelJerarquicoEmpleo.Otro]: "Otro",
};

// ─── Auth ─────────────────────────────────────────────────────────────────────

export interface LoginRequest {
  tipoIdentificacion: TipoIdentificacion;
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

export interface PedirEnlaceEmailRequest {
  tipoIdentificacion: TipoIdentificacion;
  numeroIdentificacion: string;
}

export interface CambiarContrasenaRequest {
  contraseña: string;
}

export interface InhabilitarUsuarioRequest {
  tipoIdentificacion: TipoIdentificacion;
  numeroIdentificacion: string;
  fechaFin: string;
}

// ─── Curriculum: Datos personales ────────────────────────────────────────────

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
  documentoVerificado?: boolean;
  libretaMilitar?: string;
  libretaVerificada?: boolean;
  personaExpuestaPoliticamente: boolean;
}

export interface RegistrarDatosBasicosRequest extends Omit<DatosBasicos, "fechaNacimiento"> {
  fechaNacimiento: string;
}

export interface ActualizarDatosBasicosRequest {
  claseLibretaMilitar?: ClaseLibretaMilitar;
  numeroLibretaMilitar?: string;
  distritoMilitar?: number;
  libretaMilitar?: string;
  libretaVerificada?: boolean;
  personaExpuestaPoliticamente?: boolean;
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

export type RegistrarDatosDemograficosRequest = DatosDemograficos;

export interface ActualizarDatosDemograficosRequest {
  estadoCivil?: EstadoCivil;
  preferenciaEtnica?: PreferenciaEtnica;
  discapacidad?: boolean;
}

export interface DatosContacto {
  paisResidencia: string;
  departamentoResidencia: string;
  municipioResidencia: string;
  zona: Zona;
  direccionResidencia: string;
  telefonoResidencia?: string;
  celular: string;
  telefonoOficina?: string;
  extension?: string;
  emailPersonalPrincipal: string;
  emailOficina?: string;
}

export type RegistrarDatosContactoRequest = DatosContacto;

export type ActualizarDatosContactoRequest = Partial<DatosContacto>;

export interface DatosPersonales {
  datosBasicos: DatosBasicos;
  datosDemograficos: DatosDemograficos;
  datosContacto: DatosContacto;
}

// ─── Curriculum: Educación ───────────────────────────────────────────────────

export interface FormacionAcademica {
  clientId?: string;
  id?: string;
  nivelAcademico: NivelAcademico | "";
  nivelFormacion: NivelFormacion | "";
  areaConocimiento: AreaConocimiento | "";
  pais: string;
  institucionFormacionAcademica: string;
  programaAcademico: string;
  tituloObtenido: string;
  semestresAprobados?: number;
  estadoEstudio: EstadoEstudio;
  fechaTerminacionMaterias?: string;
  fechaGrado?: string;
  estudioConvalidado: boolean;
  fechaConvalidacion?: string;
  tarjetaProfesional?: string;
  estudioExterior?: string;
  archivoTarjetaProfesional?: string;
  archivoTarjetaProfesioal?: string;
  verificTarjetaProfesional?: boolean;
  archivoEducacionFormal?: string;
  verificEducacionFormal?: boolean;
}
export interface RegistrarFormacionAcademicaRequest {
  nivelAcademico: NivelAcademico;
  nivelFormacion: NivelFormacion;
  areaConocimiento?: AreaConocimiento;
  pais: string;
  institucion: string;
  programaAcademico?: string;
  tituloObtenido: string;
  semestresAprobados?: number;
  estadoEstudio: EstadoEstudio;
  fechaTerminacionMaterias?: string;
  fechaGrado?: string;
  estudioConvalidado?: boolean;
  fechaConvalidacion?: string;
  tarjetaProfesional?: string;
  estudioExterior?: string;
  archivoTarjetaProfesional?: string;
  verificTarjetaProfesional?: boolean;
  archivoEducacionFormal?: string;
  verificEducacionFormal?: boolean;
}
export interface ActualizarFormacionAcademicaRequest extends Partial<Omit<RegistrarFormacionAcademicaRequest, "nivelAcademico" | "nivelFormacion" | "pais" | "institucion" | "tituloObtenido" | "archivoTarjetaProfesional">> {
  formacionId: string;
  archivoTarjetaProfesioal?: string;
}

export interface Idioma {
  clientId?: string;
  id?: string;
  idioma: string;
  fechaCertificado: string;
  conversacion: IdiomaNivel | "";
  lectura: IdiomaNivel | "";
  redaccion: IdiomaNivel | "";
  lenguaNativa: boolean;
  certificado?: string;
}

export interface RegistrarIdiomaRequest {
  idioma: string;
  fechaCertificado: string;
  conversacion: IdiomaNivel;
  lectura: IdiomaNivel;
  redaccion: IdiomaNivel;
  lenguaNativa: boolean;
  certificado?: string;
}

export interface ActualizarIdiomaRequest {
  idiomaId: string;
  certificado?: string;
}

export interface EducacionTrabajo {
  clientId?: string;
  id?: string;
  fechaFinalizacion: string;
  numeroTotalHoras: number;
  pais: string;
  nombre: string;
  institucion: string;
  medioCapacitacion: MedioCapacitacion;
  modalidad: ModalidadEducacionTrabajo;
  diplomaActaCertificadoEstudio: string;
}

export type RegistrarEducacionTrabajoRequest = EducacionTrabajo;

export interface ActualizarEducacionTrabajoRequest {
  educacionTrabajoId: string;
  diplomaActaCertificadoEstudio?: string;
}

export interface Educacion {
  formacionesAcademicas: FormacionAcademica[];
  educacionTrabajos: EducacionTrabajo[];
  idiomas: Idioma[];
}

// ─── Curriculum: Experiencia ─────────────────────────────────────────────────

export interface ExperienciaLaboral {
  clientId?: string;
  id?: string;
  tipoEntidad: TipoEntidad | "";
  nombreEntidad: string;
  pais: string;
  departamento: string;
  municipio: string;
  direccionEntidad: string;
  dependencia: string;
  nivelJerarquiaEmpleo: NivelJerarquicoEmpleo | "";
  cargo: string;
  telefono?: string;
  trabajoActual: boolean;
  fechaIngreso: string;
  fechaRetiro?: string;
  jornadaLaboral: JornadaLaboral | "";
  horasPromedioMes?: number;
  tiempoExperiencia: number;
  motivoRetiro?: string;
  certificadoLaboral?: string;
  documentoVerificado?: boolean;
}

export interface RegistrarExperienciaLaboralRequest {
  tipoEntidad: TipoEntidad;
  nombreEntidad: string;
  pais: string;
  departamento: string;
  municipio: string;
  direccionEntidad: string;
  dependencia: string;
  nivelJerarquicoEmpleo: NivelJerarquicoEmpleo;
  cargo: string;
  telefono?: string;
  trabajoActual: boolean;
  fechaIngreso: string;
  fechaRetiro?: string;
  jornadaLaboral: JornadaLaboral;
  horasPromedioMes?: number;
  tiempoExperiencia: number;
  motivoRetiro?: string;
  certificadoLaboral?: string;
  documentoVerificado?: boolean;
}

export interface ActualizarExperienciaLaboralRequest {
  experienciaLaboralId: string;
  telefono?: string;
  fechaRetiro?: string;
  horasPromedioMes?: number;
  motivoRetiro?: string;
  certificadoLaboral?: string;
  documentoVerificado?: boolean;
}

export interface ExperienciaLaboralDocente {
  clientId?: string;
  id?: string;
  tipoInstitucion: TipoInstitucion | "";
  nombreInstitucion: string;
  pais: string;
  departamento: string;
  municipio: string;
  nivelAcademico: NivelAcademico | "";
  areaConocimiento: AreaConocimiento | "";
  tipoZona: TipoZona | "";
  trabajoActual: boolean;
  fechaIngreso: string;
  fechaTerminacion?: string;
  jornadaLaboral: JornadaLaboral | "";
  horasPromedioMes?: number;
  motivoRetiro?: string;
  telefono?: string;
  materiaImpartida?: string;
  tiempoExperiencia: number;
  certificadoLaboral?: string;
  documentoVerificado?: boolean;
}

export interface RegistrarExperienciaLaboralDocenteRequest {
  tipoInstitucion: TipoInstitucion;
  nombreInstitucion: string;
  pais: string;
  departamento: string;
  municipio: string;
  nivelAcademico: NivelAcademico;
  areaConocimiento: AreaConocimiento;
  tipoZona: TipoZona;
  trabajoActual: boolean;
  fechaIngreso: string;
  fechaTerminacion?: string;
  jornadaLaboral: JornadaLaboral;
  horasPromedioMes?: number;
  motivoRetiro?: string;
  telefono?: string;
  materiaImpartida?: string;
  tiempoExperiencia: number;
  certificadoLaboral?: string;
  documentoVerificado?: boolean;
}

export interface ActualizarExperienciaLaboralDocenteRequest {
  experienciaLaboralDocenteId: string;
  fechaTerminacion?: string;
  horasPromedioMes?: number;
  motivoRetiro?: string;
  telefono?: string;
  materiaImpartida?: string;
  certificadoLaboral?: string;
  documentoVerificado?: boolean;
}

// ─── Curriculum: Gerencia Pública ────────────────────────────────────────────

export interface RegistrarPublicacionRequest {
  articulo: ArticuloPublicacion;
  nombreArticulo: string;
  libroResultadoInvestigacion: LibroResultadoInvestigacion;
  nombreLibroRevista: string;
  tiposProduccionBibliografica: TipoProduccionBibliografica;
  nombrePublicacion: string;
}

export interface RegistrarPremioReconocimientoRequest {
  tipo: TipoPremioReconocimiento;
  nombreEntidadOrganizacion: string;
  fecha: string;
  pais: string;
  departamento: string;
  municipio: string;
}

export interface RegistrarParticipacionProyectoRequest {
  nombre: string;
  rolDesempeñado: string;
  nombreEntidadOrganizacion: string;
  pais: string;
  departamento: string;
  municipio: string;
  fechaInicio: string;
  fechaTerminacion: string;
}

export interface RegistrarParticipacionCorporacionEntidadRequest {
  nombreCorporacion: string;
  nombreRazonSocialInstitucion: string;
  nombreEntidadOrganizacion: string;
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
  id?: string;
  rol: RolUsuario;
  tokenExp?: number;
}
