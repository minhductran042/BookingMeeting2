package com.dtsvn.bookingmeeting.service.booking.admin;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingApprovalRequest;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminSearchRequest;
import com.dtsvn.bookingmeeting.mapper.booking.BookingMapper;
import com.dtsvn.bookingmeeting.repository.booking.BookingRepository;
import com.dtsvn.bookingmeeting.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingAdminServiceImpl implements BookingAdminService {

    private final BookingRepository bookingRepository;
    private final SecurityUtils securityUtils;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse getBookingById(Long id) {
        log.info("Retrieving booking by id: {}", id);

        // Get booking by ID with proper error handling
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        return bookingMapper.toResponse(booking);
    }

    @Override
    public List<BookingResponse> getAllBookings(int page, int size, String sortBy, String sortDir) {
        log.debug("Request to get all bookings with pagination: page={}, size={}, sortBy={}, sortDir={}",
            page, size, sortBy, sortDir);

        if (page < 0) page = 0;
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "id";
        if (sortDir == null || sortDir.trim().isEmpty()) sortDir = "DESC";

        if (size <= 0) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir.toUpperCase()), sortBy);
            List<Booking> allBookings = bookingRepository.findAll(sort);
            return allBookings.stream()
                .map(bookingMapper::toResponse)
                .toList();
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        var pageResult = bookingRepository.findAll(pageable);
        return pageResult.getContent()
            .stream()
            .map(bookingMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(Long id, BookingAdminUpdateRequest request) {
        log.info("Updating booking with id: {}, request: {}", id, request);

        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));


        Booking updatedBooking = bookingMapper.updateEntity(booking, request);


        if (request.getStartTime() != null) {
            updatedBooking.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            updatedBooking.setEndTime(request.getEndTime());
        }

        // Update nullable fields only if provided (preserve existing values)
        updateNullableFields(updatedBooking, request);

        try {
            bookingRepository.save(updatedBooking);
            return bookingMapper.toResponse(updatedBooking);
        } catch (Exception e) {
            log.error("Failed to save updated booking with id: {}", id, e);
            throw new RuntimeException("Failed to update booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        log.info("Deleting booking with id: {}", id);


        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        try {
            bookingRepository.delete(booking);
            log.info("Successfully deleted booking with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete booking with id: {}", id, e);
            throw new RuntimeException("Failed to delete booking: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void approveBooking(Long id, BookingApprovalRequest request) {
        log.info("Approving booking with id: {}, request: {}", id, request);


        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        User approver = securityUtils.getCurrentAuthenticatedUser();

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in PENDING status");
        }

        booking.setStatus(BookingStatus.APPROVED);
        booking.setApprover(approver);
        booking.setApprovedAt(LocalDateTime.now());


        if (request.getAdminNotes() != null && !request.getAdminNotes().trim().isEmpty()) {
            booking.setAdminNotes(request.getAdminNotes());
        }

        bookingRepository.save(booking);
        log.info("Booking {} approved by user {} with notes: {}", id, approver.getUsername(),
            request.getAdminNotes() != null ? request.getAdminNotes() : "No notes");

        var conflicting = bookingRepository.findConflictingBookings(
            booking.getMeetingRoom(), booking.getStartTime(), booking.getEndTime());

        if (conflicting != null && !conflicting.isEmpty()) {
            LocalDateTime decisionTime = LocalDateTime.now();
            conflicting.stream()
                .filter(b -> !b.getId().equals(booking.getId()))
                .filter(b -> b.getStatus() == BookingStatus.PENDING)
                .forEach(b -> {
                    b.setStatus(BookingStatus.REJECTED);
                    b.setApprover(approver);
                    b.setApprovedAt(decisionTime);
                    String reason = "Auto-rejected due to conflict with approved booking #" + booking.getId();
                    if (b.getAdminNotes() == null || b.getAdminNotes().isBlank()) {
                        b.setAdminNotes(reason);
                    } else {
                        b.setAdminNotes(b.getAdminNotes() + " | " + reason);
                    }
                });
            bookingRepository.saveAll(conflicting);
            log.info("Auto-rejected {} conflicting pending bookings for approved booking {}", conflicting.size(), id);
        }
    }

    @Override
    @Transactional
    public void rejectBooking(Long id, BookingApprovalRequest request) {
        log.info("Rejecting booking with id: {}, request: {}", id, request);


        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        User approver = securityUtils.getCurrentAuthenticatedUser();

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in PENDING status");
        }

        booking.setStatus(BookingStatus.REJECTED);
        booking.setApprover(approver);
        booking.setApprovedAt(LocalDateTime.now());

        if (request.getAdminNotes() != null && !request.getAdminNotes().trim().isEmpty()) {
            booking.setAdminNotes(request.getAdminNotes());
        }

        bookingRepository.save(booking);
        log.info("Booking {} rejected by user {} with notes: {}", id, approver.getUsername(),
            request.getAdminNotes() != null ? request.getAdminNotes() : "No notes");
    }

    @Override
    public List<BookingResponse> getBookingsByStatus(String status, int page, int size, String sortBy, String sortDir) {
        log.debug("Getting bookings by status: {}", status);

        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "id";
        if (sortDir == null || sortDir.trim().isEmpty()) sortDir = "DESC";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            var pageResult = bookingRepository.findByStatus(bookingStatus);
            return pageResult.stream()
                .map(bookingMapper::toResponse)
                .toList();
        } catch (IllegalArgumentException e) {
            log.warn("Invalid booking status: {}", status);
            return List.of();
        }
    }

    @Override
    public List<BookingResponse> searchBookings(BookingAdminSearchRequest searchRequest, int page, int size, String sortBy, String sortDir) {
        log.debug("Searching bookings with criteria: {}", searchRequest);

        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "id";
        if (sortDir == null || sortDir.trim().isEmpty()) sortDir = "DESC";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);


        var pageResult = bookingRepository.findAll(pageable);
        return pageResult.getContent()
            .stream()
            .filter(booking -> matchesSearchCriteria(booking, searchRequest))
            .map(bookingMapper::toResponse)
            .toList();
    }

    @Override
    public long getTotalBookingsCount() {
        return bookingRepository.count();
    }

    @Override
    public long getBookingsCountByStatus(String status) {
        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            return bookingRepository.findByStatus(bookingStatus).size();
        } catch (IllegalArgumentException e) {
            log.warn("Invalid booking status: {}", status);
            return 0;
        }
    }

    @Override
    public long getBookingsCountByMeetingRoom(Long meetingRoomId) {
        return bookingRepository.findByMeetingRoomId(meetingRoomId).size();
    }

    @Override
    public long getBookingsCountByUser(Long userId) {
        return bookingRepository.findByCreatedById(userId).size();
    }



    private boolean matchesSearchCriteria(Booking booking, BookingAdminSearchRequest searchRequest) {
        if (searchRequest == null) return true;

        // Title search
        if (StringUtils.hasText(searchRequest.getTitle()) &&
            !booking.getTitle().toLowerCase().contains(searchRequest.getTitle().toLowerCase())) {
            return false;
        }

        // Username search
        if (StringUtils.hasText(searchRequest.getCreatedByUsername()) &&
            !booking.getCreatedBy().getUsername().toLowerCase().contains(searchRequest.getCreatedByUsername().toLowerCase())) {
            return false;
        }

        // Meeting room search
        if (searchRequest.getMeetingRoomId() != null &&
            !searchRequest.getMeetingRoomId().equals(booking.getMeetingRoom().getId())) {
            return false;
        }

        // Status search
        if (searchRequest.getStatus() != null &&
            !searchRequest.getStatus().equals(booking.getStatus())) {
            return false;
        }

        // Date range search
        if (searchRequest.getStartTimeFrom() != null &&
            booking.getStartTime().isBefore(searchRequest.getStartTimeFrom())) {
            return false;
        }

        if (searchRequest.getStartTimeTo() != null &&
            booking.getStartTime().isAfter(searchRequest.getStartTimeTo())) {
            return false;
        }

        if (searchRequest.getEndTimeFrom() != null &&
            booking.getEndTime().isBefore(searchRequest.getEndTimeFrom())) {
            return false;
        }

        if (searchRequest.getEndTimeTo() != null &&
            booking.getEndTime().isAfter(searchRequest.getEndTimeTo())) {
            return false;
        }

        // Created date range search
        if (searchRequest.getCreatedAtFrom() != null &&
            booking.getCreatedAt().isBefore(searchRequest.getCreatedAtFrom())) {
            return false;
        }

        if (searchRequest.getCreatedAtTo() != null &&
            booking.getCreatedAt().isAfter(searchRequest.getCreatedAtTo())) {
            return false;
        }

        // Department search
        if (StringUtils.hasText(searchRequest.getDepartment()) &&
            !searchRequest.getDepartment().equalsIgnoreCase(booking.getCreatedBy().getDepartment())) {
            return false;
        }

        return true;
    }

    private void updateNullableFields(Booking booking, BookingAdminUpdateRequest request) {
        if (request.getTitle() != null) {
            booking.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            booking.setDescription(request.getDescription());
        }
        if (request.getPurpose() != null) {
            booking.setPurpose(request.getPurpose());
        }
        if (request.getAdminNotes() != null) {
            booking.setAdminNotes(request.getAdminNotes());
        }
    }
}
