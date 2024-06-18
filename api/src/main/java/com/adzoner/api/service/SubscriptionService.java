package com.adzoner.api.service;

import com.adzoner.api.entity.User;
import com.adzoner.api.entity.membership.Subscription;
import com.adzoner.api.entity.membership.SubscriptionType;
import com.adzoner.api.repository.UserRepository;
import com.adzoner.api.repository.membership.SubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.EntityResponse;

import javax.xml.stream.events.EntityReference;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    public void checkSubscriptionExpiry() {
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionByActiveStatus(true);
        subscriptions.forEach(subscription -> System.out.println(subscription.getExpiresAt()));
    }

    public Subscription getCurrentPartnerSubscription() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> userList = userRepository.findByEmail(userEmail);
        List<Subscription> subscription =  subscriptionRepository.findLatestByUser(userList.get(0));

        if(subscription.isEmpty()){
            throw new Exception("No subscription for partner");
        }
        subscription.get(0).setRemainingAdsCount(0);
        return subscription.get(0);
    }
}
