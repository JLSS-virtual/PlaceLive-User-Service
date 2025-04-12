package com.jlss.placelive.userservice.controller;

import com.jlss.placelive.commonlib.dto.ResponseDto;
import com.jlss.placelive.commonlib.dto.ResponseListDto;
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

    @GetMapping("/friendrequests/{id}")
    public ResponseEntity<ResponseListDto<List<User>>> getUserFriendRequests(@PathVariable Long id) {
        ResponseListDto<List<User>> response = userService.getUserFriendRequests(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/friends/{id}")
    public ResponseEntity<ResponseListDto<List<User>>> getAllFriends(@PathVariable Long id) {
        ResponseListDto<List<User>> response = userService.getAllFriends(id);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/removerequest")
    public ResponseEntity<ResponseDto<String>> removeFriendRequest(@RequestParam Long removerId,@RequestParam Long requestId) {
        ResponseDto<String> response = userService.removeFriendRequest(removerId,requestId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<User>> getUserById(@PathVariable Long id) {
        ResponseDto<User> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/login")
    public ResponseEntity<ResponseDto<User>> loginUserByUserId(@RequestParam Long id) {
        ResponseDto<User> response = userService.loginUserByUserId(id);
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
    @PostMapping("/request")// this will be sent by who wants to add friend or mae a friend request.
    public ResponseEntity<ResponseDto<String>> addToFollowingList(@RequestParam Long friendRequestId,@RequestParam Long toUserId) {
        ResponseDto<String> response = userService.addToFollowingList(friendRequestId,toUserId);
        return ResponseEntity.ok(response);
    }// this following is equivalent to requests . and once accepted it will add to the followers list. no following list

    @PostMapping("/followers")
    public ResponseEntity<ResponseDto<String>> addToFollowersList(@RequestParam Long followerId,@RequestParam Long toUserId) {
        ResponseDto<String> response = userService.addToFollowersList(followerId,toUserId);
        return ResponseEntity.ok(response);
    }// this was called by who accepted the request

    @PostMapping("/by-mobile-numbers")
    public ResponseEntity<ResponseListDto<List<User>>> getUsersByMobileNumbers(
            @RequestBody List<String> mobileNumbers) {

        ResponseListDto<List<User>> response = userService.getUsersByMobileNo(mobileNumbers);
        return ResponseEntity.ok(response);
    }

}
