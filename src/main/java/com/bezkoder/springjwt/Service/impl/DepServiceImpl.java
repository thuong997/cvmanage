package com.bezkoder.springjwt.Service.impl;

import com.bezkoder.springjwt.Service.DepService;
import com.bezkoder.springjwt.models.DepEntity;
import com.bezkoder.springjwt.payload.response.DepartmentResponse;
import com.bezkoder.springjwt.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepServiceImpl implements DepService {
    @Autowired
    DepartmentRepository depRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<DepartmentResponse> getAllDepList() {
        List<DepartmentResponse> list = new ArrayList<>();
        Optional<List<DepEntity>> lstOpt = depRepository.findAllByIsDeletedFalse();
        if (lstOpt.isPresent()) {
            List<DepEntity> lstEntity = lstOpt.get();
            list = modelMapper.map(lstEntity, new TypeToken<List<DepartmentResponse>>() {
            }.getType());
        }
        return list;
    }

    @Override
    public DepEntity getDepByID(int depID) {

        return depRepository.findById(depID).get();
    }

    @Override
    public void updateDep(DepEntity dep) {

    }

    @Override
    public void deleteDep(int depId) {

    }
}
