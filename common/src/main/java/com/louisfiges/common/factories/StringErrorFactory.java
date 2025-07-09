package com.louisfiges.common.factories;

import com.louisfiges.common.dtos.StringErrorDTO;

public class StringErrorFactory {

    public static StringErrorDTO create(String cause) {
        return new StringErrorDTO(cause);
    }
}
