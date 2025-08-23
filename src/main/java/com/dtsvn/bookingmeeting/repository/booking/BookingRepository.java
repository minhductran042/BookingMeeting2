package com.dtsvn.bookingmeeting.repository.booking;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Booking entity.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCreatedById(Long userId);

    List<Booking> findByMeetingRoomId(Long meetingRoomId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByStartTimeBetweenOrEndTimeBetween(LocalDateTime startTime1, LocalDateTime endTime1, LocalDateTime startTime2, LocalDateTime endTime2);
}
