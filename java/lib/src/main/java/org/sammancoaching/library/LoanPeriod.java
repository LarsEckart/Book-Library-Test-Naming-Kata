package org.sammancoaching.library;
public record LoanPeriod(int days) {
    public static final int DEFAULT_DAYS = 14;
    public static final int MAX_DAYS = 30;
    public LoanPeriod {
        if (days < 1) {
            throw new IllegalArgumentException("Loan period must be at least one day");
        }

        if (days > MAX_DAYS) {
            throw new IllegalArgumentException("Loan period must not be longer than " + MAX_DAYS + " days");
        }
    }
    public static LoanPeriod defaultPeriod() {
        return new LoanPeriod(DEFAULT_DAYS);
    }
    public static LoanPeriod ofDays(int days) {
        return new LoanPeriod(days);
    }
}
