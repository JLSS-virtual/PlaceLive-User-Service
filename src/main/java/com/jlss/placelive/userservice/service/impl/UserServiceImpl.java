package com.jlss.placelive.userservice.service.impl;

import com.jlss.placelive.commonlib.dto.PaginatedDto;
import com.jlss.placelive.commonlib.dto.ResponseDto;
import com.jlss.placelive.commonlib.dto.ResponseListDto;
import com.jlss.placelive.commonlib.enums.ErrorCode;
import com.jlss.placelive.commonlib.service.impl.GenericServiceImpl;
import com.jlss.placelive.userservice.client.SearchServiceUserClient;
import com.jlss.placelive.userservice.dto.UserDto;
import com.jlss.placelive.userservice.entity.User;
import com.jlss.placelive.userservice.entity.UserRegion;
import com.jlss.placelive.userservice.mapper.UserMapper;
import com.jlss.placelive.userservice.repository.UserRegionRepository;
import com.jlss.placelive.userservice.repository.UserRepository;
import com.jlss.placelive.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserRepository> implements UserService {

    @Autowired
    private UserRegionRepository regionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SearchServiceUserClient searchServiceUserClient;

    private final UserMapper userMapper;


    public UserServiceImpl(@Qualifier("userRepository") UserRepository repository, UserMapper userMapper) {
        super(repository);
        this.userMapper = userMapper;
    }
    public Integer getTotalCount() {
        return (int) repository.count(); // Assuming you use Spring Data JPA
    }

    @Override
    public ResponseListDto<List<User>> getUserFriendRequests(Long id) {
       List<User> friendList = new ArrayList<>();
        // now get all the usersByFollowIngId
        for (Long requestId : userRepository.findById(id).get().getFollowing()){
            // now get all users by user id
            friendList.add(userRepository.findById(requestId).get());
        }
        return new ResponseListDto<>(true,friendList,null,null,null);
    }
    @Override
    public ResponseListDto<List<User>> getAllFriends(Long id){
        List<User> friendList = new ArrayList<>();
        // now get all the usersByFollowIngId
        for (Long requestId : userRepository.findById(id).get().getFollowers()){
            // now get all users by user id
            friendList.add(userRepository.findById(requestId).get());
        }
        return new ResponseListDto<>(true,friendList,null,null,null);
    }

    @Override
    public ResponseDto<String> removeFriendRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
        List<Long> friendList = user.getFollowing();

        Iterator<Long> iterator = friendList.iterator();
        while (iterator.hasNext()) {
            Long followingId = iterator.next();
            if (Objects.equals(followingId, requestId)) {
                iterator.remove(); // ✅ Avoids ConcurrentModificationException
            }
        }//TODO what it acctually does what is iterator.

        user.setFollowing(friendList);
        super.objectsIdPut(Math.toIntExact(userId), user);
        return new ResponseDto<>(true, ErrorCode.OK200.getCode(), null, null);
    }


    @Override
    public ResponseDto<User> loginUserByUserId(Long id) {
        return new ResponseDto<>(true,super.objectsIdGet(Math.toIntExact(id)),null,null);
    }

    @Override
    public ResponseDto<User> createUser(User user) {
        if (user.getUserRegion() == null) {
            user.setUserRegion(regionRepository.findById(1L).orElseThrow(
                    () -> new IllegalArgumentException("Default Region Not Found")));
        }
       UserRegion region = regionRepository.save(user.getUserRegion());
        user.setUserRegion(region);

        /**
         * When a new user creates add himeself as his first follower so he can see himself atleast on the places he was.
         * **/
        // Save user first to get generated ID
        User savedUser = super.createObject(user);

        // Now we can set the followers including the user's own ID
        List<Long> followers = new ArrayList<>();
        followers.add(savedUser.getId());
        savedUser.setFollowers(followers);

        // Save again to update followers
        savedUser = userRepository.save(savedUser);

        // sending rest request  to Elastic search
        // 1st need to map
        UserDto userDto = userMapper.toDto(savedUser);
        searchServiceUserClient.postUserToSearchService(userDto);
        return new ResponseDto<>(true, savedUser,null,null);
    }

    @Override
    public ResponseDto<User> updateUser(Long id, User user) {
        if (user.getUserRegion() == null) {
            user.setUserRegion(regionRepository.findById(1L).orElseThrow(
                    () -> new IllegalArgumentException("Default Region Not Found")));
        }
        UserRegion region = regionRepository.save(user.getUserRegion());
        user.setUserRegion(region);
        User savedUser = super.objectsIdPut(Math.toIntExact(id),user);
        // 1st need to map
        UserDto userDto = userMapper.toDto(user);
        try {
            searchServiceUserClient.putUserToSearchService(id,userDto);
        }
       catch (Exception e){
          throw new RuntimeException();
       }
        return new ResponseDto<>(true, savedUser,null,null);
    }

    @Override
    public ResponseDto<User> getUserById(Long id) {
        return new ResponseDto<>(true,super.objectsIdGet(Math.toIntExact(id)),null,null);
    }

    @Override
    public ResponseListDto<List<User>> getAllUsers(int page, int size, String filter, String search) {
        Page<User> places = super.getListOfObjects(page,size,filter,search);
        List<User> userList = places.getContent();
        PaginatedDto pagination = new PaginatedDto(
                places.getNumber(),         // Current page index
                places.getSize(),           // Page size
                places.getTotalElements(),  // Total elements
                places.getTotalPages()      // Total pages
        );
        return new ResponseListDto<>(true, userList,pagination,null,null);
    }

    @Override
    public ResponseDto<String> deleteUser(Long id) {
        // sending kafka event
        String OK = super.deleteObject(Math.toIntExact(id));
        if (Objects.equals(OK,"OK")) {
            try{
                searchServiceUserClient.deleteUserToSearchService(id);
            }
          catch (Exception e){
                throw new RuntimeException();
          }
        }
        return new ResponseDto<>(true,OK,null,null);
    }
    // now the above ones are only crud orrianted operations but now from here are acctual game changers.
    // 1st methode to provide all users by inputed lists of mobileno.
    // Method to fetch users by a list of mobile numbers
    @Override
    public ResponseListDto<List<User>> getUsersByMobileNo(List<String> mobileNumbers) {
        List<User> users = userRepository.findByMobileNumberIn(mobileNumbers);
        return new ResponseListDto<>(true, users, null, null, null);
    }

    @Override
    public ResponseDto<String> addToFollowingList(Long senderId, Long receiverId) {
        Optional<User> userOpt = userRepository.findById(receiverId);
        if (userOpt.isEmpty()) {
            return new ResponseDto<>(false,null, ErrorCode.ERR404.getCode(), null);
        }

        User user = userOpt.get();
        List<Long> following = user.getFollowing();
        if (following == null) {
            following = new ArrayList<>();
            following.add(senderId);
            user.setFollowing(following);
        }
        else{
            if (!following.contains(senderId)) {
                following.add(senderId);
                user.setFollowing(following);
            }else{
                user.setFollowing(following);
            }

        }
        super.objectsIdPut(Math.toIntExact(receiverId), user);

        return new ResponseDto<>(true, ErrorCode.OK200.getCode(), "Added to following list", null);
    }

    @Override
    public ResponseDto<String> addToFollowersList(Long accepterId, Long toUserId) {
        Optional<User> userOpt = userRepository.findById(accepterId);// the user who accepted the request
        Optional<User> touser =  userRepository.findById(toUserId);// the user who sended the rquwst . we need to add this acepter id to his followers lsi too

        if (userOpt.isEmpty()||touser.isEmpty()) {
            return new ResponseDto<>(false,null, ErrorCode.ERR404.getCode(), null);
        }

        User accepterUser = userOpt.get();
        User senderUser = touser.get();  // ✅ Correct: this is the user who sent the request

        List<Long> accepterUserFollowers = accepterUser.getFollowers();
        List<Long> senderUserFollowers = senderUser.getFollowers();

        if (accepterUserFollowers == null ) {
            accepterUserFollowers = new ArrayList<>();
            accepterUserFollowers.add(accepterId);
            accepterUser.setFollowers(accepterUserFollowers);
        } else {
            if (!accepterUserFollowers.contains(accepterId)) {
                accepterUserFollowers.add(accepterId);
                accepterUser.setFollowers(accepterUserFollowers);
            } else {
                if (!accepterUserFollowers.contains(toUserId)) {
                    accepterUserFollowers.add(toUserId);
                    accepterUser.setFollowers(accepterUserFollowers);
                }else{
                    // Optional: already a follower, maybe log or skip
                    accepterUser.setFollowers(accepterUserFollowers); // still setting to keep flow consistent
                }
            }
        }
        if (senderUserFollowers == null ) {
            senderUserFollowers = new ArrayList<>();
            senderUserFollowers.add(accepterId);
            senderUser.setFollowers(senderUserFollowers);
        } else {
            if (!senderUserFollowers.contains(accepterId)) {
                senderUserFollowers.add(accepterId);
                senderUser.setFollowers(senderUserFollowers);
            } else {
                // Optional: already a follower, maybe log or skip
                senderUser.setFollowers(senderUserFollowers); // still setting to keep flow consistent
            }
        }
        // now when a request was accepted we need to remove it from following list too.
        removeFriendRequest(accepterId,toUserId);
        super.objectsIdPut(Math.toIntExact(toUserId), senderUser);
        super.objectsIdPut(Math.toIntExact(toUserId), accepterUser);

        return new ResponseDto<>(true, ErrorCode.OK200.getCode(), "Added to followers list", null);
    }

}
