package com.dtsvn.bookingmeeting.service.schedule;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.room.MeetingRoom;
import com.dtsvn.bookingmeeting.dto.schedule.*;
import com.dtsvn.bookingmeeting.repository.booking.BookingRepository;
import com.dtsvn.bookingmeeting.repository.room.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminScheduleServiceImpl implements AdminScheduleService {

    private final BookingRepository bookingRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    @Override
    public DailyScheduleResponse getDailySchedule(LocalDate date, Long locationId) {
        log.info("Getting daily schedule for date: {}, locationId: {}", date, locationId);

        try {
            // 1. Lấy tất cả bookings trong ngày
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            List<Booking> dailyBookings = getBookingsInDateRange(startOfDay, endOfDay, locationId);

            // 2. Tạo time slots (8:00 - 18:00)
            List<DailyTimeSlotResponse> timeSlots = createDailyTimeSlots(date, dailyBookings, locationId);

            // 3. Tính toán thống kê đơn giản
            int totalBookings = dailyBookings.size();
            int totalRooms = getTotalRoomsInLocation(locationId);

            // 4. Kiểm tra xung đột lịch
            List<DailyConflictResponse> conflicts = findDailyConflicts(dailyBookings);

            return DailyScheduleResponse.builder()
                .date(date)
                .dayOfWeek(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()))
                .timeSlots(timeSlots)
                .totalBookings(totalBookings)
                .totalRooms(totalRooms)
                .conflicts(conflicts)
                .build();

        } catch (Exception e) {
            log.error("Error getting daily schedule for date: {}", date, e);
            throw new RuntimeException("Error getting daily schedule: " + e.getMessage());
        }
    }

    /**
     * Lấy bookings trong khoảng thời gian
     */
    private List<Booking> getBookingsInDateRange(LocalDateTime startTime, LocalDateTime endTime, Long locationId) {
        if (locationId != null) {
            // Lấy bookings theo location
            return bookingRepository.findByLocationAndDateRange(locationId, startTime, endTime);
        } else {
            // Lấy tất cả bookings
            return bookingRepository.findByStartTimeBetween(startTime, endTime);
        }
    }


    private List<DailyTimeSlotResponse> createDailyTimeSlots(LocalDate date, List<Booking> bookings, Long locationId) {
        List<DailyTimeSlotResponse> timeSlots = new ArrayList<>();

        // Tạo time slots từ 8:00 đến 18:00, mỗi slot 1 giờ
        for (int hour = 8; hour < 18; hour++) {
            LocalTime startTime = LocalTime.of(hour, 0); // Bắt đầu từ giờ
            LocalTime endTime = LocalTime.of(hour + 1, 0); // Kết thúc sau 1 giờ

            // Tìm bookings trong slot này
            List<Booking> slotBookings = findBookingsInTimeSlot(date, startTime, endTime, bookings); // Lấy bookings trong slot

            // Tính toán số phòng available và status
            int totalRooms = getTotalRoomsInLocation(locationId);
            int occupiedRooms = slotBookings.size();
            int availableRooms = Math.max(0, totalRooms - occupiedRooms);

            // Xác định status của time slot
            String status = determineTimeSlotStatus(occupiedRooms, totalRooms);

            timeSlots.add(DailyTimeSlotResponse.builder()
                .startTime(startTime)
                .endTime(endTime)
                .bookings(slotBookings.stream()
                    .map(this::mapToDailyBookingResponse)
                    .collect(Collectors.toList()))
                .availableRooms(availableRooms)
                .status(status)
                .build());
        }

        return timeSlots;
    }

    /**
     * Xác định status của time slot
     */
    private String determineTimeSlotStatus(int occupiedRooms, int totalRooms) {
        if (occupiedRooms == 0) {
            return "AVAILABLE";
        } else if (occupiedRooms < totalRooms) {
            return "PARTIALLY_BOOKED";
        } else {
            return "FULLY_BOOKED";
        }
    }

    /**
     * Tìm bookings trong time slot cụ thể
     */
    private List<Booking> findBookingsInTimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime, List<Booking> allBookings) {
        LocalDateTime slotStart = date.atTime(startTime);
        LocalDateTime slotEnd = date.atTime(endTime);

        return allBookings.stream()
            .filter(booking -> {
                // Kiểm tra xem booking có overlap với time slot không: nghĩa là booking bắt đầu trước khi slot kết thúc và kết thúc sau khi slot bắt đầu
                return (booking.getStartTime().isBefore(slotEnd) &&
                        booking.getEndTime().isAfter(slotStart)); // Có overlap
            })
            .collect(Collectors.toList());
    }

    /**
     * Lấy tổng số phòng trong location
     */
    private int getTotalRoomsInLocation(Long locationId) {
        if (locationId != null) {
            return meetingRoomRepository.countByLocationId(locationId);
        } else {
            return (int) meetingRoomRepository.count();
        }
    }

    private DailyBookingResponse mapToDailyBookingResponse(Booking booking) {
        String color = getStatusColor(booking.getStatus().name());

        return DailyBookingResponse.builder()
            .id(booking.getId())
            .title(booking.getTitle())
            .startTime(booking.getStartTime())
            .endTime(booking.getEndTime())
            .status(booking.getStatus().name())
            .roomName(booking.getMeetingRoom().getName())
            .createdBy(booking.getCreatedBy().getUsername())
            .color(color)
            .build();
    }

    /**
     * Lấy màu theo status
     */
    private String getStatusColor(String status) {
        return switch (status) {
            case "APPROVED" -> "#28a745"; // Green
            case "PENDING" -> "#ffc107";  // Yellow
            case "REJECTED" -> "#dc3545"; // Red
            case "CANCELLED" -> "#6c757d"; // Gray
            default -> "#007bff"; // Blue
        };
    }

    /**
     * Tìm xung đột lịch (theo thời gian)
     */
    private List<DailyConflictResponse> findDailyConflicts(List<Booking> bookings) {
        List<DailyConflictResponse> conflicts = new ArrayList<>();

        // Kiểm tra xung đột thời gian
        for (int i = 0; i < bookings.size(); i++) {
            for (int j = i + 1; j < bookings.size(); j++) {
                Booking booking1 = bookings.get(i);
                Booking booking2 = bookings.get(j);

                if (hasTimeConflict(booking1, booking2)) {
                    conflicts.add(createConflictResponse(booking1, booking2));
                }
            }
        }

        return conflicts;
    }

    /**
     * Kiểm tra xung đột thời gian
     */
    private boolean hasTimeConflict(Booking booking1, Booking booking2) {
        return (booking1.getStartTime().isBefore(booking2.getEndTime()) &&
                booking1.getEndTime().isAfter(booking2.getStartTime()));
    }

    /**
     * Tạo conflict response
     */
    private DailyConflictResponse createConflictResponse(Booking booking1, Booking booking2) {
        String conflictType = "OVERLAP";
        String description = String.format("Xung đột thời gian giữa cuộc họp '%s' và '%s'",
            booking1.getTitle(), booking2.getTitle());

        return DailyConflictResponse.builder()
            .conflictType(conflictType)
            .description(description)
            .conflictTime(booking1.getStartTime())
            .conflictingBookingIds(Arrays.asList(booking1.getId(), booking2.getId()))
            .severity("MEDIUM")
            .build();
    }
}
