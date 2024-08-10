package sorokin.java.course.bookingservicepavelsorokin.users.web;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(

        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
