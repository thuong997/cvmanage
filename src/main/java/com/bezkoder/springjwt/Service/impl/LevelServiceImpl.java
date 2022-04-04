package com.bezkoder.springjwt.Service.impl;
import com.bezkoder.springjwt.Service.LevelService;
import com.bezkoder.springjwt.models.LevelEntity;
import com.bezkoder.springjwt.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LevelServiceImpl implements LevelService {
    @Autowired
    LevelRepository levelRepository;

    @Override
    public LevelEntity getLevelById(int id) {
        LevelEntity levelEntity = levelRepository.findByLevelId(id);
        return  levelEntity;
    }

    @Override
    public LevelEntity findByLevelName(String levelName) {
        return levelRepository.findByLevelName(levelName);
    }
}
