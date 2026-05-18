package com.apirest.backend.services;


import com.apirest.backend.dtos.requests.curriculums.DatosPersonales.*;
import com.apirest.backend.exceptions.CurriculumAlreadyExistsException;
import com.apirest.backend.exceptions.CurriculumNotFoundException;
import com.apirest.backend.models.curriculum.CurriculumModelo;
import com.apirest.backend.models.curriculum.sections.DatosBasicos;
import com.apirest.backend.models.curriculum.sections.DatosContacto;
import com.apirest.backend.models.curriculum.sections.DatosDemograficos;
import com.apirest.backend.repositories.ICurriculumRepository;
import org.springframework.stereotype.Service;

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
        if (curriculumExiste.isPresent()){
            throw new CurriculumAlreadyExistsException("Este usuario ya tiene datos basicos registrados. ");
        }
        CurriculumModelo curriculumFinal = new CurriculumModelo();
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
        if (curriculumExiste.isPresent()){
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
        if (curriculumExiste.isPresent()){
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
        if (curriculumExiste.isPresent()){
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
        if (curriculumExiste.isPresent()){
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
        if (curriculumExiste.isPresent()){
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


}
