package com.dtsvn.bookingmeeting.service.location;

import com.dtsvn.bookingmeeting.domain.location.Location;
import com.dtsvn.bookingmeeting.dto.location.LocationCreateRequest;
import com.dtsvn.bookingmeeting.dto.location.LocationResponse;
import com.dtsvn.bookingmeeting.dto.location.LocationUpdateRequest;
import com.dtsvn.bookingmeeting.mapper.location.LocationMapper;
import com.dtsvn.bookingmeeting.repository.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    @Transactional(readOnly = true)
    public LocationResponse getLocationById(Long id) {
        Location location = getLocationByIdOrThrow(id);
        return locationMapper.toResponse(location);
    }

    @Override
    public LocationResponse createLocation(LocationCreateRequest request) {
        Location location = locationMapper.toEntity(request);
        location.setActive(true);

        locationRepository.save(location);
        log.info("Created new location with id: {}", location.getId());
        return locationMapper.toResponse(location);
    }

    @Override
    public void deleteLocation(Long id) {
        Location location = getLocationByIdOrThrow(id);
        locationRepository.delete(location);
        log.info("Deleted location with id: {}", id);
    }

    @Override
    public LocationResponse updateLocation(Long id, LocationUpdateRequest request) {
        Location location = getLocationByIdOrThrow(id);

        locationMapper.updateEntity(location, request);

        locationRepository.save(location);
        log.info("Updated location with id: {}", id);
        return locationMapper.toResponse(location);
    }

    @Override
    public LocationResponse changeLocationStatus(Long id, boolean active) {
        Location location = getLocationByIdOrThrow(id);
        location.setActive(active);
        locationRepository.save(location);
        log.info("Changed status of location with id: {} to {}", id, active);
        return locationMapper.toResponse(location);
    }

    @Override
    public List<LocationResponse> getAllLocations(int page, int size, String sortBy, String sortDirection) {
        if (page < 0) page = 0;
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "createdAt";
        }
        if (sortDirection == null || !sortDirection.equalsIgnoreCase("ASC")) {
            sortDirection = "DESC";
        }

        // Nếu size <= 0, lấy tất cả bản ghi
        if (size <= 0) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            List<Location> allLocations = locationRepository.findAll(sort);
            
            return allLocations.stream()
                .map(locationMapper::toResponse)
                .toList();
        }

        // Nếu size > 0, sử dụng phân trang
        Pageable pageable = PageRequest.of(page, size,
            Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

        Page<Location> locationPage = locationRepository.findAll(pageable);

        List<LocationResponse> locationResponses = locationPage.getContent()
            .stream()
            .map(locationMapper::toResponse)
            .toList();

        return locationResponses;
    }

    private Location getLocationByIdOrThrow(Long id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

}
