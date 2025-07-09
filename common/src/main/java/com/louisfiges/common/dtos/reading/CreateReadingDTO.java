package com.louisfiges.common.dtos.reading;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateReadingDTO(UUID subjectId, BigDecimal readingKwh, String providerUri) { }
