package com.jlss.placelive.userservice.service;

import com.jlss.placelive.commonlib.dto.ResponseDto;
import com.jlss.placelive.commonlib.dto.ResponseListDto;
import com.jlss.placelive.userservice.entity.User;


import java.util.List;

public interface UserService {

    ResponseDto<User> createUser(User user);

    ResponseDto<User> updateUser(Long id, User user);

    ResponseDto<User> getUserById(Long id);

    ResponseListDto<List<User>> getAllUsers(int page, int size, String filter, String search);

    ResponseDto<String> deleteUser(Long id);
    ResponseListDto<List<User>> getUsersByMobileNo(List<String> mobileNumbers);
    ResponseDto<String> addToFollowingList(Long id, Long toUserId);
    ResponseDto<String> addToFollowersList(Long id, Long toUserId);

    Integer getTotalCount();

    ResponseListDto<List<User>> getUserFriendRequests(Long id);

    ResponseDto<String> removeFriendRequest(Long id, Long requestId);

    ResponseDto<User> loginUserByUserId(Long id);
}
