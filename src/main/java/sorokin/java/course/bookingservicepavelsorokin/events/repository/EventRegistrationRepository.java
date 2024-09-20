package sorokin.java.course.bookingservicepavelsorokin.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sorokin.java.course.bookingservicepavelsorokin.events.api.EventRegistration;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {


    @Query("""
                    Select reg from EventRegistrationEntity reg
                    where reg.event.id = :eventId
                    and reg.userId = :userId
            """)
    Optional<EventRegistrationEntity> findRegistration(
            @Param("userId") Long userId,
            @Param("eventId") Long eventId
    );


    @Query("""
    SELECT reg.event from EventRegistrationEntity reg
    where reg.userId = :userId
""")
    List<EventEntity> findRegisteredUser(
            @Param("userId") Long userId
    );
}
