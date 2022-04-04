package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.JobEntity;
import com.bezkoder.springjwt.models.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Integer> {
    LevelEntity findByLevelId(int id);
    LevelEntity findByLevelName(String levelName);
}
