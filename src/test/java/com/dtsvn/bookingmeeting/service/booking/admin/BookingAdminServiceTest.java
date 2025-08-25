package com.dtsvn.bookingmeeting.service.booking.admin;

import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import com.dtsvn.bookingmeeting.domain.room.MeetingRoom;
import com.dtsvn.bookingmeeting.domain.user.User;
import com.dtsvn.bookingmeeting.dto.booking.BookingResponse;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminSearchRequest;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingAdminUpdateRequest;
import com.dtsvn.bookingmeeting.dto.booking.admin.BookingApprovalRequest;
import com.dtsvn.bookingmeeting.mapper.booking.BookingMapper;
import com.dtsvn.bookingmeeting.repository.booking.BookingRepository;
import com.dtsvn.bookingmeeting.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingAdminServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingAdminServiceImpl bookingAdminService;

    private User testUser;
    private User testApprover;
    private MeetingRoom testMeetingRoom;
    private Booking testBooking;
    private BookingResponse testBookingResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .fullName("Test User")
            .department("IT")
            .build();

        testApprover = User.builder()
            .id(2L)
            .username("approver")
            .email("approver@example.com")
            .fullName("Test Approver")
            .build();

        testMeetingRoom = MeetingRoom.builder()
            .id(1L)
            .name("Test Room")
            .capacity(10)
            .build();

        testBooking = Booking.builder()
            .id(1L)
            .title("Test Booking")
            .description("Test Description")
            .startTime(LocalDateTime.now().plusHours(1))
            .endTime(LocalDateTime.now().plusHours(2))
            .status(BookingStatus.PENDING)
            .createdBy(testUser)
            .meetingRoom(testMeetingRoom)
            .createdAt(LocalDateTime.now())
            .build();

        testBookingResponse = BookingResponse.builder()
            .id(1L)
            .title("Test Booking")
            .description("Test Description")
            .build();
    }

    @Test
    void getBookingById_ShouldReturnBooking_WhenValidId() {
        // Given
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingMapper.toResponse(testBooking)).thenReturn(testBookingResponse);

        // When
        BookingResponse result = bookingAdminService.getBookingById(1L);

        // Then
        assertThat(result).isEqualTo(testBookingResponse);
        verify(bookingRepository).findById(1L);
        verify(bookingMapper).toResponse(testBooking);
    }

    @Test
    void getBookingById_ShouldThrowException_WhenInvalidId() {
        // Given
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookingAdminService.getBookingById(999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Booking not found with id: 999");
    }

    @Test
    void approveBooking_ShouldApproveBooking_WhenValidRequest() {
        // Given
        BookingApprovalRequest approvalRequest = BookingApprovalRequest.builder()
            .adminNotes("Approved for team meeting")
            .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(securityUtils.getCurrentAuthenticatedUser()).thenReturn(testApprover);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // When
        bookingAdminService.approveBooking(1L, approvalRequest);

        // Then
        assertThat(testBooking.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(testBooking.getApprover()).isEqualTo(testApprover);
        assertThat(testBooking.getApprovedAt()).isNotNull();
        assertThat(testBooking.getAdminNotes()).isEqualTo("Approved for team meeting");
        verify(bookingRepository).save(testBooking);
        verify(securityUtils).getCurrentAuthenticatedUser();
    }

    @Test
    void approveBooking_ShouldThrowException_WhenNotPendingStatus() {
        // Given
        testBooking.setStatus(BookingStatus.APPROVED);
        BookingApprovalRequest approvalRequest = BookingApprovalRequest.builder()
            .adminNotes("Approved")
            .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        // When & Then
        assertThatThrownBy(() -> bookingAdminService.approveBooking(1L, approvalRequest))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Booking is not in PENDING status");
    }

    @Test
    void rejectBooking_ShouldRejectBooking_WhenValidRequest() {
        // Given
        BookingApprovalRequest rejectionRequest = BookingApprovalRequest.builder()
            .adminNotes("Rejected due to room maintenance")
            .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(securityUtils.getCurrentAuthenticatedUser()).thenReturn(testApprover);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // When
        bookingAdminService.rejectBooking(1L, rejectionRequest);

        // Then
        assertThat(testBooking.getStatus()).isEqualTo(BookingStatus.REJECTED);
        assertThat(testBooking.getApprover()).isEqualTo(testApprover);
        assertThat(testBooking.getApprovedAt()).isNotNull();
        assertThat(testBooking.getAdminNotes()).isEqualTo("Rejected due to room maintenance");
        verify(bookingRepository).save(testBooking);
        verify(securityUtils).getCurrentAuthenticatedUser();
    }

    @Test
    void rejectBooking_ShouldThrowException_WhenNotPendingStatus() {
        // Given
        testBooking.setStatus(BookingStatus.REJECTED);
        BookingApprovalRequest rejectionRequest = BookingApprovalRequest.builder()
            .adminNotes("Rejected")
            .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        // When & Then
        assertThatThrownBy(() -> bookingAdminService.rejectBooking(1L, rejectionRequest))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Booking is not in PENDING status");
    }

    @Test
    void getAllBookings_ShouldReturnPaginatedBookings() {
        // Given
        Page<Booking> page = new PageImpl<>(List.of(testBooking));
        when(bookingRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(bookingMapper.toResponse(testBooking)).thenReturn(testBookingResponse);

        // When
        List<BookingResponse> result = bookingAdminService.getAllBookings(0, 10, "id", "DESC");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testBookingResponse);
        verify(bookingRepository).findAll(any(Pageable.class));
    }

    @Test
    void updateBooking_ShouldUpdateBooking_WhenValidRequest() {
        // Given
        BookingAdminUpdateRequest updateRequest = BookingAdminUpdateRequest.builder()
            .title("Updated Title")
            .description("Updated Description")
            .status(BookingStatus.APPROVED)
            .build();

        Booking updatedBooking = Booking.builder()
            .id(1L)
            .title("Updated Title")
            .description("Updated Description")
            .status(BookingStatus.APPROVED)
            .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingMapper.updateEntity(testBooking, updateRequest)).thenReturn(updatedBooking);
        when(bookingRepository.save(updatedBooking)).thenReturn(updatedBooking);
        when(bookingMapper.toResponse(updatedBooking)).thenReturn(testBookingResponse);

        // When
        BookingResponse result = bookingAdminService.updateBooking(1L, updateRequest);

        // Then
        assertThat(result).isEqualTo(testBookingResponse);
        verify(bookingRepository).save(updatedBooking);
        verify(bookingMapper).updateEntity(testBooking, updateRequest);
    }

    @Test
    void deleteBooking_ShouldDeleteBooking_WhenValidId() {
        // Given
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        // When
        bookingAdminService.deleteBooking(1L);

        // Then
        verify(bookingRepository).delete(testBooking);
    }

    @Test
    void searchBookings_ShouldReturnFilteredBookings() {
        // Given
        BookingAdminSearchRequest searchRequest = BookingAdminSearchRequest.builder()
            .title("Test")
            .status(BookingStatus.PENDING)
            .build();

        Page<Booking> page = new PageImpl<>(List.of(testBooking));
        when(bookingRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(bookingMapper.toResponse(testBooking)).thenReturn(testBookingResponse);

        // When
        List<BookingResponse> result = bookingAdminService.searchBookings(searchRequest, 0, 10, "id", "DESC");

        // Then
        assertThat(result).hasSize(1);
        verify(bookingRepository).findAll(any(Pageable.class));
    }

    @Test
    void getBookingsByStatus_ShouldReturnBookingsWithStatus() {
        // Given
        when(bookingRepository.findByStatus(BookingStatus.PENDING)).thenReturn(List.of(testBooking));
        when(bookingMapper.toResponse(testBooking)).thenReturn(testBookingResponse);

        // When
        List<BookingResponse> result = bookingAdminService.getBookingsByStatus("PENDING", 0, 10, "id", "DESC");

        // Then
        assertThat(result).hasSize(1);
        verify(bookingRepository).findByStatus(BookingStatus.PENDING);
    }

    @Test
    void getTotalBookingsCount_ShouldReturnTotalCount() {
        // Given
        when(bookingRepository.count()).thenReturn(5L);

        // When
        long result = bookingAdminService.getTotalBookingsCount();

        // Then
        assertThat(result).isEqualTo(5L);
        verify(bookingRepository).count();
    }

    @Test
    void getBookingsCountByStatus_ShouldReturnCountForStatus() {
        // Given
        when(bookingRepository.findByStatus(BookingStatus.PENDING)).thenReturn(List.of(testBooking));

        // When
        long result = bookingAdminService.getBookingsCountByStatus("PENDING");

        // Then
        assertThat(result).isEqualTo(1L);
        verify(bookingRepository).findByStatus(BookingStatus.PENDING);
    }

    @Test
    void getBookingsCountByMeetingRoom_ShouldReturnCountForRoom() {
        // Given
        when(bookingRepository.findByMeetingRoomId(1L)).thenReturn(List.of(testBooking));

        // When
        long result = bookingAdminService.getBookingsCountByMeetingRoom(1L);

        // Then
        assertThat(result).isEqualTo(1L);
        verify(bookingRepository).findByMeetingRoomId(1L);
    }

    @Test
    void getBookingsCountByUser_ShouldReturnCountForUser() {
        // Given
        when(bookingRepository.findByCreatedById(1L)).thenReturn(List.of(testBooking));

        // When
        long result = bookingAdminService.getBookingsCountByUser(1L);

        // Then
        assertThat(result).isEqualTo(1L);
        verify(bookingRepository).findByCreatedById(1L);
    }
}
