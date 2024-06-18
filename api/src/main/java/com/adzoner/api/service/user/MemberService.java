package com.adzoner.api.service.user;

import com.adzoner.api.entity.Role;
import com.adzoner.api.entity.User;
import com.adzoner.api.repository.RoleRepository;
import com.adzoner.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    public Page<User> index(Integer offset, Integer pageSize, String search, String sort, String sortBy) {
        Role role = roleRepository.findByName("USER");
        if(role == null){
            throw new EntityNotFoundException("Partner role not found");
        }
        return userRepository.findAllUserByRole(role.getName(), PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
    }
}
