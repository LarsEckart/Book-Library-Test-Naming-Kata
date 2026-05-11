package org.sammancoaching.library;

import java.util.Objects;
public record Member(String memberId, String name) {
    public Member {
        memberId = requireText(memberId, "memberId");
        name = requireText(name, "name");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}
