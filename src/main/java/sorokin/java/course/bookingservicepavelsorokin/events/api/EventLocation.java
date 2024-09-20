package sorokin.java.course.bookingservicepavelsorokin.events.api;

import jakarta.validation.constraints.*;

public record EventLocation(
        Long id,

        @NotBlank(message = "Имя локации не может быть пустым")
        String name,

        @NotBlank(message = "Адрес локации не может быть пустым")
        String address,

        @NotNull(message = "Вместимость локации должна быть указана")
        @Min(value = 5, message = "Минимальная вместимость локации - 5 человек")
        Integer capacity,

        String description
) {}

