package com.louisfiges.common.dtos.source;

import com.louisfiges.common.http.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SourceDTO(String source) implements Response {
}
