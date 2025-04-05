package com.jlss.placelive.userservice.service;

import com.jlss.placelive.commonlib.dto.ResponseDto;
import com.jlss.placelive.commonlib.dto.ResponseListDto;
import com.jlss.placelive.userservice.dto.MobileNumberRequestDto;
import com.jlss.placelive.userservice.entity.User;


import java.util.List;

public interface UserService {

    ResponseDto<User> createUser(User user);

    ResponseDto<User> updateUser(Long id, User user);

    ResponseDto<User> getUserById(Long id);

    ResponseListDto<List<User>> getAllUsers(int page, int size, String filter, String search);

    ResponseDto<String> deleteUser(Long id);
    ResponseListDto<List<User>> getUsersByMobileNo(List<String> mobileNumbers);

    Integer getTotalCount();
}
