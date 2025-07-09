package com.louisfiges.common.dtos.subject;

import com.louisfiges.common.http.Response;

import java.util.UUID;

public record DeletedSubjectDTO(UUID subjectId) implements Response {
}
