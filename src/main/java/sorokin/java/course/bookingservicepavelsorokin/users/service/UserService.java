package sorokin.java.course.bookingservicepavelsorokin.users.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sorokin.java.course.bookingservicepavelsorokin.users.repository.UserEntity;
import sorokin.java.course.bookingservicepavelsorokin.users.repository.UserEntityMapper;
import sorokin.java.course.bookingservicepavelsorokin.users.repository.UserRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    public UserService(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public boolean isUserExistsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public User save(User user) {
        log.info("Saving user: {}", user);
        UserEntity userEntity = UserEntityMapper.toEntity(user);
        userEntity = userRepository.save(userEntity);
        return UserEntityMapper.toDomain(userEntity);
    }


    public User getUserByLogin(@NotBlank String login) {
        return userRepository.findByLogin(login)
                .map(UserEntityMapper::toDomain)
                .orElseThrow(
                        () -> new EntityNotFoundException("User %s not found".formatted(login))
                );
    }

    public User getUserById(@NotBlank Long id) {
        return userRepository.findById(id)
                .map(UserEntityMapper::toDomain)
                .orElseThrow(
                        () -> new EntityNotFoundException("User %d not found".formatted(id))
                );
    }
}
