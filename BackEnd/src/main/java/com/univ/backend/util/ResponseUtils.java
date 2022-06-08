package com.univ.backend.util;

import com.univ.backend.dto.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtils {

    public ResponseEntity<ResponseData<?>> createResponseEntity(ResponseData<?> responseData) {
        int statusCode = responseData.getHeader().getStatus();
        HttpStatus status;
        switch (statusCode) {
            case 400:
                status = HttpStatus.BAD_REQUEST;
                break;
            case 401:
                status = HttpStatus.UNAUTHORIZED;
                break;
            case 404:
                status = HttpStatus.NOT_FOUND;
                break;
            case 500:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                status = HttpStatus.OK;
                break;
        }
        System.out.println("status = " + status);
        return new ResponseEntity<>(responseData, status);
    }
}
