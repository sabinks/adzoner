package com.adzoner.api.repository;

import com.adzoner.api.entity.Advertisement;
import com.adzoner.api.entity.Favourite;
import com.adzoner.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    @Query(value = "SELECT f FROM Favourite f WHERE f.advertisement = ?1 AND f.user = ?2")
    Optional<Favourite> findByUserIdAndAdvertisementId(Advertisement advertisement, User user);

    @Query(value="SELECT f FROM Favourite f WHERE f.user = ?1")
    List<Favourite> findUserFavouriteAdvertisement(User user);
}
