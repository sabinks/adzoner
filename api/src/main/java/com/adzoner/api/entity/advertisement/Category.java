package com.adzoner.api.entity.advertisement;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.adzoner.api.entity.Advertisement;
import com.adzoner.api.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String slug;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    @JoinTable(name = "advertisement_category", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "advertisement_id"))
    private List<Advertisement> advertisements;
}
