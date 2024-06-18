package com.adzoner.api.repository;

import com.adzoner.api.entity.*;
import com.adzoner.api.entity.advertisement.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    @Query(value = "SELECT COUNT(*) FROM Advertisement a where a.user = ?1 and a.publish = ?2")
    Integer countPublishedAdvertisementByUserId(User user, Boolean publish);
    List<Advertisement> findByUserIdAndPublish(Long userId, Boolean publish);

    Page<Advertisement> findByUserIdAndPublish(Long userId, Boolean publish, PageRequest of);
    @Query(value = "SELECT a FROM Advertisement a WHERE a.publish = ?1")
    List<Advertisement> findAllPublishedAdvertisements(Boolean publish);

    @Query(value = "SELECT a FROM Advertisement a where a.publish = ?2  and (a.name like %?1% or a.companyName like %?1% or a.data like %?1%)")
    Page<Advertisement> searchTableForKey(String search, Boolean publish, Pageable pageable);


    @Query(value = "SELECT a FROM Advertisement a join a.province p where p.name = ?1 and a.publish = ?2")
    Page<Advertisement> searchByCountryId(String countryName, Boolean publish,  Pageable pageable);


    @Query(value = "SELECT a FROM Advertisement a JOIN a.categories c WHERE a.province = ?1 and c.id IN (?2) and a.publish = ?4 and (a.name like %?3% or a.companyName like %?3% or a.data like %?3%)")
    Page<Advertisement> nextSearchByProvinceAndCategoriesAndSearch(Province province, List<Long> catIds, String search, Boolean publish, Pageable pageable);

    @Query(value = "SELECT a FROM Advertisement a JOIN a.categories c WHERE a.country = ?1 and c.id IN (?2) and a.publish = ?4 and (a.name like %?3% or a.companyName like %?3% or a.data like %?3%)")
    Page<Advertisement> nextSearchByCountryAndCategoriesAndSearch(Country country, List<Long> catIds, String search, Boolean publish, PageRequest of);

    @Query(value = "SELECT a FROM Advertisement a WHERE a.province = ?1 and a.publish = ?3 and (a.name like %?2% or a.companyName like %?2% or a.data like %?2%)")
    Page<Advertisement> nextSearchByProvinceAndSearch(Province province, String search, Boolean publish, PageRequest of);

    @Query(value = "SELECT a FROM Advertisement a WHERE a.country = ?1 and a.publish = ?3 and (a.name like %?2% or a.companyName like %?2% or a.data like %?2%)")
    Page<Advertisement> nextSearchByCountryAndSearch(Country country, String search, Boolean b, PageRequest of);

    @Query(value = "SELECT a FROM Advertisement a JOIN a.categories c WHERE a.province = ?1 and c.id IN (?2) and a.publish = ?3")
    Page<Advertisement> nextSearchByProvinceAndCategories(Province province, List<Long> catIds, Boolean publish,  Pageable pageable);
    @Query(value = "SELECT a FROM Advertisement a JOIN a.categories c WHERE a.country = ?1 and c.id IN  (?2) and a.publish = ?3")
    Page<Advertisement> nextSearchByCountryAndCategories(Country country, List<Long> categories, Boolean publish,  Pageable pageable);

    @Query(value = "SELECT a FROM Advertisement a WHERE a.province = ?1 and a.publish = ?2")
    Page<Advertisement> nextSearchByProvince(Province province, Boolean publish, PageRequest pageRequest);

    @Query(value = "SELECT a FROM Advertisement a WHERE a.country = ?1 and a.publish = ?2")
    Page<Advertisement> nextSearchByCountry(Country country, Boolean publish, PageRequest pageRequest);

    @Query(value = "SELECT a FROM Advertisement a where a.publish = ?1")
    Page<Advertisement> findAllAdvertisements(Boolean publish, PageRequest of);

    @Query(value="SELECT a FROM Advertisement a where a.publish = ?1")
    Page<Advertisement> findPublishedAdvertisements(Boolean publish, PageRequest of);

    @Query(value="SELECT a FROM Advertisement a where a.user = ?1")
    Page<Advertisement> findAllByUser(User user, PageRequest of);

    @Query(value = "SELECT a FROM Advertisement a join a.country c where c.name = ?1")
    Page<Advertisement> findAllByCountry(String country, PageRequest of);
}
