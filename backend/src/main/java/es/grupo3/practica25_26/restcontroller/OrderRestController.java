package es.grupo3.practica25_26.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo3.practica25_26.service.BillService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private BillService billService;

    @GetMapping("/{orderId}/bill")
    public ResponseEntity<byte[]> getMethodName(@PathVariable long orderId, HttpServletRequest request)
            throws IOException {
        return billService.pdfConfig(orderId);
    }

}
