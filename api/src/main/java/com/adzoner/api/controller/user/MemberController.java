package com.adzoner.api.controller.user;

import com.adzoner.api.entity.User;
import com.adzoner.api.service.user.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class MemberController {
    @Autowired
    MemberService memberService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/users")
    public Page<User> index(@RequestParam(defaultValue = "0") Integer offset,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "DESC") String sort,
                            @RequestParam(defaultValue = "createdAt") String sortBy) {

        return memberService.index(offset, pageSize, search, sort, sortBy);
    }
}
