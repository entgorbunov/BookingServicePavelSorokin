package sorokin.java.course.bookingservicepavelsorokin.location;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    public LocationController(LocationService locationService, LocationDtoMapper locationDtoMapper) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        LOGGER.info("GET request for all locations");
        List<Location> locationList = locationService.getAllLocations();
        List<LocationDto> locationDtoList = locationList.stream().map(LocationDtoMapper::toDto).toList();
        return ResponseEntity.ok(locationDtoList);
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationDto
    ) {
        LOGGER.info("POST location request: locationDto={}", locationDto);
        Location location = LocationDtoMapper.toDomain(locationDto);
        Location createdLocation = locationService.createLocation(location);
        return ResponseEntity.status(201).body(LocationDtoMapper.toDto(createdLocation));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(
            @PathVariable @Valid Long locationId
    ) {
        LOGGER.info("GET location request: locationId{}", locationId);
        Location location = locationService.getLocationById(locationId);
        return ResponseEntity.status(204).body(LocationDtoMapper.toDto(location));
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable @Valid Long locationId,
            @RequestBody @Valid LocationDto locationDto) {
        LOGGER.info("UPDATE location request: locationId{}, locationDto{}",
                locationId, locationDto);

        Location location = locationService.updateLocation(LocationDtoMapper.toDomain(locationDto), locationId);
        return ResponseEntity.ok(LocationDtoMapper.toDto(location));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable @Valid Long locationId) {
        locationService.deleteLocation(locationId);
        LOGGER.info("DELETE location request: locationId{}", locationId);
        return ResponseEntity.noContent().build();
    }
}


