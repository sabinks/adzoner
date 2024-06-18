package com.adzoner.api.controller.next;

import com.adzoner.api.entity.advertisement.Category;
import com.adzoner.api.service.next.NextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/next")
public class NextAdsTypeController {

    @Autowired
    NextService nextService;

    @GetMapping("/categories")
    public List<Category> getCategoryList(){
        return nextService.getCategoryList();
    }
}
