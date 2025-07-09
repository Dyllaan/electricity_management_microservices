package com.louisfiges.common.dtos.status;

import com.louisfiges.common.http.Response;

/**
 * Represents the status of a validation
 */
public record ValidationStatus(boolean valid, String cause) implements Response { }