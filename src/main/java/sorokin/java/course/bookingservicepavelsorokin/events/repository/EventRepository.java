package sorokin.java.course.bookingservicepavelsorokin.events.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sorokin.java.course.bookingservicepavelsorokin.events.service.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Modifying
    @Transactional
    @Query("update EventEntity e set e.status = :status where e.id = :id")
    void changeEventStatus(
            @Param("id") Long eventId,
            @Param("status") EventStatus status
    );

    @Query("""
            SELECT e FROM EventEntity e\s
            WHERE (:name IS NULL OR e.name LIKE %:name%)\s
            AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)\s
            AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)\s
            AND (CAST(:dateStartAfter as date) IS NULL OR e.date >= :dateStartAfter)\s
            AND (CAST(:dateStartBefore as date) IS NULL OR e.date <= :dateStartBefore)\s
            AND (:costMin IS NULL OR e.cost >= :costMin)\s
            AND (:costMax IS NULL OR e.cost <= :costMax)\s
            AND (:durationMin IS NULL OR e.duration >= :durationMin)\s
            AND (:durationMax IS NULL OR e.duration <= :durationMax)\s
            AND (:locationId IS NULL OR e.locationId = :locationId)\s
            AND (:eventStatus IS NULL OR e.status = :eventStatus)
           \s""")
    List<EventEntity> findEvents(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") BigDecimal costMin,
            @Param("costMax") BigDecimal costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Integer locationId,
            @Param("eventStatus") EventStatus eventStatus
    );

    List<EventEntity> findAllByOwnerIdIs(Long ownerId);


    @Query("""
        SELECT e.id from EventEntity e
        where e.date < CURRENT_TIMESTAMP
        AND e.status = :status
    """)
    List<Long> findStartedEventsWithStatus(@Param("status") EventStatus status);

    @Query(value = """
        SELECT e.id from events e
        where e.date + INTERVAL '1 minute' * e.duration < NOW()
        AND e.status = :status
    """, nativeQuery = true)
    List<Long> findEndedEventsWithStatus(@Param("status") EventStatus status);
}
