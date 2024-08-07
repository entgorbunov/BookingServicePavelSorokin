//package sorokin.java.course.bookingservicepavelsorokin.user;
//
//import org.apache.catalina.User;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping
//    public ResponseEntity<User> registerUser(@RequestBody User user) {
//        User registeredUser = userService.registerUser(user);
//        return ResponseEntity.ok(registeredUser);
//    }
//
//    @GetMapping("/{userId}")
//    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
//        User user =
//    }
//}
