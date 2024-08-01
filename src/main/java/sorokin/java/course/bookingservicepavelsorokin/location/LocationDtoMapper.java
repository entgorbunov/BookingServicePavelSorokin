package sorokin.java.course.bookingservicepavelsorokin.location;

import org.springframework.stereotype.Component;

@Component
public class LocationDtoMapper {

    public LocationDtoMapper() {
    }

    public static LocationDto toDto(Location location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public static Location toDomain(LocationDto locationDto) {
        return new Location(
                locationDto.id(),
                locationDto.name(),
                locationDto.address(),
                locationDto.capacity(),
                locationDto.description()
        );
    }
}