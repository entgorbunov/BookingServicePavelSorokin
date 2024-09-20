package sorokin.java.course.bookingservicepavelsorokin.users;

import jakarta.validation.constraints.*;

public record UserRegistration(
        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @NotBlank(message = "Пароль не может быть пустым")
        String password,

        @NotNull(message = "Возраст должен быть указан")
        @Min(value = 18, message = "Возраст должен быть не менее 18 лет")
        Integer age
) {}
