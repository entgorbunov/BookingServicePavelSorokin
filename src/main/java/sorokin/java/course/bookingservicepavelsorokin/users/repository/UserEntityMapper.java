package sorokin.java.course.bookingservicepavelsorokin.users.repository;

import org.springframework.stereotype.Component;
import sorokin.java.course.bookingservicepavelsorokin.users.service.User;
import sorokin.java.course.bookingservicepavelsorokin.users.service.UserRole;

@Component
public class UserEntityMapper {
    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.age(),
                String.valueOf(user.role()),
                user.passwordHash()
        );

    }

    public static User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                UserRole.fromString(userEntity.getRole()),
                userEntity.getPasswordHash()
        );
    }
}
