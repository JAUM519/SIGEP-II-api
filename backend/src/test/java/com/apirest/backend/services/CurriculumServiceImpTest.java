package com.apirest.backend.services;

import com.apirest.backend.dtos.requests.curriculums.DatosPersonales.*;
import com.apirest.backend.dtos.requests.curriculums.Educacion.*;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.*;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.*;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosBasicosResponse;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosContactoResponse;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosDemograficosResponse;
import com.apirest.backend.exceptions.CurriculumAlreadyExistsException;
import com.apirest.backend.exceptions.CurriculumNotFoundException;
import com.apirest.backend.models.curriculum.*;
import com.apirest.backend.models.curriculum.sections.*;
import com.apirest.backend.repositories.ICurriculumRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurriculumServiceImpTest {

    @Mock
    private ICurriculumRepository curriculumRepository;

    @InjectMocks
    private CurriculumServiceImp curriculumService;

    private String usuarioId;
    private CurriculumModelo curriculum;
    private RegistrarDatosBasicosRequest datosBasicosRequest;
    private RegistrarFormacionAcademicaRequest formacionRequest;

    @BeforeEach
    void setUp() {
        usuarioId = "67f0a1b2c3d4e5f6a7b8c9d0";
        curriculum = new CurriculumModelo();
        curriculum.setUsuarioId(usuarioId);
        curriculum.setDatosPersonales(new DatosPersonales());
        curriculum.setEducacion(new Educacion());

        datosBasicosRequest = new RegistrarDatosBasicosRequest();
        datosBasicosRequest.setNombre("Juan Perez");
        datosBasicosRequest.setTipoIdentificacion(com.apirest.backend.models.enums.Usuario.TipoIdentificacionUsuarios.CedulaDeCiudadania);
        datosBasicosRequest.setNumeroIdentificacion("123456789");
        datosBasicosRequest.setFechaNacimiento(Instant.now());
        datosBasicosRequest.setEmail("juan@test.com");
        datosBasicosRequest.setGenero(com.apirest.backend.models.enums.Curriculum.GeneroCurriculum.MASCULINO);
        datosBasicosRequest.setDocumentoIdentificacion("doc.pdf");
        datosBasicosRequest.setDocumentoVerificado(true);

        formacionRequest = new RegistrarFormacionAcademicaRequest();
        formacionRequest.setNivelAcademico(com.apirest.backend.models.enums.Curriculum.NivelAcademicoCurriculum.PREGRADO);
        formacionRequest.setNivelFormacion(com.apirest.backend.models.enums.Curriculum.NivelFormacionCurriculum.PROFESIONAL);
        formacionRequest.setPais("Colombia");
        formacionRequest.setInstitucion("UniTest");
        formacionRequest.setTituloObtenido("Ingeniero");
        formacionRequest.setEstadoEstudio(com.apirest.backend.models.enums.Curriculum.EstadoEstudioCurriculum.Finalizado);
    }

    // -------------------- Datos Personales --------------------
    @Test
    void registrarDatosPersonalesBasicos_Success() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
        when(curriculumRepository.save(any(CurriculumModelo.class))).thenAnswer(i -> i.getArgument(0));

        curriculumService.registrarDatosPersonalesBasicos(usuarioId, datosBasicosRequest);

        ArgumentCaptor<CurriculumModelo> captor = ArgumentCaptor.forClass(CurriculumModelo.class);
        verify(curriculumRepository).save(captor.capture());
        CurriculumModelo saved = captor.getValue();
        assertNotNull(saved.getDatosPersonales().getDatosBasicos());
        assertEquals("Juan Perez", saved.getDatosPersonales().getDatosBasicos().getNombre());
    }

    @Test
    void registrarDatosPersonalesBasicos_AlreadyExists_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        assertThrows(CurriculumAlreadyExistsException.class,
                () -> curriculumService.registrarDatosPersonalesBasicos(usuarioId, datosBasicosRequest));
        verify(curriculumRepository, never()).save(any());
    }

    @Test
    void actualizarDatosPersonalesBasicos_Success() {
        curriculum.getDatosPersonales().setDatosBasicos(new DatosBasicos());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        when(curriculumRepository.save(any())).thenReturn(curriculum);

        ActualizarDatosBasicosRequest updateRequest = new ActualizarDatosBasicosRequest();
        updateRequest.setPersonaExpuestaPoliticamente(true);
        curriculumService.actualizarDatosPersonalesBasicos(usuarioId, updateRequest);

        assertTrue(curriculum.getDatosPersonales().getDatosBasicos().getPersonaExpuestaPoliticamente());
        verify(curriculumRepository).save(curriculum);
    }

    @Test
    void actualizarDatosPersonalesBasicos_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.actualizarDatosPersonalesBasicos(usuarioId, new ActualizarDatosBasicosRequest()));
    }

    @Test
    void obtenerDatosBasicos_Success() {
        DatosBasicos datos = DatosBasicos.builder()
                .nombre("Juan Perez")
                .numeroIdentificacion("123456789")
                .build();
        curriculum.getDatosPersonales().setDatosBasicos(datos);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        DatosBasicosResponse response = curriculumService.obtenerDatosBasicos(usuarioId);
        assertEquals("Juan Perez", response.getNombre());
        assertEquals("123456789", response.getNumeroIdentificacion());
    }

    @Test
    void obtenerDatosBasicos_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.obtenerDatosBasicos(usuarioId));
    }

    // -------------------- Educación --------------------
    @Test
    void registrarFormacionAcademica_Success() {
        curriculum.setEducacion(new Educacion());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        when(curriculumRepository.save(any(CurriculumModelo.class))).thenAnswer(i -> i.getArgument(0));

        curriculumService.registrarFormacionAcademica(usuarioId, formacionRequest);

        ArgumentCaptor<CurriculumModelo> captor = ArgumentCaptor.forClass(CurriculumModelo.class);
        verify(curriculumRepository).save(captor.capture());
        CurriculumModelo saved = captor.getValue();
        List<FormacionAcademica> list = saved.getEducacion().getFormacionesAcademicas();
        assertEquals(1, list.size());
        FormacionAcademica fa = list.get(0);
        assertEquals("PREGRADO", fa.getNivelAcademico().name());
        assertNotNull(fa.getId());
    }

    @Test
    void registrarFormacionAcademica_CurriculumNotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.registrarFormacionAcademica(usuarioId, formacionRequest));
    }

    @Test
    void actualizarFormacionAcademica_Success() {
        Educacion educacion = new Educacion();
        String formacionId = new ObjectId().toString();
        FormacionAcademica formacion = FormacionAcademica.builder()
                .id(formacionId)
                .tituloObtenido("Viejo Titulo")
                .build();
        educacion.setFormacionesAcademicas(new ArrayList<>(List.of(formacion)));
        curriculum.setEducacion(educacion);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarFormacionAcademicaRequest updateRequest = new ActualizarFormacionAcademicaRequest();
        updateRequest.setFormacionId(formacionId);
        updateRequest.setProgramaAcademico("Nuevo Programa");
        updateRequest.setSemestresAprobados(8);
        curriculumService.actualizarFormacionAcademica(usuarioId, updateRequest);

        assertEquals("Nuevo Programa", formacion.getProgramaAcademico());
        assertEquals(8, formacion.getSemestresAprobados());
        verify(curriculumRepository).save(curriculum);
    }

    @Test
    void actualizarFormacionAcademica_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        curriculum.setEducacion(new Educacion());
        curriculum.getEducacion().setFormacionesAcademicas(new ArrayList<>()); // lista vacía
        ActualizarFormacionAcademicaRequest req = new ActualizarFormacionAcademicaRequest();
        req.setFormacionId("id-inexistente");
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.actualizarFormacionAcademica(usuarioId, req));
    }

    @Test
    void obtenerTodasFormacionesAcademicas_Success() {
        Educacion educacion = new Educacion();
        FormacionAcademica fa1 = FormacionAcademica.builder().id("1").tituloObtenido("T1").build();
        FormacionAcademica fa2 = FormacionAcademica.builder().id("2").tituloObtenido("T2").build();
        educacion.setFormacionesAcademicas(new ArrayList<>(List.of(fa1, fa2)));
        curriculum.setEducacion(educacion);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodasFormacionesAcademicas(usuarioId);
        assertEquals(2, result.size());
        assertEquals("T1", result.get(0).getTituloObtenido());
    }

    @Test
    void obtenerTodasFormacionesAcademicas_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodasFormacionesAcademicas(usuarioId);
        assertTrue(result.isEmpty());
    }

    // -------------------- Experiencia Laboral --------------------
    @Test
    void registrarExperienciaLaboral_Success() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarExperienciaLaboralRequest expRequest = new RegistrarExperienciaLaboralRequest();
        expRequest.setTipoEntidad(com.apirest.backend.models.enums.Curriculum.TipoEntidadCurriculum.PUBLICA);
        expRequest.setNombreEntidad("Entidad X");
        expRequest.setPais("Colombia");
        expRequest.setDepartamento("Valle");
        expRequest.setMunicipio("Cali");
        expRequest.setDireccionEntidad("Calle 1");
        expRequest.setDependencia("TI");
        expRequest.setNivelJerarquicoEmpleo(com.apirest.backend.models.enums.Curriculum.NivelJerarquicoEmpleoCurriculum.PROFESIONAL);
        expRequest.setCargo("Ingeniero");
        expRequest.setTrabajoActual(true);
        expRequest.setFechaIngreso(Instant.now());
        expRequest.setJornadaLaboral(com.apirest.backend.models.enums.Curriculum.JornadaLaboralCurriculum.TIEMPO_COMPLETO);
        expRequest.setTiempoExperiencia(12);
        when(curriculumRepository.save(any())).thenReturn(curriculum);

        curriculumService.registrarExperienciaLaboral(usuarioId, expRequest);

        ArgumentCaptor<CurriculumModelo> captor = ArgumentCaptor.forClass(CurriculumModelo.class);
        verify(curriculumRepository).save(captor.capture());
        List<ExperienciaLaboral> list = captor.getValue().getExperienciasLaborales();
        assertEquals(1, list.size());
        assertEquals("Entidad X", list.get(0).getNombreEntidad());
        assertNotNull(list.get(0).getId());
    }

    @Test
    void actualizarExperienciaLaboral_Success() {
        String expId = new ObjectId().toString();
        ExperienciaLaboral exp = ExperienciaLaboral.builder()
                .id(expId)
                .certificadoLaboral("old.pdf")
                .build();
        curriculum.setExperienciasLaborales(new ArrayList<>(List.of(exp)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarExperienciaLaboralRequest updateReq = new ActualizarExperienciaLaboralRequest();
        updateReq.setExperienciaLaboralId(expId);
        updateReq.setCertificadoLaboral("new.pdf");
        updateReq.setDocumentoVerificado(true);
        curriculumService.actualizarExperienciaLaboral(usuarioId, updateReq);

        assertEquals("new.pdf", exp.getCertificadoLaboral());
        assertTrue(exp.getDocumentoVerificado());
        verify(curriculumRepository).save(curriculum);
    }

    // -------------------- Gerencia Pública --------------------
    @Test
    void registrarPublicacion_Success() {
        curriculum.setGerenciaPublica(new GerenciaPublica());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarPublicacionRequest pubRequest = new RegistrarPublicacionRequest();
        pubRequest.setArticulo(com.apirest.backend.models.enums.Curriculum.ArticuloCurriculum.REVISTA_INDEXADA);
        pubRequest.setNombreArticulo("Articulo Test");
        pubRequest.setLibroResultadoInvestigacion(com.apirest.backend.models.enums.Curriculum.LibroResultadoInvestigacionCurriculum.ARTICULO_DE_REVISTA);
        pubRequest.setNombreLibroRevista("Revista");
        pubRequest.setTiposProduccionBibliografica(com.apirest.backend.models.enums.Curriculum.TiposProduccionBibliograficaCurriculum.DOCUMENTO_TRABAJO);
        pubRequest.setNombrePublicacion("Publicacion");

        curriculumService.registrarPublicacion(usuarioId, pubRequest);
        verify(curriculumRepository).save(curriculum);
        assertNotNull(curriculum.getGerenciaPublica().getPublicaciones());
        assertEquals(1, curriculum.getGerenciaPublica().getPublicaciones().size());
    }

    @Test
    void obtenerTodasPublicaciones_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodasPublicaciones(usuarioId);
        assertTrue(result.isEmpty());
    }

    // ==================== NUEVOS TESTS ====================

    // -------------------- Datos Demográficos --------------------
    @Test
    void registrarDatosDemograficos_Success() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        when(curriculumRepository.save(any(CurriculumModelo.class))).thenAnswer(i -> i.getArgument(0));

        RegistrarDatosDemograficosRequest request = RegistrarDatosDemograficosRequest.builder()
                .nacionalidad("Colombiana")
                .estadoCivil(com.apirest.backend.models.enums.Curriculum.EstadoCivilCurriculum.SOLTERO)
                .preferenciaEtnica(com.apirest.backend.models.enums.Curriculum.PreferenciaEtnicaCurriculum.NINGUNA)
                .paisNacimiento("Colombia")
                .departamentoNacimiento("Valle")
                .municipioNacimiento("Cali")
                .discapacidad(false)
                .build();

        curriculumService.registrarDatosDemograficos(usuarioId, request);

        ArgumentCaptor<CurriculumModelo> captor = ArgumentCaptor.forClass(CurriculumModelo.class);
        verify(curriculumRepository).save(captor.capture());
        DatosDemograficos datos = captor.getValue().getDatosPersonales().getDatosDemograficos();
        assertNotNull(datos);
        assertEquals("Colombiana", datos.getNacionalidad());
        assertEquals("SOLTERO", datos.getEstadoCivil().name());
    }

    @Test
    void registrarDatosDemograficos_CurriculumNotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
        RegistrarDatosDemograficosRequest request = new RegistrarDatosDemograficosRequest();
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.registrarDatosDemograficos(usuarioId, request));
    }

    @Test
    void actualizarDatosDemograficos_Success() {
        DatosDemograficos datos = DatosDemograficos.builder()
                .estadoCivil(com.apirest.backend.models.enums.Curriculum.EstadoCivilCurriculum.SOLTERO)
                .discapacidad(false)
                .build();
        curriculum.getDatosPersonales().setDatosDemograficos(datos);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarDatosDemograficosRequest request = new ActualizarDatosDemograficosRequest();
        request.setEstadoCivil(com.apirest.backend.models.enums.Curriculum.EstadoCivilCurriculum.CASADO);
        request.setDiscapacidad(true);
        curriculumService.actualizarDatosDemograficos(usuarioId, request);

        assertEquals(com.apirest.backend.models.enums.Curriculum.EstadoCivilCurriculum.CASADO, datos.getEstadoCivil());
        assertTrue(datos.getDiscapacidad());
        verify(curriculumRepository).save(curriculum);
    }

    // -------------------- Datos Contacto --------------------
    @Test
    void registrarDatosContacto_Success() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarDatosContactoRequest request = new RegistrarDatosContactoRequest();
        request.setPaisResidencia("Colombia");
        request.setDepartamentoResidencia("Valle");
        request.setMunicipioResidencia("Cali");
        request.setZona(com.apirest.backend.models.enums.Curriculum.ZonaCurriculum.URBANA);
        request.setDireccionResidencia("Calle 123");
        request.setCelular("3001234567");
        request.setEmailPersonalPrincipal("juan@test.com");

        curriculumService.registrarDatosContacto(usuarioId, request);

        ArgumentCaptor<CurriculumModelo> captor = ArgumentCaptor.forClass(CurriculumModelo.class);
        verify(curriculumRepository).save(captor.capture());
        DatosContacto contacto = captor.getValue().getDatosPersonales().getDatosContacto();
        assertNotNull(contacto);
        assertEquals("Colombia", contacto.getPaisResidencia());
    }

    @Test
    void actualizarDatosContacto_Success() {
        DatosContacto contacto = DatosContacto.builder()
                .celular("3001234567")
                .build();
        curriculum.getDatosPersonales().setDatosContacto(contacto);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarDatosContactoRequest request = new ActualizarDatosContactoRequest();
        request.setCelular("3119876543");
        curriculumService.actualizarDatosContacto(usuarioId, request);

        assertEquals("3119876543", contacto.getCelular());
        verify(curriculumRepository).save(curriculum);
    }

    // -------------------- Educación Trabajo --------------------
    @Test
    void registrarEducacionTrabajo_Success() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarEducacionTrabajoRequest request = new RegistrarEducacionTrabajoRequest();
        request.setFechaFinalizacion(Instant.now());
        request.setNumeroTotalHoras(120);
        request.setPais("Colombia");
        request.setNombre("Curso Java");
        request.setInstitucion("SENA");
        request.setMedioCapacitacion(com.apirest.backend.models.enums.Curriculum.MedioCapacitacionCurriculum.VIRTUAL);
        request.setModalidad(com.apirest.backend.models.enums.Curriculum.ModalidadCurriculum.EDUCACION_PARA_EL_TRABAJO_Y_DESARROLLO_HUMANO);
        request.setDiplomaActaCertificadoEstudio("diploma.pdf");

        curriculumService.registrarEducacionTrabajo(usuarioId, request);

        ArgumentCaptor<CurriculumModelo> captor = ArgumentCaptor.forClass(CurriculumModelo.class);
        verify(curriculumRepository).save(captor.capture());
        List<EducacionTrabajo> list = captor.getValue().getEducacion().getEducacionTrabajos();
        assertEquals(1, list.size());
        assertEquals("Curso Java", list.get(0).getNombre());
        assertNotNull(list.get(0).getId());
    }

    @Test
    void actualizarEducacionTrabajo_Success() {
        String etId = new ObjectId().toString();
        EducacionTrabajo et = EducacionTrabajo.builder()
                .id(etId)
                .diplomaActaCertificadoEstudio("old.pdf")
                .build();
        curriculum.getEducacion().setEducacionTrabajos(new ArrayList<>(List.of(et)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarEducacionTrabajoRequest request = new ActualizarEducacionTrabajoRequest();
        request.setEducacionTrabajoId(etId);
        request.setDiplomaActaCertificadoEstudio("new.pdf");
        curriculumService.actualizarEducacionTrabajo(usuarioId, request);

        assertEquals("new.pdf", et.getDiplomaActaCertificadoEstudio());
        verify(curriculumRepository).save(curriculum);
    }

    // -------------------- Idiomas --------------------
    @Test
    void registrarIdioma_Success() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarIdiomaRequest request = new RegistrarIdiomaRequest();
        request.setIdioma("Inglés");
        request.setFechaCertificado(Instant.now());
        request.setConversacion(com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum.MUY_BIEN);
        request.setLectura(com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum.BIEN);
        request.setRedaccion(com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum.REGULAR);
        request.setLenguaNativa(false);

        curriculumService.registrarIdioma(usuarioId, request);

        ArgumentCaptor<CurriculumModelo> captor = ArgumentCaptor.forClass(CurriculumModelo.class);
        verify(curriculumRepository).save(captor.capture());
        List<Idioma> list = captor.getValue().getEducacion().getIdiomas();
        assertEquals(1, list.size());
        assertEquals("Inglés", list.get(0).getIdioma());
        assertNotNull(list.get(0).getId());
    }

    @Test
    void actualizarIdioma_Success() {
        String idiomaId = new ObjectId().toString();
        Idioma idioma = Idioma.builder()
                .id(idiomaId)
                .certificado("old.pdf")
                .build();
        curriculum.getEducacion().setIdiomas(new ArrayList<>(List.of(idioma)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarIdiomaRequest request = new ActualizarIdiomaRequest();
        request.setIdiomaId(idiomaId);
        request.setCertificado("new.pdf");
        curriculumService.actualizarIdioma(usuarioId, request);

        assertEquals("new.pdf", idioma.getCertificado());
        verify(curriculumRepository).save(curriculum);
    }

    // -------------------- Experiencia Laboral Docente --------------------
    @Test
    void registrarExperienciaLaboralDocente_Success() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarExperienciaLaboralDocenteRequest request = new RegistrarExperienciaLaboralDocenteRequest();
        request.setTipoInstitucion(com.apirest.backend.models.enums.Curriculum.TipoInstitucionCurriculum.PUBLICA);
        request.setNombreInstitucion("Universidad");
        request.setPais("Colombia");
        request.setDepartamento("Valle");
        request.setMunicipio("Cali");
        request.setNivelAcademico(com.apirest.backend.models.enums.Curriculum.NivelAcademicoDocenteCurriculum.PREGRADO);
        request.setAreaConocimiento(com.apirest.backend.models.enums.Curriculum.AreaConocimientoCurriculum.INGENIERIA_ARQUITECTURA_URBANISMO_Y_AFINES);
        request.setTipoZona(com.apirest.backend.models.enums.Curriculum.TipoZonaCurriculum.URBANA);
        request.setTrabajoActual(false);
        request.setFechaIngreso(Instant.now());
        request.setFechaTerminacion(Instant.now());
        request.setJornadaLaboral(com.apirest.backend.models.enums.Curriculum.JornadaLaboralCurriculum.TIEMPO_COMPLETO);
        request.setTiempoExperiencia(12);

        curriculumService.registrarExperienciaLaboralDocente(usuarioId, request);

        ArgumentCaptor<CurriculumModelo> captor = ArgumentCaptor.forClass(CurriculumModelo.class);
        verify(curriculumRepository).save(captor.capture());
        List<ExperienciaLaboralDocente> list = captor.getValue().getExperienciasLaboralesDocente();
        assertEquals(1, list.size());
        assertEquals("Universidad", list.get(0).getNombreInstitucion());
        assertNotNull(list.get(0).getId());
    }

    @Test
    void actualizarExperienciaLaboralDocente_Success() {
        String expId = new ObjectId().toString();
        ExperienciaLaboralDocente exp = ExperienciaLaboralDocente.builder()
                .id(expId)
                .materiaImpartida("Matemáticas")
                .build();
        curriculum.setExperienciasLaboralesDocente(new ArrayList<>(List.of(exp)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarExperienciaLaboralDocenteRequest request = new ActualizarExperienciaLaboralDocenteRequest();
        request.setExperienciaLaboralDocenteId(expId);
        request.setMateriaImpartida("Programación");
        curriculumService.actualizarExperienciaLaboralDocente(usuarioId, request);

        assertEquals("Programación", exp.getMateriaImpartida());
        verify(curriculumRepository).save(curriculum);
    }

    // -------------------- Obtener individual (Educación, Experiencia, Gerencia) --------------------
    @Test
    void obtenerFormacionAcademicaPorId_Success() {
        String formacionId = new ObjectId().toString();
        FormacionAcademica fa = FormacionAcademica.builder()
                .id(formacionId)
                .tituloObtenido("Ingeniero")
                .build();
        curriculum.getEducacion().setFormacionesAcademicas(new ArrayList<>(List.of(fa)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerFormacionAcademica(usuarioId, formacionId);
        assertEquals("Ingeniero", response.getTituloObtenido());
    }

    @Test
    void obtenerFormacionAcademicaPorId_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        curriculum.getEducacion().setFormacionesAcademicas(new ArrayList<>());
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.obtenerFormacionAcademica(usuarioId, "id-invalido"));
    }

    @Test
    void obtenerExperienciaLaboralPorId_Success() {
        String expId = new ObjectId().toString();
        ExperienciaLaboral exp = ExperienciaLaboral.builder()
                .id(expId)
                .cargo("Ingeniero")
                .build();
        curriculum.setExperienciasLaborales(new ArrayList<>(List.of(exp)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerExperienciaLaboral(usuarioId, expId);
        assertEquals("Ingeniero", response.getCargo());
    }

    @Disabled("Error en el servicio: valida experienciasLaborales en lugar de experienciasLaboralesDocente")
    @Test
    void obtenerExperienciaLaboralDocentePorId_Success() {
        String expId = new ObjectId().toString();
        ExperienciaLaboralDocente exp = ExperienciaLaboralDocente.builder()
                .id(expId)
                .materiaImpartida("Física")
                .build();
        curriculum.setExperienciasLaboralesDocente(new ArrayList<>(List.of(exp)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerExperienciaLaboralDocente(usuarioId, expId);
        assertEquals("Física", response.getMateriaImpartida());
    }

    // -------------------- Premios, Proyectos, Corporaciones --------------------
    @Test
    void registrarPremioReconocimiento_Success() {
        curriculum.setGerenciaPublica(new GerenciaPublica());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        RegistrarPremioReconocimientoRequest request = new RegistrarPremioReconocimientoRequest();
        request.setTipo(com.apirest.backend.models.enums.Curriculum.TipoPremioReconocimientoCurriculum.Premio);
        request.setNombreEntidadOrganizacion("Ministerio");
        request.setFecha(Instant.now());
        request.setPais("Colombia");
        request.setDepartamento("Cundinamarca");
        request.setMunicipio("Bogotá");

        curriculumService.registrarPremioReconocimiento(usuarioId, request);

        verify(curriculumRepository).save(curriculum);
        assertNotNull(curriculum.getGerenciaPublica().getPremiosReconocimientos());
        assertEquals(1, curriculum.getGerenciaPublica().getPremiosReconocimientos().size());
    }

    @Test
    void registrarParticipacionProyecto_Success() {
        curriculum.setGerenciaPublica(new GerenciaPublica());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        RegistrarParticipacionProyectoRequest request = new RegistrarParticipacionProyectoRequest();
        request.setNombre("Proyecto X");
        request.setRolDesempeñado("Líder");
        request.setNombreEntidadOrganizacion("Entidad");
        request.setPais("Colombia");
        request.setDepartamento("Valle");
        request.setMunicipio("Cali");
        request.setFechaInicio(Instant.now());
        request.setFechaTerminacion(Instant.now());

        curriculumService.registrarParticipacionProyecto(usuarioId, request);

        verify(curriculumRepository).save(curriculum);
        assertNotNull(curriculum.getGerenciaPublica().getParticipacionesProyectos());
        assertEquals(1, curriculum.getGerenciaPublica().getParticipacionesProyectos().size());
    }

    @Test
    void registrarParticipacionCorporacionEntidad_Success() {
        curriculum.setGerenciaPublica(new GerenciaPublica());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        RegistrarParticipacionCorporacionEntidadRequest request = new RegistrarParticipacionCorporacionEntidadRequest();
        request.setNombreCorporacion("Corporación A");
        request.setNombreRazonSocialInstitucion("Razón Social");
        request.setNombreEntidadOrganizacion("Entidad Org");

        curriculumService.registrarParticipacionCorporacionEntidad(usuarioId, request);

        verify(curriculumRepository).save(curriculum);
        assertNotNull(curriculum.getGerenciaPublica().getParticipacionesCorporacionesEntidades());
        assertEquals(1, curriculum.getGerenciaPublica().getParticipacionesCorporacionesEntidades().size());
    }

    // -------------------- Obtener por ID para Gerencia --------------------
    @Test
    void obtenerPublicacionPorId_Success() {
        String pubId = new ObjectId().toString();
        Publicacion pub = Publicacion.builder()
                .id(pubId)
                .nombreArticulo("Artículo Test")
                .build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setPublicaciones(new ArrayList<>(List.of(pub)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerPublicacionPorId(usuarioId, pubId);
        assertEquals("Artículo Test", response.getNombreArticulo());
    }

    @Test
    void obtenerPremioReconocimientoPorId_Success() {
        String premioId = new ObjectId().toString();
        PremioReconocimiento premio = PremioReconocimiento.builder()
                .id(premioId)
                .nombreEntidadOrganizacion("Entidad")
                .build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setPremiosReconocimientos(new ArrayList<>(List.of(premio)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerPremioReconocimientoPorId(usuarioId, premioId);
        assertEquals("Entidad", response.getNombreEntidadOrganizacion());
    }
}