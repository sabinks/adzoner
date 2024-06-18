package com.adzoner.api.repository.monitor;

import com.adzoner.api.entity.monitor.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {
    Optional<SiteVisit> findByIpAddress(String ipAddress);
}
