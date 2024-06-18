package com.adzoner.api.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
@Getter
@Setter
@Service
public class UserCanPublishDto {
    private Boolean status;
}
