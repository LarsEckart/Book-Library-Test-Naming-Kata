package org.sammancoaching.library;
public final class LibraryDemo {
    private LibraryDemo() {
    }
    public static void main(String[] args) {
        Member alice = new Member("MEMBER-001", "Alice");
        Member bob = new Member("MEMBER-002", "Bob");

        Book book = Book.available(
                "Refactoring",
                Isbn.of("978-0201485677"),
                "BOOK-001"
        );

        Book borrowedBook = book.borrow(alice);

        System.out.println("Borrowed by: " + borrowedBook.borrowedBy().orElseThrow().name());
        System.out.println("Due date: " + borrowedBook.dueDate());

        try {
            borrowedBook.borrow(bob);
        } catch (IllegalStateException exception) {
            System.out.println("Bob cannot borrow it: " + exception.getMessage());
        }
    }
}
