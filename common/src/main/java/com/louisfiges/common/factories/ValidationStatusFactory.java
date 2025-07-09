package com.louisfiges.common.factories;

import com.louisfiges.common.dtos.status.ValidationStatus;

public class ValidationStatusFactory {

    public static ValidationStatus create(boolean valid, String cause) {
        return new ValidationStatus(valid, cause);
    }

    /**
     * Valid if no string is passed
     * @return a ValidationStatus object
     */
    public static ValidationStatus valid() {
        return create(true, null);
    }

    /**
     * Invalid if string is passed
     * @param cause the cause of the invalidation
     * @return a ValidationStatus object
     */
    public static ValidationStatus invalid(String cause) {
        return create(false, cause);
    }
}
