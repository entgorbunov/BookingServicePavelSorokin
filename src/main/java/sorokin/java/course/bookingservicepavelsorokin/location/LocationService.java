package sorokin.java.course.bookingservicepavelsorokin.location;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository repository;
    private final LocationEntityMapper mapper;


    public LocationService(LocationRepository repository, LocationEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Location> getAllLocations() {
        return repository.findAll()
                .stream()
                .map(LocationEntityMapper::toDomain)
                .toList();
    }

    public Location createLocation(Location locationToCreate) {
        if (locationToCreate.id() != null) {
            throw new IllegalArgumentException("locationToCreate cannot be created. Id must be empty");
        }

        LocationEntity locationEntity = repository.save(LocationEntityMapper.toEntity(locationToCreate));
        return LocationEntityMapper.toDomain(locationEntity);

    }

    public Location updateLocation(Location locationToUpdate, Long locationId) {
        if (locationToUpdate.id() != null) {
            throw new IllegalArgumentException("locationToUpdate cannot be updated. Id must be empty");
        }
        return repository.findById(locationId)
                .map(existingLocation -> {

                    existingLocation.setName(locationToUpdate.name());
                    existingLocation.setAddress(locationToUpdate.address());
                    existingLocation.setCapacity(locationToUpdate.capacity());
                    existingLocation.setDescription(locationToUpdate.description());
                    LocationEntity updatedEntity = repository.save(existingLocation);
                    return LocationEntityMapper.toDomain(updatedEntity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Location entity with id " + locationId + " does not exist"));
    }

    public void deleteLocation(Long locationId) {
        LocationEntity locationEntity = repository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location entity with id " + locationId + " does not exist"));
        repository.delete(locationEntity);
    }

    public Location getLocationById(Long locationId) {
        LocationEntity locationEntity = repository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location entity does not exist id = %s".formatted(locationId)));
        return LocationEntityMapper.toDomain(locationEntity);

    }
}
