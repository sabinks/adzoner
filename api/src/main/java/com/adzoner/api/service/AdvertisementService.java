package com.adzoner.api.service;

import com.adzoner.api.dto.ReceiverDto;
import com.adzoner.api.dto.advertisement.AdvertisementDto;
import com.adzoner.api.dto.advertisement.AdvertisementStatusDto;
import com.adzoner.api.entity.*;
import com.adzoner.api.entity.advertisement.AdvertisementImage;
import com.adzoner.api.entity.advertisement.Category;
import com.adzoner.api.entity.membership.Subscription;
import com.adzoner.api.mail.AdvertisementCreatedMail;
import com.adzoner.api.repository.*;
import com.adzoner.api.repository.advertisement.AdvertisementImageRepository;
import com.adzoner.api.repository.advertisement.CategoryRepository;
import com.adzoner.api.repository.membership.SubscriptionRepository;
import com.adzoner.api.repository.membership.SubscriptionTypeRepository;
import com.adzoner.api.utility.MyUtilityClass;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

import static com.adzoner.api.utility.MyUtilityClass.*;

@Service
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FavouriteRepository favouriteRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final AdvertisementCreatedMail advertisementCreatedMail;
    private final AdvertisementImageRepository advertisementImageRepository;
    private final SubscriptionRepository subscriptionRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository, CategoryRepository categoryRepository,
                                FavouriteRepository favouriteRepository,
                                UserRepository userRepository,
                                CountryRepository countryRepository,
                                ProvinceRepository provinceRepository,
                                DistrictRepository districtRepository,
                                AdvertisementCreatedMail advertisementCreatedMail,
                                SubscriptionTypeRepository subscriptionTypeRepository, AdvertisementImageRepository advertisementImageRepository,
                                SubscriptionRepository subscriptionRepository) {
        this.advertisementRepository = advertisementRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.favouriteRepository = favouriteRepository;
        this.countryRepository = countryRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.advertisementCreatedMail = advertisementCreatedMail;
        this.advertisementImageRepository = advertisementImageRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Value("${spring.app.dashboard_url}")
    private String dashboardUrl;
    @Value("${spring.admin.email}")
    private String adminEmail;
    @Value("${spring.app.name}")
    private String appName;

    @Value("${spring.upload.directory}")
    private String uploadDirectory;
    @Value("${spring.user.ads.limit}")
    private String userAdsLimit;

    public Page<Advertisement> index(Integer offset, Integer pageSize, String search, String sort, String country, String sortBy) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> userList = userRepository.findByEmail(userEmail);
        List<Role> roles = userList.get(0).getRoles();

        Page<Advertisement> page = null;
        PageRequest pageable = PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy);
        Optional<Country> country1 = countryRepository.findByName(country);
        if (country1.isEmpty()) {
            throw new Exception("Country name not found!");
        }
        List<Favourite> favAds = favouriteRepository.findUserFavouriteAdvertisement(userList.get(0));
        List<Long> userFavAdIds = favAds.isEmpty() ? new ArrayList<>() : favAds.stream().map(fav -> fav.getAdvertisement().getId()).toList();
        if (roles.get(0).getName().matches("USER")) {
            page = advertisementRepository.findAllByUser(userList.get(0), PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
        } else if (roles.get(0).getName().matches("PARTNER")) {
            page = advertisementRepository.findAll(PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
        } else {
            if (!search.isEmpty() && !country.isEmpty()) {
                page = advertisementRepository.nextSearchByCountryAndSearch(
                        country1.get(), search, true, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy)
                );
            } else if (!country.isEmpty()) {
                page = advertisementRepository.findAllByCountry(country, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
            } else {
                page = advertisementRepository.findAll(PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
            }
        }

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

    public Page<Advertisement> gridAdsList(Integer offset, Integer pageSize, String search, String sort, String country, String sortBy) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> userList = userRepository.findByEmail(userEmail);
        List<Role> roles = userList.get(0).getRoles();

        Page<Advertisement> page = null;
        PageRequest pageable = PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy);

        List<Favourite> favAds = favouriteRepository.findUserFavouriteAdvertisement(userList.get(0));
        List<Long> userFavAdIds = favAds.isEmpty() ? new ArrayList<>() : favAds.stream().map(fav -> fav.getAdvertisement().getId()).toList();
        if (roles.get(0).getName().matches("USER")) {
            if(!country.isEmpty()){
                page = advertisementRepository.findAllByCountry(country, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
            }else{
                page = advertisementRepository.findAll(PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
            }
        } else if (roles.get(0).getName().matches("PARTNER")) {
            if(!country.isEmpty()){
                page = advertisementRepository.findAllByCountry(country, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
            }else{
                page = advertisementRepository.findAll(PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
            }
        } else {
            if (country.isEmpty()) {
                page = advertisementRepository.findAll(PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
            } else {
                page = advertisementRepository.findAllByCountry(country, PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy));
            }
        }

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

    @Transactional
    public Long store(AdvertisementDto advertisementDto
            , MultipartFile[] adImages
    ) throws Exception {
        Advertisement advertisement = new Advertisement();
        advertisement.setName(advertisementDto.getName());
        advertisement.setData(advertisementDto.getData());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> users = userRepository.findByEmail(userEmail);
        if (!users.get(0).getCanPublish() && users.get(0).getRoles().get(0).getName().matches("PARTNER")) {
            throw new Exception("Partner cannot publish advertisements, please contact admin!");
        }
//        Optional<AdvertisementType> advertisementType = advertisementTypeRepository.findById(advertisementDto.getAdvertisementTypeId());
//        if (advertisementType.isEmpty()) {
//            throw new EntityNotFoundException("Advertisement type not found");
//        }

        Integer adsCount = advertisementRepository.countPublishedAdvertisementByUserId(users.get(0), true);
        if (users.get(0).getRoles().get(0).getName().matches("USER") && adsCount >= Integer.parseInt(userAdsLimit)) {
            throw new Exception("User type can only publish "+ userAdsLimit+" advertisements, please contact admin if you required to publish more advertisements.");
        }
        if (users.get(0).getRoles().get(0).getName().matches("PARTNER")) {
            List<Subscription> subscription = subscriptionRepository.findByUserId(users.get(0).getId());
            if (!subscription.get(0).getStatus()) {
                throw new Exception("You are now allowed to add advertisement, please contact admin for details.");
            }
            if (adsCount >= subscription.get(0).getSubscriptionType().getAdsCount()) {
                throw new Exception("Your current subscription plan advertisement quota limit exceeds, please upgrade your subscription plan or unpublish previous advertisements.");
            }
        }

        Optional<Country> country = countryRepository.findById(advertisementDto.getCountryId());
        if (country.isEmpty()) {
            throw new EntityNotFoundException("Country not found");
        }

        Optional<Province> province = provinceRepository.findById(advertisementDto.getProvinceId());
        if (province.isEmpty()) {
            throw new EntityNotFoundException("Province not found");
        }

        Optional<District> district = districtRepository.findById(advertisementDto.getDistrictId());
        district.ifPresent(advertisement::setDistrict);
        List<Long> selectedCategories = List.of(stringToLong(advertisementDto.getSelectedCategoryIds()));
        List<Category> categories = categoryRepository.findAllById(selectedCategories);
        advertisement.setCategories(categories);
        advertisement.setCompanyName(advertisementDto.getCompanyName());
        advertisement.setCountry(country.get());
        advertisement.setProvince(province.get());
        advertisement.setEmail(advertisementDto.getEmail());
        advertisement.setContactNumber(advertisementDto.getContactNumber());
        advertisement.setWebsite(advertisementDto.getWebsite());
        advertisement.setUser(users.get(0));
        advertisement.setShowEmail(advertisementDto.getShowEmail());
        advertisement.setShowWebsite(advertisementDto.getShowWebsite());
        advertisement.setShowContactNumber(advertisementDto.getShowContactNumber());
        advertisement.setPublish(false);
        advertisement.setCreatedAt(currentDateTime());
        advertisement.setUpdatedAt(currentDateTime());
        Advertisement advertisement1 =  advertisementRepository.save(advertisement);

        Random random = new Random();
        if (adImages != null) {
            if (adImages.length > 3) {
                throw new Exception("Max image(s) upload limit is 3");
            }
            for (MultipartFile image : adImages) {
                AdvertisementImage advertisementImage = new AdvertisementImage();
                if (image != null) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String original_document_name = image.getOriginalFilename();
                    Optional<String> extension = getExtensionFromFilename(original_document_name);
                    if (extension.isEmpty()) {
                        throw new Exception("File does not have extension!");
                    }
                    int randomNumber = random.nextInt(999999);
                    String document_name = timestamp.getTime() + randomNumber + "." + extension.get();
                    Path filenameAndPath = Paths.get(uploadDirectory + "/advertisements", document_name);
                    advertisementImage.setAdvertisement(advertisement);
                    advertisementImage.setDocumentName(document_name);
                    advertisementImage.setOriginalDocumentName(original_document_name);
                    advertisementImage.setCreatedAt(timestamp.toString());
                    advertisementImage.setUpdatedAt(timestamp.toString());
                    Files.write(filenameAndPath, image.getBytes());

                    advertisementImageRepository.save(advertisementImage);
                }
            }
        }

        ReceiverDto receiverDto = new ReceiverDto();
        if (users.get(0).getRoles().get(0).getName().matches("PARTNER")) {
            String url = dashboardUrl + "/advertisements";
            receiverDto.setEmail(adminEmail);
            receiverDto.setName(appName);
            receiverDto.setSubject("New Advertisement Created");
            receiverDto.setUrl(url);
            Map<String, String> data = new HashMap<>();
            data.put("partnerName", users.get(0).getName());
            data.put("partnerEmail", users.get(0).getEmail());
            data.put("message", "Advertisement is created by " + users.get(0).getName() + " partner, which requires your attention.");
            receiverDto.setData(data);

            advertisementCreatedMail.sendMail(receiverDto);
        }
        return advertisement1.getId();
    }

    public Advertisement show(Long id) {
        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        if (advertisement.isEmpty()) {
            throw new EntityNotFoundException("No advertisement found!");
        }
        return advertisement.get();
    }

    public void changePublishStatus(AdvertisementStatusDto statusDto, Long id) throws Exception {

        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        if (advertisement.isEmpty()) {
            throw new EntityNotFoundException("No advertisement found!");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> users = userRepository.findByEmail(userEmail);
        Integer adsCount = advertisementRepository.countPublishedAdvertisementByUserId(users.get(0), true);

        if (users.get(0).getRoles().get(0).getName().matches("USER") && adsCount >= Integer.parseInt(userAdsLimit) && statusDto.getStatus()) {
            throw new Exception("User type can only publish "  + userAdsLimit +" advertisements, please contact admin if you required to add more advertisements.");
        }
        if (users.get(0).getRoles().get(0).getName().matches("PARTNER")) {
            List<Subscription> subscription = subscriptionRepository.findByUserId(users.get(0).getId());
            if (!subscription.get(0).getStatus()) {
                throw new Exception("You are now allowed to add advertisement, please contact admin for details.");
            }
            if (adsCount >= subscription.get(0).getSubscriptionType().getAdsCount() && statusDto.getStatus()) {
                throw new Exception("Your current subscription plan advertisement quota limit exceeds, please upgrade your subscription plan.");
            }
        }
        if (users.get(0).getRoles().get(0).getName().matches("PARTNER") || users.get(0).getRoles().get(0).getName().matches("USER")) {
            if (Objects.equals(advertisement.get().getUser().getId(), users.get(0).getId())) {
                advertisement.get().setPublish(statusDto.getStatus());
                advertisement.get().setUpdatedAt(currentDateTime());
                advertisementRepository.save(advertisement.get());
//                if (!statusDto.getStatus()) {
//                    advertisement.get().setPublish(false);
//                    advertisement.get().setUpdatedAt(currentDateTime());
//                } else {
//                    throw new Exception(
//                            "Partner or User cannot publish advertisement, advertisement will be published only after it meets our policy requirements."
//                    );
//                }
            } else {
                throw new Exception(
                        "Cannot publish advertisement, your action is recorded!"
                );
            }
        } else {
            advertisement.get().setPublish(statusDto.getStatus());
            advertisement.get().setUpdatedAt(currentDateTime());
            advertisementRepository.save(advertisement.get());
        }
    }


    @Transactional
    public void update(AdvertisementDto advertisementDto, MultipartFile[] adImages, Long id) throws Exception {
        Long[] imageRemoveIds = stringToLong(advertisementDto.getImageRemoveIds());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> users = userRepository.findByEmail(userEmail);
        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        if (advertisement.isEmpty()) {
            throw new EntityNotFoundException("No advertisement found!");
        }
//        if (advertisement.get().getPublish() &&
//                (users.get(0).getRoles().get(0).getName().matches("PARTNER") ||
//                        users.get(0).getRoles().get(0).getName().matches("USER"))) {
//            throw new Exception("Advertisement published, cannot update!");
//        }
//        Optional<AdvertisementType> advertisementType = advertisementTypeRepository.findById(advertisementDto.getAdvertisementTypeId());
//        if (advertisementType.isEmpty()) {
//            throw new EntityNotFoundException("No advertisement type found!");
//        }

        Optional<Country> country = countryRepository.findById(advertisementDto.getCountryId());
        if (country.isEmpty()) {
            throw new EntityNotFoundException("Country not found");
        }

        Optional<Province> province = provinceRepository.findById(advertisementDto.getProvinceId());
        if (province.isEmpty()) {
            throw new EntityNotFoundException("Province not found");
        }
        if (advertisementDto.getDistrictId() != null) {
            Optional<District> district = districtRepository.findById(advertisementDto.getDistrictId());
            district.ifPresent(advertisement.get()::setDistrict);
        }
        List<Long> selectedCategories = List.of(stringToLong(advertisementDto.getSelectedCategoryIds()));
        List<Category> categories = categoryRepository.findAllById(selectedCategories);
        advertisement.get().setCategories(categories);
        advertisement.get().setName(advertisementDto.getName());
        advertisement.get().setCompanyName(advertisementDto.getCompanyName());
        advertisement.get().setCountry(country.get());
        advertisement.get().setProvince(province.get());
        advertisement.get().setEmail(advertisementDto.getEmail());
        advertisement.get().setContactNumber(advertisementDto.getContactNumber());
        advertisement.get().setWebsite(advertisementDto.getWebsite());
        advertisement.get().setData(advertisementDto.getData());
        advertisement.get().setPublish(users.get(0).getRoles().get(0).getName().matches("SUPERADMIN") || users.get(0).getRoles().get(0).getName().matches("ADMIN"));
        advertisement.get().setUpdatedAt(currentDateTime());
        advertisement.get().setShowEmail(advertisementDto.getShowEmail());
        advertisement.get().setShowWebsite(advertisementDto.getShowWebsite());
        advertisement.get().setShowContactNumber(advertisementDto.getShowContactNumber());
        advertisementRepository.save(advertisement.get());
        Random random = new Random();
        if (adImages != null) {
            if (adImages.length > 3) {
                throw new Exception("Max image(s) upload limit is 3");
            }
            for (MultipartFile image : adImages) {
                AdvertisementImage advertisementImage = new AdvertisementImage();
                if (image != null) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String original_document_name = image.getOriginalFilename();
                    Optional<String> extension = getExtensionFromFilename(original_document_name);
                    if (extension.isEmpty()) {
                        throw new Exception("File does not have extension!");
                    }
                    int randomNumber = random.nextInt(999999);
                    String document_name = timestamp.getTime() + randomNumber + "." + extension.get();
                    Path filenameAndPath = Paths.get(uploadDirectory + "/advertisements", document_name);
                    advertisementImage.setAdvertisement(advertisement.get());
                    advertisementImage.setDocumentName(document_name);
                    advertisementImage.setOriginalDocumentName(original_document_name);
                    advertisementImage.setCreatedAt(timestamp.toString());
                    advertisementImage.setUpdatedAt(timestamp.toString());
                    Files.write(filenameAndPath, image.getBytes());

                    advertisementImageRepository.save(advertisementImage);
                }
            }
        }
        for (Long imageRemoveId : imageRemoveIds) {
            if (imageRemoveId == 0) {
                break;
            }
            Optional<AdvertisementImage> advertisementImage = advertisementImageRepository.findById(imageRemoveId);
            if (advertisementImage.isPresent()) {
                Path filenameAndPath = Paths.get(uploadDirectory + "/advertisements", advertisementImage.get().getDocumentName());
                Files.delete(filenameAndPath);
                advertisementImageRepository.deleteById(imageRemoveId);
            }
        }
        ReceiverDto receiverDto = new ReceiverDto();
        if (users.get(0).getRoles().get(0).getName().matches("PARTNER")) {
            String url = dashboardUrl + "/advertisements";
            receiverDto.setEmail(adminEmail);
            receiverDto.setName(appName);
            receiverDto.setSubject("Advertisement Updated");
            receiverDto.setUrl(url);

            Map<String, String> data = new HashMap<>();
            data.put("partnerName", users.get(0).getName());
            data.put("partnerEmail", users.get(0).getEmail());
            data.put("message", "Advertisement is updated by " + users.get(0).getName() + " partner, which requires your attention.");
            receiverDto.setData(data);

            advertisementCreatedMail.sendMail(receiverDto);
        }

    }

    @Transactional
    public void delete(Long id) {

        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        if (advertisement.isEmpty()) {
            throw new EntityNotFoundException("No advertisement found!");
        }
        advertisementRepository.delete(advertisement.get());
    }

    public List<Advertisement> nextGetAdvertisements() {

        return advertisementRepository.findAllPublishedAdvertisements(true);
    }

    @Transactional
    public void setFavourite(Long id, Boolean status) throws Exception {
        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        if (advertisement.isEmpty()) {
            throw new EntityNotFoundException("No advertisement found!");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> users = userRepository.findByEmail(userEmail);
        if (users.isEmpty()) {
            throw new Exception("User not found!");
        }
        Optional<Favourite> favouriteList = favouriteRepository.findByUserIdAndAdvertisementId(advertisement.get(), users.get(0));
        if (favouriteList.isEmpty()) {
            Favourite favourite = new Favourite();
            favourite.setAdvertisement(advertisement.get());
            favourite.setUser(users.get(0));
            favouriteRepository.save(favourite);
        } else {
            favouriteList.get().setAdvertisement(advertisement.get());
            favouriteList.get().setUser(users.get(0));
            if (!status) {
                favouriteRepository.delete(favouriteList.get());
            } else {
                favouriteRepository.save(favouriteList.get());
            }
        }
    }

    public Page<Advertisement> favouriteAds(Integer offset, Integer pageSize, String search, String sort, String sortBy) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getPrincipal().toString();
        List<User> userList = userRepository.findByEmail(userEmail);
        PageRequest pageable = PageRequest.of(offset, pageSize, Sort.Direction.valueOf(sort), sortBy);
        List<Favourite> favAds = favouriteRepository.findUserFavouriteAdvertisement(userList.get(0));

        List<Long> userFavAdIds = favAds.stream().map(fav -> fav.getAdvertisement().getId()).toList();

        Page<Advertisement> page = advertisementRepository.findPublishedAdvertisements(true, pageable);

        List<Advertisement> list = page.getContent().stream().map(ads -> {
            ads.setFavourite(false);
            if (userFavAdIds.contains(ads.getId())) {
                ads.setFavourite(true);
                return ads;
            }
            return ads;
        }).toList();

        return new PageImpl<>(list, pageable, pageSize);

    }
}
