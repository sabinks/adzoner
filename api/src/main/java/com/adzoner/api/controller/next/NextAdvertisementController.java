package com.adzoner.api.controller.next;

import com.adzoner.api.entity.Advertisement;
import com.adzoner.api.service.next.NextService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/next")
public class NextAdvertisementController {
    @Autowired
    NextService nextService;
    @Autowired
    ServletContext servletContext;

    @GetMapping("/advertisements")
    public Page<Advertisement> index(@RequestParam(defaultValue = "Nepal") String country,
                                     @RequestParam(defaultValue = "") String province,
                                     @RequestParam(defaultValue = "0") String categoryIds,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "0") Integer offset,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "DESC") String sort,
                                     @RequestParam(defaultValue = "createdAt") String sortBy) {
        return nextService.index(country, province, categoryIds, search, offset, pageSize, sort, sortBy);
    }
}
