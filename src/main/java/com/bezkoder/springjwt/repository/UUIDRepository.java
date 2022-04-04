package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.UUIDEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UUIDRepository extends JpaRepository<UUIDEntity, Integer> {

    UUIDEntity findByUuidStr(String uuid);
    UUIDEntity findUserByUuidStr(String uuid);
}
