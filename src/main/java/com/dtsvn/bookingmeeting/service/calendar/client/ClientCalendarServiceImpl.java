package com.dtsvn.bookingmeeting.service.calendar.client;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.calendar.CalendarBookingResponse;
import com.dtsvn.bookingmeeting.dto.calendar.CalendarDayResponse;
import com.dtsvn.bookingmeeting.dto.calendar.CalendarOverviewResponse;
import com.dtsvn.bookingmeeting.dto.calendar.CalendarRequest;
import com.dtsvn.bookingmeeting.repository.booking.BookingRepository;
import com.dtsvn.bookingmeeting.repository.location.LocationRepository;
import com.dtsvn.bookingmeeting.repository.room.MeetingRoomRepository;
import com.dtsvn.bookingmeeting.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientCalendarServiceImpl implements ClientCalendarService{

    private final BookingRepository bookingRepository;
    private final LocationRepository locationRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public CalendarOverviewResponse getMyCalendar(CalendarRequest request) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        log.info("Fetching calendar for user: {}", currentUser.getUsername());
        try {
            LocalDateTime startDateTime = request.getStartDate().atStartOfDay(); // Bắt đầu ngày
            LocalDateTime endDateTime = request.getEndDate().atTime(23,59,59); // Kết thúc ngày

            List<Booking> myBookings = getMyBookingInDateRange(currentUser, startDateTime, endDateTime);

            List<CalendarDayResponse> days = createPersonalCalendarDays(request.getStartDate(), request.getEndDate(), myBookings);

            int totalBookings = myBookings.size();
            int availableRooms = 0; // Với lịch cá nhân không cần
            int totalLocation = (int) locationRepository.count();

            return CalendarOverviewResponse.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalBookings(totalBookings)
                .totalRooms(availableRooms)
                .totalLocations(totalLocation)
                .days(days)
                .build();
        } catch (Exception e) {
            log.error("Error parsing dates from request: {}", request, e);
            throw new IllegalArgumentException("Invalid date format in request");
        }
    }

    private List<Booking> getMyBookingInDateRange(User user, LocalDateTime start, LocalDateTime end) {
        Set<Booking> myBookings = new HashSet<>();
        List<Booking> createdBookings = bookingRepository.findByCreatedByOrderByCreatedAtDesc(user)
            .stream()
            .filter(booking -> isBookingInDateRange(booking, start, end))
            .collect(Collectors.toList());


        List<Booking> participatedBookings = bookingRepository.findByParticipantsUserOrderByCreatedAtDesc(user)
            .stream()
            .filter(booking -> isBookingInDateRange(booking,start,end))
            .collect(Collectors.toList());

        myBookings.addAll(createdBookings);
        myBookings.addAll(participatedBookings);

        return new ArrayList<>(myBookings);

    }

    private boolean isBookingInDateRange(Booking booking, LocalDateTime start, LocalDateTime end) {
        return booking.getStartTime().isAfter(start) && booking.getEndTime().isBefore(end);
    }

    private List<CalendarDayResponse> createPersonalCalendarDays(LocalDate startDate, LocalDate endDate, List<Booking> myBookings) {

        List<CalendarDayResponse> days = new ArrayList<>();
        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            List<Booking> bookingsForDay = myBookings.stream().filter(booking -> booking.getStartTime().toLocalDate().isEqual(currentDate))
                .collect(Collectors.toList()); // lấy ra các booking trong ngày hiện tại

            CalendarDayResponse day = createPersonalCalendarDay(date, bookingsForDay);
            days.add(day);
        }
        return days;
    }

    private CalendarDayResponse createPersonalCalendarDay(LocalDate date, List<Booking> bookingsForDay) {
        int totalBookings = bookingsForDay.size();

        int availableRooms = 0; // Với lịch cá nhân không cần

        String status = determinPersonalStatus(totalBookings);
        List<CalendarBookingResponse> calendarBookings = bookingsForDay.stream()
            .map(this::mapToPersonCalendarBookingResponse)
            .collect(Collectors.toList());

        return CalendarDayResponse.builder()
            .date(date)
            .totalBookings(totalBookings)
            .availableRooms(availableRooms)
            .status(status)
            .bookings(calendarBookings)
            .build();
    }

    private CalendarBookingResponse mapToPersonCalendarBookingResponse(Booking booking) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        boolean isCreator = booking.getCreatedBy().getId().equals(currentUser.getId());
        // Thêm role vào title để phân biệt
        String displayTitle = String.format("[%s] %s", currentUser.getRole(), booking.getTitle());

        String color = getPersonalStatusColor(booking.getStatus().name(), isCreator);

        return CalendarBookingResponse.builder()
            .id(booking.getId())
            .title(displayTitle)
            .startTime(booking.getStartTime())
            .endTime(booking.getEndTime())
            .roomName(booking.getMeetingRoom().getName())
            .locationName(booking.getMeetingRoom().getLocation().getName())
            .createdBy(booking.getCreatedBy().getFullName())
            .status(booking.getStatus().name())
            .color(color)
            .build();
    }



    private String determinPersonalStatus(int totalBookings) {
        if(totalBookings == 0) {
            return "FREE";
        } else if (totalBookings <= 2) {
            return "LIGHT";
        } else if (totalBookings <= 5) {
            return "BUSY";
        } else {
            return "VERY_BUSY";
        }
    }

    private String getPersonalStatusColor(String status, boolean isCreator) {
        // Màu khác nhau cho Creator vs Participant
        String baseColor = switch (status) {
            case "APPROVED" -> "#28a745"; // Green
            case "PENDING" -> "#ffc107"; // Yellow
            case "CANCELLED" -> "#dc3545"; // Red
            case "REJECTED" -> "#6c757d"; // Gray
            default -> "#007bff"; // Blue
        };

        // Nếu là participant, làm màu nhạt hơn
        if (!isCreator) {
            return baseColor + "80"; // Thêm transparency
        }

        return baseColor;
    }

}
