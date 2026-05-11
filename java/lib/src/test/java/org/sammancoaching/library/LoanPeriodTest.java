package org.sammancoaching.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoanPeriodTest {

    @Test
    void nopeAgain() {
        int days = 0;

        Executable createLoanPeriod = () -> LoanPeriod.ofDays(days);

        assertThrows(IllegalArgumentException.class, createLoanPeriod);
    }

    @Test
    void nopeAgain2() {
        int days = -1;

        Executable createLoanPeriod = () -> LoanPeriod.ofDays(days);

        assertThrows(IllegalArgumentException.class, createLoanPeriod);
    }

    @Test
    void testDefaultLoanPeriod() {
        assertEquals(14, LoanPeriod.defaultPeriod().days());
    }

    @Test
    void testMaximumLoanPeriod() {
        assertEquals(30, LoanPeriod.ofDays(30).days());
    }

    @Test
    void tooMuch() {
        int days = 31;

        Executable createLoanPeriod = () -> LoanPeriod.ofDays(days);

        assertThrows(IllegalArgumentException.class, createLoanPeriod);
    }
}
