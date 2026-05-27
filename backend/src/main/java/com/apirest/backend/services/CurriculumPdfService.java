package com.apirest.backend.services;

import com.apirest.backend.models.curriculum.CurriculumModelo;
import com.apirest.backend.models.curriculum.DatosPersonales;
import com.apirest.backend.models.curriculum.Educacion;
import com.apirest.backend.models.curriculum.GerenciaPublica;
import com.apirest.backend.models.curriculum.ExperienciaLaboral;
import com.apirest.backend.models.curriculum.ExperienciaLaboralDocente;
import com.apirest.backend.models.curriculum.sections.DatosBasicos;
import com.apirest.backend.models.curriculum.sections.DatosContacto;
import com.apirest.backend.models.curriculum.sections.DatosDemograficos;
import com.apirest.backend.models.curriculum.sections.EducacionTrabajo;
import com.apirest.backend.models.curriculum.sections.FormacionAcademica;
import com.apirest.backend.models.curriculum.sections.Idioma;
import com.apirest.backend.models.curriculum.sections.ParticipacionCorporacionEntidad;
import com.apirest.backend.models.curriculum.sections.ParticipacionProyecto;
import com.apirest.backend.models.curriculum.sections.PremioReconocimiento;
import com.apirest.backend.models.curriculum.sections.Publicacion;
import com.apirest.backend.repositories.ICurriculumRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class CurriculumPdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/yyyy")
            .withZone(ZoneId.systemDefault());

    private static final Color BRAND_BLUE = new Color(30, 64, 175);
    private static final Color LIGHT_BLUE = new Color(239, 246, 255);
    private static final Color LIGHT_GRAY = new Color(248, 250, 252);
    private static final Color BORDER_GRAY = new Color(203, 213, 225);
    private static final Color TEXT_GRAY = new Color(71, 85, 105);

    private final ICurriculumRepository curriculumRepository;

    public CurriculumPdfService(ICurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    public byte[] generarHojaVida(String usuarioId) {
        CurriculumModelo curriculum = curriculumRepository.findByUsuarioId(usuarioId).orElse(null);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.LETTER, 36, 36, 36, 40);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addCover(document, datosBasicos(curriculum));
            addDatosPersonales(document, datosPersonales(curriculum));
            addEducacion(document, educacion(curriculum));
            addExperiencia(document, curriculum);
            addGerenciaPublica(document, gerenciaPublica(curriculum));

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException ex) {
            throw new IllegalStateException("No fue posible generar la hoja de vida en PDF.", ex);
        }
    }

    private void addCover(Document document, DatosBasicos datosBasicos) throws DocumentException {
        Paragraph title = new Paragraph("Hoja de Vida", font(22, Font.BOLD, BRAND_BLUE));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(8);
        document.add(title);

        Paragraph subtitle = new Paragraph("Sistema de Gestión del Empleo Público", font(11, Font.NORMAL, TEXT_GRAY));
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(18);
        document.add(subtitle);

        PdfPTable summary = new PdfPTable(1);
        summary.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(LIGHT_BLUE);
        cell.setBorderColor(BORDER_GRAY);
        cell.setPadding(14);
        cell.addElement(new Paragraph(value(datosBasicos != null ? datosBasicos.getNombre() : null), font(16, Font.BOLD, BRAND_BLUE)));
        cell.addElement(new Paragraph("Identificación: " + value(datosBasicos != null ? datosBasicos.getNumeroIdentificacion() : null), font(10, Font.NORMAL, TEXT_GRAY)));
        cell.addElement(new Paragraph("Correo: " + value(datosBasicos != null ? datosBasicos.getEmail() : null), font(10, Font.NORMAL, TEXT_GRAY)));
        cell.addElement(new Paragraph("Fecha de generación: " + DATE_FORMATTER.format(Instant.now()), font(10, Font.NORMAL, TEXT_GRAY)));
        summary.addCell(cell);
        summary.setSpacingAfter(18);
        document.add(summary);
    }

    private void addDatosPersonales(Document document, DatosPersonales datosPersonales) throws DocumentException {
        addSectionTitle(document, "Datos personales");

        if (datosPersonales == null) {
            addEmptyMessage(document, "No hay datos personales registrados.");
            return;
        }

        DatosBasicos basicos = datosPersonales.getDatosBasicos();
        DatosDemograficos demograficos = datosPersonales.getDatosDemograficos();
        DatosContacto contacto = datosPersonales.getDatosContacto();

        if (basicos == null && demograficos == null && contacto == null) {
            addEmptyMessage(document, "No hay datos personales registrados.");
            return;
        }

        PdfPTable table = table();
        if (basicos != null) {
            addField(table, "Nombre", basicos.getNombre());
            addField(table, "Tipo de identificación", basicos.getTipoIdentificacion());
            addField(table, "Número de identificación", basicos.getNumeroIdentificacion());
            addField(table, "Fecha de nacimiento", basicos.getFechaNacimiento());
            addField(table, "Correo", basicos.getEmail());
            addField(table, "Género", basicos.getGenero());
            boolean tieneLibretaMilitar = Boolean.TRUE.equals(basicos.getTieneLibretaMilitar())
                    || basicos.getClaseLibretaMilitar() != null
                    || hasText(basicos.getNumeroLibretaMilitar())
                    || basicos.getDistritoMilitar() != null
                    || hasText(basicos.getLibretaMilitar());
            addField(table, "Libreta militar", tieneLibretaMilitar ? "Sí" : "No aplica");
            if (tieneLibretaMilitar) {
                addField(table, "Clase de libreta militar", basicos.getClaseLibretaMilitar());
                addField(table, "Número libreta militar", basicos.getNumeroLibretaMilitar());
                addField(table, "Distrito militar", basicos.getDistritoMilitar());
                addField(table, "Archivo libreta militar", basicos.getLibretaMilitar());
                addField(table, "Libreta verificada", basicos.getLibretaVerificada());
            }
            addField(table, "Documento de identificación", basicos.getDocumentoIdentificacion());
            addField(table, "Documento verificado", basicos.getDocumentoVerificado());
            addField(table, "Persona expuesta políticamente", basicos.getPersonaExpuestaPoliticamente());
        }
        if (demograficos != null) {
            addField(table, "Nacionalidad", demograficos.getNacionalidad());
            addField(table, "Estado civil", demograficos.getEstadoCivil());
            addField(table, "Preferencia étnica", demograficos.getPreferenciaEtnica());
            addField(table, "País de nacimiento", demograficos.getPaisNacimiento());
            addField(table, "Departamento de nacimiento", demograficos.getDepartamentoNacimiento());
            addField(table, "Municipio de nacimiento", demograficos.getMunicipioNacimiento());
            addField(table, "Discapacidad", demograficos.getDiscapacidad());
        }
        if (contacto != null) {
            addField(table, "País de residencia", contacto.getPaisResidencia());
            addField(table, "Departamento de residencia", contacto.getDepartamentoResidencia());
            addField(table, "Municipio de residencia", contacto.getMunicipioResidencia());
            addField(table, "Zona", contacto.getZona());
            addField(table, "Dirección", contacto.getDireccionResidencia());
            addField(table, "Teléfono residencia", contacto.getTelefonoResidencia());
            addField(table, "Celular", contacto.getCelular());
            addField(table, "Teléfono oficina", contacto.getTelefonoOficina());
            addField(table, "Extensión", contacto.getExtension());
            addField(table, "Correo personal", contacto.getEmailPersonalPrincipal());
            addField(table, "Correo oficina", contacto.getEmailOficina());
        }
        document.add(table);
    }

    private void addEducacion(Document document, Educacion educacion) throws DocumentException {
        addSectionTitle(document, "Educación");

        addSubsectionTitle(document, "Formación académica");
        List<FormacionAcademica> formaciones = educacion != null ? safeList(educacion.getFormacionesAcademicas()) : Collections.emptyList();
        if (formaciones.isEmpty()) {
            addEmptyMessage(document, "No hay formación académica registrada.");
        } else {
            for (FormacionAcademica item : formaciones) {
                PdfPTable table = table();
                addField(table, "Nivel académico", item.getNivelAcademico());
                addField(table, "Nivel de formación", item.getNivelFormacion());
                addField(table, "Área de conocimiento", item.getAreaConocimiento());
                addField(table, "País", item.getPais());
                addField(table, "Institución", item.getInstitucion());
                addField(table, "Programa académico", item.getProgramaAcademico());
                addField(table, "Título obtenido", item.getTituloObtenido());
                addField(table, "Semestres aprobados", item.getSemestresAprobados());
                addField(table, "Estado", item.getEstadoEstudio());
                addField(table, "Fecha terminación materias", item.getFechaTerminacionMaterias());
                addField(table, "Fecha de grado", item.getFechaGrado());
                addField(table, "Estudio convalidado", item.getEstudioConvalidado());
                addField(table, "Fecha convalidación", item.getFechaConvalidacion());
                addField(table, "Tarjeta profesional", item.getTarjetaProfesional());
                addField(table, "Archivo educación formal", item.getArchivoEducacionFormal());
                addField(table, "Archivo tarjeta profesional", item.getArchivoTarjetaProfesional());
                document.add(table);
            }
        }

        addSubsectionTitle(document, "Educación para el trabajo");
        List<EducacionTrabajo> educacionesTrabajo = educacion != null ? safeList(educacion.getEducacionTrabajos()) : Collections.emptyList();
        if (educacionesTrabajo.isEmpty()) {
            addEmptyMessage(document, "No hay educación para el trabajo registrada.");
        } else {
            for (EducacionTrabajo item : educacionesTrabajo) {
                PdfPTable table = table();
                addField(table, "Nombre", item.getNombre());
                addField(table, "Institución", item.getInstitucion());
                addField(table, "País", item.getPais());
                addField(table, "Fecha de finalización", item.getFechaFinalizacion());
                addField(table, "Total de horas", item.getNumeroTotalHoras());
                addField(table, "Medio de capacitación", item.getMedioCapacitacion());
                addField(table, "Modalidad", item.getModalidad());
                addField(table, "Certificado", item.getDiplomaActaCertificadoEstudio());
                document.add(table);
            }
        }

        addSubsectionTitle(document, "Idiomas");
        List<Idioma> idiomas = educacion != null ? safeList(educacion.getIdiomas()) : Collections.emptyList();
        if (idiomas.isEmpty()) {
            addEmptyMessage(document, "No hay idiomas registrados.");
        } else {
            for (Idioma item : idiomas) {
                PdfPTable table = table();
                addField(table, "Idioma", item.getIdioma());
                addField(table, "Fecha certificado", item.getFechaCertificado());
                addField(table, "Conversación", item.getConversacion());
                addField(table, "Lectura", item.getLectura());
                addField(table, "Redacción", item.getRedaccion());
                addField(table, "Lengua nativa", item.getLenguaNativa());
                addField(table, "Certificado", item.getCertificado());
                document.add(table);
            }
        }
    }

    private void addExperiencia(Document document, CurriculumModelo curriculum) throws DocumentException {
        addSectionTitle(document, "Experiencia laboral");

        addSubsectionTitle(document, "Experiencia general");
        List<ExperienciaLaboral> experienciasLaborales = curriculum != null ? safeList(curriculum.getExperienciasLaborales()) : Collections.emptyList();
        if (experienciasLaborales.isEmpty()) {
            addEmptyMessage(document, "No hay experiencia laboral registrada.");
        } else {
            for (ExperienciaLaboral item : experienciasLaborales) {
                PdfPTable table = table();
                addField(table, "Tipo de entidad", item.getTipoEntidad());
                addField(table, "Nombre de entidad", item.getNombreEntidad());
                addField(table, "País", item.getPais());
                addField(table, "Departamento", item.getDepartamento());
                addField(table, "Municipio", item.getMunicipio());
                addField(table, "Dirección", item.getDireccionEntidad());
                addField(table, "Dependencia", item.getDependencia());
                addField(table, "Nivel jerárquico", item.getNivelJerarquicoEmpleo());
                addField(table, "Cargo", item.getCargo());
                addField(table, "Teléfono", item.getTelefono());
                addField(table, "Trabajo actual", item.getTrabajoActual());
                addField(table, "Fecha ingreso", item.getFechaIngreso());
                addField(table, "Fecha retiro", item.getFechaRetiro());
                addField(table, "Jornada", item.getJornadaLaboral());
                addField(table, "Horas promedio mes", item.getHorasPromedioMes());
                addField(table, "Tiempo de experiencia", item.getTiempoExperiencia());
                addField(table, "Motivo retiro", item.getMotivoRetiro());
                addField(table, "Certificado laboral", item.getCertificadoLaboral());
                document.add(table);
            }
        }

        addSubsectionTitle(document, "Experiencia docente");
        List<ExperienciaLaboralDocente> experienciasDocentes = curriculum != null ? safeList(curriculum.getExperienciasLaboralesDocente()) : Collections.emptyList();
        if (experienciasDocentes.isEmpty()) {
            addEmptyMessage(document, "No hay experiencia docente registrada.");
        } else {
            for (ExperienciaLaboralDocente item : experienciasDocentes) {
                PdfPTable table = table();
                addField(table, "Tipo de institución", item.getTipoInstitucion());
                addField(table, "Nombre institución", item.getNombreInstitucion());
                addField(table, "País", item.getPais());
                addField(table, "Departamento", item.getDepartamento());
                addField(table, "Municipio", item.getMunicipio());
                addField(table, "Nivel académico", item.getNivelAcademico());
                addField(table, "Área de conocimiento", item.getAreaConocimiento());
                addField(table, "Zona", item.getTipoZona());
                addField(table, "Trabajo actual", item.getTrabajoActual());
                addField(table, "Fecha ingreso", item.getFechaIngreso());
                addField(table, "Fecha terminación", item.getFechaTerminacion());
                addField(table, "Jornada", item.getJornadaLaboral());
                addField(table, "Horas promedio mes", item.getHorasPromedioMes());
                addField(table, "Motivo retiro", item.getMotivoRetiro());
                addField(table, "Teléfono", item.getTelefono());
                addField(table, "Materia impartida", item.getMateriaImpartida());
                addField(table, "Tiempo de experiencia", item.getTiempoExperiencia());
                addField(table, "Certificado laboral", item.getCertificadoLaboral());
                document.add(table);
            }
        }
    }

    private void addGerenciaPublica(Document document, GerenciaPublica gerenciaPublica) throws DocumentException {
        addSectionTitle(document, "Gerencia pública");

        addSubsectionTitle(document, "Publicaciones");
        List<Publicacion> publicaciones = gerenciaPublica != null ? safeList(gerenciaPublica.getPublicaciones()) : Collections.emptyList();
        if (publicaciones.isEmpty()) {
            addEmptyMessage(document, "No hay publicaciones registradas.");
        } else {
            for (Publicacion item : publicaciones) {
                PdfPTable table = table();
                addField(table, "Artículo", item.getArticulo());
                addField(table, "Nombre artículo", item.getNombreArticulo());
                addField(table, "Libro / investigación", item.getLibroResultadoInvestigacion());
                addField(table, "Libro o revista", item.getNombreLibroRevista());
                addField(table, "Tipo producción", item.getTiposProduccionBibliografica());
                addField(table, "Nombre publicación", item.getNombrePublicacion());
                document.add(table);
            }
        }

        addSubsectionTitle(document, "Premios y reconocimientos");
        List<PremioReconocimiento> premios = gerenciaPublica != null ? safeList(gerenciaPublica.getPremiosReconocimientos()) : Collections.emptyList();
        if (premios.isEmpty()) {
            addEmptyMessage(document, "No hay premios o reconocimientos registrados.");
        } else {
            for (PremioReconocimiento item : premios) {
                PdfPTable table = table();
                addField(table, "Tipo", item.getTipo());
                addField(table, "Entidad u organización", item.getNombreEntidadOrganizacion());
                addField(table, "Fecha", item.getFecha());
                addField(table, "País", item.getPais());
                addField(table, "Departamento", item.getDepartamento());
                addField(table, "Municipio", item.getMunicipio());
                document.add(table);
            }
        }

        addSubsectionTitle(document, "Participación en proyectos");
        List<ParticipacionProyecto> proyectos = gerenciaPublica != null ? safeList(gerenciaPublica.getParticipacionesProyectos()) : Collections.emptyList();
        if (proyectos.isEmpty()) {
            addEmptyMessage(document, "No hay proyectos registrados.");
        } else {
            for (ParticipacionProyecto item : proyectos) {
                PdfPTable table = table();
                addField(table, "Nombre", item.getNombre());
                addField(table, "Rol desempeñado", item.getRolDesempeñado());
                addField(table, "Entidad u organización", item.getNombreEntidadOrganizacion());
                addField(table, "País", item.getPais());
                addField(table, "Departamento", item.getDepartamento());
                addField(table, "Municipio", item.getMunicipio());
                addField(table, "Fecha inicio", item.getFechaInicio());
                addField(table, "Fecha terminación", item.getFechaTerminacion());
                document.add(table);
            }
        }

        addSubsectionTitle(document, "Participación en corporaciones o entidades");
        List<ParticipacionCorporacionEntidad> corporaciones = gerenciaPublica != null ? safeList(gerenciaPublica.getParticipacionesCorporacionesEntidades()) : Collections.emptyList();
        if (corporaciones.isEmpty()) {
            addEmptyMessage(document, "No hay participaciones registradas.");
        } else {
            for (ParticipacionCorporacionEntidad item : corporaciones) {
                PdfPTable table = table();
                addField(table, "Corporación", item.getNombreCorporacion());
                addField(table, "Razón social", item.getNombreRazonSocialInstitucion());
                addField(table, "Entidad u organización", item.getNombreEntidadOrganizacion());
                document.add(table);
            }
        }
    }

    private DatosPersonales datosPersonales(CurriculumModelo curriculum) {
        return curriculum != null ? curriculum.getDatosPersonales() : null;
    }

    private DatosBasicos datosBasicos(CurriculumModelo curriculum) {
        DatosPersonales datosPersonales = datosPersonales(curriculum);
        return datosPersonales != null ? datosPersonales.getDatosBasicos() : null;
    }

    private Educacion educacion(CurriculumModelo curriculum) {
        return curriculum != null ? curriculum.getEducacion() : null;
    }

    private GerenciaPublica gerenciaPublica(CurriculumModelo curriculum) {
        return curriculum != null ? curriculum.getGerenciaPublica() : null;
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        Paragraph paragraph = new Paragraph(title, font(14, Font.BOLD, BRAND_BLUE));
        paragraph.setSpacingBefore(12);
        paragraph.setSpacingAfter(8);
        document.add(paragraph);
    }

    private void addSubsectionTitle(Document document, String title) throws DocumentException {
        Paragraph paragraph = new Paragraph(title, font(11, Font.BOLD, TEXT_GRAY));
        paragraph.setSpacingBefore(8);
        paragraph.setSpacingAfter(6);
        document.add(paragraph);
    }

    private void addEmptyMessage(Document document, String message) throws DocumentException {
        Paragraph paragraph = new Paragraph(message, font(9, Font.ITALIC, TEXT_GRAY));
        paragraph.setSpacingAfter(8);
        document.add(paragraph);
    }

    private PdfPTable table() throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{32, 68});
        table.setSpacingAfter(8);
        return table;
    }

    private void addField(PdfPTable table, String label, Object rawValue) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font(8, Font.BOLD, TEXT_GRAY)));
        labelCell.setBackgroundColor(LIGHT_GRAY);
        labelCell.setBorderColor(BORDER_GRAY);
        labelCell.setPadding(6);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value(rawValue), font(8, Font.NORMAL, Color.BLACK)));
        valueCell.setBorderColor(BORDER_GRAY);
        valueCell.setPadding(6);
        table.addCell(valueCell);
    }

    private Font font(int size, int style, Color color) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, size, style);
        font.setColor(color);
        return font;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String value(Object rawValue) {
        if (rawValue == null) {
            return "No registrado";
        }

        if (rawValue instanceof Instant instant) {
            return DATE_FORMATTER.format(instant);
        }

        if (rawValue instanceof Boolean bool) {
            return bool ? "Sí" : "No";
        }

        if (rawValue instanceof Enum<?> enumValue) {
            return humanize(enumValue.name());
        }

        String value = Objects.toString(rawValue, "").trim();
        return value.isEmpty() ? "No registrado" : humanize(value);
    }

    private String humanize(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "No registrado";
        }

        String trimmed = raw.trim();

        // No tocar enlaces ni rutas de archivos
        if (isUrl(trimmed) || trimmed.contains("/api/archivos/")) {
            return trimmed;
        }

        String cleaned = trimmed.replace('_', ' ').replace('-', ' ').trim();

        if (cleaned.isEmpty()) {
            return "No registrado";
        }

        String[] words = cleaned.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isBlank()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    private boolean isUrl(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        String text = value.trim().toLowerCase();

        return text.startsWith("http://") || text.startsWith("https://");
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? Collections.emptyList() : values;
    }
}