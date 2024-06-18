package com.adzoner.api.database.seeder;

import com.adzoner.api.entity.Role;
import com.adzoner.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;

    public void run(String... args) throws Exception {
//        seedRoleData();
    }

    private void seedRoleData() {
        if (roleRepository.count() == 0) {
            Role role1 = new Role();
            role1.setName("SUPERADMIN");
            roleRepository.save(role1);

            Role role2 = new Role();
            role2.setName("ADMIN");
            roleRepository.save(role2);

            Role role3 = new Role();
            role3.setName("PARTNER");
            roleRepository.save(role3);

            Role role4 = new Role();
            role4.setName("USER");
            roleRepository.save(role4);
        }
//        System.out.println("Total roles: " + roleRepository.count());
    }
}
