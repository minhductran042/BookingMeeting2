package com.dtsvn.bookingmeeting.repository.booking;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN b.participants bp " +
           "WHERE bp.user = :user " +
           "ORDER BY b.createdAt DESC")
    List<Booking> findByParticipantsUserOrderByCreatedAtDesc(@Param("user") com.dtsvn.bookingmeeting.domain.user.User user);

    /**
     * Tìm bookings xung đột thời gian với meeting room
     */
    @Query("SELECT b FROM Booking b " +
           "WHERE b.meetingRoom = :meetingRoom " +
           "AND b.status != 'CANCELLED' " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime) " + // day la cau lenh kiem tra xem co giao nhau ko giua 2 khoang thoi gian
           "OR (b.startTime = :startTime) " +
           "OR (b.endTime = :endTime))") // check xem có trùng startTime hoặc endTime không
    List<Booking> findConflictingBookings(
            @Param("meetingRoom") com.dtsvn.bookingmeeting.domain.room.MeetingRoom meetingRoom,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);

    List<Booking> findByStatusAndCreatedByOrderByCreatedAtDesc(BookingStatus status, com.dtsvn.bookingmeeting.domain.user.User user);

    @Query("SELECT DISTINCT b FROM Booking b " +
           "JOIN b.participants bp " +
           "WHERE b.status = :status AND bp.user = :user " +
           "ORDER BY b.createdAt DESC")
    List<Booking> findByStatusAndParticipantsUserOrderByCreatedAtDesc(
            @Param("status") BookingStatus status,
            @Param("user") com.dtsvn.bookingmeeting.domain.user.User user);

    /**
     * Tìm bookings theo khoảng thời gian
     */
    @Query("SELECT b FROM Booking b WHERE b.startTime BETWEEN :startTime AND :endTime")
    List<Booking> findByStartTimeBetween(@Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    /**
     * Tìm bookings theo location và khoảng thời gian
     */
    @Query("SELECT b FROM Booking b JOIN b.meetingRoom r JOIN r.location l " +
           "WHERE l.id = :locationId AND b.startTime BETWEEN :startTime AND :endTime")
    List<Booking> findByLocationAndDateRange(@Param("locationId") Long locationId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
}
