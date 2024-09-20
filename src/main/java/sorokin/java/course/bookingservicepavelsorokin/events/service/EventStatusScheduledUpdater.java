package sorokin.java.course.bookingservicepavelsorokin.events.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventEntity;
import sorokin.java.course.bookingservicepavelsorokin.events.repository.EventRepository;

import java.util.List;

@Component
public class EventStatusScheduledUpdater {

    public static final Logger log = LoggerFactory.getLogger(EventStatusScheduledUpdater.class);

    private final EventRepository eventRepository;

    public EventStatusScheduledUpdater(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "${event.stats.cron}")
    public void updateEventStatuses() {
        log.info("EventStatusScheduledUpdater started");

        var startedEvents = eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START);
        startedEvents.forEach(eventId ->
                eventRepository.changeEventStatus(eventId, EventStatus.STARTED)
        );

        var endedEvents = eventRepository.findEndedEventsWithStatus(EventStatus.STARTED);
        endedEvents.forEach(eventId ->
                eventRepository.changeEventStatus(eventId, EventStatus.FINISHED)
        );
    }

}
