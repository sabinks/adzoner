package com.adzoner.api.controller;

import com.adzoner.api.entity.District;
import com.adzoner.api.service.DistrictService;
import com.adzoner.api.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DistrictController {
    @Autowired
    DistrictService districtService;
    @GetMapping("/districts")
    List<District> getAllDistricts(){
        return districtService.findAll();
    }

    @GetMapping("/provinces/{id}/districts")
    List<District> getDistrictsByProvinceId(@PathVariable Long id){
        return districtService.findDistrictsByProviceId(id);
    }
}
