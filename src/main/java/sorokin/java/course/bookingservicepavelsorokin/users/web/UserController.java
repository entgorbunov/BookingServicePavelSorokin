package sorokin.java.course.bookingservicepavelsorokin.users.web;

import io.jsonwebtoken.impl.JwtTokenizer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sorokin.java.course.bookingservicepavelsorokin.security.JwtManager;
import sorokin.java.course.bookingservicepavelsorokin.users.service.AuthenticationService;
import sorokin.java.course.bookingservicepavelsorokin.users.service.UserRegistrationService;
import sorokin.java.course.bookingservicepavelsorokin.users.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRegistrationService userRegistrationService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UserController(
            UserRegistrationService userRegistrationService,
            AuthenticationService authenticationService,
            UserService userService
    ) {
        this.userRegistrationService = userRegistrationService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        log.info("Registering user: {}", signUpRequest);
        var newUser = userRegistrationService.register(signUpRequest);


        return ResponseEntity.status(201).body(UserDtoMapper.toDto(newUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        log.info("Getting user: {}", userId);
        userService.getUserById(userId);
        return ResponseEntity.status(200).body(UserDtoMapper.toDto(userService.getUserById(userId)));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(@RequestBody @Valid SignInRequest signInRequest) {
        log.info("Authenticating user: {}", signInRequest.login());
        String token = authenticationService.authenticateUser(signInRequest);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}
