package com.adzoner.api.controller;

import com.adzoner.api.dto.country.CountryDto;
import com.adzoner.api.entity.Advertisement;
import com.adzoner.api.entity.Country;
import com.adzoner.api.entity.Province;
import com.adzoner.api.service.CountryService;
import com.adzoner.api.service.ProvinceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CountryController {
    @Autowired
    CountryService countryService;

    @Autowired
    ProvinceService provinceService;

    @GetMapping("/countries-list")
    public List<Country> getCountriesList(){
        return countryService.getCountriesList();
    }

    @GetMapping("/countries")
    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    public Page<Country> index(@RequestParam(defaultValue = "0") Integer offset,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "") String search,
                               @RequestParam(defaultValue = "DESC") String sort,
                               @RequestParam(defaultValue = "createdAt") String sortBy){
        return countryService.index(offset, pageSize, search, sort, sortBy);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/countries/{id}")
    public ResponseEntity<Country> show(@PathVariable Long id) throws IOException {
        Country country = countryService.show(id);

        return new ResponseEntity<>(country, HttpStatus.OK);
    }
    @GetMapping("/countries/{id}/provinces")
    public List<Province> getProvincesByCountry(@PathVariable Long id){
        return provinceService.getProvincesByCountryId(id);
    }

    @PostMapping("/countries")
    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    public ResponseEntity<String> store(@Valid @RequestBody CountryDto countryDto){
        countryService.store(countryDto);

        return new ResponseEntity<>("Country Saved", HttpStatus.CREATED);
    }

}
