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

    private String userName;

    private String password;

    private String email;

    @ToString.Exclude
    @Type(StringArrayType.class)
    @Column(name = "roles", columnDefinition = "text[]")
    private String[] roles = new String[1];

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ReservationInterval> reservationList;
}
