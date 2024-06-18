package com.adzoner.api.service;

import com.adzoner.api.dto.country.CountryDto;
import com.adzoner.api.entity.Advertisement;
import com.adzoner.api.entity.Country;
import com.adzoner.api.repository.CountryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    @Autowired
    CountryRepository countryRepository;

    public List<Country> getCountriesList() {
        return countryRepository.findAll();
    }

    public void store(CountryDto countryDto) {
        Optional<Country>  country = countryRepository.findByName(countryDto.getName());
        if(country.isPresent()){
            throw new EntityExistsException("Country name already exists");
        }
        Country country1 = new Country();
        country1.setName(countryDto.getName());
        country1.setSlug(countryDto.getName().toLowerCase());

        countryRepository.save(country1);
    }

    public Page<Country> index(Integer offset, Integer pageSize, String search, String sort, String sortBy) {
        return countryRepository.findAll(PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
    }

    public Country show(Long id) {
        Optional<Country> country = countryRepository.findById(id);
        if (country.isEmpty()) {
            throw new EntityNotFoundException("No country found!");
        }
        return country.get();
    }
}
