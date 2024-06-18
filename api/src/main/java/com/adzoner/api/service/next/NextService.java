package com.adzoner.api.service.next;


import com.adzoner.api.dto.ReceiverDto;
import com.adzoner.api.dto.next.ContactUsDto;
import com.adzoner.api.entity.*;
import com.adzoner.api.entity.advertisement.Category;
import com.adzoner.api.mail.next.ContactUsFormMail;
import com.adzoner.api.repository.*;
import com.adzoner.api.repository.advertisement.CategoryRepository;
import com.adzoner.api.service.SendMailService;
import com.adzoner.api.utility.MyUtilityClass;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class NextService {
    private final ContactUsFormMail contactUsFormMail;

    private final CategoryRepository categoryRepository;
    private final AdvertisementRepository advertisementRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;
    @Value("${spring.admin.email}")
    private String adminEmail;
    @Value("${spring.app.name}")
    private String appName;

    public NextService(ContactUsFormMail contactUsFormMail, CategoryRepository categoryRepository, AdvertisementRepository advertisementRepository, CountryRepository countryRepository, ProvinceRepository provinceRepository, UserRepository userRepository, FavouriteRepository favouriteRepository) {
        this.contactUsFormMail = contactUsFormMail;
        this.categoryRepository = categoryRepository;
        this.advertisementRepository = advertisementRepository;
        this.countryRepository = countryRepository;
        this.provinceRepository = provinceRepository;
        this.userRepository = userRepository;
        this.favouriteRepository = favouriteRepository;
    }

    public void contactUsForm(ContactUsDto contactUsDto) throws Exception {
        ReceiverDto receiverDto = new ReceiverDto();
        receiverDto.setEmail(adminEmail);
        receiverDto.setName(appName);
        receiverDto.setUrl("");
        Map<String, String> data = new HashMap<>();
        data.put("name", contactUsDto.getName());
        data.put("email", contactUsDto.getEmail());
        data.put("phone", contactUsDto.getPhone());
        data.put("subject", contactUsDto.getSubject());
        data.put("message", contactUsDto.getMessage());

        receiverDto.setData(data);
        contactUsFormMail.sendMail(receiverDto);
    }

    public Page<Advertisement> index(String countryName, String provinceName, String categoryIds, String search, Integer offset, Integer pageSize, String sort, String sortBy) {

        List<Long> ids = new ArrayList<>();
        if (categoryIds.contains(",")) {
            ids = Stream.of(categoryIds.split(",")).map(Long::parseLong).toList();
        } else {
            ids.add(Long.valueOf(categoryIds));
        }
        Optional<Country> country = countryRepository.findByName(countryName);
        Optional<Province> province = provinceRepository.findByName(provinceName);
//        List<Long> catIds = List.of(MyUtilityClass.stringToLong(categoryIds));
//        List<Category> categories = categoryRepository.findAllById(ids);
        Page<Advertisement> page = null;

        if (!Objects.equals(search, "") && ids.get(0) != 0 && country.isPresent() && province.isPresent()) {
            System.out.println("From Search 1");
            page = advertisementRepository.nextSearchByProvinceAndCategoriesAndSearch(
                    province.get(), ids, search, true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy)
            );
        } else if (!Objects.equals(search, "") && ids.get(0) != 0 && country.isPresent() && province.isEmpty()) {
            System.out.println("From Search 2");
            page = advertisementRepository.nextSearchByCountryAndCategoriesAndSearch(
                    country.get(), ids, search, true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy)
            );
        } else if (!Objects.equals(search, "") && ids.get(0) == 0 && country.isPresent() && province.isPresent()) {
            System.out.println("From Search 3");
            page = advertisementRepository.nextSearchByProvinceAndSearch(
                    province.get(), search, true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy)
            );
        } else if (!Objects.equals(search, "") && ids.get(0) == 0 && country.isPresent() && province.isEmpty()) {
            System.out.println("From Search 4");
            page = advertisementRepository.nextSearchByCountryAndSearch(
                    country.get(), search, true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy)
            );
        } else if (Objects.equals(search, "") && ids.get(0) != 0 && country.isPresent() && province.isPresent()) {
            System.out.println("From Search 5");
            page = advertisementRepository.nextSearchByProvinceAndCategories(
                    province.get(), ids, true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy)
            );
        } else if (Objects.equals(search, "") && ids.get(0) != 0 && country.isPresent() && province.isEmpty()) {
            System.out.println("From Search 6");
            page = advertisementRepository.nextSearchByCountryAndCategories(country.get(), ids, true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
        } else if (Objects.equals(search, "") && ids.get(0) == 0 && country.isPresent() && province.isPresent()) {
            System.out.println("From Search 7");
            page = advertisementRepository.nextSearchByProvince(
                    province.get(), true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy)
            );
        } else if (Objects.equals(search, "") && ids.get(0) == 0 && country.isPresent() && province.isEmpty()) {
            System.out.println("From Search 8");
            page = advertisementRepository.nextSearchByCountry(
                    country.get(), true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy)
            );
        } else {
            System.out.println("From Search 9");
            page = advertisementRepository.findAllAdvertisements(true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> userList = userRepository.findByEmail(userEmail);
        if (!userList.isEmpty()) {
            PageRequest pageable = PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy);

            List<Favourite> favAds = favouriteRepository.findUserFavouriteAdvertisement(userList.get(0));
            List<Long> userFavAdIds = favAds.isEmpty() ? new ArrayList<>() : favAds.stream().map(fav -> fav.getAdvertisement().getId()).toList();
            List<Advertisement> list = page.getContent().stream().map(ads -> {
                ads.setFavourite(false);
                if (userFavAdIds.contains(ads.getId())) {
                    ads.setFavourite(true);
                    return ads;
                }
                return ads;
            }).toList();

            return new PageImpl<>(list, pageable, page.getTotalElements());
        }
        return page;

    }


    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

}
