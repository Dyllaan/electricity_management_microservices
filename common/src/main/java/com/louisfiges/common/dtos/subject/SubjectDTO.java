package com.louisfiges.common.dtos.subject;

import com.louisfiges.common.http.Response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubjectDTO(UUID subjectId, String sourceName, LocalDateTime subjectAdded, Long customerId) implements Response {
}
