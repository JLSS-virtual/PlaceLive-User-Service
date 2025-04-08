package com.jlss.placelive.userservice.service.impl;

import com.jlss.placelive.commonlib.dto.PaginatedDto;
import com.jlss.placelive.commonlib.dto.ResponseDto;
import com.jlss.placelive.commonlib.dto.ResponseListDto;
import com.jlss.placelive.commonlib.service.impl.GenericServiceImpl;
import com.jlss.placelive.userservice.client.SearchServiceUserClient;
import com.jlss.placelive.userservice.dto.UserDto;
import com.jlss.placelive.userservice.entity.User;
import com.jlss.placelive.userservice.entity.UserRegion;
import com.jlss.placelive.userservice.kafka.UserEventProducer;
import com.jlss.placelive.userservice.mapper.UserMapper;
import com.jlss.placelive.userservice.repository.UserRegionRepository;
import com.jlss.placelive.userservice.repository.UserRepository;
import com.jlss.placelive.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public ResponseDto<User> createUser(User user) {
        if (user.getUserRegion() == null) {
            user.setUserRegion(regionRepository.findById(1L).orElseThrow(
                    () -> new IllegalArgumentException("Default Region Not Found")));
        }
       UserRegion region = regionRepository.save(user.getUserRegion());
        user.setUserRegion(region);
        User savedUser = super.createObject(user);
        // sending rest request  to Elastic search
        // 1st need to map
        UserDto userDto = userMapper.toDto(user);
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
}
