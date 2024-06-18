package com.adzoner.api.database.seeder;

import com.adzoner.api.entity.membership.SubscriptionType;
import com.adzoner.api.repository.membership.SubscriptionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionTypeSeeder implements CommandLineRunner {
    @Autowired
    SubscriptionTypeRepository repository;

    public void run(String... args) throws Exception {
        seedMembershipTypeData();
    }

    private void seedMembershipTypeData() {
        if(repository.count() == 0){
            SubscriptionType subscriptionType = new SubscriptionType();
            subscriptionType.setName("Free Plan");
            subscriptionType.setAdsCount(3);
            repository.save(subscriptionType);

            SubscriptionType subscriptionType2 = new SubscriptionType();
            subscriptionType2.setName("Bronze Plan");
            subscriptionType2.setAdsCount(10);
            repository.save(subscriptionType2);

            SubscriptionType subscriptionType3 = new SubscriptionType();
            subscriptionType3.setName("Silver Plan");
            subscriptionType3.setAdsCount(20);
            repository.save(subscriptionType3);
        }
    }
}
