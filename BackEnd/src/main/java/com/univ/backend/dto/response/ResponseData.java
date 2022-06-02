package com.univ.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseData<T> {
    private Header header;
    private T body;

    public ResponseData(Header header, T body) {
        this.header = header;
        this.body = body;
    }
}
