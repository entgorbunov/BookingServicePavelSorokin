package sorokin.java.course.bookingservicepavelsorokin.users.web;

import sorokin.java.course.bookingservicepavelsorokin.users.service.UserRole;

public record UserDto (
        Long id,
        String login,
        Integer age,
        UserRole role
) {
}
