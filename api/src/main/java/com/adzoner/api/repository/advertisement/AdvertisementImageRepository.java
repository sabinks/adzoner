package com.adzoner.api.repository.advertisement;

import com.adzoner.api.entity.advertisement.AdvertisementImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementImageRepository extends JpaRepository<AdvertisementImage, Long> {
}
