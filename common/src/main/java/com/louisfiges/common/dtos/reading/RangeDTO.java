package com.louisfiges.common.dtos.reading;

import com.louisfiges.common.http.Response;

import java.time.LocalDateTime;
import java.util.List;

public record RangeDTO(LocalDateTime reportStart, LocalDateTime reportEnd, List<ReadingWithSourceDTO> readings) implements Response {
}
