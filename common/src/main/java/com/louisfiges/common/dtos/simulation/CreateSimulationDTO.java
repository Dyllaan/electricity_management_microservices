package com.louisfiges.common.dtos.simulation;

import java.time.LocalDateTime;

public record CreateSimulationDTO(LocalDateTime startDate, LocalDateTime endDate) {
}
