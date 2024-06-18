package com.adzoner.api.service;

import com.adzoner.api.entity.District;
import com.adzoner.api.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    @Autowired
    DistrictRepository districtRepository;

    public List<District> findAll() {
        return districtRepository.findAll();
    }

    public List<District> findDistrictsByProviceId(Long id) {
        return districtRepository.findByProvinceId(id);
    }
}
