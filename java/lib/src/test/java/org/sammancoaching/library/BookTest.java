package org.sammancoaching.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookTest {

    @Test
    void testSuccessfulBorrow() {
        Book book = availableCopy();
        Member reader = alice();
        LocalDate expectedDueDate = LocalDate.now().plusDays(14);

        Book borrowedBook = book.borrow(reader);

        assertTrue(borrowedBook.isBorrowed());
        assertEquals(Optional.of(reader), borrowedBook.borrowedBy());
        assertEquals(expectedDueDate, borrowedBook.dueDate());
    }

    @Test
    void testFailBorrow() {
        Book book = borrowedCopyFor(alice(), LoanPeriod.ofDays(14));

        Executable borrowByAnotherReader = () -> book.borrow(bob(), LoanPeriod.ofDays(14));

        assertThrows(IllegalStateException.class, borrowByAnotherReader);
    }

    @Test
    void testSuccessfulBorrow2() {
        Member reader = alice();
        Book book = availableCopy().reservedBy(reader);

        Book borrowedBook = book.borrow(reader, LoanPeriod.ofDays(10));

        assertEquals(Optional.of(reader), borrowedBook.borrowedBy());
        assertEquals(Optional.empty(), borrowedBook.reservedBy());
    }

    @Test
    void testFailBorrow2() {
        Book book = availableCopy().reservedBy(alice());

        Executable borrowReservedBook = () -> book.borrow(bob(), LoanPeriod.ofDays(10));

        assertThrows(IllegalStateException.class, borrowReservedBook);
    }

    @Test
    void returnBookWorksCorrectly() {
        Member reader = alice();
        Book book = borrowedCopyFor(reader, LoanPeriod.ofDays(14));

        Book returnedBook = book.returnBook(reader);

        assertFalse(returnedBook.isBorrowed());
        assertEquals(Optional.empty(), returnedBook.borrowedBy());
        assertNull(returnedBook.dueDate());
    }

    @Test
    void returnStuffNope() {
        Book book = borrowedCopyFor(alice(), LoanPeriod.ofDays(14));

        Executable returnBookByAnotherReader = () -> book.returnBook(bob());

        assertThrows(IllegalStateException.class, returnBookByAnotherReader);
    }

    @Test
    void notBorrowedSadPath() {
        Book book = availableCopy();

        Executable returnAvailableBook = () -> book.returnBook(alice());

        assertThrows(IllegalStateException.class, returnAvailableBook);
    }

    @Test
    void test1() {
        Member reader = alice();
        Book book = borrowedCopyFor(reader, LoanPeriod.ofDays(14));
        LocalDate originalDueDate = book.dueDate();

        Book renewedBook = book.renew(reader, LoanPeriod.ofDays(7));

        assertEquals(originalDueDate.plusDays(7), renewedBook.dueDate());
    }

    @Test
    void renewingAReservedBookExtendsTheDueDateAndKeepsTheReservation() {
        Book book = borrowedCopyFor(alice(), LoanPeriod.ofDays(14))
                .reservedBy(bob());

        Executable renewReservedBook = () -> book.renew(alice(), LoanPeriod.ofDays(7));

        assertThrows(IllegalStateException.class, renewReservedBook);
    }

    @Test
    void renewalBadThings() {
        Book book = borrowedCopyFor(alice(), LoanPeriod.ofDays(14));

        Executable renewBookByAnotherReader = () -> book.renew(bob(), LoanPeriod.ofDays(7));

        assertThrows(IllegalStateException.class, renewBookByAnotherReader);
    }

    @Test
    void moreRenewalBadThings() {
        Book book = availableCopy();

        Executable renewAvailableBook = () -> book.renew(alice(), LoanPeriod.ofDays(7));

        assertThrows(IllegalStateException.class, renewAvailableBook);
    }

    @Test
    void memberThings() {
        Member reader = bob();

        Book book = availableCopy().reservedBy(reader);

        assertEquals(Optional.of(reader), book.reservedBy());
    }

    @Test
    void testInvalidRenewal() {
        Member reader = alice();
        Book book = borrowedCopyFor(reader, LoanPeriod.ofDays(20));

        Executable renewPastMaximumLoanPeriod = () -> book.renew(reader, LoanPeriod.ofDays(11));

        assertThrows(IllegalStateException.class, renewPastMaximumLoanPeriod);
    }

    @Test
    void thirtyDaysIsFineOrWhatever() {
        Member reader = alice();
        Book book = borrowedCopyFor(reader, LoanPeriod.ofDays(20));

        Book renewedBook = book.renew(reader, LoanPeriod.ofDays(10));

        assertEquals(LocalDate.now().plusDays(30), renewedBook.dueDate());
    }

    @Test
    void testInvalidBookTitle() {
        Executable createBookWithoutTitle = () -> Book.available(" ", Isbn.of("978-0201485677"), "BOOK-001");

        assertThrows(IllegalArgumentException.class, createBookWithoutTitle);
    }

    @Test
    void testInvalidBookCopyId() {
        Executable createBookWithoutCopyId = () -> Book.available("Refactoring", Isbn.of("978-0201485677"), " ");

        assertThrows(IllegalArgumentException.class, createBookWithoutCopyId);
    }

    private static Book availableCopy() {
        return Book.available("Refactoring", Isbn.of("978-0201485677"), "BOOK-001");
    }

    private static Book borrowedCopyFor(Member member, LoanPeriod loanPeriod) {
        return availableCopy().borrow(member, loanPeriod);
    }

    private static Member alice() {
        return new Member("MEMBER-001", "Alice");
    }

    private static Member bob() {
        return new Member("MEMBER-002", "Bob");
    }
}
