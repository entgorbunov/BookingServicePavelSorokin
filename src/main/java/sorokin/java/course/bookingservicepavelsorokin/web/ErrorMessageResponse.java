package sorokin.java.course.bookingservicepavelsorokin.web;

import java.time.LocalDateTime;

public record ErrorMessageResponse(String message, String detailedMessage, LocalDateTime dateTime) {
}
