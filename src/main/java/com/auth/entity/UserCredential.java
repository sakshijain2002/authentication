package com.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bouncycastle.tsp.TimeStampRequest;
import org.hibernate.annotations.Cascade;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor


@Table(name="user")
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> role;
    @Column(unique = true)
    private String email;
    private String password;
    private Long contactNumber;
    private String address;
    private String dob;
    private String gender;
    private String status;
    private Time timeStamp;


}