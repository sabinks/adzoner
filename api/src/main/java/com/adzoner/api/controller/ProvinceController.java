package com.adzoner.api.controller;

import com.adzoner.api.entity.Province;
import com.adzoner.api.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProvinceController {
    @Autowired
    ProvinceService provinceService;
    @GetMapping("/provinces")
    List<Province> getProvince(){
        return provinceService.findAll();
    }
}
