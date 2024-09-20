package sorokin.java.course.bookingservicepavelsorokin.users;

import jakarta.validation.constraints.NotBlank;

public record UserCredentials(
        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {}
