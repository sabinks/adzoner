package com.adzoner.api.repository.membership;

import com.adzoner.api.entity.User;
import com.adzoner.api.entity.membership.SubscriptionType;
import com.adzoner.api.entity.membership.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long id);

    @Query(value = "SELECT s from Subscription s where s.user = ?1")
    List<Subscription> findLatestByUser(User user);

    @Query(value = "SELECT s from Subscription s where s.status = ?1")
    List<Subscription> findSubscriptionByActiveStatus(Boolean status);
}
