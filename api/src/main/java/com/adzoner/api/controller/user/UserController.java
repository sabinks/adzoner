package com.adzoner.api.controller.user;

import com.adzoner.api.dto.user.*;
import com.adzoner.api.entity.User;
import com.adzoner.api.repository.UserRepository;
import com.adzoner.api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto)
            throws Exception {
        userService.forgotPassword(forgotPasswordDto);

        return new ResponseEntity<>("Please check your email for password reset link!", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto)
            throws Exception {
        userService.resetPassword(resetPasswordDto);

        return new ResponseEntity<>("Password reset complete!", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) throws Exception {
        userService.changePassword(passwordChangeDto);

        return new ResponseEntity<>("Password changed!", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @PostMapping("/get-user")
    public ResponseEntity<?> getUser() throws Exception {
        Map<String, String> userData = userService.getUser();

        return new ResponseEntity<>(userData, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @PostMapping("/get-member-published-ads-count")
    public ResponseEntity<?> currentPublishedAds() throws Exception {
        Map<String, String> userData = userService.currentPublishedAds();

        return new ResponseEntity<>(userData, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @PostMapping("/set-user")
    public ResponseEntity<String> setUser(@Valid @ModelAttribute UserDetailDto userDetailDto,
                                          @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws Exception {
        userService.setUser(userDetailDto, profileImage);

        return new ResponseEntity<String>("User detail saved!", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PostMapping("/user-status-change/{id}")
    public ResponseEntity<String> userStatusChange(@PathVariable Long id, @RequestBody ActiveStatusChangeDto userData) {
        userService.userStatusChange(id, userData);

        return new ResponseEntity<>("User status changed!", HttpStatus.OK);
    }

}
