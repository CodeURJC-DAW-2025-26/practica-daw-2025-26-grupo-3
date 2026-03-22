package es.grupo3.practica25_26.dto;

import java.util.List;

public record DataGraphicDTO(
        List<String> labels, // names (x-axis)
        List<Long> data // amounts (y-axis)
) {

}
