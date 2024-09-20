package sorokin.java.course.bookingservicepavelsorokin.events.api;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventUpdateRequestDto(
        @Size(min = 1, max = 255, message = "Длина названия мероприятия должна быть от 1 до 255 символов")
        String name,

        @Min(value = 1, message = "Количество мест должно быть не менее 1")
        Integer maxPlaces,

        @Future(message = "Дата и время проведения мероприятия должны быть в будущем")
        LocalDateTime date,

        @DecimalMin(value = "0.0", inclusive = true, message = "Стоимость мероприятия не может быть отрицательной")
        Integer cost,

        @Min(value = 30, message = "Минимальная длительность мероприятия - 30 минут")
        Integer duration,

        @Positive(message = "Идентификатор локации должен быть положительным числом")
        Long locationId
) {}
