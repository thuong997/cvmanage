package com.bezkoder.springjwt.Service;

import com.bezkoder.springjwt.models.LevelEntity;

public interface LevelService {
    LevelEntity getLevelById(int id);
    LevelEntity findByLevelName(String levelName);
}
