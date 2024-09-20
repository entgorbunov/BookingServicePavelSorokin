package sorokin.java.course.bookingservicepavelsorokin.users;

public record User(
        Long id,
        String login,
        Integer age,
        UserRole role
) {
    public enum UserRole {
        ADMIN, USER
    }
}

