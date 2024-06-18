package com.adzoner.api.entity.membership;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.adzoner.api.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name="subscription_type_id")
//    @JsonIgnore
    private SubscriptionType subscriptionType;

    @Transient
    private Integer remainingAdsCount;

    private Boolean status;

    private String startedAt;
    private String createdAt;

    private String expiresAt;

}
