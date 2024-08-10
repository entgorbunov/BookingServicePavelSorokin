package sorokin.java.course.bookingservicepavelsorokin.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login );

    Optional<UserEntity> Id(Long id);

}
