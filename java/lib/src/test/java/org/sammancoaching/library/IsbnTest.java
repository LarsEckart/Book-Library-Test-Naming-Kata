package org.sammancoaching.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IsbnTest {

    @Test
    void testIsbnDigits() {
        Isbn isbn = Isbn.of("978-0201485677");

        assertEquals("9780201485677", isbn.digits());
    }

    @Test
    void testIsbnFormat() {
        Isbn isbn = Isbn.of("978 0201485677");

        assertEquals("9780201485677", isbn.digits());
    }

    @Test
    void testIsbnEquality() {
        assertEquals(Isbn.of("978-0201485677"), Isbn.of("9780201485677"));
    }

    @Test
    void testIsbnInequality() {
        assertNotEquals(Isbn.of("978-0201485677"), Isbn.of("978-0134757599"));
    }

    @Test
    void testInvalidInput() {
        Executable parseIsbnWithInvalidChecksum = () -> Isbn.of("978-0201485670");

        assertThrows(IllegalArgumentException.class, parseIsbnWithInvalidChecksum);
    }

    @Test
    void testInvalidInput2() {
        Executable parseNonNumericIsbn = () -> Isbn.of("abc");

        assertThrows(IllegalArgumentException.class, parseNonNumericIsbn);
    }

    @Test
    void badInputIsBadIguess() {
        Executable parseShortIsbn = () -> Isbn.of("978-020148567");

        assertThrows(IllegalArgumentException.class, parseShortIsbn);
    }
}
