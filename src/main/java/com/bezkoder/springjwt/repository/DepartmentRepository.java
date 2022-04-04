package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.DepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepEntity, Integer> {
    Optional<List<DepEntity>> findByDepIdIn(Integer[] lstDepId);

    Optional<List<DepEntity>> findAllByIsDeletedFalse();

    Optional<DepEntity> findByDepId(int depId);

}
