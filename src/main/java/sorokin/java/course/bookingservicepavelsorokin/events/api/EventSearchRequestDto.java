package sorokin.java.course.bookingservicepavelsorokin.events.api;

import jakarta.validation.constraints.*;
import sorokin.java.course.bookingservicepavelsorokin.events.service.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequestDto(
        String name,

        @Min(value = 0, message = "Минимальная вместимость не может быть отрицательной")
        Integer placesMin,

        @Min(value = 0, message = "Максимальная вместимость не может быть отрицательной")
        Integer placesMax,

        LocalDateTime dateStartAfter,

        LocalDateTime dateStartBefore,

        @DecimalMin(value = "0.0", message = "Минимальная стоимость не может быть отрицательной")
        BigDecimal costMin,

        @DecimalMin(value = "0.0", message = "Максимальная стоимость не может быть отрицательной")
        BigDecimal costMax,

        @Min(value = 0, message = "Минимальная длительность не может быть отрицательной")
        Integer durationMin,

        @Min(value = 0, message = "Максимальная длительность не может быть отрицательной")
        Integer durationMax,

        @Positive(message = "Идентификатор локации должен быть положительным числом")
        Long locationId,

        EventStatus eventStatus
) {}
