package sorokin.java.course.bookingservicepavelsorokin.events.api;

import sorokin.java.course.bookingservicepavelsorokin.events.service.EventStatus;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventDto(
        @NotNull(message = "ID мероприятия не может быть пустым")
        @Positive(message = "ID мероприятия должен быть положительным числом")
        Long id,

        @NotBlank(message = "Название мероприятия не может быть пустым")
        @Size(min = 1, max = 255, message = "Длина названия мероприятия должна быть от 1 до 255 символов")
        String name,

        @NotBlank(message = "ID создателя мероприятия не может быть пустым")
        Long ownerId,

        @NotNull(message = "Максимальное количество мест должно быть указано")
        @Min(value = 1, message = "Максимальное количество мест должно быть не менее 1")
        Integer maxPlaces,

        @NotNull(message = "Количество занятых мест должно быть указано")
        @PositiveOrZero(message = "Количество занятых мест не может быть отрицательным")
        Integer occupiedPlaces,

        @NotNull(message = "Дата и время проведения мероприятия должны быть указаны")
        LocalDateTime date,

        @NotNull(message = "Стоимость мероприятия должна быть указана")
        @DecimalMin(value = "0.0", inclusive = true, message = "Стоимость мероприятия не может быть отрицательной")
        Integer cost,

        @NotNull(message = "Длительность мероприятия должна быть указана")
        @Min(value = 30, message = "Минимальная длительность мероприятия - 30 минут")
        Integer duration,

        @NotNull(message = "Идентификатор локации должен быть указан")
        @Positive(message = "Идентификатор локации должен быть положительным числом")
        Long locationId,

        @NotNull(message = "Статус мероприятия должен быть указан")
        EventStatus status
) {


    public EventDto {
        if (occupiedPlaces > maxPlaces) {
            throw new IllegalArgumentException("Количество занятых мест не может превышать максимальное количество мест");
        }
    }
}

