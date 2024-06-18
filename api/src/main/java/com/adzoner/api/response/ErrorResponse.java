package com.adzoner.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    @Setter
    private Integer status;
    private String message;
    private String timeStamp;

}
