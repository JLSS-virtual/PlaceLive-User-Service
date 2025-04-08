package com.jlss.placelive.userservice.mapper;

import com.jlss.placelive.userservice.dto.UserDto;
import com.jlss.placelive.userservice.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserMapper {

    public  UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setBio(user.getBio());
        dto.setLoggedIn(user.isLoggedIn());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setProfileImageUrl(user.getProfileImageUrl());

        if (user.getUserRegion() != null) {
            dto.setCountry(user.getUserRegion().getCountry());
            dto.setState(user.getUserRegion().getState());
            dto.setCity(user.getUserRegion().getCity());
            dto.setStreet(user.getUserRegion().getStreet());
        }

        dto.setFollowers(user.getFollowers());
        dto.setFollowing(user.getFollowing());
        dto.setCloseFriends(user.getCloseFriends());

        dto.setAccountCreatedAt(user.getAccountCreatedAt());
        dto.setLastLoginAt(user.getLastLoginAt());

        return dto;
    }


}
