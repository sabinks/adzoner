package com.adzoner.api.controller;


import com.adzoner.api.dto.RegisterDto;
import com.adzoner.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class RegisterController {
    @Autowired
    UserService userService;
    
    @PostMapping("/user-register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDto registerDto) throws Exception{
       userService.registerUser(registerDto, "USER");

       return new ResponseEntity<String>("User registered, pleas check email for verification!", HttpStatus.CREATED);
    }
    @PostMapping("/partner-register")
    public ResponseEntity<String> registerBusiness(@Valid @RequestBody RegisterDto registerDto) throws Exception{
        userService.registerUser(registerDto, "PARTNER");

        return new ResponseEntity<String>("Business user registered, pleas check email for verification!", HttpStatus.CREATED);
    }
}
