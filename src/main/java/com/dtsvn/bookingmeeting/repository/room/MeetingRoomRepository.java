package com.dtsvn.bookingmeeting.repository.room;

import com.dtsvn.bookingmeeting.domain.room.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the MeetingRoom entity.
 */
@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

    List<MeetingRoom> findByActive(boolean active);

    List<MeetingRoom> findByLocationId(Long locationId);

    List<MeetingRoom> findByActiveAndLocationId(boolean active, Long locationId);

    @Query("SELECT mr FROM MeetingRoom mr WHERE mr.active = true AND mr.id NOT IN " +
           "(SELECT DISTINCT b.meetingRoom.id FROM Booking b WHERE b.status = 'CONFIRMED' " +
           "AND ((b.startTime <= :startTime AND b.endTime > :startTime) " +
           "OR (b.startTime < :endTime AND b.endTime >= :endTime) " +
           "OR (b.startTime >= :startTime AND b.endTime <= :endTime)))")
    List<MeetingRoom> findAvailableRooms(@Param("startTime") LocalDateTime startTime, 
                                        @Param("endTime") LocalDateTime endTime);

    @Query("SELECT mr FROM MeetingRoom mr WHERE mr.active = true AND mr.location.id = :locationId " +
           "AND mr.id NOT IN " +
           "(SELECT DISTINCT b.meetingRoom.id FROM Booking b WHERE b.status = 'CONFIRMED' " +
           "AND ((b.startTime <= :startTime AND b.endTime > :startTime) " +
           "OR (b.startTime < :endTime AND b.endTime >= :endTime) " +
           "OR (b.startTime >= :startTime AND b.endTime <= :endTime)))")
    List<MeetingRoom> findAvailableRoomsByLocation(@Param("startTime") LocalDateTime startTime, 
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("locationId") Long locationId);

    /**
     * Đếm số phòng theo location
     */
    @Query("SELECT COUNT(mr) FROM MeetingRoom mr WHERE mr.location.id = :locationId")
    int countByLocationId(@Param("locationId") Long locationId);
}
