package com.adzoner.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.adzoner.api.entity.advertisement.AdvertisementImage;
import com.adzoner.api.entity.advertisement.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String companyName;

    @Column(name="data",columnDefinition="LONGTEXT")
    private String data;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "advertisement_category", joinColumns = @JoinColumn(name = "advertisement_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @ManyToOne
    @JoinColumn(name="country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name="province_id")
    private Province province;

    @ManyToOne
    @JoinColumn(name="district_id")
    private District district;

    private String email;
    private Boolean showEmail;

    private String contactNumber;
    private Boolean showContactNumber;

    private String website;
    private Boolean showWebsite;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Favourite> favouriteList;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.REMOVE)
    private List<AdvertisementImage> advertisementImages;

    @Transient
    private Boolean favourite;

    @Transient
    private String image_remove_ids;

    private Boolean publish;

    private String createdAt;

    private String updatedAt;
}
