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

    /**
     * Tìm bookings theo user tạo
     */
    List<Booking> findByCreatedByOrderByCreatedAtDesc(com.dtsvn.bookingmeeting.domain.user.User user);

    /**
     * Tìm bookings mà user tham gia
     */
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN b.participants bp " +
           "WHERE bp.user = :user " +
           "ORDER BY b.createdAt DESC")
    List<Booking> findByParticipantsUserOrderByCreatedAtDesc(@org.springframework.data.repository.query.Param("user") com.dtsvn.bookingmeeting.domain.user.User user);

    /**
     * Tìm bookings xung đột thời gian với meeting room
     */
    @org.springframework.data.jpa.repository.Query("SELECT b FROM Booking b " +
           "WHERE b.meetingRoom = :meetingRoom " +
           "AND b.status != 'CANCELLED' " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime) " +
           "OR (b.startTime = :startTime) " +
           "OR (b.endTime = :endTime))")
    List<Booking> findConflictingBookings(
            @org.springframework.data.repository.query.Param("meetingRoom") com.dtsvn.bookingmeeting.domain.room.MeetingRoom meetingRoom,
            @org.springframework.data.repository.query.Param("startTime") LocalDateTime startTime,
            @org.springframework.data.repository.query.Param("endTime") LocalDateTime endTime);
}
