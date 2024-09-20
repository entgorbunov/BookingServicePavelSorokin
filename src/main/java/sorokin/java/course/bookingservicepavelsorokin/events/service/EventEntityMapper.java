package sorokin.java.course.bookingservicepavelsorokin.events.service;

import org.springframework.stereotype.Component;
import sorokin.java.course.bookingservicepavelsorokin.events.api.EventRegistration;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventEntity;

@Component
public class EventEntityMapper {

    public static Event toDomain(EventEntity entity) {
        return new Event(
                entity.getId(),
                entity.getName(),
                entity.getOwnerId(),
                entity.getMaxPlaces(),
                entity.getRegistrationList().stream()
                        .map(eventRegistration -> new EventRegistration(
                                eventRegistration.getId(),
                                eventRegistration.getUserId(),
                                eventRegistration.getEvent().getId()
                        ))
                        .toList(),
                entity.getDate(),
                entity.getCost(),
                entity.getDuration(),
                entity.getLocationId(),
                entity.getStatus()
        );
    }
}
