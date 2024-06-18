package com.adzoner.api.controller;

import com.adzoner.api.dto.advertisement.AdvertisementDto;
import com.adzoner.api.dto.advertisement.AdvertisementStatusDto;
import com.adzoner.api.dto.advertisement.FavouriteDto;
import com.adzoner.api.entity.Advertisement;
import com.adzoner.api.service.AdvertisementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class AdvertisementController {

    @Autowired
    AdvertisementService advertisementService;

    @GetMapping("/advertisements")
    public Page<Advertisement> index(@RequestParam(defaultValue = "0") Integer offset,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "DESC") String sort,
                                     @RequestParam(defaultValue = "Australia") String country,
                                     @RequestParam(defaultValue = "createdAt") String sortBy) throws Exception {
        return advertisementService.index(offset, pageSize, search, sort, country, sortBy);
    }
    @GetMapping("/advertisements-all")
    public Page<Advertisement> listAll(@RequestParam(defaultValue = "0") Integer offset,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "DESC") String sort,
                                       @RequestParam(defaultValue = "") String country,
                                     @RequestParam(defaultValue = "createdAt") String sortBy) throws Exception {
        return advertisementService.gridAdsList(offset, pageSize, search, sort, country, sortBy);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','PARTNER','USER')")
    @PostMapping("/advertisements")
    public ResponseEntity<Map<String, String >> store(@Valid @ModelAttribute AdvertisementDto advertisementDto,
                                       @RequestParam(value = "adImages", required = false) MultipartFile[] adImages
    ) throws Exception {
        Long id = advertisementService.store(advertisementDto, adImages);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Advertisement Saved!");
        response.put("id", id.toString());

        return new ResponseEntity<>( response, HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @GetMapping("/advertisements/{id}")
    public ResponseEntity<Advertisement> show(@PathVariable Long id) throws IOException {
        Advertisement advertisement = advertisementService.show(id);

        return new ResponseEntity<>(advertisement, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @PatchMapping("/advertisements/{id}")
    public ResponseEntity<String> update(@Valid @ModelAttribute AdvertisementDto advertisementDto,
                                         @RequestParam(value = "adImages", required = false) MultipartFile[] adImages,
                                         @PathVariable Long id) throws Exception {
        advertisementService.update(advertisementDto, adImages, id);

        return new ResponseEntity<>("Advertisement updated!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @DeleteMapping("/advertisements/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws IOException {
        advertisementService.delete(id);

        return new ResponseEntity<>("Advertisement deleted!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @PostMapping("/advertisements/{id}/publish")
    public ResponseEntity<String> changePublishStatus(@RequestBody AdvertisementStatusDto statusDto, @PathVariable Long id)
            throws Exception {
        advertisementService.changePublishStatus(statusDto, id);

        return new ResponseEntity<>("Advertisement publish status changed!", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @GetMapping("/favourite-advertisements")
    Page<Advertisement> favouriteAds(@RequestParam(defaultValue = "0") Integer offset,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "DESC") String sort,
                                     @RequestParam(defaultValue = "createdAt") String sortBy) {
        return advertisementService.favouriteAds(offset, pageSize, search, sort, sortBy);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'PARTNER', 'USER')")
    @PostMapping("/advertisement/{id}/favourite")
    ResponseEntity<String> setFavouriteAdvertisement(@PathVariable Long id, @Valid @RequestBody FavouriteDto favouriteDto) throws Exception {
        advertisementService.setFavourite(id, favouriteDto.getStatus());

        return new ResponseEntity<>("Favourite saved!", HttpStatus.NO_CONTENT);
    }
}
