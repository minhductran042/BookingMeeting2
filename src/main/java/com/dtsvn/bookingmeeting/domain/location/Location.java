package com.dtsvn.bookingmeeting.domain.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.dtsvn.bookingmeeting.domain.room.MeetingRoom;
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
 * A location.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "locations")
@EntityListeners(AuditingEntityListener.class)
public class Location {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    @Size(max = 500)
    @Column(length = 500)
    private String address;

    @Size(max = 100)
    @Column(length = 100)
    private String city;

    @Size(max = 100)
    @Column(length = 100)
    private String country;

    @Size(max = 20)
    @Column(length = 20)
    private String postalCode;

    @Column(name = "is_active")
    private boolean active = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private Set<MeetingRoom> meetingRooms = new HashSet<>();
}
