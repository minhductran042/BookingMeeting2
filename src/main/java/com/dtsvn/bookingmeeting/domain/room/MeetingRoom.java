package com.dtsvn.bookingmeeting.domain.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.location.Location;
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
 * A meeting room.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "meeting_rooms")
@EntityListeners(AuditingEntityListener.class)
public class MeetingRoom {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column
    private Integer capacity;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String equipments;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active")
    private boolean active = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "meetingRoom")
    @JsonIgnore
    private Set<Booking> bookings = new HashSet<>();
}
