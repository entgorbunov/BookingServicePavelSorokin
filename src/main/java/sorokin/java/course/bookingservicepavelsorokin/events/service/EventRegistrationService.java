package sorokin.java.course.bookingservicepavelsorokin.events.service;

import org.springframework.stereotype.Service;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventEntity;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventRegistrationEntity;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventRegistrationRepository;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventRepository;
import sorokin.java.course.bookingservicepavelsorokin.users.service.User;

import java.util.List;
import java.util.Optional;

@Service
public class EventRegistrationService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventService eventService;

    public EventRegistrationService(
            EventRepository eventRepository,
            EventRegistrationRepository eventRegistrationRepository,
            EventService eventService
    ) {
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventService = eventService;
    }

    public void registerUserOnEvent(
            User user,
            Long eventId
    ) {

        Event eventById = eventService.getEventById(eventId);

        if (user.id().equals(eventById.ownerId())) {
            throw new IllegalArgumentException("Owner cannot register on event");
        }

        Optional<EventRegistrationEntity> registration = eventRegistrationRepository
                .findRegistration(user.id(), eventId);
        if (registration.isPresent()) {
            throw new IllegalArgumentException("User already registered on Event");
        }

        if (!eventById.status().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Can't register. Event is already at launch");
        }

        eventRegistrationRepository.save(
                new EventRegistrationEntity(
                        null,
                        user.id(),
                        eventRepository.findById(eventId).orElseThrow()
                )
        );
    }

    public List<Event> getUserRegisteredEvents(Long userId) {
        List<EventEntity> registeredUser = eventRegistrationRepository.findRegisteredUser(userId);
        return registeredUser.stream()
                .map(EventEntityMapper::toDomain)
                .toList();
    }

    public void cancelUserRegistrationOnEvent(
            User user,
            Long eventId
    ) {
        Event eventById = eventService.getEventById(eventId);

        Optional<EventRegistrationEntity> registration = eventRegistrationRepository
                .findRegistration(user.id(), eventId);

        if (registration.isEmpty()) {
            throw new IllegalArgumentException("Can't cancel registration. Event not found");
        }

        if (!eventById.status().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Can't cancel registration. Event is already at launch");
        }

        eventRegistrationRepository.delete(registration.orElseThrow());

    }
}
