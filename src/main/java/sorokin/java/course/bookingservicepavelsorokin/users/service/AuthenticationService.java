package sorokin.java.course.bookingservicepavelsorokin.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sorokin.java.course.bookingservicepavelsorokin.security.JwtManager;
import sorokin.java.course.bookingservicepavelsorokin.users.web.SignInRequest;

@Service
public class AuthenticationService {

private final JwtManager jwtManager;
private final UserService userService;
private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthenticationService(JwtManager jwtManager, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtManager = jwtManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticateUser(SignInRequest signInRequest) {

        if (!userService.isUserExistsByLogin(signInRequest.login())) {
            throw new BadCredentialsException("Invalid username");
        }

        User user = userService.getUserByLogin(signInRequest.login());

        if (!passwordEncoder.matches(signInRequest.password(), user.passwordHash())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return jwtManager.generateToken(user);
    }
}
