package com.dtsvn.bookingmeeting.mapper.location;

import com.dtsvn.bookingmeeting.domain.location.Location;
import com.dtsvn.bookingmeeting.dto.location.LocationCreateRequest;
import com.dtsvn.bookingmeeting.dto.location.LocationResponse;
import com.dtsvn.bookingmeeting.dto.location.LocationUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    
    Location toEntity(LocationCreateRequest request);
    
    LocationResponse toResponse(Location location);
    
    void updateEntity(@MappingTarget Location location, LocationUpdateRequest request);
    
    List<LocationResponse> toResponseList(List<Location> locations);
}
