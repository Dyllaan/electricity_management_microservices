package com.louisfiges.common.dtos.customer;

import com.louisfiges.common.dtos.subject.SubjectNoCustomerDTO;
import com.louisfiges.common.http.Response;

import java.time.LocalDateTime;
import java.util.Set;

public record CustomerDTO(Long customerId, String firstName, String lastName, LocalDateTime customerCreated, Set<SubjectNoCustomerDTO> subjects) implements Response {
}
