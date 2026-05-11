namespace SammanCoaching.Library;
public static class LibraryDemo
{
    public static void Main(string[] args)
    {
        var alice = new Member("MEMBER-001", "Alice");
        var bob = new Member("MEMBER-002", "Bob");

        var book = Book.Available(
            "Refactoring",
            Isbn.Of("978-0201485677"),
            "BOOK-001");

        var borrowedBook = book.Borrow(alice);

        Console.WriteLine($"Borrowed by: {borrowedBook.BorrowedBy!.Name}");
        Console.WriteLine($"Due date: {borrowedBook.DueDate}");

        try
        {
            borrowedBook.Borrow(bob);
        }
        catch (InvalidOperationException exception)
        {
            Console.WriteLine($"Bob cannot borrow it: {exception.Message}");
        }
    }
}
