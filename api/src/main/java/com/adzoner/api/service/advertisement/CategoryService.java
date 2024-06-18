package com.adzoner.api.service.advertisement;

import com.adzoner.api.dto.advertisement.CategoryDto;
import com.adzoner.api.entity.advertisement.Category;
import com.adzoner.api.repository.advertisement.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void store(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());

        categoryRepository.save(category);
    }
    public void update(Long id, CategoryDto categoryDto) {
        Optional<Category> category =  categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new EntityNotFoundException("Category not found!");
        }
        category.get().setName(categoryDto.getName());

        categoryRepository.save(category.get());
    }

    public Category show(Long id) {
        Optional<Category> category =  categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new EntityNotFoundException("Category not found!");
        }
        return category.get();
    }
}
