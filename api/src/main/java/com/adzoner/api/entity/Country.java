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
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String name;

    private String slug;

    @OneToMany(mappedBy = "country")
    @JsonIgnore
    private List<Province> provinceList;

    private String createdAt;

    private String updatedAt;
}
