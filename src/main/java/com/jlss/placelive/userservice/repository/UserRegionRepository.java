package com.jlss.placelive.userservice.repository;

import com.jlss.placelive.userservice.entity.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegionRepository extends JpaRepository<UserRegion,Long>{
}
