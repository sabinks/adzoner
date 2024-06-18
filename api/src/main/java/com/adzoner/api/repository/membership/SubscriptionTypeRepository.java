package com.adzoner.api.repository.membership;

import com.adzoner.api.entity.membership.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionTypeRepository  extends JpaRepository<SubscriptionType, Long> {
    Optional<SubscriptionType> findByName(String planName);
}
