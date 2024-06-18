package com.adzoner.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String name;

    private String nepaliName;

    private String slug;

    private String createdAt;

    private String updatedAt;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "province_id")
    private Province province;

    @OneToMany(mappedBy = "district")
    @JsonIgnore
    private List<Advertisement> advertisementList;
}
