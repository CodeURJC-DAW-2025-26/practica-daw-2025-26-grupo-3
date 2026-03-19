package es.grupo3.practica25_26.service;

import com.samskivert.mustache.Mustache;

import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.User;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private UserService userService;

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

    public ResponseEntity<byte[]> orderPdfConfig(long orderId, User loggedUser) throws IOException {
        Optional<Order> op = orderService.findById(orderId);

        if (op.isPresent()) {
            Order order = op.get();
            if (order.getUser().getId() == loggedUser.getId() || loggedUser.getRoles().indexOf("ADMIN") != -1) {
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
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "You can't get the PDF bill from order " + order.getOrderID()
                                + " because you are not the owner.");
            }

        }
        return new ResponseEntity<>(org.springframework.http.HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<byte[]> allOrdersPdfConfig(User loggedUser) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss");

        List<Order> orders = loggedUser.getOrders();

        if (orders.size() > 0) {
            Map<String, Object> billData = new java.util.HashMap<>();
            billData.put("user", loggedUser);
            billData.put("orders", loggedUser.getOrders());
            billData.put("billDate", LocalDateTime.now().format(formatter));
            billData.put("isAll", true);
            billData.put("billTotal", userService.getAllOrdersPrice(loggedUser));

            byte[] pdfContents = this.generateBillFromTemplate(billData);

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDisposition(org.springframework.http.ContentDisposition.inline()
                    .filename("invoice-all.pdf")
                    .build());

            return new ResponseEntity<>(pdfContents, headers, org.springframework.http.HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "You can't get the all orders PDF bill from user " + loggedUser.getUserName()
                            + " because user has no orders.");
        }
    }
}