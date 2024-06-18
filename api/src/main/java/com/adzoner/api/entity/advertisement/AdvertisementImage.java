package com.adzoner.api.entity.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.adzoner.api.entity.Advertisement;
import com.adzoner.api.entity.DocumentType;
import com.adzoner.api.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name="advertisement_images")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdvertisementImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    @JsonIgnore
    private Advertisement advertisement;

    private String documentName;

    private String originalDocumentName;

    private String createdAt;

    private String updatedAt;
}
