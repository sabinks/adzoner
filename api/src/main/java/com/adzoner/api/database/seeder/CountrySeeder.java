package com.adzoner.api.database.seeder;

import com.adzoner.api.entity.Country;
import com.adzoner.api.entity.Role;
import com.adzoner.api.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CountrySeeder implements CommandLineRunner {
    @Autowired
    CountryRepository countryRepository;
    @Override
    public void run(String... args) throws Exception {
//        seedCountryData();
    }

    private void seedCountryData() {
        if (countryRepository.count() == 0) {
            Country country1 = new Country();
            country1.setName("Nepal");
            country1.setSlug("nepal");
            countryRepository.save(country1);

            Country country2 = new Country();
            country2.setName("Australia");
            country2.setSlug("australia");
            countryRepository.save(country2);
        }
    }
}
