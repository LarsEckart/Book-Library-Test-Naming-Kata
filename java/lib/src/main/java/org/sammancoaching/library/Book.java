package org.sammancoaching.library;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
public final class Book {
    private final String title;
    private final Isbn isbn;
    private final String copyId;
    private final Member borrowedBy;
    private final Member reservedBy;
    private final LocalDate borrowedOn;
    private final LocalDate dueDate;

    private Book(
            String title,
            Isbn isbn,
            String copyId,
            Member borrowedBy,
            Member reservedBy,
            LocalDate borrowedOn,
            LocalDate dueDate
    ) {
        this.title = requireText(title, "title");
        this.isbn = Objects.requireNonNull(isbn, "isbn");
        this.copyId = requireText(copyId, "copyId");
        this.borrowedBy = borrowedBy;
        this.reservedBy = reservedBy;
        this.borrowedOn = borrowedOn;
        this.dueDate = dueDate;
    }
    public static Book available(String title, Isbn isbn, String copyId) {
        return availableCopy(title, isbn, copyId, null);
    }

    private static Book availableCopy(String title, Isbn isbn, String copyId, Member reservedBy) {
        return new Book(title, isbn, copyId, null, reservedBy, null, null);
    }
    public Book reservedBy(Member member) {
        return new Book(title, isbn, copyId, borrowedBy, Objects.requireNonNull(member, "member"), borrowedOn, dueDate);
    }
    public Book borrow(Member member) {
        return borrow(member, LoanPeriod.defaultPeriod());
    }
    public Book borrow(Member member, LoanPeriod loanPeriod) {
        Objects.requireNonNull(member, "member");
        Objects.requireNonNull(loanPeriod, "loanPeriod");
        if (isBorrowed()) {
            throw new IllegalStateException("Book is already borrowed");
        }

        if (reservedBy != null && !reservedBy.equals(member)) {
            throw new IllegalStateException("Book is reserved by another reader");
        }

        LocalDate today = LocalDate.now();
        return new Book(title, isbn, copyId, member, null, today, today.plusDays(loanPeriod.days()));
    }
    public Book returnBook(Member member) {
        if (!isBorrowed()) {
            throw new IllegalStateException("Book is not borrowed");
        }

        if (!borrowedBy.equals(Objects.requireNonNull(member, "member"))) {
            throw new IllegalStateException("Book is borrowed by another reader");
        }

        return availableCopy(title, isbn, copyId, reservedBy);
    }
    public Book renew(Member member, LoanPeriod loanPeriod) {
        Objects.requireNonNull(loanPeriod, "loanPeriod");
        if (!isBorrowed()) {
            throw new IllegalStateException("Book is not borrowed");
        }

        if (!borrowedBy.equals(Objects.requireNonNull(member, "member"))) {
            throw new IllegalStateException("Book is borrowed by another reader");
        }

        if (reservedBy != null && !reservedBy.equals(member)) {
            throw new IllegalStateException("Book is reserved by another reader");
        }

        LocalDate renewedDueDate = dueDate.plusDays(loanPeriod.days());
        if (renewedDueDate.isAfter(borrowedOn.plusDays(LoanPeriod.MAX_DAYS))) {
            throw new IllegalStateException("Book cannot be borrowed longer than " + LoanPeriod.MAX_DAYS + " days");
        }

        return new Book(title, isbn, copyId, borrowedBy, reservedBy, borrowedOn, renewedDueDate);
    }
    public String title() {
        return title;
    }
    public Isbn isbn() {
        return isbn;
    }
    public String copyId() {
        return copyId;
    }
    public boolean isBorrowed() {
        return borrowedBy != null;
    }
    public Optional<Member> borrowedBy() {
        return Optional.ofNullable(borrowedBy);
    }
    public Optional<Member> reservedBy() {
        return Optional.ofNullable(reservedBy);
    }
    public LocalDate dueDate() {
        return dueDate;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}
