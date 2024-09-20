package sorokin.java.course.bookingservicepavelsorokin.events.api;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record EventCreateRequestDto(
        @NotBlank(message = "Название мероприятия не может быть пустым")
        @Size(min = 1, max = 255, message = "Длина названия мероприятия должна быть от 1 до 255 символов")
        String name,

        @NotNull(message = "Максимальное количество мест должно быть указано")
        @Min(value = 1, message = "Максимальное количество мест должно быть не менее 1")
        Integer maxPlaces,

        @NotNull(message = "Дата и время проведения мероприятия должны быть указаны")
        @Future(message = "Дата и время проведения мероприятия должны быть в будущем")
        LocalDateTime date,

        @NotNull(message = "Стоимость мероприятия должна быть указана")
        @DecimalMin(value = "0", message = "Стоимость мероприятия не может быть отрицательной")
        Integer cost,

        @NotNull(message = "Длительность мероприятия должна быть указана")
        @Min(value = 30, message = "Минимальная длительность мероприятия - 30 минут")
        Integer duration,

        @NotNull(message = "Идентификатор локации должен быть указан")
        @Positive(message = "Идентификатор локации должен быть положительным числом")
        Long locationId
) {}
