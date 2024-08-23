package sorokin.java.course.bookingservicepavelsorokin.users.service;

public record User(
        Long id,
        String login,
        Integer age,
        UserRole role,
        String passwordHash
) {

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", login='" + login + '\'' +
               ", age=" + age +
               ", role=" + role +
               '}';
    }
}
