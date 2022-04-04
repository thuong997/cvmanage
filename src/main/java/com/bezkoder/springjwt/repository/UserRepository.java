package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);

    Optional<UserEntity> findByUsernameAndIsDeletedFalse(String username);

    Optional<UserEntity> findByUserIdAndIsDeletedFalse(int userId);

    List<UserEntity> findAllByIsDeletedFalse();

//    Optional<UserEntity> findByRoleAndIsDeleteFalse(int userId);

    Optional<UserEntity> findByPassword(String password);

    Boolean existsByUsername(String username);

    Optional<List<UserEntity>> findByUserIdIn(Integer[] lstDepId);



}
