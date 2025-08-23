package com.dtsvn.bookingmeeting.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.dtsvn.bookingmeeting.domain.auth.RefreshToken;
import com.dtsvn.bookingmeeting.domain.booking.Booking;
import com.dtsvn.bookingmeeting.domain.enumeration.Role;
import com.dtsvn.bookingmeeting.domain.notification.Notification;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "uk_users_username", columnNames = {"username"}),
    @UniqueConstraint(name = "uk_users_email", columnNames = {"email"})
})
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String username;

    @JsonIgnore
    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String password;

    @Size(max = 100)
    @Column(name = "full_name", length = 100)
    private String fullName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, nullable = false)
    private String email;

    @Size(max = 20)
    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Size(max = 100)
    @Column(length = 100)
    private String department;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private Set<Booking> bookingsCreated = new HashSet<>();

    @OneToMany(mappedBy = "approver")
    @JsonIgnore
    private Set<Booking> bookingsApproved = new HashSet<>();

    @OneToMany(mappedBy = "cancelledBy")
    @JsonIgnore
    private Set<Booking> bookingsCancelled = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
