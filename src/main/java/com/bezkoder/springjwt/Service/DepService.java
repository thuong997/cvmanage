package com.bezkoder.springjwt.Service;

import com.bezkoder.springjwt.models.DepEntity;
import com.bezkoder.springjwt.payload.response.DepartmentResponse;

import java.util.List;

public interface DepService {
    List<DepartmentResponse> getAllDepList();

    DepEntity getDepByID(int userId);

    void updateDep(DepEntity dep);

    void deleteDep(int depId);
}
