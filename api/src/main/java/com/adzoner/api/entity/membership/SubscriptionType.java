package com.adzoner.api.entity.membership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubscriptionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer adsCount;


    @OneToMany(mappedBy = "subscriptionType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Subscription> subscriptionList;
}
