package com.apirest.backend.services;


import com.apirest.backend.dtos.requests.curriculums.DatosPersonales.*;
import com.apirest.backend.dtos.requests.curriculums.Educacion.*;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.ActualizarExperienciaLaboralDocenteRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.ActualizarExperienciaLaboralRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralDocenteRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarParticipacionCorporacionEntidadRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarParticipacionProyectoRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarPremioReconocimientoRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarPublicacionRequest;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosBasicosResponse;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosContactoResponse;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosDemograficosResponse;
import com.apirest.backend.dtos.responses.curriculums.Educacion.EducacionTrabajoResponse;
import com.apirest.backend.dtos.responses.curriculums.Educacion.FormacionAcademicaResponse;
import com.apirest.backend.dtos.responses.curriculums.Educacion.IdiomaResponse;
import com.apirest.backend.dtos.responses.curriculums.ExperienciaLaboral.ExperienciaLaboralDocenteResponse;
import com.apirest.backend.dtos.responses.curriculums.ExperienciaLaboral.ExperienciaLaboralResponse;
import com.apirest.backend.dtos.responses.curriculums.GerenciaPublica.ParticipacionCorporacionEntidadResponse;
import com.apirest.backend.dtos.responses.curriculums.GerenciaPublica.ParticipacionProyectoResponse;
import com.apirest.backend.dtos.responses.curriculums.GerenciaPublica.PremioReconocimientoResponse;
import com.apirest.backend.dtos.responses.curriculums.GerenciaPublica.PublicacionResponse;
import com.apirest.backend.exceptions.CurriculumAlreadyExistsException;
import com.apirest.backend.exceptions.CurriculumNotFoundException;
import com.apirest.backend.models.curriculum.*;
import com.apirest.backend.models.curriculum.sections.*;
import com.apirest.backend.repositories.ICurriculumRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurriculumServiceImp implements ICurriculumService{
    private final ICurriculumRepository curriculumRepository;

    public CurriculumServiceImp(ICurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }


    @Override
    public void registrarDatosPersonalesBasicos(String usuarioId, RegistrarDatosBasicosRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (curriculumExiste.isPresent()){
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
        if (curriculumRequest.getDocumentoIdentificacion() != null && !curriculumRequest.getDocumentoIdentificacion().isBlank()){
            curriculumFinal.getDatosPersonales().getDatosBasicos().setDocumentoIdentificacion(curriculumRequest.getDocumentoIdentificacion());
        }
        if (curriculumRequest.getDocumentoVerificado() != null) {
            curriculumFinal.getDatosPersonales().getDatosBasicos().setDocumentoVerificado(curriculumRequest.getDocumentoVerificado());
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
    public DatosBasicosResponse obtenerDatosBasicos(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado datos basicos registrados para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        DatosBasicosResponse datosBasicosResponse = DatosBasicosResponse.builder()
                .nombre(curriculumFinal.getDatosPersonales().getDatosBasicos().getNombre())
                .tipoIdentificacion(curriculumFinal.getDatosPersonales().getDatosBasicos().getTipoIdentificacion())
                .numeroIdentificacion(curriculumFinal.getDatosPersonales().getDatosBasicos().getNumeroIdentificacion())
                .fechaNacimiento(curriculumFinal.getDatosPersonales().getDatosBasicos().getFechaNacimiento())
                .email(curriculumFinal.getDatosPersonales().getDatosBasicos().getEmail())
                .genero(curriculumFinal.getDatosPersonales().getDatosBasicos().getGenero())
                .claseLibretaMilitar(curriculumFinal.getDatosPersonales().getDatosBasicos().getClaseLibretaMilitar())
                .numeroLibretaMilitar(curriculumFinal.getDatosPersonales().getDatosBasicos().getNumeroLibretaMilitar())
                .distritoMilitar(curriculumFinal.getDatosPersonales().getDatosBasicos().getDistritoMilitar())
                .documentoIdentificacion(curriculumFinal.getDatosPersonales().getDatosBasicos().getDocumentoIdentificacion())
                .documentoVerificado(curriculumFinal.getDatosPersonales().getDatosBasicos().getDocumentoVerificado())
                .libretaMilitar(curriculumFinal.getDatosPersonales().getDatosBasicos().getLibretaMilitar())
                .libretaVerificada(curriculumFinal.getDatosPersonales().getDatosBasicos().getLibretaVerificada())
                .personaExpuestaPoliticamente(curriculumFinal.getDatosPersonales().getDatosBasicos().getPersonaExpuestaPoliticamente())
                .build();
        return datosBasicosResponse;
    }

    @Override
    public DatosDemograficosResponse obtenerDatosDemograficos(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado datos basicos registrados para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();
        DatosDemograficosResponse datosDemograficosResponse = DatosDemograficosResponse.builder()
                .nacionalidad(curriculumFinal.getDatosPersonales().getDatosDemograficos().getNacionalidad())
                .estadoCivil(curriculumFinal.getDatosPersonales().getDatosDemograficos().getEstadoCivil())
                .preferenciaEtnica(curriculumFinal.getDatosPersonales().getDatosDemograficos().getPreferenciaEtnica())
                .paisNacimiento(curriculumFinal.getDatosPersonales().getDatosDemograficos().getPaisNacimiento())
                .departamentoNacimiento(curriculumFinal.getDatosPersonales().getDatosDemograficos().getDepartamentoNacimiento())
                .municipioNacimiento(curriculumFinal.getDatosPersonales().getDatosDemograficos().getMunicipioNacimiento())
                .discapacidad(curriculumFinal.getDatosPersonales().getDatosDemograficos().getDiscapacidad())
                .build();

        return datosDemograficosResponse;
    }

    @Override
    public DatosContactoResponse obtenerDatosContacto(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado datos basicos registrados para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        DatosContactoResponse datosContactoResponse = DatosContactoResponse.builder()
                .paisResidencia(curriculumFinal.getDatosPersonales().getDatosContacto().getPaisResidencia())
                .departamentoResidencia(curriculumFinal.getDatosPersonales().getDatosContacto().getDepartamentoResidencia())
                .municipioResidencia(curriculumFinal.getDatosPersonales().getDatosContacto().getMunicipioResidencia())
                .zona(curriculumFinal.getDatosPersonales().getDatosContacto().getZona())
                .direccionResidencia(curriculumFinal.getDatosPersonales().getDatosContacto().getDireccionResidencia())
                .telefonoResidencia(curriculumFinal.getDatosPersonales().getDatosContacto().getTelefonoResidencia())
                .celular(curriculumFinal.getDatosPersonales().getDatosContacto().getCelular())
                .telefonoOficina(curriculumFinal.getDatosPersonales().getDatosContacto().getTelefonoOficina())
                .extension(curriculumFinal.getDatosPersonales().getDatosContacto().getExtension())
                .emailPersonalPrincipal(curriculumFinal.getDatosPersonales().getDatosContacto().getEmailPersonalPrincipal())
                .emailOficina(curriculumFinal.getDatosPersonales().getDatosContacto().getEmailOficina())
                .build();

        return datosContactoResponse;
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
                .id(new ObjectId().toString())
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
                .archivoTarjetaProfesional(curriculumRequest.getArchivoTarjetaProfesional())
                .verificTarjetaProfesional(curriculumRequest.getVerificTarjetaProfesional())
                .archivoEducacionFormal(curriculumRequest.getArchivoEducacionFormal())
                .verificEducacionFormal(curriculumRequest.getVerificEducacionFormal())
                .build();

        curriculumFinal.getEducacion().getFormacionesAcademicas().add(formacionAcademica);

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public void actualizarFormacionAcademica(String usuarioId, ActualizarFormacionAcademicaRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        FormacionAcademica formacionAcademica = curriculumFinal.getEducacion().getFormacionesAcademicas().stream()
                .filter(f -> f.getId().equals(curriculumRequest.getFormacionId()))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Formación académica no encontrada"));

        if (curriculumRequest.getAreaConocimiento() != null) {
            formacionAcademica.setAreaConocimiento(curriculumRequest.getAreaConocimiento());
        }
        if (curriculumRequest.getProgramaAcademico() != null && !curriculumRequest.getProgramaAcademico().isBlank()) {
            formacionAcademica.setProgramaAcademico(curriculumRequest.getProgramaAcademico());
        }
        if (curriculumRequest.getSemestresAprobados() != null) {
            formacionAcademica.setSemestresAprobados(curriculumRequest.getSemestresAprobados());
        }
        if (curriculumRequest.getEstadoEstudio() != null) {
            formacionAcademica.setEstadoEstudio(curriculumRequest.getEstadoEstudio());
        }
        if (curriculumRequest.getFechaTerminacionMaterias() != null) {
            formacionAcademica.setFechaTerminacionMaterias(curriculumRequest.getFechaTerminacionMaterias());
        }
        if (curriculumRequest.getFechaGrado() != null) {
            formacionAcademica.setFechaGrado(curriculumRequest.getFechaGrado());
        }
        if (curriculumRequest.getEstudioConvalidado() != null) {
            formacionAcademica.setEstudioConvalidado(curriculumRequest.getEstudioConvalidado());
        }
        if (curriculumRequest.getFechaConvalidacion() != null) {
            formacionAcademica.setFechaConvalidacion(curriculumRequest.getFechaConvalidacion());
        }
        if (curriculumRequest.getTarjetaProfesional() != null && !curriculumRequest.getTarjetaProfesional().isBlank()) {
            formacionAcademica.setTarjetaProfesional(curriculumRequest.getTarjetaProfesional());
        }
        if (curriculumRequest.getEstudioExterior() != null) {
            formacionAcademica.setEstudioExterior(curriculumRequest.getEstudioExterior());
        }
        if (curriculumRequest.getArchivoTarjetaProfesioal() != null && !curriculumRequest.getArchivoTarjetaProfesioal().isBlank()) {
            formacionAcademica.setArchivoTarjetaProfesional(curriculumRequest.getArchivoTarjetaProfesioal());
        }
        if (curriculumRequest.getVerificTarjetaProfesional() != null) {
            formacionAcademica.setVerificTarjetaProfesional(curriculumRequest.getVerificTarjetaProfesional());
        }
        if (curriculumRequest.getArchivoEducacionFormal() != null && !curriculumRequest.getArchivoEducacionFormal().isBlank()) {
            formacionAcademica.setArchivoEducacionFormal(curriculumRequest.getArchivoEducacionFormal());
        }
        if (curriculumRequest.getVerificEducacionFormal() != null) {
            formacionAcademica.setVerificEducacionFormal(curriculumRequest.getVerificEducacionFormal());
        }


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
                .id(new ObjectId().toString())
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
    public void actualizarEducacionTrabajo(String usuarioId, ActualizarEducacionTrabajoRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        EducacionTrabajo educacionTrabajo = curriculumFinal.getEducacion().getEducacionTrabajos().stream()
                .filter(f -> f.getId().equals(curriculumRequest.getEducacionTrabajoId()))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Formación académica no encontrada"));

        if (curriculumRequest.getDiplomaActaCertificadoEstudio() != null && !curriculumRequest.getDiplomaActaCertificadoEstudio().isBlank()) {
            educacionTrabajo.setDiplomaActaCertificadoEstudio(curriculumRequest.getDiplomaActaCertificadoEstudio());
        }

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
                .id(new ObjectId().toString())
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
    public void actualizarIdioma(String usuarioId, ActualizarIdiomaRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        Idioma idioma = curriculumFinal.getEducacion().getIdiomas().stream()
                .filter(f -> f.getId().equals(curriculumRequest.getIdiomaId()))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Formación académica no encontrada"));

        if (curriculumRequest.getCertificado() != null && !curriculumRequest.getCertificado().isBlank()) {
            idioma.setCertificado(curriculumRequest.getCertificado());
        }

        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public List<FormacionAcademicaResponse> obtenerTodasFormacionesAcademicas(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getEducacion() == null || curriculumFinal.getEducacion().getFormacionesAcademicas() == null) {
            return Collections.emptyList();
        }

        return  curriculumFinal.getEducacion().getFormacionesAcademicas().stream()
                .map(fa -> FormacionAcademicaResponse.builder()
                        .id(fa.getId())
                        .nivelAcademico(fa.getNivelAcademico())
                        .nivelFormacion(fa.getNivelFormacion())
                        .areaConocimiento(fa.getAreaConocimiento())
                        .pais(fa.getPais())
                        .institucion(fa.getInstitucion())
                        .programaAcademico(fa.getProgramaAcademico())
                        .tituloObtenido(fa.getTituloObtenido())
                        .semestresAprobados(fa.getSemestresAprobados())
                        .estadoEstudio(fa.getEstadoEstudio())
                        .fechaTerminacionMaterias(fa.getFechaTerminacionMaterias())
                        .fechaGrado(fa.getFechaGrado())
                        .estudioConvalidado(fa.getEstudioConvalidado())
                        .fechaConvalidacion(fa.getFechaConvalidacion())
                        .tarjetaProfesional(fa.getTarjetaProfesional())
                        .estudioExterior(fa.getEstudioExterior())
                        .archivoTarjetaProfesioal(fa.getArchivoTarjetaProfesional())
                        .verificTarjetaProfesional(fa.getVerificTarjetaProfesional())
                        .archivoEducacionFormal(fa.getArchivoEducacionFormal())
                        .verificEducacionFormal(fa.getVerificEducacionFormal())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<EducacionTrabajoResponse> obtenerTodaEducacionTrabajo(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getEducacion() == null || curriculumFinal.getEducacion().getEducacionTrabajos() == null) {
            return Collections.emptyList();
        }

        return curriculumFinal.getEducacion().getEducacionTrabajos().stream()
                .map(et -> EducacionTrabajoResponse.builder()
                        .id(et.getId())
                        .fechaFinalizacion(et.getFechaFinalizacion())
                        .numeroTotalHoras(et.getNumeroTotalHoras())
                        .pais(et.getPais())
                        .nombre(et.getNombre())
                        .institucion(et.getInstitucion())
                        .medioCapacitacion(et.getMedioCapacitacion())
                        .modalidad(et.getModalidad())
                        .diplomaActaCertificadoEstudio(et.getDiplomaActaCertificadoEstudio())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<IdiomaResponse> obtenerTodosIdiomas(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getEducacion() == null || curriculumFinal.getEducacion().getIdiomas() == null) {
            return Collections.emptyList();
        }

        return curriculumFinal.getEducacion().getIdiomas().stream()
                .map(idioma -> IdiomaResponse.builder()
                        .id(idioma.getId())
                        .idioma(idioma.getIdioma())
                        .fechaCertificado(idioma.getFechaCertificado())
                        .conversacion(idioma.getConversacion())
                        .lectura(idioma.getLectura())
                        .redaccion(idioma.getRedaccion())
                        .lenguaNativa(idioma.getLenguaNativa())
                        .certificado(idioma.getCertificado())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public FormacionAcademicaResponse obtenerFormacionAcademica(String usuarioId, String formacionId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        FormacionAcademica formacionAcademica = curriculumFinal.getEducacion().getFormacionesAcademicas().stream()
                .filter(f -> f.getId().equals(formacionId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("No se ha encontrado el apartado formacion academica con el id " + formacionId));

        FormacionAcademicaResponse formacionAcademicaResponse = FormacionAcademicaResponse.builder()
                .id(formacionAcademica.getId())
                .nivelAcademico(formacionAcademica.getNivelAcademico())
                .nivelFormacion(formacionAcademica.getNivelFormacion())
                .areaConocimiento(formacionAcademica.getAreaConocimiento())
                .pais(formacionAcademica.getPais())
                .institucion(formacionAcademica.getInstitucion())
                .programaAcademico(formacionAcademica.getProgramaAcademico())
                .tituloObtenido(formacionAcademica.getTituloObtenido())
                .semestresAprobados(formacionAcademica.getSemestresAprobados())
                .estadoEstudio(formacionAcademica.getEstadoEstudio())
                .fechaTerminacionMaterias(formacionAcademica.getFechaTerminacionMaterias())
                .fechaGrado(formacionAcademica.getFechaGrado())
                .estudioConvalidado(formacionAcademica.getEstudioConvalidado())
                .fechaConvalidacion(formacionAcademica.getFechaConvalidacion())
                .tarjetaProfesional(formacionAcademica.getTarjetaProfesional())
                .estudioExterior(formacionAcademica.getEstudioExterior())
                .archivoTarjetaProfesioal(formacionAcademica.getArchivoTarjetaProfesional())
                .verificTarjetaProfesional(formacionAcademica.getVerificTarjetaProfesional())
                .archivoEducacionFormal(formacionAcademica.getArchivoEducacionFormal())
                .verificEducacionFormal(formacionAcademica.getVerificEducacionFormal())
                .build();

        return formacionAcademicaResponse;
    }

    @Override
    public EducacionTrabajoResponse obtenerEducacionTrabajo(String usuarioId, String educacionId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        EducacionTrabajo educacionTrabajo = curriculumFinal.getEducacion().getEducacionTrabajos().stream()
                .filter(f -> f.getId().equals(educacionId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("No se ha encontrado el apartado educacion trabajo registrado con el id " + educacionId));

        EducacionTrabajoResponse educacionTrabajoResponse = EducacionTrabajoResponse.builder()
                .fechaFinalizacion(educacionTrabajo.getFechaFinalizacion())
                .numeroTotalHoras(educacionTrabajo.getNumeroTotalHoras())
                .pais(educacionTrabajo.getPais())
                .nombre(educacionTrabajo.getNombre())
                .institucion(educacionTrabajo.getInstitucion())
                .medioCapacitacion(educacionTrabajo.getMedioCapacitacion())
                .modalidad(educacionTrabajo.getModalidad())
                .diplomaActaCertificadoEstudio(educacionTrabajo.getDiplomaActaCertificadoEstudio())
                .build();

        return educacionTrabajoResponse;
    }

    @Override
    public IdiomaResponse obtenerIdioma(String usuarioId, String idiomaId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        Idioma idioma = curriculumFinal.getEducacion().getIdiomas().stream()
                .filter(f -> f.getId().equals(idiomaId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("No se ha encontrado el apartado idioma registrado con el id " + idiomaId));

        IdiomaResponse idiomaResponse = IdiomaResponse.builder()
                .idioma(idioma.getIdioma())
                .fechaCertificado(idioma.getFechaCertificado())
                .conversacion(idioma.getConversacion())
                .lectura(idioma.getLectura())
                .redaccion(idioma.getRedaccion())
                .lenguaNativa(idioma.getLenguaNativa())
                .certificado(idioma.getCertificado())
                .build();

        return idiomaResponse;
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
                .id(new ObjectId().toString())
                .tipoEntidad(curriculumRequest.getTipoEntidad())
                .nombreEntidad(curriculumRequest.getNombreEntidad())
                .pais(curriculumRequest.getPais())
                .departamento(curriculumRequest.getDepartamento())
                .municipio(curriculumRequest.getMunicipio())
                .direccionEntidad(curriculumRequest.getDireccionEntidad())
                .dependencia(curriculumRequest.getDependencia())
                .nivelJerarquicoEmpleo(curriculumRequest.getNivelJerarquicoEmpleo())
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
    public void actualizarExperienciaLaboral(String usuarioId, ActualizarExperienciaLaboralRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        ExperienciaLaboral experienciaLaboral = curriculumFinal.getExperienciasLaborales().stream()
                .filter(f -> f.getId().equals(curriculumRequest.getExperienciaLaboralId()))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Experiencia laboral no encontrada "));

        if (curriculumRequest.getTelefono() != null && !curriculumRequest.getTelefono().isBlank()) {
            experienciaLaboral.setTelefono(curriculumRequest.getTelefono());
        }
        if (curriculumRequest.getFechaRetiro() != null) {
            experienciaLaboral.setFechaRetiro(curriculumRequest.getFechaRetiro());
        }
        if (curriculumRequest.getHorasPromedioMes() != null && curriculumRequest.getHorasPromedioMes() != 0) {
            experienciaLaboral.setHorasPromedioMes(curriculumRequest.getHorasPromedioMes());
        }
        if (curriculumRequest.getMotivoRetiro() != null && !curriculumRequest.getMotivoRetiro().isBlank()) {
            experienciaLaboral.setMotivoRetiro(curriculumRequest.getMotivoRetiro());
        }
        if (curriculumRequest.getCertificadoLaboral() != null && !curriculumRequest.getCertificadoLaboral().isBlank()) {
            experienciaLaboral.setCertificadoLaboral(curriculumRequest.getCertificadoLaboral());
        }
        if (curriculumRequest.getDocumentoVerificado() != null) {
            experienciaLaboral.setDocumentoVerificado(curriculumRequest.getDocumentoVerificado());
        }


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
                .id(new ObjectId().toString())
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
    public void actualizarExperienciaLaboralDocente(String usuarioId, ActualizarExperienciaLaboralDocenteRequest curriculumRequest) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        ExperienciaLaboralDocente experienciaLaboralDocente = curriculumFinal.getExperienciasLaboralesDocente().stream()
                .filter(f -> f.getId().equals(curriculumRequest.getExperienciaLaboralDocenteId()))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Experiencia laboral de docente no encontrada "));


        if (curriculumRequest.getFechaTerminacion() != null) {
            experienciaLaboralDocente.setFechaTerminacion(curriculumRequest.getFechaTerminacion());
        }
        if (curriculumRequest.getHorasPromedioMes() != null && curriculumRequest.getHorasPromedioMes() != 0) {
            experienciaLaboralDocente.setHorasPromedioMes(curriculumRequest.getHorasPromedioMes());
        }
        if (curriculumRequest.getMotivoRetiro() != null && !curriculumRequest.getMotivoRetiro().isBlank()) {
            experienciaLaboralDocente.setMotivoRetiro(curriculumRequest.getMotivoRetiro());
        }
        if (curriculumRequest.getTelefono() != null && !curriculumRequest.getTelefono().isBlank()) {
            experienciaLaboralDocente.setTelefono(curriculumRequest.getTelefono());
        }
        if (curriculumRequest.getMateriaImpartida() != null && !curriculumRequest.getMateriaImpartida().isBlank()) {
            experienciaLaboralDocente.setMateriaImpartida(curriculumRequest.getMateriaImpartida());
        }
        if (curriculumRequest.getCertificadoLaboral() != null && !curriculumRequest.getCertificadoLaboral().isBlank()) {
            experienciaLaboralDocente.setCertificadoLaboral(curriculumRequest.getCertificadoLaboral());
        }
        if (curriculumRequest.getDocumentoVerificado() != null) {
            experienciaLaboralDocente.setDocumentoVerificado(curriculumRequest.getDocumentoVerificado());
        }


        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public List<ExperienciaLaboralResponse> obtenerTodasExperienciaLaboral(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getExperienciasLaborales() == null) {
            return Collections.emptyList();
        }

        return curriculumFinal.getExperienciasLaborales().stream()
                .map(experienciaLaboral -> ExperienciaLaboralResponse.builder()
                        .id(experienciaLaboral.getId())
                        .tipoEntidad(experienciaLaboral.getTipoEntidad())
                        .nombreEntidad(experienciaLaboral.getNombreEntidad())
                        .pais(experienciaLaboral.getPais())
                        .departamento(experienciaLaboral.getDepartamento())
                        .municipio(experienciaLaboral.getMunicipio())
                        .direccionEntidad(experienciaLaboral.getDireccionEntidad())
                        .dependencia(experienciaLaboral.getDependencia())
                        .nivelJerarquiaEmpleo(experienciaLaboral.getNivelJerarquicoEmpleo())
                        .cargo(experienciaLaboral.getCargo())
                        .telefono(experienciaLaboral.getTelefono())
                        .trabajoActual(experienciaLaboral.getTrabajoActual())
                        .fechaIngreso(experienciaLaboral.getFechaIngreso())
                        .fechaRetiro(experienciaLaboral.getFechaRetiro())
                        .jornadaLaboral(experienciaLaboral.getJornadaLaboral())
                        .horasPromedioMes(experienciaLaboral.getHorasPromedioMes())
                        .tiempoExperiencia(experienciaLaboral.getTiempoExperiencia())
                        .motivoRetiro(experienciaLaboral.getMotivoRetiro())
                        .certificadoLaboral(experienciaLaboral.getCertificadoLaboral())
                        .documentoVerificado(experienciaLaboral.getDocumentoVerificado())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ExperienciaLaboralDocenteResponse> obtenerTodasExperienciaLaboralDocente(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()){
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getExperienciasLaboralesDocente() == null) {
            return Collections.emptyList();
        }

        return curriculumFinal.getExperienciasLaboralesDocente().stream()
                .map(experienciaLaboralDocente -> ExperienciaLaboralDocenteResponse.builder()
                        .id(experienciaLaboralDocente.getId())
                        .tipoInstitucion(experienciaLaboralDocente.getTipoInstitucion())
                        .nombreInstitucion(experienciaLaboralDocente.getNombreInstitucion())
                        .pais(experienciaLaboralDocente.getPais())
                        .departamento(experienciaLaboralDocente.getDepartamento())
                        .municipio(experienciaLaboralDocente.getMunicipio())
                        .nivelAcademico(experienciaLaboralDocente.getNivelAcademico())
                        .areaConocimiento(experienciaLaboralDocente.getAreaConocimiento())
                        .tipoZona(experienciaLaboralDocente.getTipoZona())
                        .trabajoActual(experienciaLaboralDocente.getTrabajoActual())
                        .fechaIngreso(experienciaLaboralDocente.getFechaIngreso())
                        .fechaTerminacion(experienciaLaboralDocente.getFechaTerminacion())
                        .jornadaLaboral(experienciaLaboralDocente.getJornadaLaboral())
                        .horasPromedioMes(experienciaLaboralDocente.getHorasPromedioMes())
                        .motivoRetiro(experienciaLaboralDocente.getMotivoRetiro())
                        .telefono(experienciaLaboralDocente.getTelefono())
                        .materiaImpartida(experienciaLaboralDocente.getMateriaImpartida())
                        .tiempoExperiencia(experienciaLaboralDocente.getTiempoExperiencia())
                        .certificadoLaboral(experienciaLaboralDocente.getCertificadoLaboral())
                        .documentoVerificado(experienciaLaboralDocente.getDocumentoVerificado())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ExperienciaLaboralResponse obtenerExperienciaLaboral(String usuarioId, String experienciaLaboralId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getExperienciasLaborales() == null) {
            throw new CurriculumNotFoundException("No hay experiencias laborales registradas");
        }

        ExperienciaLaboral experienciaLaboral = curriculumFinal.getExperienciasLaborales().stream()
                .filter(e -> e.getId().equals(experienciaLaboralId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Experiencia laboral no encontrada con id: " + experienciaLaboralId));

        return ExperienciaLaboralResponse.builder()
                .id(experienciaLaboral.getId())
                .tipoEntidad(experienciaLaboral.getTipoEntidad())
                .nombreEntidad(experienciaLaboral.getNombreEntidad())
                .pais(experienciaLaboral.getPais())
                .departamento(experienciaLaboral.getDepartamento())
                .municipio(experienciaLaboral.getMunicipio())
                .direccionEntidad(experienciaLaboral.getDireccionEntidad())
                .dependencia(experienciaLaboral.getDependencia())
                .nivelJerarquiaEmpleo(experienciaLaboral.getNivelJerarquicoEmpleo())
                .cargo(experienciaLaboral.getCargo())
                .telefono(experienciaLaboral.getTelefono())
                .trabajoActual(experienciaLaboral.getTrabajoActual())
                .fechaIngreso(experienciaLaboral.getFechaIngreso())
                .fechaRetiro(experienciaLaboral.getFechaRetiro())
                .jornadaLaboral(experienciaLaboral.getJornadaLaboral())
                .horasPromedioMes(experienciaLaboral.getHorasPromedioMes())
                .tiempoExperiencia(experienciaLaboral.getTiempoExperiencia())
                .motivoRetiro(experienciaLaboral.getMotivoRetiro())
                .certificadoLaboral(experienciaLaboral.getCertificadoLaboral())
                .documentoVerificado(experienciaLaboral.getDocumentoVerificado())
                .build();
    }

    @Override
    public ExperienciaLaboralDocenteResponse obtenerExperienciaLaboralDocente(String usuarioId, String experienciaLaboralId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }

        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getExperienciasLaboralesDocente() == null) {
            throw new CurriculumNotFoundException("No hay experiencias laborales docentes registradas");
        }

        ExperienciaLaboralDocente experienciaLaboralDocente = curriculumFinal.getExperienciasLaboralesDocente().stream()
                .filter(e -> e.getId().equals(experienciaLaboralId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Experiencia laboral docente no encontrada con id: " + experienciaLaboralId));

        return ExperienciaLaboralDocenteResponse.builder()
                .id(experienciaLaboralDocente.getId())
                .tipoInstitucion(experienciaLaboralDocente.getTipoInstitucion())
                .nombreInstitucion(experienciaLaboralDocente.getNombreInstitucion())
                .pais(experienciaLaboralDocente.getPais())
                .departamento(experienciaLaboralDocente.getDepartamento())
                .municipio(experienciaLaboralDocente.getMunicipio())
                .nivelAcademico(experienciaLaboralDocente.getNivelAcademico())
                .areaConocimiento(experienciaLaboralDocente.getAreaConocimiento())
                .tipoZona(experienciaLaboralDocente.getTipoZona())
                .trabajoActual(experienciaLaboralDocente.getTrabajoActual())
                .fechaIngreso(experienciaLaboralDocente.getFechaIngreso())
                .fechaTerminacion(experienciaLaboralDocente.getFechaTerminacion())
                .jornadaLaboral(experienciaLaboralDocente.getJornadaLaboral())
                .horasPromedioMes(experienciaLaboralDocente.getHorasPromedioMes())
                .motivoRetiro(experienciaLaboralDocente.getMotivoRetiro())
                .telefono(experienciaLaboralDocente.getTelefono())
                .materiaImpartida(experienciaLaboralDocente.getMateriaImpartida())
                .tiempoExperiencia(experienciaLaboralDocente.getTiempoExperiencia())
                .certificadoLaboral(experienciaLaboralDocente.getCertificadoLaboral())
                .documentoVerificado(experienciaLaboralDocente.getDocumentoVerificado())
                .build();

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
                .id(new ObjectId().toString())
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
                .id(new ObjectId().toString())
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

        if (curriculumFinal.getGerenciaPublica().getParticipacionesProyectos() == null) {
            curriculumFinal.getGerenciaPublica().setParticipacionesProyectos(new ArrayList<>());
        }

        ParticipacionProyecto participacionProyecto = ParticipacionProyecto.builder()
                .id(new ObjectId().toString())
                .nombre(curriculumRequest.getNombre())
                .rolDesempeñado(curriculumRequest.getRolDesempeñado())
                .nombreEntidadOrganizacion(curriculumRequest.getNombreEntidadOrganizacion())
                .pais(curriculumRequest.getPais())
                .departamento(curriculumRequest.getDepartamento())
                .municipio(curriculumRequest.getMunicipio())
                .fechaInicio(curriculumRequest.getFechaInicio())
                .fechaTerminacion(curriculumRequest.getFechaTerminacion())
                .build();

        curriculumFinal.getGerenciaPublica().getParticipacionesProyectos().add(participacionProyecto);
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
                .id(new ObjectId().toString())
                .nombreCorporacion(curriculumRequest.getNombreCorporacion())
                .nombreRazonSocialInstitucion(curriculumRequest.getNombreRazonSocialInstitucion())
                .nombreEntidadOrganizacion(curriculumRequest.getNombreEntidadOrganizacion())
                .build();

        curriculumFinal.getGerenciaPublica().getParticipacionesCorporacionesEntidades().add(participacionCorporacionEntidad);
        curriculumRepository.save(curriculumFinal);
    }

    @Override
    public List<PublicacionResponse> obtenerTodasPublicaciones(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null || curriculumFinal.getGerenciaPublica().getPublicaciones() == null) {
            return Collections.emptyList();
        }

        return curriculumFinal.getGerenciaPublica().getPublicaciones().stream()
                .map(publicacion -> PublicacionResponse.builder()
                        .id(publicacion.getId())
                        .articulo(publicacion.getArticulo())
                        .nombreArticulo(publicacion.getNombreArticulo())
                        .libroResultadoInvestigacion(publicacion.getLibroResultadoInvestigacion())
                        .nombreLibroRevista(publicacion.getNombreLibroRevista())
                        .tiposProduccionBibliografica(publicacion.getTiposProduccionBibliografica())
                        .nombrePublicacion(publicacion.getNombrePublicacion())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public List<PremioReconocimientoResponse> obtenerTodosPremiosReconocimientos(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null || curriculumFinal.getGerenciaPublica().getPremiosReconocimientos() == null) {
            return Collections.emptyList();
        }
        return curriculumFinal.getGerenciaPublica().getPremiosReconocimientos().stream()
                .map(premioReconocimiento -> PremioReconocimientoResponse.builder()
                        .id(premioReconocimiento.getId())
                        .tipo(premioReconocimiento.getTipo())
                        .nombreEntidadOrganizacion(premioReconocimiento.getNombreEntidadOrganizacion())
                        .fecha(premioReconocimiento.getFecha())
                        .pais(premioReconocimiento.getPais())
                        .departamento(premioReconocimiento.getDepartamento())
                        .municipio(premioReconocimiento.getMunicipio())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipacionProyectoResponse> obtenerTodasParticipacionesProyectos(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null || curriculumFinal.getGerenciaPublica().getParticipacionesProyectos() == null) {
            return Collections.emptyList();
        }

        return curriculumFinal.getGerenciaPublica().getParticipacionesProyectos().stream()
                .map(participacionProyecto -> ParticipacionProyectoResponse.builder()
                        .id(participacionProyecto.getId())
                        .nombre(participacionProyecto.getNombre())
                        .rolDesempeñado(participacionProyecto.getRolDesempeñado())
                        .nombreEntidadOrganizacion(participacionProyecto.getNombreEntidadOrganizacion())
                        .pais(participacionProyecto.getPais())
                        .departamento(participacionProyecto.getDepartamento())
                        .municipio(participacionProyecto.getMunicipio())
                        .fechaInicio(participacionProyecto.getFechaInicio())
                        .fechaTerminacion(participacionProyecto.getFechaTerminacion())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipacionCorporacionEntidadResponse> obtenerTodasParticipacionesCorporacionesEntidades(String usuarioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null || curriculumFinal.getGerenciaPublica().getParticipacionesCorporacionesEntidades() == null) {
            return Collections.emptyList();
        }

        return curriculumFinal.getGerenciaPublica().getParticipacionesCorporacionesEntidades().stream()
                .map(participacionCorporacionEntidad -> ParticipacionCorporacionEntidadResponse.builder()
                        .id(participacionCorporacionEntidad.getId())
                        .nombreCorporacion(participacionCorporacionEntidad.getNombreCorporacion())
                        .nombreRazonSocialInstitucion(participacionCorporacionEntidad.getNombreRazonSocialInstitucion())
                        .nombreEntidadOrganizacion(participacionCorporacionEntidad.getNombreEntidadOrganizacion())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PublicacionResponse obtenerPublicacionPorId(String usuarioId, String publicacionId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null || curriculumFinal.getGerenciaPublica().getPublicaciones() == null) {
            throw new CurriculumNotFoundException("No hay publicaciones registradas");
        }

        Publicacion publicacion = curriculumFinal.getGerenciaPublica().getPublicaciones().stream()
                .filter(p -> p.getId().equals(publicacionId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Publicación no encontrada con id: " + publicacionId));

        return PublicacionResponse.builder()
                .id(publicacion.getId())
                .articulo(publicacion.getArticulo())
                .nombreArticulo(publicacion.getNombreArticulo())
                .libroResultadoInvestigacion(publicacion.getLibroResultadoInvestigacion())
                .nombreLibroRevista(publicacion.getNombreLibroRevista())
                .tiposProduccionBibliografica(publicacion.getTiposProduccionBibliografica())
                .nombrePublicacion(publicacion.getNombrePublicacion())
                .build();
    }

    @Override
    public PremioReconocimientoResponse obtenerPremioReconocimientoPorId(String usuarioId, String premioId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null || curriculumFinal.getGerenciaPublica().getPremiosReconocimientos() == null) {
            throw new CurriculumNotFoundException("No hay premios o reconocimientos registrados");
        }

        PremioReconocimiento premioReconocimiento = curriculumFinal.getGerenciaPublica().getPremiosReconocimientos().stream()
                .filter(p -> p.getId().equals(premioId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Premio/reconocimiento no encontrado con id: " + premioId));

        return PremioReconocimientoResponse.builder()
                .id(premioReconocimiento.getId())
                .tipo(premioReconocimiento.getTipo())
                .nombreEntidadOrganizacion(premioReconocimiento.getNombreEntidadOrganizacion())
                .fecha(premioReconocimiento.getFecha())
                .pais(premioReconocimiento.getPais())
                .departamento(premioReconocimiento.getDepartamento())
                .municipio(premioReconocimiento.getMunicipio())
                .build();
    }

    @Override
    public ParticipacionProyectoResponse obtenerParticipacionProyectoPorId(String usuarioId, String participacionId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null || curriculumFinal.getGerenciaPublica().getParticipacionesProyectos() == null) {
            throw new CurriculumNotFoundException("No hay participaciones en proyectos registradas");
        }

        ParticipacionProyecto participacionProyecto = curriculumFinal.getGerenciaPublica().getParticipacionesProyectos().stream()
                .filter(p -> p.getId().equals(participacionId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Participación en proyecto no encontrada con id: " + participacionId));

        return ParticipacionProyectoResponse.builder()
                .id(participacionProyecto.getId())
                .nombre(participacionProyecto.getNombre())
                .rolDesempeñado(participacionProyecto.getRolDesempeñado())
                .nombreEntidadOrganizacion(participacionProyecto.getNombreEntidadOrganizacion())
                .pais(participacionProyecto.getPais())
                .departamento(participacionProyecto.getDepartamento())
                .municipio(participacionProyecto.getMunicipio())
                .fechaInicio(participacionProyecto.getFechaInicio())
                .fechaTerminacion(participacionProyecto.getFechaTerminacion())
                .build();
    }

    @Override
    public ParticipacionCorporacionEntidadResponse obtenerParticipacionCorporacionEntidadPorId(String usuarioId, String corporacionId) {
        Optional<CurriculumModelo> curriculumExiste = curriculumRepository.findByUsuarioId(usuarioId);
        if (!curriculumExiste.isPresent()) {
            throw new CurriculumNotFoundException("No se ha encontrado un curriculum registrado para el usuario " + usuarioId);
        }
        CurriculumModelo curriculumFinal = curriculumExiste.get();

        if (curriculumFinal.getGerenciaPublica() == null || curriculumFinal.getGerenciaPublica().getParticipacionesCorporacionesEntidades() == null) {
            throw new CurriculumNotFoundException("No hay participaciones en corporaciones/entidades registradas");
        }

        ParticipacionCorporacionEntidad participacionCorporacionEntidad = curriculumFinal.getGerenciaPublica().getParticipacionesCorporacionesEntidades().stream()
                .filter(c -> c.getId().equals(corporacionId))
                .findFirst()
                .orElseThrow(() -> new CurriculumNotFoundException("Participación en corporación/entidad no encontrada con id: " + corporacionId));

        return ParticipacionCorporacionEntidadResponse.builder()
                .id(participacionCorporacionEntidad.getId())
                .nombreCorporacion(participacionCorporacionEntidad.getNombreCorporacion())
                .nombreRazonSocialInstitucion(participacionCorporacionEntidad.getNombreRazonSocialInstitucion())
                .nombreEntidadOrganizacion(participacionCorporacionEntidad.getNombreEntidadOrganizacion())
                .build();
    }


}
