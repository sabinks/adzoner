package com.adzoner.api.controller.user;

import com.adzoner.api.entity.User;
import com.adzoner.api.service.AdminService;
import com.adzoner.api.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class AdminController {
    @Autowired
    AdminService adminService;

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("/admins")
    public Page<User> index(@RequestParam(defaultValue = "0") Integer offset,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "DESC") String sort,
                            @RequestParam(defaultValue = "createdAt") String sortBy) {
        return adminService.index(offset, pageSize, search, sort, sortBy);
    }
}
