package sorokin.java.course.bookingservicepavelsorokin.events.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sorokin.java.course.bookingservicepavelsorokin.events.service.Event;
import sorokin.java.course.bookingservicepavelsorokin.events.service.EventRegistrationService;
import sorokin.java.course.bookingservicepavelsorokin.users.service.AuthenticationService;
import sorokin.java.course.bookingservicepavelsorokin.users.service.User;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class EventRegistrationController {

    public static final Logger log = LoggerFactory.getLogger(EventRegistrationController.class);


    private final EventRegistrationService eventRegistrationService;
    private final AuthenticationService authenticationService;

    public EventRegistrationController(
            EventRegistrationService eventRegistrationService,
            AuthenticationService authenticationService
    ) {
        this.eventRegistrationService = eventRegistrationService;
        this.authenticationService = authenticationService;
    }

    @RequestMapping("/{eventId}")
    public ResponseEntity<Void> registerOnEvent(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("registerOnEvent called with eventId {}", eventId);
        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();
        eventRegistrationService.registerUserOnEvent(currentAuthenticatedUser, eventId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("cancel/{eventId}")
    public ResponseEntity<Void> cancelRegistrationOnEvent(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("cancelRegistrationOnEvent called with eventId {}", eventId);
        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();
        eventRegistrationService.cancelUserRegistrationOnEvent(currentAuthenticatedUser, eventId);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getUserRegistrations() {
        log.info("getUserRegistrations called");

        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();
        List<Event> userRegisteredEvents = eventRegistrationService
                .getUserRegisteredEvents(currentAuthenticatedUser.id());

        return ResponseEntity.status(201).body(userRegisteredEvents.stream()
                .map(EventDtoMapper::convertToDto).toList());
    }


}
