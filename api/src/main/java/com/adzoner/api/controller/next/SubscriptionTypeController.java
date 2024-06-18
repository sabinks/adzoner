package com.adzoner.api.controller.next;

import com.adzoner.api.entity.membership.Subscription;
import com.adzoner.api.entity.membership.SubscriptionType;
import com.adzoner.api.service.SubscriptionService;
import com.adzoner.api.service.membership.SubscriptionTypeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class SubscriptionTypeController {
    private final SubscriptionTypeService subscriptionTypeService;

    public SubscriptionTypeController(SubscriptionTypeService subscriptionTypeService) {
        this.subscriptionTypeService = subscriptionTypeService;
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','PARTNER','USER')")
    @GetMapping("subscription-type-list")
    public List<SubscriptionType> getSubscriptionTypeList() {
        return subscriptionTypeService.getSubscriptionTypeList();
    }
}
