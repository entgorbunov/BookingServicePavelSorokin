package sorokin.java.course.bookingservicepavelsorokin.users.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sorokin.java.course.bookingservicepavelsorokin.users.web.SignUpRequest;

@Service
public class UserRegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(SignUpRequest signUpRequest) {
        if (userService.isUserExistsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("Login already exists");
        }
        String encode = passwordEncoder.encode(signUpRequest.password());
        User user = new User(
                null,
                signUpRequest.login(),
                signUpRequest.age(),
                UserRole.USER,
                encode
        );
        return userService.save(user);
    }
}
