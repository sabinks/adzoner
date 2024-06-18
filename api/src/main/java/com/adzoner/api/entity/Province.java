package com.adzoner.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String name;

    private String nepaliName;

    private String slug;

    private String createdAt;

    private String updatedAt;

    @OneToMany(mappedBy = "province")
    @JsonIgnore
    private List<District> districtList;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonIgnore
    private Country country;

    @OneToMany(mappedBy = "province")
    @JsonIgnore
    private List<Advertisement> advertisementList;
}
