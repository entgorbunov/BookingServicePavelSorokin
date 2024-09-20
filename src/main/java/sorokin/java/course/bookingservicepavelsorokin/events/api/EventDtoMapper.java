package sorokin.java.course.bookingservicepavelsorokin.events.api;

import org.springframework.stereotype.Component;
import sorokin.java.course.bookingservicepavelsorokin.events.service.Event;

@Component
public class EventDtoMapper {

    public static EventDto convertToDto(Event event) {
        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.registrationList().size(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }
}
