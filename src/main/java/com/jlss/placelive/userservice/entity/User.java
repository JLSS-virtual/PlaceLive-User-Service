package com.jlss.placelive.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;


import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;  // Changed to long (Firebase/Google UID can be stored as String if needed)

    @Column(name = "name")
    private String name;

    @Column(name = "bio")
    private String bio = "";  // Default empty

    @Column(name = "is_logged_in")
    private boolean isLoggedIn = false;  // Fixed typo ("isLongedIn" → "isLoggedIn")

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;  // Changed from Number to String (for formatting)

    @ManyToOne
    @JoinColumn(name = "user_region_id")
    private UserRegion userRegion; // This will be used to set the user's current region by fetching.

    @Convert(converter = JsonToListConverter.class)
    private List<Long> followers;

    @Convert(converter = JsonToListConverter.class)
    private List<Long> following;

    @Convert(converter = JsonToListConverter.class)
    private List<Long> closeFriends;

    @Column(name = "profile_image_url")
    private String profileImageUrl = "";  // Default empty (for Google/Facebook images)

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "account_created_at", updatable = false)
    private Date accountCreatedAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_at")
    private Date lastLoginAt;

}