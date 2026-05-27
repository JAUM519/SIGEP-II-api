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
import com.apirest.backend.models.enums.Curriculum.ClaseLibretaMilitarCurriculum;
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

    // ==================== NUEVOS TESTS ====================

    // -------------------- Obtener datos demográficos --------------------
    @Test
    void obtenerDatosDemograficos_Success() {
        DatosDemograficos datos = DatosDemograficos.builder()
                .nacionalidad("Colombiana")
                .estadoCivil(com.apirest.backend.models.enums.Curriculum.EstadoCivilCurriculum.SOLTERO)
                .build();
        curriculum.getDatosPersonales().setDatosDemograficos(datos);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        DatosDemograficosResponse response = curriculumService.obtenerDatosDemograficos(usuarioId);
        assertEquals("Colombiana", response.getNacionalidad());
        assertEquals("SOLTERO", response.getEstadoCivil().name());
    }

    @Test
    void obtenerDatosDemograficos_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.obtenerDatosDemograficos(usuarioId));
    }

    // -------------------- Obtener datos contacto --------------------
    @Test
    void obtenerDatosContacto_Success() {
        DatosContacto contacto = DatosContacto.builder()
                .paisResidencia("Colombia")
                .celular("3001234567")
                .build();
        curriculum.getDatosPersonales().setDatosContacto(contacto);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        DatosContactoResponse response = curriculumService.obtenerDatosContacto(usuarioId);
        assertEquals("Colombia", response.getPaisResidencia());
        assertEquals("3001234567", response.getCelular());
    }

    @Test
    void obtenerDatosContacto_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.obtenerDatosContacto(usuarioId));
    }

    // -------------------- Obtener todas las educaciones trabajo --------------------
    @Test
    void obtenerTodaEducacionTrabajo_Success() {
        EducacionTrabajo et1 = EducacionTrabajo.builder().id("1").nombre("Curso1").build();
        EducacionTrabajo et2 = EducacionTrabajo.builder().id("2").nombre("Curso2").build();
        Educacion educacion = new Educacion();
        educacion.setEducacionTrabajos(new ArrayList<>(List.of(et1, et2)));
        curriculum.setEducacion(educacion);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodaEducacionTrabajo(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Curso1", result.get(0).getNombre());
    }

    @Test
    void obtenerTodaEducacionTrabajo_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodaEducacionTrabajo(usuarioId);
        assertTrue(result.isEmpty());
    }

    // -------------------- Obtener todas los idiomas --------------------
    @Test
    void obtenerTodosIdiomas_Success() {
        Idioma i1 = Idioma.builder().id("1").idioma("Inglés").build();
        Idioma i2 = Idioma.builder().id("2").idioma("Francés").build();
        Educacion educacion = new Educacion();
        educacion.setIdiomas(new ArrayList<>(List.of(i1, i2)));
        curriculum.setEducacion(educacion);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodosIdiomas(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Inglés", result.get(0).getIdioma());
    }

    @Test
    void obtenerTodosIdiomas_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodosIdiomas(usuarioId);
        assertTrue(result.isEmpty());
    }

    // -------------------- Obtener educación trabajo por ID --------------------
    @Test
    void obtenerEducacionTrabajoPorId_Success() {
        String etId = new ObjectId().toString();
        EducacionTrabajo et = EducacionTrabajo.builder()
                .id(etId)
                .nombre("Curso Spring")
                .build();
        Educacion educacion = new Educacion();
        educacion.setEducacionTrabajos(new ArrayList<>(List.of(et)));
        curriculum.setEducacion(educacion);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerEducacionTrabajo(usuarioId, etId);
        assertEquals("Curso Spring", response.getNombre());
    }

    @Test
    void obtenerEducacionTrabajoPorId_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        curriculum.getEducacion().setEducacionTrabajos(new ArrayList<>());
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.obtenerEducacionTrabajo(usuarioId, "id-invalido"));
    }

    // -------------------- Obtener idioma por ID --------------------
    @Test
    void obtenerIdiomaPorId_Success() {
        String idiomaId = new ObjectId().toString();
        Idioma idioma = Idioma.builder()
                .id(idiomaId)
                .idioma("Portugués")
                .build();
        Educacion educacion = new Educacion();
        educacion.setIdiomas(new ArrayList<>(List.of(idioma)));
        curriculum.setEducacion(educacion);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerIdioma(usuarioId, idiomaId);
        assertEquals("Portugués", response.getIdioma());
    }

    @Test
    void obtenerIdiomaPorId_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        curriculum.getEducacion().setIdiomas(new ArrayList<>());
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.obtenerIdioma(usuarioId, "id-invalido"));
    }

    // -------------------- Obtener todas experiencias laborales --------------------
    @Test
    void obtenerTodasExperienciaLaboral_Success() {
        ExperienciaLaboral exp1 = ExperienciaLaboral.builder().id("1").cargo("Ingeniero").build();
        ExperienciaLaboral exp2 = ExperienciaLaboral.builder().id("2").cargo("Analista").build();
        curriculum.setExperienciasLaborales(new ArrayList<>(List.of(exp1, exp2)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodasExperienciaLaboral(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Ingeniero", result.get(0).getCargo());
    }

    @Test
    void obtenerTodasExperienciaLaboral_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodasExperienciaLaboral(usuarioId);
        assertTrue(result.isEmpty());
    }

    // -------------------- Obtener todas experiencias laborales docente --------------------
    @Test
    void obtenerTodasExperienciaLaboralDocente_Success() {
        ExperienciaLaboralDocente exp1 = ExperienciaLaboralDocente.builder().id("1").materiaImpartida("Matemáticas").build();
        ExperienciaLaboralDocente exp2 = ExperienciaLaboralDocente.builder().id("2").materiaImpartida("Física").build();
        curriculum.setExperienciasLaboralesDocente(new ArrayList<>(List.of(exp1, exp2)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodasExperienciaLaboralDocente(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Matemáticas", result.get(0).getMateriaImpartida());
    }

    @Test
    void obtenerTodasExperienciaLaboralDocente_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodasExperienciaLaboralDocente(usuarioId);
        assertTrue(result.isEmpty());
    }

    // -------------------- Obtener experiencia laboral docente por ID (corregido) --------------------
    // Nota: Para que este test pase, debes corregir el método obtenerExperienciaLaboralDocente en CurriculumServiceImp.
    // Cambia la línea:
    // if (curriculumFinal.getExperienciasLaborales() == null) { ... }
    // por:
    // if (curriculumFinal.getExperienciasLaboralesDocente() == null) { ... }
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

    // -------------------- Obtener todas participaciones proyectos --------------------
    @Test
    void obtenerTodasParticipacionesProyectos_Success() {
        ParticipacionProyecto p1 = ParticipacionProyecto.builder().id("1").nombre("Proyecto A").build();
        ParticipacionProyecto p2 = ParticipacionProyecto.builder().id("2").nombre("Proyecto B").build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setParticipacionesProyectos(new ArrayList<>(List.of(p1, p2)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodasParticipacionesProyectos(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Proyecto A", result.get(0).getNombre());
    }

    @Test
    void obtenerTodasParticipacionesProyectos_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodasParticipacionesProyectos(usuarioId);
        assertTrue(result.isEmpty());
    }

    // -------------------- Obtener todas participaciones corporaciones --------------------
    @Test
    void obtenerTodasParticipacionesCorporacionesEntidades_Success() {
        ParticipacionCorporacionEntidad c1 = ParticipacionCorporacionEntidad.builder().id("1").nombreCorporacion("Corp A").build();
        ParticipacionCorporacionEntidad c2 = ParticipacionCorporacionEntidad.builder().id("2").nombreCorporacion("Corp B").build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setParticipacionesCorporacionesEntidades(new ArrayList<>(List.of(c1, c2)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodasParticipacionesCorporacionesEntidades(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Corp A", result.get(0).getNombreCorporacion());
    }

    @Test
    void obtenerTodasParticipacionesCorporacionesEntidades_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodasParticipacionesCorporacionesEntidades(usuarioId);
        assertTrue(result.isEmpty());
    }

    // -------------------- Obtener participación proyecto por ID --------------------
    @Test
    void obtenerParticipacionProyectoPorId_Success() {
        String proyId = new ObjectId().toString();
        ParticipacionProyecto proy = ParticipacionProyecto.builder()
                .id(proyId)
                .nombre("Proyecto Especial")
                .build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setParticipacionesProyectos(new ArrayList<>(List.of(proy)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerParticipacionProyectoPorId(usuarioId, proyId);
        assertEquals("Proyecto Especial", response.getNombre());
    }

    @Test
    void obtenerParticipacionProyectoPorId_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.obtenerParticipacionProyectoPorId(usuarioId, "id-invalido"));
    }

    // -------------------- Obtener participación corporación por ID --------------------
    @Test
    void obtenerParticipacionCorporacionEntidadPorId_Success() {
        String corpId = new ObjectId().toString();
        ParticipacionCorporacionEntidad corp = ParticipacionCorporacionEntidad.builder()
                .id(corpId)
                .nombreCorporacion("Corporación XYZ")
                .build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setParticipacionesCorporacionesEntidades(new ArrayList<>(List.of(corp)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var response = curriculumService.obtenerParticipacionCorporacionEntidadPorId(usuarioId, corpId);
        assertEquals("Corporación XYZ", response.getNombreCorporacion());
    }

    @Test
    void obtenerParticipacionCorporacionEntidadPorId_NotFound_ThrowsException() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        assertThrows(CurriculumNotFoundException.class,
                () -> curriculumService.obtenerParticipacionCorporacionEntidadPorId(usuarioId, "id-invalido"));
    }

    // -------------------- Obtener todas premios (ya existe? añado) --------------------
    @Test
    void obtenerTodosPremiosReconocimientos_Success() {
        PremioReconocimiento p1 = PremioReconocimiento.builder().id("1").nombreEntidadOrganizacion("Premio1").build();
        PremioReconocimiento p2 = PremioReconocimiento.builder().id("2").nombreEntidadOrganizacion("Premio2").build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setPremiosReconocimientos(new ArrayList<>(List.of(p1, p2)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodosPremiosReconocimientos(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Premio1", result.get(0).getNombreEntidadOrganizacion());
    }

    @Test
    void obtenerTodosPremiosReconocimientos_Empty_ReturnsEmptyList() {
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodosPremiosReconocimientos(usuarioId);
        assertTrue(result.isEmpty());
    }

    // Más casos de actualización de datos personales
    @Test
    void actualizarDatosPersonalesBasicos_ConDocumentoIdentificacion_Success() {
        curriculum.getDatosPersonales().setDatosBasicos(new DatosBasicos());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarDatosBasicosRequest request = new ActualizarDatosBasicosRequest();
        request.setDocumentoIdentificacion("nuevo_doc.pdf");
        request.setDocumentoVerificado(true);
        curriculumService.actualizarDatosPersonalesBasicos(usuarioId, request);

        assertEquals("nuevo_doc.pdf", curriculum.getDatosPersonales().getDatosBasicos().getDocumentoIdentificacion());
        assertTrue(curriculum.getDatosPersonales().getDatosBasicos().getDocumentoVerificado());
        verify(curriculumRepository).save(curriculum);
    }

    @Test
    void actualizarDatosPersonalesBasicos_ConLibretaMilitar_Success() {
        curriculum.getDatosPersonales().setDatosBasicos(new DatosBasicos());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarDatosBasicosRequest request = new ActualizarDatosBasicosRequest();
        request.setLibretaMilitar("libreta.pdf");
        request.setLibretaVerificada(true);
        curriculumService.actualizarDatosPersonalesBasicos(usuarioId, request);

        assertEquals("libreta.pdf", curriculum.getDatosPersonales().getDatosBasicos().getLibretaMilitar());
        assertTrue(curriculum.getDatosPersonales().getDatosBasicos().getLibretaVerificada());
        verify(curriculumRepository).save(curriculum);
    }

    // Actualizar datos demográficos con preferencia étnica
    @Test
    void actualizarDatosDemograficos_ConPreferenciaEtnica_Success() {
        DatosDemograficos datos = new DatosDemograficos();
        curriculum.getDatosPersonales().setDatosDemograficos(datos);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarDatosDemograficosRequest request = new ActualizarDatosDemograficosRequest();
        request.setPreferenciaEtnica(com.apirest.backend.models.enums.Curriculum.PreferenciaEtnicaCurriculum.AFROCOLOMBIANO);
        curriculumService.actualizarDatosDemograficos(usuarioId, request);

        assertEquals(com.apirest.backend.models.enums.Curriculum.PreferenciaEtnicaCurriculum.AFROCOLOMBIANO, datos.getPreferenciaEtnica());
        verify(curriculumRepository).save(curriculum);
    }

    // Actualizar datos contacto con varios campos
    @Test
    void actualizarDatosContacto_ConMultiplesCampos_Success() {
        DatosContacto contacto = new DatosContacto();
        curriculum.getDatosPersonales().setDatosContacto(contacto);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarDatosContactoRequest request = new ActualizarDatosContactoRequest();
        request.setPaisResidencia("México");
        request.setDepartamentoResidencia("CDMX");
        request.setMunicipioResidencia("Cuauhtémoc");
        request.setZona(com.apirest.backend.models.enums.Curriculum.ZonaCurriculum.URBANA);
        request.setDireccionResidencia("Calle Reforma");
        request.setEmailPersonalPrincipal("nuevo@test.com");
        curriculumService.actualizarDatosContacto(usuarioId, request);

        assertEquals("México", contacto.getPaisResidencia());
        assertEquals("CDMX", contacto.getDepartamentoResidencia());
        assertEquals("Cuauhtémoc", contacto.getMunicipioResidencia());
        assertEquals(com.apirest.backend.models.enums.Curriculum.ZonaCurriculum.URBANA, contacto.getZona());
        assertEquals("Calle Reforma", contacto.getDireccionResidencia());
        assertEquals("nuevo@test.com", contacto.getEmailPersonalPrincipal());
        verify(curriculumRepository).save(curriculum);
    }

    @Test
    void actualizarDatosPersonalesBasicos_ConVariosCampos_Success() {
        curriculum.getDatosPersonales().setDatosBasicos(new DatosBasicos());
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarDatosBasicosRequest request = new ActualizarDatosBasicosRequest();
        request.setClaseLibretaMilitar(ClaseLibretaMilitarCurriculum.PRIMERA_CLASE);
        request.setNumeroLibretaMilitar("LM-123");
        request.setDistritoMilitar(8);
        request.setDocumentoIdentificacion("doc.pdf");
        request.setDocumentoVerificado(true);
        request.setLibretaMilitar("libreta.pdf");
        request.setLibretaVerificada(true);
        request.setPersonaExpuestaPoliticamente(true);

        curriculumService.actualizarDatosPersonalesBasicos(usuarioId, request);

        DatosBasicos datos = curriculum.getDatosPersonales().getDatosBasicos();
        assertEquals(ClaseLibretaMilitarCurriculum.PRIMERA_CLASE, datos.getClaseLibretaMilitar());
        assertEquals("LM-123", datos.getNumeroLibretaMilitar());
        assertEquals(8, datos.getDistritoMilitar());
        assertEquals("doc.pdf", datos.getDocumentoIdentificacion());
        assertTrue(datos.getDocumentoVerificado());
        assertEquals("libreta.pdf", datos.getLibretaMilitar());
        assertTrue(datos.getLibretaVerificada());
        assertTrue(datos.getPersonaExpuestaPoliticamente());
        verify(curriculumRepository).save(curriculum);
    }

    // ==================== TESTS ADICIONALES PARA AUMENTAR COBERTURA ====================

    // --- registrarEducacionTrabajo cuando educacion es null ---
    @Test
    void registrarEducacionTrabajo_CuandoEducacionEsNull_CreaEducacionYLista() {
        curriculum.setEducacion(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarEducacionTrabajoRequest request = new RegistrarEducacionTrabajoRequest();
        request.setFechaFinalizacion(Instant.now());
        request.setNumeroTotalHoras(100);
        request.setPais("Colombia");
        request.setNombre("Curso");
        request.setInstitucion("Inst");
        request.setMedioCapacitacion(com.apirest.backend.models.enums.Curriculum.MedioCapacitacionCurriculum.PRESENCIAL);
        request.setModalidad(com.apirest.backend.models.enums.Curriculum.ModalidadCurriculum.EDUCACION_INFORMAL);
        request.setDiplomaActaCertificadoEstudio("diploma.pdf");
        curriculumService.registrarEducacionTrabajo(usuarioId, request);
        assertNotNull(curriculum.getEducacion());
        assertNotNull(curriculum.getEducacion().getEducacionTrabajos());
        assertEquals(1, curriculum.getEducacion().getEducacionTrabajos().size());
    }

    // --- registrarIdioma cuando educacion es null ---
    @Test
    void registrarIdioma_CuandoEducacionEsNull_CreaEducacionYLista() {
        curriculum.setEducacion(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarIdiomaRequest request = new RegistrarIdiomaRequest();
        request.setIdioma("Inglés");
        request.setFechaCertificado(Instant.now());
        request.setConversacion(com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum.BIEN);
        request.setLectura(com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum.BIEN);
        request.setRedaccion(com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum.REGULAR);
        request.setLenguaNativa(false);
        curriculumService.registrarIdioma(usuarioId, request);
        assertNotNull(curriculum.getEducacion());
        assertNotNull(curriculum.getEducacion().getIdiomas());
        assertEquals(1, curriculum.getEducacion().getIdiomas().size());
    }

    // --- registrarPublicacion cuando gerenciaPublica es null ---
    @Test
    void registrarPublicacion_CuandoGerenciaPublicaEsNull_CreaGerenciaYLista() {
        curriculum.setGerenciaPublica(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarPublicacionRequest request = new RegistrarPublicacionRequest();
        request.setArticulo(com.apirest.backend.models.enums.Curriculum.ArticuloCurriculum.REVISTA_INDEXADA);
        request.setNombreArticulo("Test");
        request.setLibroResultadoInvestigacion(com.apirest.backend.models.enums.Curriculum.LibroResultadoInvestigacionCurriculum.ARTICULO_DE_REVISTA);
        request.setNombreLibroRevista("Revista");
        request.setTiposProduccionBibliografica(com.apirest.backend.models.enums.Curriculum.TiposProduccionBibliograficaCurriculum.DOCUMENTO_TRABAJO);
        request.setNombrePublicacion("Publicacion");
        curriculumService.registrarPublicacion(usuarioId, request);
        assertNotNull(curriculum.getGerenciaPublica());
        assertNotNull(curriculum.getGerenciaPublica().getPublicaciones());
        assertEquals(1, curriculum.getGerenciaPublica().getPublicaciones().size());
    }

    // Similar para registrarPremioReconocimiento, registrarParticipacionProyecto, registrarParticipacionCorporacionEntidad
    @Test
    void registrarPremioReconocimiento_CuandoGerenciaPublicaEsNull_CreaGerenciaYLista() {
        curriculum.setGerenciaPublica(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        RegistrarPremioReconocimientoRequest request = new RegistrarPremioReconocimientoRequest();
        request.setTipo(com.apirest.backend.models.enums.Curriculum.TipoPremioReconocimientoCurriculum.Premio);
        request.setNombreEntidadOrganizacion("Org");
        request.setFecha(Instant.now());
        request.setPais("Colombia");
        request.setDepartamento("Cund");
        request.setMunicipio("Bogotá");
        curriculumService.registrarPremioReconocimiento(usuarioId, request);
        assertNotNull(curriculum.getGerenciaPublica());
        assertNotNull(curriculum.getGerenciaPublica().getPremiosReconocimientos());
        assertEquals(1, curriculum.getGerenciaPublica().getPremiosReconocimientos().size());
    }

    // --- actualizarExperienciaLaboralDocente con todos los campos ---
    @Test
    void actualizarExperienciaLaboralDocente_ActualizaTodosLosCampos() {
        String expId = new ObjectId().toString();
        ExperienciaLaboralDocente exp = ExperienciaLaboralDocente.builder()
                .id(expId)
                .fechaTerminacion(Instant.now().minus(1, java.time.temporal.ChronoUnit.DAYS))
                .horasPromedioMes(10)
                .motivoRetiro("Viejo")
                .telefono("123")
                .materiaImpartida("Matemáticas")
                .certificadoLaboral("old.pdf")
                .documentoVerificado(false)
                .build();
        curriculum.setExperienciasLaboralesDocente(new ArrayList<>(List.of(exp)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarExperienciaLaboralDocenteRequest request = new ActualizarExperienciaLaboralDocenteRequest();
        request.setExperienciaLaboralDocenteId(expId);
        request.setFechaTerminacion(Instant.now());
        request.setHorasPromedioMes(20);
        request.setMotivoRetiro("Renuncia");
        request.setTelefono("456");
        request.setMateriaImpartida("Física");
        request.setCertificadoLaboral("new.pdf");
        request.setDocumentoVerificado(true);
        curriculumService.actualizarExperienciaLaboralDocente(usuarioId, request);

        assertNotNull(exp.getFechaTerminacion());
        assertEquals(20, exp.getHorasPromedioMes());
        assertEquals("Renuncia", exp.getMotivoRetiro());
        assertEquals("456", exp.getTelefono());
        assertEquals("Física", exp.getMateriaImpartida());
        assertEquals("new.pdf", exp.getCertificadoLaboral());
        assertTrue(exp.getDocumentoVerificado());
    }

    // --- actualizarExperienciaLaboral con todos los campos ---
    @Test
    void actualizarExperienciaLaboral_ActualizaTodosLosCampos() {
        String expId = new ObjectId().toString();
        ExperienciaLaboral exp = ExperienciaLaboral.builder()
                .id(expId)
                .telefono("old")
                .fechaRetiro(Instant.now().minus(1, java.time.temporal.ChronoUnit.DAYS))
                .horasPromedioMes(30)
                .motivoRetiro("Viejo")
                .certificadoLaboral("old.pdf")
                .documentoVerificado(false)
                .build();
        curriculum.setExperienciasLaborales(new ArrayList<>(List.of(exp)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarExperienciaLaboralRequest request = new ActualizarExperienciaLaboralRequest();
        request.setExperienciaLaboralId(expId);
        request.setTelefono("new");
        request.setFechaRetiro(Instant.now());
        request.setHorasPromedioMes(40);
        request.setMotivoRetiro("Renuncia");
        request.setCertificadoLaboral("new.pdf");
        request.setDocumentoVerificado(true);
        curriculumService.actualizarExperienciaLaboral(usuarioId, request);

        assertEquals("new", exp.getTelefono());
        assertNotNull(exp.getFechaRetiro());
        assertEquals(40, exp.getHorasPromedioMes());
        assertEquals("Renuncia", exp.getMotivoRetiro());
        assertEquals("new.pdf", exp.getCertificadoLaboral());
        assertTrue(exp.getDocumentoVerificado());
    }

    // --- actualizarDatosContacto con todos los campos (incluyendo los que faltaban) ---
    @Test
    void actualizarDatosContacto_ConTodosLosCampos() {
        DatosContacto contacto = new DatosContacto();
        curriculum.getDatosPersonales().setDatosContacto(contacto);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarDatosContactoRequest request = new ActualizarDatosContactoRequest();
        request.setPaisResidencia("Chile");
        request.setDepartamentoResidencia("Santiago");
        request.setMunicipioResidencia("Providencia");
        request.setZona(com.apirest.backend.models.enums.Curriculum.ZonaCurriculum.URBANA);
        request.setDireccionResidencia("Av. Siempre Viva");
        request.setTelefonoResidencia("222222");
        request.setCelular("333333");
        request.setTelefonoOficina("444444");
        request.setExtension("101");
        request.setEmailPersonalPrincipal("personal@test.com");
        request.setEmailOficina("oficina@test.com");
        curriculumService.actualizarDatosContacto(usuarioId, request);

        assertEquals("Chile", contacto.getPaisResidencia());
        assertEquals("Santiago", contacto.getDepartamentoResidencia());
        assertEquals("Providencia", contacto.getMunicipioResidencia());
        assertEquals(com.apirest.backend.models.enums.Curriculum.ZonaCurriculum.URBANA, contacto.getZona());
        assertEquals("Av. Siempre Viva", contacto.getDireccionResidencia());
        assertEquals("222222", contacto.getTelefonoResidencia());
        assertEquals("333333", contacto.getCelular());
        assertEquals("444444", contacto.getTelefonoOficina());
        assertEquals("101", contacto.getExtension());
        assertEquals("personal@test.com", contacto.getEmailPersonalPrincipal());
        assertEquals("oficina@test.com", contacto.getEmailOficina());
    }

    // --- obtenerTodasPublicaciones con datos ---
    @Test
    void obtenerTodasPublicaciones_ConDatos_RetornaLista() {
        Publicacion pub1 = Publicacion.builder().id("1").nombreArticulo("Art1").build();
        Publicacion pub2 = Publicacion.builder().id("2").nombreArticulo("Art2").build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setPublicaciones(new ArrayList<>(List.of(pub1, pub2)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodasPublicaciones(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Art1", result.get(0).getNombreArticulo());
    }

    // --- obtenerFormacionAcademica cuando educacion es null ---
    @Test
    void obtenerFormacionAcademica_CuandoEducacionEsNull_LanzaNullPointer() {
        curriculum.setEducacion(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        assertThrows(NullPointerException.class,
                () -> curriculumService.obtenerFormacionAcademica(usuarioId, "anyId"));
    }

    @Test
    void obtenerEducacionTrabajo_CuandoEducacionEsNull_LanzaNullPointer() {
        curriculum.setEducacion(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        assertThrows(NullPointerException.class,
                () -> curriculumService.obtenerEducacionTrabajo(usuarioId, "anyId"));
    }

    @Test
    void obtenerIdioma_CuandoEducacionEsNull_LanzaNullPointer() {
        curriculum.setEducacion(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        assertThrows(NullPointerException.class,
                () -> curriculumService.obtenerIdioma(usuarioId, "anyId"));
    }

    // --- obtenerTodasPublicaciones cuando gerenciaPublica es null ---
    @Test
    void obtenerTodasPublicaciones_CuandoGerenciaPublicaEsNull_RetornaEmptyList() {
        curriculum.setGerenciaPublica(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        var result = curriculumService.obtenerTodasPublicaciones(usuarioId);
        assertTrue(result.isEmpty());
    }

    @Test
    void registrarFormacionAcademica_CuandoEducacionEsNull_CreaEducacionYLista() {
        // Eliminamos la educacion del curriculum
        curriculum.setEducacion(null);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));
        when(curriculumRepository.save(any(CurriculumModelo.class))).thenAnswer(i -> i.getArgument(0));

        curriculumService.registrarFormacionAcademica(usuarioId, formacionRequest);

        assertNotNull(curriculum.getEducacion());
        assertNotNull(curriculum.getEducacion().getFormacionesAcademicas());
        assertEquals(1, curriculum.getEducacion().getFormacionesAcademicas().size());
        verify(curriculumRepository).save(curriculum);
    }

    @Test
    void actualizarFormacionAcademica_ConTodosLosCampos() {
        String formacionId = new ObjectId().toString();
        FormacionAcademica formacion = FormacionAcademica.builder()
                .id(formacionId)
                .tituloObtenido("Viejo")
                .build();
        curriculum.getEducacion().setFormacionesAcademicas(new ArrayList<>(List.of(formacion)));
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        ActualizarFormacionAcademicaRequest request = new ActualizarFormacionAcademicaRequest();
        request.setFormacionId(formacionId);
        request.setAreaConocimiento(com.apirest.backend.models.enums.Curriculum.AreaConocimientoCurriculum.INGENIERIA_ARQUITECTURA_URBANISMO_Y_AFINES);
        request.setProgramaAcademico("Nuevo Programa");
        request.setSemestresAprobados(8);
        request.setEstadoEstudio(com.apirest.backend.models.enums.Curriculum.EstadoEstudioCurriculum.Finalizado);
        request.setFechaTerminacionMaterias(Instant.now());
        request.setFechaGrado(Instant.now());
        request.setEstudioConvalidado(true);
        request.setFechaConvalidacion(Instant.now());
        request.setTarjetaProfesional("TP-123");
        request.setEstudioExterior(Instant.now());
        request.setArchivoTarjetaProfesioal("tarjeta.pdf");
        request.setVerificTarjetaProfesional(true);
        request.setArchivoEducacionFormal("diploma.pdf");
        request.setVerificEducacionFormal(true);

        curriculumService.actualizarFormacionAcademica(usuarioId, request);

        assertEquals(com.apirest.backend.models.enums.Curriculum.AreaConocimientoCurriculum.INGENIERIA_ARQUITECTURA_URBANISMO_Y_AFINES, formacion.getAreaConocimiento());
        assertEquals("Nuevo Programa", formacion.getProgramaAcademico());
        assertEquals(8, formacion.getSemestresAprobados());
        assertEquals(com.apirest.backend.models.enums.Curriculum.EstadoEstudioCurriculum.Finalizado, formacion.getEstadoEstudio());
        assertNotNull(formacion.getFechaTerminacionMaterias());
        assertNotNull(formacion.getFechaGrado());
        assertTrue(formacion.getEstudioConvalidado());
        assertNotNull(formacion.getFechaConvalidacion());
        assertEquals("TP-123", formacion.getTarjetaProfesional());
        assertNotNull(formacion.getEstudioExterior());
        assertEquals("tarjeta.pdf", formacion.getArchivoTarjetaProfesional());
        assertTrue(formacion.getVerificTarjetaProfesional());
        assertEquals("diploma.pdf", formacion.getArchivoEducacionFormal());
        assertTrue(formacion.getVerificEducacionFormal());
        verify(curriculumRepository).save(curriculum);
    }

    @Test
    void obtenerTodosPremiosReconocimientos_ConDatos_RetornaLista() {
        PremioReconocimiento p1 = PremioReconocimiento.builder()
                .id("1")
                .nombreEntidadOrganizacion("Premio 1")
                .tipo(com.apirest.backend.models.enums.Curriculum.TipoPremioReconocimientoCurriculum.Premio)
                .build();
        PremioReconocimiento p2 = PremioReconocimiento.builder()
                .id("2")
                .nombreEntidadOrganizacion("Reconocimiento 1")
                .tipo(com.apirest.backend.models.enums.Curriculum.TipoPremioReconocimientoCurriculum.Reconocimiento)
                .build();
        GerenciaPublica gp = new GerenciaPublica();
        gp.setPremiosReconocimientos(new ArrayList<>(List.of(p1, p2)));
        curriculum.setGerenciaPublica(gp);
        when(curriculumRepository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(curriculum));

        var result = curriculumService.obtenerTodosPremiosReconocimientos(usuarioId);
        assertEquals(2, result.size());
        assertEquals("Premio 1", result.get(0).getNombreEntidadOrganizacion());
    }



}

