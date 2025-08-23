package com.dtsvn.bookingmeeting.repository.location;

import com.dtsvn.bookingmeeting.domain.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Location entity.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByActive(boolean active);
}
