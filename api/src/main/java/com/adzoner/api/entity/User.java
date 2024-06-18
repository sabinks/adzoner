package com.adzoner.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.adzoner.api.entity.user.Document;
import com.adzoner.api.entity.membership.SubscriptionType;
import com.adzoner.api.entity.membership.Subscription;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String contactNumber;

    private String name;

    @JsonIgnore
    private String password;

    private Boolean active;

    private Boolean canPublish;

    @JsonIgnore
    @Column(name = "email_verified_at")
    private String emailVerifiedAt;

    @JsonIgnore
    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "create_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @JsonIgnore
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name="profile_image")
    private String profileImage;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    @JoinTable(name = "user_subscription", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "subscription_type_id"))
    private List<SubscriptionType> subscriptionTypeList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Advertisement> advertisements;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Favourite> favouriteList;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Subscription> subscriptionList;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Document> documents;

    public void addRole(Role role){
        if(roles == null){
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    public void addSubscriptionType(SubscriptionType subscriptionType){
        if(subscriptionTypeList == null){
            subscriptionTypeList = new ArrayList<>();
        }
        subscriptionTypeList.add(subscriptionType);
    }

    public User(String email, String contactNumber, String name, String password, Boolean active, String emailVerifiedAt, String createdAt, String updatedAt) {
        this.email = email;
        this.contactNumber = contactNumber;
        this.name = name;
        this.password = password;
        this.active = active;
        this.emailVerifiedAt = emailVerifiedAt;
        this.createdAt = createdAt;
        this.updatedAt= updatedAt;
    }

    @Override
    public String toString() {
        return "User:{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", emailVerifiedAt='" + emailVerifiedAt + '\'' +
                ", verificationToken='" + verificationToken + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
