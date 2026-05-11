package org.sammancoaching.library;

import java.util.Objects;
public final class Isbn {
    private final String digits;

    private Isbn(String digits) {
        this.digits = digits;
    }
    public static Isbn of(String value) {
        Objects.requireNonNull(value, "value");

        String digits = value.replace("-", "").replace(" ", "");
        if (!digits.matches("\\d{13}")) {
            throw new IllegalArgumentException("ISBN must contain 13 digits");
        }

        if (!hasValidChecksum(digits)) {
            throw new IllegalArgumentException("ISBN has an invalid checksum");
        }

        return new Isbn(digits);
    }
    public String digits() {
        return digits;
    }

    private static boolean hasValidChecksum(String digits) {
        int sum = 0;
        for (int index = 0; index < 12; index++) {
            int digit = Character.digit(digits.charAt(index), 10);
            sum += index % 2 == 0 ? digit : digit * 3;
        }

        int expectedCheckDigit = (10 - (sum % 10)) % 10;
        int actualCheckDigit = Character.digit(digits.charAt(12), 10);
        return expectedCheckDigit == actualCheckDigit;
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Isbn isbn)) {
            return false;
        }
        return digits.equals(isbn.digits);
    }

    @Override
    public int hashCode() {
        return digits.hashCode();
    }
    @Override
    public String toString() {
        return digits;
    }
}
