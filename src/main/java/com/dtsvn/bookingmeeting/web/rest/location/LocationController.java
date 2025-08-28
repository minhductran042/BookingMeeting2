package com.dtsvn.bookingmeeting.web.rest.location;

import com.dtsvn.bookingmeeting.dto.ApiResponse;
import com.dtsvn.bookingmeeting.dto.location.LocationCreateRequest;
import com.dtsvn.bookingmeeting.dto.location.LocationResponse;
import com.dtsvn.bookingmeeting.dto.location.LocationUpdateRequest;
import com.dtsvn.bookingmeeting.service.location.LocationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Slf4j
@AllArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/{id}")
    public ApiResponse<LocationResponse> getLocationById(@PathVariable Long id) {
        log.info("Get Location By Id: {}", id);
        LocationResponse location = locationService.getLocationById(id);
        if(location == null) {
            return new ApiResponse<>(404, "Location not found");
        }
        return new ApiResponse<>(200, "Location found", location);
    }

    @GetMapping
    public ApiResponse<List<LocationResponse>> getLocationList(@RequestParam(required = false, defaultValue = "0") int page,
                                                               @RequestParam(required = false, defaultValue = "0") int size,
                                                               @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                               @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        log.info("Get Location List: page={}, size={}, sortBy={}, sortDirection={}", page, size, sortBy, sortDirection);
        List<LocationResponse> locations = locationService.getAllLocations(page, size, sortBy, sortDirection);
        return new ApiResponse<>(200, "Locations retrieved successfully", locations);
    }

    @PostMapping
    public ApiResponse<LocationResponse> createLocation(@RequestBody LocationCreateRequest request) {
        log.info("Create Location: {}", request);
        try {
            LocationResponse location = locationService.createLocation(request);
            return new ApiResponse<>(201, "Location created successfully", location);
        } catch (Exception e) {
            log.error("Error creating location: {}", e.getMessage());
            return new ApiResponse<>(500, "Error creating location: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<LocationResponse> updateLocation(@PathVariable Long id, @RequestBody LocationUpdateRequest request) {
        log.info("Update Location: id={}, request={}", id, request);
        try {
            LocationResponse location = locationService.updateLocation(id, request);
            return new ApiResponse<>(200, "Location updated successfully", location);
        } catch (Exception e) {
            log.error("Error updating location: {}", e.getMessage());
            return new ApiResponse<>(500, "Error updating location: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteLocation(@PathVariable Long id) {
        log.info("Delete Location: id={}", id);
        try {
            locationService.deleteLocation(id);
            return new ApiResponse<>(204, "Location deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting location: {}", e.getMessage());
            return new ApiResponse<>(500, "Error deleting location: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<LocationResponse> changeLocationStatus(@PathVariable Long id, @RequestParam boolean active) {
        log.info("Change Location Status: id={}, active={}", id, active);
        try {
            LocationResponse location = locationService.changeLocationStatus(id, active);
            return new ApiResponse<>(200, "Location status changed successfully", location);
        } catch (Exception e) {
            log.error("Error changing location status: {}", e.getMessage());
            return new ApiResponse<>(500, "Error changing location status: " + e.getMessage());
        }
    }
}
