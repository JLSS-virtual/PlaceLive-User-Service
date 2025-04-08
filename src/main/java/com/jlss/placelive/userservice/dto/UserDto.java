package com.jlss.placelive.userservice.dto;


import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String bio;
    private boolean isLoggedIn;
    private String email;
    private String mobileNumber;
    private String profileImageUrl;

    // Flattened region fields
    private String country;
    private String state;
    private String city;
    private String street;

    private List<Long> followers;
    private List<Long> following;
    private List<Long> closeFriends;

    private Date accountCreatedAt;
    private Date lastLoginAt;
}
