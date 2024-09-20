package sorokin.java.course.bookingservicepavelsorokin.events.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorokin.java.course.bookingservicepavelsorokin.events.api.EventCreateRequestDto;
import sorokin.java.course.bookingservicepavelsorokin.events.api.EventDtoMapper;
import sorokin.java.course.bookingservicepavelsorokin.events.api.EventSearchRequestDto;
import sorokin.java.course.bookingservicepavelsorokin.events.api.EventUpdateRequestDto;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventEntity;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventRepository;
import sorokin.java.course.bookingservicepavelsorokin.location.Location;
import sorokin.java.course.bookingservicepavelsorokin.location.LocationService;
import sorokin.java.course.bookingservicepavelsorokin.users.service.AuthenticationService;
import sorokin.java.course.bookingservicepavelsorokin.users.service.User;
import sorokin.java.course.bookingservicepavelsorokin.users.service.UserRole;
import sorokin.java.course.bookingservicepavelsorokin.users.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService, LocationService locationService, AuthenticationService authenticationService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Event createEvent(EventCreateRequestDto eventCreateRequestDto) {

        Location location = locationService.getLocationById(eventCreateRequestDto.locationId());

        if (location.capacity() < eventCreateRequestDto.maxPlaces()) {
            throw new IllegalArgumentException("Capacity of location is: %d, but maxPlaces is: %d"
                    .formatted(location.capacity(), eventCreateRequestDto.maxPlaces()));
        }

        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();

        EventEntity eventEntity = new EventEntity(
                null,
                eventCreateRequestDto.name(),
                currentAuthenticatedUser.id(),
                eventCreateRequestDto.maxPlaces(),
                List.of(),
                eventCreateRequestDto.date(),
                eventCreateRequestDto.cost(),
                eventCreateRequestDto.duration(),
                eventCreateRequestDto.locationId(),
                EventStatus.WAIT_START
        );

        EventEntity entity = eventRepository.save(eventEntity);

        log.info("Created event eventId: {}", entity.getId());

        return EventEntityMapper.toDomain(entity);

    }


    public Event getEventById(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId).orElseThrow(EntityNotFoundException::new);
        return EventEntityMapper.toDomain(eventEntity);
    }

    public void cancelEvent(Long eventId) {
        User currentAuthenticatedUser = authenticationService.getCurrentAuthenticatedUser();

        EventEntity eventEntity = eventRepository
                .findById(eventId)
                .orElseThrow(EntityNotFoundException::new);

        if (!Objects.equals(eventEntity.getOwnerId(), currentAuthenticatedUser.id()) &&
            !currentAuthenticatedUser.role().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("Can't cancel event because the current user is not the owner or admin.");
        }

        if (eventEntity.getStatus() != EventStatus.WAIT_START) {
            throw new IllegalArgumentException("Can't cancel event because the event status is not WAIT_START.");
        }
        eventEntity.setStatus(EventStatus.CANCELLED);
        eventRepository.save(eventEntity);
    }

    public Event updateEvent(Long eventId,
                             EventUpdateRequestDto updateRequest) {
        checkCurrentUserCanModifyEvent(eventId);
        var event = eventRepository.findById(eventId).orElseThrow();

        if (!event.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Cannot modify event in status: %s"
                    .formatted(event.getStatus()));
        }

        if (updateRequest.maxPlaces() != null || updateRequest.locationId() != null) {
            var locationId = Optional.ofNullable(updateRequest.locationId())
                    .orElse(event.getLocationId());
            var maxPlaces = Optional.ofNullable(updateRequest.maxPlaces())
                    .orElse(event.getMaxPlaces());

            var location = locationService.getLocationById(locationId);
            if (location.capacity() < maxPlaces) {
                throw new IllegalArgumentException(
                        "Capacity of location less than maxPlaces: capacity=%s, maxPlaces=%s"
                                .formatted(location.capacity(), maxPlaces)
                );
            }
        }

        if (updateRequest.maxPlaces() != null
            && event.getRegistrationList().size() > updateRequest.maxPlaces()) {
            throw new IllegalArgumentException(
                    "Registration count is more than maxPlaces: regCount=%d, maxPlaces=%d"
                            .formatted(event.getRegistrationList().size(), updateRequest.maxPlaces()));
        }

        Optional.ofNullable(updateRequest.name())
                .ifPresent(event::setName);
        Optional.ofNullable(updateRequest.maxPlaces())
                .ifPresent(event::setMaxPlaces);
        Optional.ofNullable(updateRequest.date())
                .ifPresent(event::setDate);
        Optional.ofNullable(updateRequest.cost())
                .ifPresent(event::setCost);
        Optional.ofNullable(updateRequest.duration())
                .ifPresent(event::setDuration);
        Optional.ofNullable(updateRequest.locationId())
                .ifPresent(event::setLocationId);

        eventRepository.save(event);

        return EventEntityMapper.toDomain(event);
    }


    private void checkCurrentUserCanModifyEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthenticatedUser();
        var event = getEventById(eventId);

        if (!event.ownerId().equals(currentUser.id())
            && !currentUser.role().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("This user cannot modify this event");
        }
    }


    public List<Event> searchByFilter(EventSearchRequestDto searchFilter) {
        List<EventEntity> events = eventRepository.findEvents(
                searchFilter.name(),
                searchFilter.placesMin(),
                searchFilter.placesMax(),
                searchFilter.dateStartAfter(),
                searchFilter.dateStartBefore(),
                searchFilter.costMin(),
                searchFilter.costMax(),
                searchFilter.durationMin(),
                searchFilter.durationMax(),
                Math.toIntExact(searchFilter.locationId()),
                searchFilter.eventStatus()
        );
        return events.stream()
                .map(EventEntityMapper::toDomain)
                .toList();
    }

    public List<Event> getAllUserEvents() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        return eventRepository.findAllByOwnerIdIs(user.id()).stream()
                .map(EventEntityMapper::toDomain).toList();
    }
}

