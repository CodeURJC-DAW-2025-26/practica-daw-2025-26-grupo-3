package es.grupo3.practica25_26.service;

import com.samskivert.mustache.Mustache;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BillService {
    private final ResourceLoader resourceLoader;

    public BillService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public byte[] generateBillFromTemplate(Map<String, Object> data) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:templates/pdf-bill.html");
        String baseUri = resourceLoader.getResource("classpath:/static/").getURL().toString();

        String htmlTemplate;
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            htmlTemplate = new BufferedReader(reader).lines().collect(Collectors.joining("\n"));
        }

        String htmlFinal = Mustache.compiler().compile(htmlTemplate).execute(data);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlFinal, baseUri);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
        }
    }
}