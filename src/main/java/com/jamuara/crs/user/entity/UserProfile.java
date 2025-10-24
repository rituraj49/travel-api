/*
package com.jamuara.crs.user.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String userName;

    @Size(min = 7, max = 15, message = "Password must be between 7 and 15 characters")
    @Column(nullable = false, unique = true, length = 15)
    private String password;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    private String phone;

    private String address;
}






*/
