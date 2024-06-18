package com.adzoner.api.service.membership;

import com.adzoner.api.entity.membership.SubscriptionType;
import com.adzoner.api.repository.membership.SubscriptionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionTypeService {
    @Autowired
    SubscriptionTypeRepository subscriptionTypeRepository;

    public List<SubscriptionType> getSubscriptionTypeList() {
        return subscriptionTypeRepository.findAll();
    }
}
