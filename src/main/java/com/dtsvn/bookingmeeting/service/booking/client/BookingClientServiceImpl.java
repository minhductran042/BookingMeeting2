package com.dtsvn.bookingmeeting.service.booking.client;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.booking.BookingParticipant;
import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import com.dtsvn.bookingmeeting.domain.room.MeetingRoom;
import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.dto.booking.client.BookingCreateRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.BookingUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.AddParticipantRequest;
import com.dtsvn.bookingmeeting.dto.booking.client.RemoveParticipantRequest;
import com.dtsvn.bookingmeeting.mapper.booking.BookingMapper;
import com.dtsvn.bookingmeeting.repository.booking.BookingRepository;
import com.dtsvn.bookingmeeting.repository.room.MeetingRoomRepository;
import com.dtsvn.bookingmeeting.repository.user.UserRepository;
import com.dtsvn.bookingmeeting.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingClientServiceImpl implements BookingClientService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final SecurityUtils securityUtils;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingResponse> getMyBookings() {
        log.info("Fetching bookings created by the current user");
        try {
            User currentUser = securityUtils.getCurrentAuthenticatedUser();
            List<Booking> bookings = bookingRepository.findByCreatedByOrderByCreatedAtDesc(currentUser);
            return bookingMapper.toResponseList(bookings);
        } catch (Exception e) {
            log.error("Error fetching bookings: {}", e.getMessage());
            throw new RuntimeException("Error fetching bookings", e);
        }
    }

    @Override
    public List<BookingResponse> getParticipatedBookings() {
        log.info("Fetching bookings where the current user is a participant");
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByParticipantsUserOrderByCreatedAtDesc(currentUser);
        return bookingMapper.toResponseList(bookings);
    }

    @Override
    public List<BookingResponse> getMyApprovedBookings() {
        log.info("Fetching approved bookings created by the current user");
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByStatusAndCreatedByOrderByCreatedAtDesc(
                com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus.APPROVED, currentUser);
        return bookingMapper.toResponseList(bookings);
    }

    @Override
    public List<BookingResponse> getParticipatedApprovedBookings() {
        log.info("Fetching approved bookings where the current user is a participant");
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByStatusAndParticipantsUserOrderByCreatedAtDesc(
                com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus.APPROVED, currentUser);
        return bookingMapper.toResponseList(bookings);
    }

    @Override
    public List<BookingResponse> getMyPendingBookings() {
        log.info("Fetching pending bookings created by the current user");
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByStatusAndCreatedByOrderByCreatedAtDesc(
                com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus.PENDING, currentUser);
        return bookingMapper.toResponseList(bookings);
    }

    @Override
    public List<BookingResponse> getParticipatedPendingBookings() {
        log.info("Fetching pending bookings where the current user is a participant");
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByStatusAndParticipantsUserOrderByCreatedAtDesc(
                com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus.PENDING, currentUser);
        return bookingMapper.toResponseList(bookings);
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        Booking booking = getBookingByIdOrThrow(id);

        // Kiểm tra quyền truy cập (người tạo hoặc người tham gia)
        if (!booking.getCreatedBy().equals(currentUser) &&
            !booking.getParticipants().stream()
                    .map(BookingParticipant::getUser)
                    .anyMatch(user -> user.equals(currentUser))) {
            throw new RuntimeException("Access denied");
        }

        return bookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse createBooking(BookingCreateRequest request) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        MeetingRoom meetingRoom = getMeetingRoomByIdOrThrow(request.getMeetingRoomId());

        // Validate thời gian
        validateBookingTime(request.getStartTime(), request.getEndTime(), meetingRoom);
        
        LocalDateTime startUtc = request.getStartTime();
        LocalDateTime endUtc = request.getEndTime();

        // Tạo booking
        Booking booking = Booking.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .purpose(request.getPurpose())
                .startTime(startUtc)
                .endTime(endUtc)
                .status(BookingStatus.PENDING)
                .meetingRoom(meetingRoom)
                .createdBy(currentUser)
                .build();

        // Thêm người tạo vào danh sách tham gia
        addParticipantToBooking(booking, currentUser);

        // Thêm người tham gia khác
        if (request.getParticipantIds() != null) {
            for (Long participantId : request.getParticipantIds()) {
                User participant = userRepository.findById(participantId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                addParticipantToBooking(booking, participant);
            }
        }

        // Lưu booking một lần duy nhất với tất cả participants
        Booking savedBooking = bookingRepository.save(booking);
        
        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingUpdateRequest request) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Kiểm tra quyền sửa
        if (!booking.getCreatedBy().equals(currentUser)) {
            throw new RuntimeException("You can only edit your own bookings");
        }

        // Chỉ cho phép sửa khi pending
        if (booking.getStatus() != com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus.PENDING) {
            throw new RuntimeException("Cannot edit approved/rejected bookings");
        }

        // Cập nhật thông tin
        booking.setTitle(request.getTitle());
        booking.setDescription(request.getDescription());
        booking.setPurpose(request.getPurpose());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());

        // Cập nhật người tham gia
        updateParticipants(booking, request.getParticipantIds());

        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponse(updatedBooking);
    }



    @Override
    public void addParticipant(Long bookingId, AddParticipantRequest request) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Kiểm tra quyền thêm người tham gia
        if (!booking.getCreatedBy().equals(currentUser)) {
            throw new RuntimeException("Only booking creator can add participants");
        }

        User participant = userRepository.findById(request.getParticipantId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        addParticipantToBooking(booking, participant);
        bookingRepository.save(booking);
    }

    @Override
    public void removeParticipant(Long bookingId, RemoveParticipantRequest request) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Kiểm tra quyền xóa người tham gia
        if (!booking.getCreatedBy().equals(currentUser)) {
            throw new RuntimeException("Only booking creator can remove participants");
        }

        User participant = userRepository.findById(request.getParticipantId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        removeParticipantFromBooking(booking, participant);
        bookingRepository.save(booking);
    }

    @Override
    public void cancelledBooking(Long id) {
        User currentUser = securityUtils.getCurrentAuthenticatedUser();
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getCreatedBy().equals(currentUser)) {
            throw new RuntimeException("You can only cancel your own bookings");
        }

        // Chỉ cho phép hủy khi pending hoặc approved
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.APPROVED) {
            throw new RuntimeException("Cannot cancel this booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancelledBy(currentUser);

        bookingRepository.save(booking);
    }

    private void validateBookingTime(LocalDateTime startTime, LocalDateTime endTime, MeetingRoom meetingRoom) {
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("Start time must be before end time");
        }

        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                meetingRoom, startTime, endTime);

        boolean hasApprovedConflict = conflictingBookings.stream()
                .anyMatch(b -> b.getStatus() == BookingStatus.APPROVED);
        if (hasApprovedConflict) {
            throw new RuntimeException("Time slot conflicts with an approved booking");
        }

        boolean hasPendingConflict = conflictingBookings.stream()
                .anyMatch(b -> b.getStatus() == BookingStatus.PENDING);
        if (hasPendingConflict) {
            log.warn("Creating booking in a time slot that conflicts with pending bookings for room {} between {} and {}",
                    meetingRoom.getId(), startTime, endTime);
        }
    }

    private void addParticipantToBooking(Booking booking, User user) {
        if (booking.getParticipants() == null) {
            booking.setParticipants(new HashSet<>());
        }
        
        // Kiểm tra xem user đã tham gia chưa
        boolean alreadyParticipant = booking.getParticipants().stream()
                .anyMatch(participant -> participant.getUser().equals(user));

        if (!alreadyParticipant) {
            BookingParticipant participant = BookingParticipant.builder()
                    .booking(booking)
                    .user(user)
                    .required(true)
                    .build();
            booking.getParticipants().add(participant);
        }
    }

    private void removeParticipantFromBooking(Booking booking, User user) {
        // Không cho phép xóa người tạo
        if (booking.getCreatedBy().equals(user)) {
            throw new RuntimeException("Cannot remove booking creator");
        }

        // Đảm bảo participants không null
        if (booking.getParticipants() != null) {
            booking.getParticipants().removeIf(participant -> participant.getUser().equals(user));
        }
    }

    private void updateParticipants(Booking booking, List<Long> participantIds) {
        // Đảm bảo participants không null
        if (booking.getParticipants() == null) {
            booking.setParticipants(new HashSet<>());
        }
        
        // Xóa tất cả người tham gia hiện tại (trừ người tạo)
        booking.getParticipants().removeIf(participant ->
                !participant.getUser().equals(booking.getCreatedBy()));

        // Thêm người tham gia mới
        if (participantIds != null) {
            for (Long participantId : participantIds) {
                User participant = userRepository.findById(participantId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                addParticipantToBooking(booking, participant);
            }
        }
    }

    private Booking getBookingByIdOrThrow(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    private MeetingRoom getMeetingRoomByIdOrThrow(Long id) {
        return meetingRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting room not found"));
    }
}
