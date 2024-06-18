package com.adzoner.api.repository;

import com.adzoner.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);

    @Query(value="SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    Page<User> findAllUserByRole(String roleName, PageRequest of);
}
