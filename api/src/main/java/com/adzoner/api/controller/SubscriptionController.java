package com.adzoner.api.controller;

import com.adzoner.api.entity.membership.Subscription;
import com.adzoner.api.entity.membership.SubscriptionType;
import com.adzoner.api.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("api")
public class SubscriptionController {
   @Autowired
   SubscriptionService subscriptionService;

    @GetMapping("check-subscription-expiry")
    void checkSubscriptionExpiry(){
        subscriptionService.checkSubscriptionExpiry();
    }

    @PreAuthorize("hasAnyRole('PARTNER')")
    @GetMapping("current-subscription")
    public Subscription getCurrentSubscription() throws Exception {
        return subscriptionService.getCurrentPartnerSubscription();
    }
}
