package com.adzoner.api.controller;


import com.adzoner.api.dto.advertisement.CategoryDto;
import com.adzoner.api.entity.advertisement.Category;
import com.adzoner.api.service.advertisement.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasAnyRole('PARTNER', 'SUPERADMIN', 'ADMIN', 'USER')")
    @GetMapping("/categories")
    public List<Category> getCategoryList() {
        return categoryService.findAll();
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PostMapping("/categories")
    public ResponseEntity<String> store(@Valid @RequestBody CategoryDto categoryDto
//                                        @ModelAttribute MultipartFile image
    ) throws Exception {
        categoryService.store(categoryDto);

        return new ResponseEntity<>("Advertisement Type Saved!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> show(@PathVariable Long id
    ) throws Exception {
        Category category =  categoryService.show(id);

        return new ResponseEntity<>( category, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/categories/{id}")
    public ResponseEntity<String> store(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto
//                                        @ModelAttribute MultipartFile image
    ) throws Exception {
        categoryService.update(id, categoryDto);

        return new ResponseEntity<>("Category Saved!", HttpStatus.CREATED);
    }
}
