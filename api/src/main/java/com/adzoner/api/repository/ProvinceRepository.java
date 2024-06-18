package com.adzoner.api.repository;

import com.adzoner.api.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    List<Province> findByCountryId(Long id);

    Optional<Province> findByName(String provinceName);
}
