package com.dtsvn.bookingmeeting.service.location;

import com.dtsvn.bookingmeeting.domain.location.Location;
import com.dtsvn.bookingmeeting.dto.location.LocationCreateRequest;
import com.dtsvn.bookingmeeting.dto.location.LocationResponse;
import com.dtsvn.bookingmeeting.dto.location.LocationUpdateRequest;

import java.util.List;

public interface LocationService {
    public LocationResponse getLocationById(Long id);
    public LocationResponse createLocation(LocationCreateRequest request);
    public LocationResponse updateLocation(Long id, LocationUpdateRequest request);
    public void deleteLocation(Long id);
    public LocationResponse changeLocationStatus(Long id, boolean active);
    public List<LocationResponse> getAllLocations(int page, int size, String sortBy, String sortDirection);
}
