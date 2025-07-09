package com.louisfiges.smartcity.dtos;

import java.math.BigDecimal;

public record AggregatedDataDTO(String providerName, int totalReadings, BigDecimal totalKwh, double averageKwh) {
}
