package com.louisfiges.common.dtos.subject;

import com.louisfiges.common.http.Response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubjectNoCustomerDTO(UUID subjectId, String sourceName, LocalDateTime subjectAdded) implements Response {
}
