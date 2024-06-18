package com.adzoner.api.controller.user;

import com.adzoner.api.dto.user.UserCanPublishDto;
import com.adzoner.api.entity.User;
import com.adzoner.api.service.user.MemberService;
import com.adzoner.api.service.user.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class PartnerController {
    @Autowired
    PartnerService partnerService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/partners")
    public Page<User> index(@RequestParam(defaultValue = "0") Integer offset,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "DESC") String sort,
                            @RequestParam(defaultValue = "createdAt") String sortBy) {

        return partnerService.index(offset, pageSize, search, sort, sortBy);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PostMapping("/user-can-publish/{id}")
    public ResponseEntity<String> partnerCanPublishStatusChange(@PathVariable Long id, @RequestBody UserCanPublishDto userCanPublishDto)
            throws Exception {
        return partnerService.partnerCanPublishStatus(userCanPublishDto, id);
    }
}
