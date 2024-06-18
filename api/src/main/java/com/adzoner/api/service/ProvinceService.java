package com.adzoner.api.service;

import com.adzoner.api.entity.Province;
import com.adzoner.api.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {
    @Autowired
    ProvinceRepository provinceRepository;

    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    public List<Province> getProvincesByCountryId(Long id) {
        return provinceRepository.findByCountryId(id);
    }
}
