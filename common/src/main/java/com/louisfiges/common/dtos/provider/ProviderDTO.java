package com.louisfiges.common.dtos.provider;

import com.louisfiges.common.http.Response;

public record ProviderDTO(String providerId, String providerName) implements Response {
}
