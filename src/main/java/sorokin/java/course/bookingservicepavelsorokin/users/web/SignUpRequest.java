package sorokin.java.course.bookingservicepavelsorokin.users.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(

        @NotBlank
        String login,

        @Min(18)
        Integer age,

        @NotBlank
        String password
) {
        @Override
        public String toString() {
                return "SignInUser{" +
                       "login='" + login + '\'' +
                       ", age=" + age +
                       '}';
        }
}
