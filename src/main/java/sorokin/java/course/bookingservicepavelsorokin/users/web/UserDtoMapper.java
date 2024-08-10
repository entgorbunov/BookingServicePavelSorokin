package sorokin.java.course.bookingservicepavelsorokin.users.web;

import sorokin.java.course.bookingservicepavelsorokin.users.service.User;

public class UserDtoMapper {

    public static UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                user.role()
        );
    }

}
