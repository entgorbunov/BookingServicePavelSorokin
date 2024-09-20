package sorokin.java.course.bookingservicepavelsorokin.events.api;

public record EventRegistration(
        Long id,
        Long userId,
        Long eventId
) {
}
