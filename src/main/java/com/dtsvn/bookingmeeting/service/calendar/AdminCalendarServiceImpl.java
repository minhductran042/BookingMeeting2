package com.dtsvn.bookingmeeting.service.calendar;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.dto.calendar.*;
import com.dtsvn.bookingmeeting.repository.booking.BookingRepository;
import com.dtsvn.bookingmeeting.repository.location.LocationRepository;
import com.dtsvn.bookingmeeting.repository.room.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation cho admin calendar management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCalendarServiceImpl implements AdminCalendarService {

    private final BookingRepository bookingRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final LocationRepository locationRepository;

    @Override
    public CalendarOverviewResponse getCalendarOverview(CalendarRequest request) {
        log.info("Getting calendar overview with request: {}", request);

        try {
            // 1. Lấy tất cả bookings trong khoảng thời gian
            LocalDateTime startDateTime = request.getStartDate().atStartOfDay();
            LocalDateTime endDateTime = request.getEndDate().atTime(23, 59, 59);

            List<Booking> bookings = getBookingsInDateRange(startDateTime, endDateTime, request.getLocationId());

            // 2. Tạo danh sách các ngày
            List<CalendarDayResponse> days = createCalendarDays(
                request.getStartDate(), 
                request.getEndDate(), 
                bookings, 
                request.getLocationId()
            );

            // 3. Tính toán thống kê
            int totalBookings = bookings.size();
            int totalRooms = getTotalRoomsInLocation(request.getLocationId());
            int totalLocations = request.getLocationId() != null ? 1 : getTotalLocations();

            return CalendarOverviewResponse.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalBookings(totalBookings)
                .totalRooms(totalRooms)
                .totalLocations(totalLocations)
                .days(days)
                .build();

        } catch (Exception e) {
            log.error("Error getting calendar overview with request: {}", request, e);
            throw new RuntimeException("Failed to get calendar overview", e);
        }
    }

    @Override
    public CalendarOverviewResponse getRoomCalendar(Long roomId, RoomCalendarRequest request) {
        log.info("Getting room calendar for roomId: {}, with request: {}", roomId, request);

        try {
            // 1. Lấy tất cả bookings của phòng trong khoảng thời gian
            LocalDateTime startDateTime = request.getStartDate().atStartOfDay();
            LocalDateTime endDateTime = request.getEndDate().atTime(23, 59, 59);

            List<Booking> roomBookings = getBookingsByRoom(roomId, startDateTime, endDateTime);

            // 2. Tạo danh sách các ngày cho phòng cụ thể
            List<CalendarDayResponse> days = createRoomCalendarDays(
                request.getStartDate(), 
                request.getEndDate(), 
                roomBookings, 
                roomId
            );

            // 3. Tính toán thống kê cho phòng
            int totalBookings = roomBookings.size();
            int totalRooms = 1; // Chỉ 1 phòng
            int totalLocations = 1; // Chỉ 1 location của phòng

            return CalendarOverviewResponse.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalBookings(totalBookings)
                .totalRooms(totalRooms)
                .totalLocations(totalLocations)
                .days(days)
                .build();

        } catch (Exception e) {
            log.error("Error getting room calendar for roomId: {}", roomId, e);
            throw new RuntimeException("Failed to get room calendar", e);
        }
    }

    /**
     * Lấy bookings trong khoảng thời gian
     */
    private List<Booking> getBookingsInDateRange(LocalDateTime startTime, LocalDateTime endTime, Long locationId) {
        if (locationId != null) {
            return bookingRepository.findByLocationAndDateRange(locationId, startTime, endTime);
        } else {
            return bookingRepository.findByStartTimeBetween(startTime, endTime);
        }
    }

    /**
     * Lấy bookings theo phòng cụ thể
     */
    private List<Booking> getBookingsByRoom(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingRepository.findByStartTimeBetween(startTime, endTime)
            .stream()
            .filter(booking -> booking.getMeetingRoom().getId().equals(roomId))
            .collect(Collectors.toList());
    }

    /**
     * Tạo danh sách các ngày cho calendar
     */
    private List<CalendarDayResponse> createCalendarDays(LocalDate startDate, LocalDate endDate,
                                                        List<Booking> allBookings, Long locationId) {
        List<CalendarDayResponse> days = new ArrayList<>();

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            // Lấy bookings của ngày hiện tại
            final LocalDate date = currentDate;
            List<Booking> dayBookings = allBookings.stream()
                .filter(booking -> booking.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());

            // Tạo CalendarDayResponse
            CalendarDayResponse day = createCalendarDay(date, dayBookings, locationId);
            days.add(day);
        }

        return days;
    }

    /**
     * Tạo danh sách các ngày cho phòng cụ thể
     */
    private List<CalendarDayResponse> createRoomCalendarDays(LocalDate startDate, LocalDate endDate,
                                                            List<Booking> roomBookings, Long roomId) {
        List<CalendarDayResponse> days = new ArrayList<>();

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            // Lấy bookings của ngày hiện tại
            final LocalDate date = currentDate;
            List<Booking> dayBookings = roomBookings.stream()
                .filter(booking -> booking.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());

            // Tạo CalendarDayResponse cho phòng
            CalendarDayResponse day = createRoomCalendarDay(date, dayBookings, roomId);
            days.add(day);
        }

        return days;
    }

    /**
     * Tạo một ngày cho calendar
     */
    private CalendarDayResponse createCalendarDay(LocalDate date, List<Booking> dayBookings, Long locationId) {
        int totalBookings = dayBookings.size();
        int totalRooms = getTotalRoomsInLocation(locationId);

        int availableRooms = calculateAvailableRooms(dayBookings, totalRooms);

        // Xác định status của ngày
        String status = determineDayStatus(availableRooms, totalRooms);

        // Map bookings
        List<CalendarBookingResponse> calendarBookings = dayBookings.stream()
            .map(this::mapToCalendarBookingResponse)
            .collect(Collectors.toList());

        return CalendarDayResponse.builder()
            .date(date)
            .dayOfWeek(date.getDayOfWeek())
            .totalBookings(totalBookings)
            .availableRooms(availableRooms)
            .bookings(calendarBookings)
            .status(status)
            .build();
    }

    /**
     * Tạo một ngày cho phòng cụ thể
     */
    private CalendarDayResponse createRoomCalendarDay(LocalDate date, List<Booking> dayBookings, Long roomId) {
        int totalBookings = dayBookings.size();
        int totalRooms = 1; // Chỉ 1 phòng

        int availableRooms = calculateAvailableRooms(dayBookings, totalRooms);

        // Xác định status của ngày
        String status = determineDayStatus(availableRooms, totalRooms);

        // Map bookings
        List<CalendarBookingResponse> calendarBookings = dayBookings.stream()
            .map(this::mapToCalendarBookingResponse)
            .collect(Collectors.toList());

        return CalendarDayResponse.builder()
            .date(date)
            .dayOfWeek(date.getDayOfWeek())
            .totalBookings(totalBookings)
            .availableRooms(availableRooms)
            .bookings(calendarBookings)
            .status(status)
            .build();
    }

    /**
     * Tính số phòng available dựa trên thời gian thực tế
     */
    private int calculateAvailableRooms(List<Booking> dayBookings, int totalRooms) {
        if (dayBookings.isEmpty()) {
            return totalRooms; // Không có booking nào = tất cả phòng available
        }

        // Tạo time slots từ 8:00 đến 18:00
        Set<Integer> occupiedTimeSlots = new HashSet<>();

        for (Booking booking : dayBookings) {
            LocalDateTime start = booking.getStartTime();
            LocalDateTime end = booking.getEndTime();

            // Chuyển về giờ trong ngày
            int startHour = start.getHour();
            int endHour = end.getHour();

            // Đánh dấu các giờ bị chiếm
            for (int hour = startHour; hour < endHour; hour++) {
                if (hour >= 8 && hour < 18) { // Chỉ tính trong giờ làm việc
                    occupiedTimeSlots.add(hour);
                }
            }
        }

        // Số giờ có booking / tổng số giờ làm việc
        int totalWorkingHours = 10; // 8:00 - 18:00 = 10 giờ
        int occupiedHours = occupiedTimeSlots.size();

        // Tính tỷ lệ sử dụng
        double utilizationRate = (double) occupiedHours / totalWorkingHours;

        // Số phòng available = tổng phòng * (1 - tỷ lệ sử dụng)
        return Math.max(0, (int) Math.round(totalRooms * (1 - utilizationRate)));
    }

    /**
     * Map Booking sang CalendarBookingResponse
     */
    private CalendarBookingResponse mapToCalendarBookingResponse(Booking booking) {
        String color = getStatusColor(booking.getStatus().name());

        return CalendarBookingResponse.builder()
            .id(booking.getId())
            .title(booking.getTitle())
            .startTime(booking.getStartTime())
            .endTime(booking.getEndTime())
            .status(booking.getStatus().name())
            .roomName(booking.getMeetingRoom().getName())
            .locationName(booking.getMeetingRoom().getLocation().getName())
            .createdBy(booking.getCreatedBy().getFullName())
            .color(color)
            .build();
    }

    /**
     * Xác định status dựa trên số phòng available
     */
    private String determineDayStatus(int availableRooms, int totalRooms) {
        if (availableRooms == totalRooms) {
            return "AVAILABLE";
        } else if (availableRooms > 0) {
            return "PARTIALLY_BUSY";
        } else {
            return "FULL";
        }
    }

    /**
     * Lấy màu theo status
     */
    private String getStatusColor(String status) {
        return switch (status) {
            case "APPROVED" -> "#28a745"; // Green
            case "PENDING" -> "#ffc107"; // Yellow
            case "CANCELLED" -> "#dc3545"; // Red
            case "REJECTED" -> "#6c757d"; // Gray
            default -> "#007bff"; // Blue
        };
    }

    /**
     * Lấy tổng số phòng trong location
     */
    private int getTotalRoomsInLocation(Long locationId) {
        if (locationId != null) {
            return meetingRoomRepository.countByLocationId(locationId);
        }
        return (int) meetingRoomRepository.count();
    }

    /**
     * Lấy tổng số locations
     */
    private int getTotalLocations() {
        return (int) locationRepository.count();
    }
}
