package com.booking.demo.model;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String password;

    private String email;

    @ToString.Exclude
    @Type(StringArrayType.class)
    @Column(name = "roles", columnDefinition = "text[]")
    private String[] roles = new String[] {RoleType.ROLE_USER.toString()};

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ReservationInterval> reservationList;
}
