package sorokin.java.course.bookingservicepavelsorokin.events.api;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sorokin.java.course.bookingservicepavelsorokin.events.service.Event;
import sorokin.java.course.bookingservicepavelsorokin.events.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventCreateRequestDto eventCreateRequestDto
    ) {

        log.info("Getting request for creating new Event: {}", eventCreateRequestDto);
        Event event = eventService.createEvent(eventCreateRequestDto);

        return ResponseEntity.status(201).body(EventDtoMapper.convertToDto(event));

    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable Long eventId,
            @RequestBody @Valid EventUpdateRequestDto updateRequestDto
    ) {
        log.info("Getting request for updating event: {}, with id: {}", updateRequestDto, eventId);
        var updatedEvent = eventService.updateEvent(eventId, updateRequestDto);
        return ResponseEntity.status(200).body(EventDtoMapper.convertToDto(updatedEvent));
            }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable("eventId") Long eventId) {

        log.info("Getting event by id: {}", eventId);

        Event eventById = eventService.getEventById(eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(EventDtoMapper.convertToDto(eventById));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEventById(
            @PathVariable("eventId") Long eventId
    ) {
        eventService.cancelEvent(eventId);
        return ResponseEntity.status(204).build();
    }

    @PostMapping("search")
    public ResponseEntity<List<EventDto>> searchEvent(
            @RequestBody @Valid EventSearchRequestDto searchFilter
    ) {
        log.info("Get request for searching events: filter{}", searchFilter);

        var foundEvents = eventService.searchByFilter(searchFilter);

        return ResponseEntity
                .status(200)
                .body(foundEvents.stream()
                        .map(EventDtoMapper::convertToDto)
                        .toList());
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getUserEvents(
            @PathVariable EventSearchRequestDto searchFilter
    ) {
        log.info("Get request for getting user events: filter{}", searchFilter);
        List<Event> events = eventService.getAllUserEvents();

        return ResponseEntity.status(200).body(events
                .stream()
                .map(EventDtoMapper::convertToDto)
                .toList());

    }
}
