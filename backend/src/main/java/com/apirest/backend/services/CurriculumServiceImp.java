package com.apirest.backend.services;


import com.apirest.backend.dtos.requests.curriculums.DatosPersonales.*;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarEducacionTrabajoRequest;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarFormacionAcademicaRequest;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarIdiomaRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralDocenteRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarParticipacionCorporacionEntidadRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarParticipacionProyectoRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarPremioReconocimientoRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarPublicacionRequest;
import com.apirest.backend.exceptions.CurriculumAlreadyExistsException;
import com.apirest.backend.exceptions.CurriculumNotFoundException;
import com.apirest.backend.models.curriculum.*;
import com.apirest.backend.models.curriculum.sections.*;
import com.apirest.backend.repositories.ICurriculumRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CurriculumServiceImp implements ICurriculumService{
    private final ICurriculumRepository curriculumRepository;

    public CurriculumServiceImp(ICurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }


    @Override
    public void registrarDatosPersonalesBasicos(String usuarioId, RegistrarDatosBasicosRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumAlreadyExistsException("Este usuario ya tiene datos basicos registrados. ");
        }
        CurriculumModelo curriculumFinal = new CurriculumModelo();
        if (curriculumFinal.getDatosPersonales() == null) {
            curriculumFinal.setDatosPersonales(new DatosPersonales());
        }
        curriculumFinal.setUsuarioId(usuarioId);
        curriculumFinal.getDatosPersonales().setDatosBasicos(DatosBasicos.builder()
                        .nombre(curriculumRequest.getNombre())
                        .tipoIdentificacion(curriculumRequest.getTipoIdentificacion())
                        .numeroIdentificacion(curriculumRequest.getNumeroIdentificacion())
                        .fechaNacimiento(curriculumRequest.getFechaNacimiento())
                        .email(curriculumRequest.getEmail())
                        .genero(curriculumRequest.getGenero())
                        .claseLibretaMilitar(curriculumRequest.getClaseLibretaMilitar())
                        .numeroLibretaMilitar(curriculumRequest.getNumeroLibretaMilitar())
                        .distritoMilitar(curriculumRequest.getDistritoMilitar())
                        .documentoIdentificacion(curriculumRequest.getDocumentoIdentificacion())
                        .documentoVerificado(curriculumRequest.getDocumentoVerificado())
                        .libretaMilitar(curriculumRequest.getLibretaMilitar())
                        .libretaVerificada(curriculumRequest.getLibretaVerificada())
                        .personaExpuestaPoliticamente(curriculumRequest.getPersonaExpuestaPoliticamente())
                        .build());

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void actualizarDatosPersonalesBasicos(String usuarioId, ActualizarDatosBasicosRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado datos basicos registrados para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if(curriculumRequest.getClaseLibretaMilitar() != null) {
            curriculumFinal.getDatosPersonales().getDatosBasicos().setClaseLibretaMilitar(curriculumRequest.getClaseLibretaMilitar());
        }
        if (curriculumRequest.getNumeroLibretaMilitar() != null && !curriculumRequest.getNumeroLibretaMilitar().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosBasicos().setNumeroLibretaMilitar(curriculumRequest.getNumeroLibretaMilitar());
        }
        if (curriculumRequest.getDistritoMilitar() != null){
            curriculumFinal.getDatosPersonales().getDatosBasicos().setDistritoMilitar(curriculumRequest.getDistritoMilitar());
        }
        if (curriculumRequest.getLibretaMilitar() != null && !curriculumRequest.getLibretaMilitar().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosBasicos().setLibretaMilitar(curriculumRequest.getLibretaMilitar());
        }
        if (curriculumRequest.getLibretaVerificada() != null) {
            curriculumFinal.getDatosPersonales().getDatosBasicos().setLibretaVerificada(curriculumRequest.getLibretaVerificada());
        }
        if (curriculumRequest.getPersonaExpuestaPoliticamente() != null){
            curriculumFinal.getDatosPersonales().getDatosBasicos().setPersonaExpuestaPoliticamente(curriculumRequest.getPersonaExpuestaPoliticamente());
        }

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarDatosDemograficos(String usuarioId, RegistrarDatosDemograficosRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        curriculumFinal.getDatosPersonales().setDatosDemograficos(DatosDemograficos.builder()
                        .nacionalidad(curriculumRequest.getNacionalidad())
                        .estadoCivil(curriculumRequest.getEstadoCivil())
                        .preferenciaEtnica(curriculumRequest.getPreferenciaEtnica())
                        .paisNacimiento(curriculumRequest.getPaisNacimiento())
                        .departamentoNacimiento(curriculumRequest.getDepartamentoNacimiento())
                        .municipioNacimiento(curriculumRequest.getMunicipioNacimiento())
                        .discapacidad(curriculumRequest.getDiscapacidad())
                        .build());

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void actualizarDatosDemograficos(String usuarioId, ActualizarDatosDemograficosRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumRequest.getEstadoCivil() != null ){
            curriculumFinal.getDatosPersonales().getDatosDemograficos().setEstadoCivil(curriculumRequest.getEstadoCivil());
        }
        if (curriculumRequest.getPreferenciaEtnica() != null ){
            curriculumFinal.getDatosPersonales().getDatosDemograficos().setPreferenciaEtnica(curriculumRequest.getPreferenciaEtnica());
        }
        if (curriculumRequest.getDiscapacidad() != null ){
            curriculumFinal.getDatosPersonales().getDatosDemograficos().setDiscapacidad(curriculumRequest.getDiscapacidad());
        }

        curriculumRepository.save(curriculumFinal);

    }

    @Override
    public void registrarDatosContacto(String usuarioId, RegistrarDatosContactoRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        curriculumFinal.getDatosPersonales().setDatosContacto(DatosContacto.builder()
                        .paisResidencia(curriculumRequest.getPaisResidencia())
                        .departamentoResidencia(curriculumRequest.getDepartamentoResidencia())
                        .municipioResidencia(curriculumRequest.getMunicipioResidencia())
                        .zona(curriculumRequest.getZona())
                        .direccionResidencia(curriculumRequest.getDireccionResidencia())
                        .telefonoResidencia(curriculumRequest.getTelefonoResidencia())
                        .celular(curriculumRequest.getCelular())
                        .telefonoOficina(curriculumRequest.getTelefonoOficina())
                        .extension(curriculumRequest.getExtension())
                        .emailPersonalPrincipal(curriculumRequest.getEmailPersonalPrincipal())
                        .emailOficina(curriculumRequest.getEmailOficina())
                        .build());

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void actualizarDatosContacto(String usuarioId, ActualizarDatosContactoRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumRequest.getPaisResidencia() != null && !curriculumRequest.getPaisResidencia().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setPaisResidencia(curriculumRequest.getPaisResidencia());
        }
        if (curriculumRequest.getDepartamentoResidencia() != null && !curriculumRequest.getDepartamentoResidencia().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setDepartamentoResidencia(curriculumRequest.getDepartamentoResidencia());
        }
        if (curriculumRequest.getMunicipioResidencia() != null && !curriculumRequest.getMunicipioResidencia().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setMunicipioResidencia(curriculumRequest.getMunicipioResidencia());
        }
        if (curriculumRequest.getZona() != null){
            curriculumFinal.getDatosPersonales().getDatosContacto().setZona(curriculumRequest.getZona());
        }
        if (curriculumRequest.getDireccionResidencia() != null && !curriculumRequest.getDireccionResidencia().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setDireccionResidencia(curriculumRequest.getDireccionResidencia());
        }
        if (curriculumRequest.getTelefonoResidencia() != null && !curriculumRequest.getTelefonoResidencia().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setTelefonoResidencia(curriculumRequest.getTelefonoResidencia());
        }
        if (curriculumRequest.getCelular() != null && !curriculumRequest.getCelular().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setCelular(curriculumRequest.getCelular());
        }
        if (curriculumRequest.getTelefonoOficina() != null && !curriculumRequest.getTelefonoOficina().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setTelefonoOficina(curriculumRequest.getTelefonoOficina());
        }
        if (curriculumRequest.getExtension() != null && !curriculumRequest.getExtension().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setExtension(curriculumRequest.getExtension());
        }
        if (curriculumRequest.getEmailPersonalPrincipal() != null && !curriculumRequest.getEmailPersonalPrincipal().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setEmailPersonalPrincipal(curriculumRequest.getEmailPersonalPrincipal());
        }
        if (curriculumRequest.getEmailOficina() != null && !curriculumRequest.getEmailOficina().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosContacto().setEmailOficina(curriculumRequest.getEmailOficina());
        }

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarFormacionAcademica(String usuarioId, RegistrarFormacionAcademicaRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getEducacion() == null) {
            curriculumFinal.setEducacion(new Educacion());
        }

        if (curriculumFinal.getEducacion().getFormacionesAcademicas() == null) {
            curriculumFinal.getEducacion().setFormacionesAcademicas(new ArrayList<>());
        }

        FormacionAcademica formacionAcademica = FormacionAcademica.builder()
                .nivelAcademico(curriculumRequest.getNivelAcademico())
                .nivelFormacion(curriculumRequest.getNivelFormacion())
                .areaConocimiento(curriculumRequest.getAreaConocimiento())
                .pais(curriculumRequest.getPais())
                .institucion(curriculumRequest.getInstitucion())
                .programaAcademico(curriculumRequest.getProgramaAcademico())
                .tituloObtenido(curriculumRequest.getTituloObtenido())
                .semestresAprobados(curriculumRequest.getSemestresAprobados())
                .estadoEstudio(curriculumRequest.getEstadoEstudio())
                .fechaTerminacionMaterias(curriculumRequest.getFechaTerminacionMaterias())
                .fechaGrado(curriculumRequest.getFechaGrado())
                .estudioConvalidado(curriculumRequest.getEstudioConvalidado())
                .fechaConvalidacion(curriculumRequest.getFechaConvalidacion())
                .tarjetaProfesional(curriculumRequest.getTarjetaProfesional())
                .estudioExterior(curriculumRequest.getEstudioExterior())
                .archivoTarjetaProfesioal(curriculumRequest.getArchivoTarjetaProfesioal())
                .verificTarjetaProfesional(curriculumRequest.getVerificTarjetaProfesional())
                .archivoEducacionFormal(curriculumRequest.getArchivoEducacionFormal())
                .verificEducacionFormal(curriculumRequest.getVerificEducacionFormal())
                .build();

        curriculumFinal.getEducacion().getFormacionesAcademicas().add(formacionAcademica);

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarEducacionTrabajo(String usuarioId, RegistrarEducacionTrabajoRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getEducacion() == null) {
            curriculumFinal.setEducacion(new Educacion());
        }

        if (curriculumFinal.getEducacion().getEducacionTrabajos() == null) {
            curriculumFinal.getEducacion().setEducacionTrabajos(new ArrayList<>());
        }

        EducacionTrabajo educacionTrabajo = EducacionTrabajo.builder()
                .fechaFinalizacion(curriculumRequest.getFechaFinalizacion())
                .numeroTotalHoras(curriculumRequest.getNumeroTotalHoras())
                .pais(curriculumRequest.getPais())
                .nombre(curriculumRequest.getNombre())
                .institucion(curriculumRequest.getInstitucion())
                .medioCapacitacion(curriculumRequest.getMedioCapacitacion())
                .modalidad(curriculumRequest.getModalidad())
                .diplomaActaCertificadoEstudio(curriculumRequest.getDiplomaActaCertificadoEstudio())
                .build();

        curriculumFinal.getEducacion().getEducacionTrabajos().add(educacionTrabajo);

        curriculumRepository.save(curriculumFinal);

    }

    @Override
    public void registrarIdioma(String usuarioId, RegistrarIdiomaRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getEducacion() == null) {
            curriculumFinal.setEducacion(new Educacion());
        }

        if (curriculumFinal.getEducacion().getIdiomas() == null) {
            curriculumFinal.getEducacion().setIdiomas(new ArrayList<>());
        }

        Idioma idiomaFinal = Idioma.builder()
                .idioma(curriculumRequest.getIdioma())
                .fechaCertificado(curriculumRequest.getFechaCertificado())
                .conversacion(curriculumRequest.getConversacion())
                .lectura(curriculumRequest.getLectura())
                .redaccion(curriculumRequest.getRedaccion())
                .lenguaNativa(curriculumRequest.getLenguaNativa())
                .certificado(curriculumRequest.getCertificado())
                .build();

        curriculumFinal.getEducacion().getIdiomas().add(idiomaFinal);

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarExperienciaLaboral(String usuarioId, RegistrarExperienciaLaboralRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getExperienciasLaborales() == null) {
            curriculumFinal.setExperienciasLaborales(new ArrayList<>());
        }

        ExperienciaLaboral experienciaLaboral = ExperienciaLaboral.builder()
                .tipoEntidad(curriculumRequest.getTipoEntidad())
                .nombreEntidad(curriculumRequest.getNombreEntidad())
                .pais(curriculumRequest.getPais())
                .departamento(curriculumRequest.getDepartamento())
                .municipio(curriculumRequest.getMunicipio())
                .direccionEntidad(curriculumRequest.getDireccionEntidad())
                .dependencia(curriculumRequest.getDependencia())
                .nivelJerarquiaEmpleo(curriculumRequest.getNivelJerarquiaEmpleo())
                .cargo(curriculumRequest.getCargo())
                .telefono(curriculumRequest.getTelefono())
                .trabajoActual(curriculumRequest.getTrabajoActual())
                .fechaIngreso(curriculumRequest.getFechaIngreso())
                .fechaRetiro(curriculumRequest.getFechaRetiro())
                .jornadaLaboral(curriculumRequest.getJornadaLaboral())
                .horasPromedioMes(curriculumRequest.getHorasPromedioMes())
                .tiempoExperiencia(curriculumRequest.getTiempoExperiencia())
                .motivoRetiro(curriculumRequest.getMotivoRetiro())
                .certificadoLaboral(curriculumRequest.getCertificadoLaboral())
                .documentoVerificado(curriculumRequest.getDocumentoVerificado())
                .build();

        curriculumFinal.getExperienciasLaborales().add(experienciaLaboral);

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarExperienciaLaboralDocente(String usuarioId, RegistrarExperienciaLaboralDocenteRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getExperienciasLaboralesDocente() == null) {
            curriculumFinal.setExperienciasLaboralesDocente(new ArrayList<>());
        }

        ExperienciaLaboralDocente experienciaLaboralDocente = ExperienciaLaboralDocente.builder()
                .tipoInstitucion(curriculumRequest.getTipoInstitucion())
                .nombreInstitucion(curriculumRequest.getNombreInstitucion())
                .pais(curriculumRequest.getPais())
                .departamento(curriculumRequest.getDepartamento())
                .municipio(curriculumRequest.getMunicipio())
                .nivelAcademico(curriculumRequest.getNivelAcademico())
                .areaConocimiento(curriculumRequest.getAreaConocimiento())
                .tipoZona(curriculumRequest.getTipoZona())
                .trabajoActual(curriculumRequest.getTrabajoActual())
                .fechaIngreso(curriculumRequest.getFechaIngreso())
                .fechaTerminacion(curriculumRequest.getFechaTerminacion())
                .jornadaLaboral(curriculumRequest.getJornadaLaboral())
                .horasPromedioMes(curriculumRequest.getHorasPromedioMes())
                .motivoRetiro(curriculumRequest.getMotivoRetiro())
                .telefono(curriculumRequest.getTelefono())
                .materiaImpartida(curriculumRequest.getMateriaImpartida())
                .tiempoExperiencia(curriculumRequest.getTiempoExperiencia())
                .certificadoLaboral(curriculumRequest.getCertificadoLaboral())
                .documentoVerificado(curriculumRequest.getDocumentoVerificado())
                .build();

        curriculumFinal.getExperienciasLaboralesDocente().add(experienciaLaboralDocente);

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarPublicacion(String usuarioId, RegistrarPublicacionRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null) {
            curriculumFinal.setGerenciaPublica(new GerenciaPublica());
        }

        if (curriculumFinal.getGerenciaPublica().getPublicaciones() == null) {
            curriculumFinal.getGerenciaPublica().setPublicaciones(new ArrayList<>());
        }

        Publicacion publicacion = Publicacion.builder()
                .articulo(curriculumRequest.getArticulo())
                .nombreArticulo(curriculumRequest.getNombreArticulo())
                .libroResultadoInvestigacion(curriculumRequest.getLibroResultadoInvestigacion())
                .nombreLibroRevista(curriculumRequest.getNombreLibroRevista())
                .tiposProduccionBibliografica(curriculumRequest.getTiposProduccionBibliografica())
                .nombrePublicacion(curriculumRequest.getNombrePublicacion())
                .build();

        curriculumFinal.getGerenciaPublica().getPublicaciones().add(publicacion);
        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarPremioReconocimiento(String usuarioId, RegistrarPremioReconocimientoRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null) {
            curriculumFinal.setGerenciaPublica(new GerenciaPublica());
        }

        if (curriculumFinal.getGerenciaPublica().getPremiosReconocimientos() == null) {
            curriculumFinal.getGerenciaPublica().setPremiosReconocimientos(new ArrayList<>());
        }

        PremioReconocimiento premioReconocimiento = PremioReconocimiento.builder()
                .tipo(curriculumRequest.getTipo())
                .nombreEntidadOrganizacion(curriculumRequest.getNombreEntidadOrganizacion())
                .fecha(curriculumRequest.getFecha())
                .pais(curriculumRequest.getPais())
                .departamento(curriculumRequest.getDepartamento())
                .municipio(curriculumRequest.getMunicipio())
                .build();

        curriculumFinal.getGerenciaPublica().getPremiosReconocimientos().add(premioReconocimiento);
        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarParticipacionProyecto(String usuarioId, RegistrarParticipacionProyectoRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null) {
            curriculumFinal.setGerenciaPublica(new GerenciaPublica());
        }

        if (curriculumFinal.getGerenciaPublica().getParticipacionesPoryectos() == null) {
            curriculumFinal.getGerenciaPublica().setParticipacionesPoryectos(new ArrayList<>());
        }

        ParticipacionProyecto participacionProyecto = ParticipacionProyecto.builder()
                .nombre(curriculumRequest.getNombre())
                .rolDesempeñado(curriculumRequest.getRolDesempeñado())
                .nombreEntidadOrganizacion(curriculumRequest.getNombreEntidadOrganizacion())
                .pais(curriculumRequest.getPais())
                .departamento(curriculumRequest.getDepartamento())
                .municipio(curriculumRequest.getMunicipio())
                .fechaInicio(curriculumRequest.getFechaInicio())
                .fechaTerminacion(curriculumRequest.getFechaTerminacion())
                .build();

        curriculumFinal.getGerenciaPublica().getParticipacionesPoryectos().add(participacionProyecto);
        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void registrarParticipacionCorporacionEntidad(String usuarioId, RegistrarParticipacionCorporacionEntidadRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null) {
            curriculumFinal.setGerenciaPublica(new GerenciaPublica());
        }

        if (curriculumFinal.getGerenciaPublica().getParticipacionesCorporacionesEntidades() == null) {
            curriculumFinal.getGerenciaPublica().setParticipacionesCorporacionesEntidades(new ArrayList<>());
        }

        ParticipacionCorporacionEntidad participacionCorporacionEntidad = ParticipacionCorporacionEntidad.builder()
                .nombreCorporacion(curriculumRequest.getNombreCorporacion())
                .nombreRazonSocialInstitucion(curriculumRequest.getNombreRazonSocialInstitucion())
                .nombreEntidadOrganizacion(curriculumRequest.getNombreEntidadOrganizacion())
                .build();

        curriculumFinal.getGerenciaPublica().getParticipacionesCorporacionesEntidades().add(participacionCorporacionEntidad);
        curriculumRepository.save(curriculumFinal);
    }


}
