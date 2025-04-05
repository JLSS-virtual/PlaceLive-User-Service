package com.jlss.placelive.userservice.controller;

import com.jlss.placelive.commonlib.dto.ResponseDto;
import com.jlss.placelive.commonlib.dto.ResponseListDto;
import com.jlss.placelive.userservice.dto.MobileNumberRequestDto;
import com.jlss.placelive.userservice.entity.User;
import com.jlss.placelive.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDto<User>> createUser(@RequestBody User user) {
        ResponseDto<User> response = userService.createUser(user);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        ResponseDto<User> response = userService.updateUser(id, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<User>> getUserById(@PathVariable Long id) {
        ResponseDto<User> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseListDto<List<User>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "") String search) {
        if (size == null || size == 0) {
            // Fetch total number of trackers from the database
            size = userService.getTotalCount(); // Define this method in your service
        }
        ResponseListDto<List<User>> response = userService.getAllUsers(page, size, filter, search);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> deleteUser(@PathVariable Long id) {
        ResponseDto<String> response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/by-mobile-numbers")
    public ResponseEntity<ResponseListDto<List<User>>> getUsersByMobileNumbers(
            @RequestBody List<String> mobileNumbers) {

        ResponseListDto<List<User>> response = userService.getUsersByMobileNo(mobileNumbers);
        return ResponseEntity.ok(response);
    }

}
