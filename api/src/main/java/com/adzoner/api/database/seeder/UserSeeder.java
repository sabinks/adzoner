package com.adzoner.api.database.seeder;

import com.adzoner.api.entity.Role;
import com.adzoner.api.entity.User;
import com.adzoner.api.repository.RoleRepository;
import com.adzoner.api.repository.UserRepository;
import com.adzoner.api.utility.MyUtilityClass;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MyUtilityClass myUtilityClass;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//        loadUserData();
    }


    @Transactional
    public void loadUserData(){
        if (userRepository.count() == 0 && roleRepository.count() > 0) {
            String passwordA = passwordEncoder.encode("P@ss1234");
            User userA = new User("superadmin@adzoner.com", "", "Super Admin", passwordA, true, MyUtilityClass.currentDateTime(), MyUtilityClass.currentDateTime(), MyUtilityClass.currentDateTime());
            Role role = roleRepository.findByName("SUPERADMIN");
            userA.addRole(role);
            userRepository.save(userA);
        }
//        System.out.println("Total users: " + userRepository.count());
    }
}
