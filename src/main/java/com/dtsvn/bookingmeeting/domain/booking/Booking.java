package com.dtsvn.bookingmeeting.domain.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.dtsvn.bookingmeeting.domain.enumeration.BookingStatus;
import com.dtsvn.bookingmeeting.domain.notification.Notification;
import com.dtsvn.bookingmeeting.domain.room.MeetingRoom;
import com.dtsvn.bookingmeeting.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A booking.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
@EntityListeners(AuditingEntityListener.class)
public class Booking {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "meeting_room_id", nullable = false)
    private MeetingRoom meetingRoom;

    @Size(max = 200)
    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Size(max = 500)
    @Column(length = 500)
    private String purpose;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @ManyToOne
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    private Set<BookingParticipant> participants = new HashSet<>();
}
