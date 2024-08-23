package sorokin.java.course.bookingservicepavelsorokin.users.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers() {
        createDefaultUsers("admin", "admin", UserRole.ADMIN);
        createDefaultUsers("user", "user", UserRole.USER);
    }

    private void createDefaultUsers(
            String login,
            String password,
            UserRole role
    ) {
        if (userService.isUserExistsByLogin(login)) {
            return;
        }

        String encode = passwordEncoder.encode(password);
        User user = new User(
                null,
                login,
                21,
                role,
                encode
        );
        userService.save(user);


    }
}
