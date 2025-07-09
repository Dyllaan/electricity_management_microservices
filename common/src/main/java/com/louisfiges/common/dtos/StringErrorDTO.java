package com.louisfiges.common.dtos;

import com.louisfiges.common.http.Response;

public record StringErrorDTO(String cause) implements Response {
}