package com.dtsvn.bookingmeeting.dto.location;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO for creating/updating a location.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationCreateRequest {

    @NotBlank(message = "Location name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    private String description;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    private boolean active;
}
