package es.grupo3.practica25_26.service;

import com.samskivert.mustache.Mustache;

import es.grupo3.practica25_26.model.Order;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ResourceLoader resourceLoader;

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

    public ResponseEntity<byte[]> pdfConfig(long orderId) throws IOException {
        Optional<Order> op = orderService.findById(orderId);

        if (op.isPresent()) {
            Order order = op.get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            Map<String, Object> billData = new java.util.HashMap<>();
            billData.put("user", order.getUser());
            billData.put("orders", java.util.Collections.singletonList(order));
            billData.put("billDate", java.time.LocalDateTime.now().format(formatter));

            byte[] pdfContents = generateBillFromTemplate(billData);

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDisposition(org.springframework.http.ContentDisposition.inline()
                    .filename("invoice-" + orderId + ".pdf")
                    .build());

            return new ResponseEntity<>(pdfContents, headers, org.springframework.http.HttpStatus.OK);
        }
        return new ResponseEntity<>(org.springframework.http.HttpStatus.NOT_FOUND);
    }
}