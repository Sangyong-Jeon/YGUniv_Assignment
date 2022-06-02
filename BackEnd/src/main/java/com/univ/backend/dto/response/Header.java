package com.univ.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Header {
    private int status;
    private String code;
    private String message;

    public Header(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
