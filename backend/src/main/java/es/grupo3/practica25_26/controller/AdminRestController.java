package es.grupo3.practica25_26.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo3.practica25_26.dto.DataGraphicDTO;
import es.grupo3.practica25_26.dto.KpiDTO;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.service.OrderService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/admin/stats")
public class AdminRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get KPIs statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KPIs retrieved successfully")
    })
    @GetMapping("/kpis")
    public ResponseEntity<KpiDTO> getKpis() {
        // we obtain the KPIs from the services
        long totalUsers = userService.countTotalUsers();
        long totalProducts = productService.countTotalProducts();
        long pendingOrders = orderService.countPendingOrders();
        double totalAmountMoney = orderService.calculateTotalSalesAmount();

        // we return the KPIs in a DTO
        return ResponseEntity.ok(new KpiDTO(totalUsers, totalProducts, pendingOrders, totalAmountMoney));
    }

    @Operation(summary = "Get top selling products for graphic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top selling products retrieved successfully")
    })
    @GetMapping("/top-products")
    public ResponseEntity<DataGraphicDTO> getTopProductsGraphic() {
        // obytain the info
        List<Object[]> topProducts = orderService.getTopSellingProducts(PageRequest.of(0, 5));
        List<String> names = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (Object[] row : topProducts) {
            names.add(row[0].toString());
            data.add(((Number) row[1]).longValue());
        }

        // return 2 lists with the data that are in json format
        return ResponseEntity.ok(new DataGraphicDTO(names, data));

    }

    @Operation(summary = "Get product state distribution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product state distribution retrieved successfully")
    })
    @GetMapping("/product-states")
    public ResponseEntity<DataGraphicDTO> getProductsStates() {
        // Get product state distribution data
        List<Product> allProducts = productService.findAll();
        long newCount = allProducts.stream().filter(p -> p.getState() == 0).count();
        long reconditionedCount = allProducts.stream().filter(p -> p.getState() == 1).count();
        long secondHandCount = allProducts.stream().filter(p -> p.getState() == 2).count();

        List<String> names = List.of("Nuevos", "Reacondicionados", "Segunda mano");
        List<Long> data = List.of(newCount, reconditionedCount, secondHandCount);

        // return 2 lists with the data that are in json format
        return ResponseEntity.ok(new DataGraphicDTO(names, data));

    }

}
