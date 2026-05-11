using NUnit.Framework;
using SammanCoaching.Library;

namespace Library.Tests;

public class BookTest
{
    [Test]
    public void testSuccessfulBorrow()
    {
        var book = AvailableCopy();
        var reader = Alice();
        var expectedDueDate = DateOnly.FromDateTime(DateTime.Today).AddDays(14);

        var borrowedBook = book.Borrow(reader);

        Assert.That(borrowedBook.IsBorrowed(), Is.True);
        Assert.That(borrowedBook.BorrowedBy, Is.EqualTo(reader));
        Assert.That(borrowedBook.DueDate, Is.EqualTo(expectedDueDate));
    }

    [Test]
    public void testFailBorrow()
    {
        var book = BorrowedCopyFor(Alice(), LoanPeriod.OfDays(14));
        var reader = Bob();
        var loanPeriod = LoanPeriod.OfDays(14);

        Action act = () => book.Borrow(reader, loanPeriod);

        Assert.Throws<InvalidOperationException>(act);
    }

    [Test]
    public void testSuccessfulBorrow2()
    {
        var reader = Alice();
        var book = AvailableCopy().ReservedByMember(reader);

        var borrowedBook = book.Borrow(reader, LoanPeriod.OfDays(10));

        Assert.That(borrowedBook.BorrowedBy, Is.EqualTo(reader));
        Assert.That(borrowedBook.ReservedBy, Is.Null);
    }

    [Test]
    public void testFailBorrow2()
    {
        var book = AvailableCopy().ReservedByMember(Alice());
        var reader = Bob();
        var loanPeriod = LoanPeriod.OfDays(10);

        Action act = () => book.Borrow(reader, loanPeriod);

        Assert.Throws<InvalidOperationException>(act);
    }

    [Test]
    public void returnBookWorksCorrectly()
    {
        var reader = Alice();
        var book = BorrowedCopyFor(reader, LoanPeriod.OfDays(14));

        var returnedBook = book.ReturnBook(reader);

        Assert.That(returnedBook.IsBorrowed(), Is.False);
        Assert.That(returnedBook.BorrowedBy, Is.Null);
        Assert.That(returnedBook.DueDate, Is.Null);
    }

    [Test]
    public void returnStuffNope()
    {
        var book = BorrowedCopyFor(Alice(), LoanPeriod.OfDays(14));
        var reader = Bob();

        Action act = () => book.ReturnBook(reader);

        Assert.Throws<InvalidOperationException>(act);
    }

    [Test]
    public void notBorrowedSadPath()
    {
        var book = AvailableCopy();
        var reader = Alice();

        Action act = () => book.ReturnBook(reader);

        Assert.Throws<InvalidOperationException>(act);
    }

    [Test]
    public void test1()
    {
        var reader = Alice();
        var book = BorrowedCopyFor(reader, LoanPeriod.OfDays(14));
        var originalDueDate = book.DueDate;

        var renewedBook = book.Renew(reader, LoanPeriod.OfDays(7));

        Assert.That(renewedBook.DueDate, Is.EqualTo(originalDueDate!.Value.AddDays(7)));
    }

    [Test]
    public void renewingAReservedBookExtendsTheDueDateAndKeepsTheReservation()
    {
        var book = BorrowedCopyFor(Alice(), LoanPeriod.OfDays(14))
            .ReservedByMember(Bob());
        var reader = Alice();
        var renewalPeriod = LoanPeriod.OfDays(7);

        Action act = () => book.Renew(reader, renewalPeriod);

        Assert.Throws<InvalidOperationException>(act);
    }

    [Test]
    public void renewalBadThings()
    {
        var book = BorrowedCopyFor(Alice(), LoanPeriod.OfDays(14));
        var reader = Bob();
        var renewalPeriod = LoanPeriod.OfDays(7);

        Action act = () => book.Renew(reader, renewalPeriod);

        Assert.Throws<InvalidOperationException>(act);
    }

    [Test]
    public void moreRenewalBadThings()
    {
        var book = AvailableCopy();
        var reader = Alice();
        var renewalPeriod = LoanPeriod.OfDays(7);

        Action act = () => book.Renew(reader, renewalPeriod);

        Assert.Throws<InvalidOperationException>(act);
    }

    [Test]
    public void memberThings()
    {
        var reader = Bob();

        var book = AvailableCopy().ReservedByMember(reader);

        Assert.That(book.ReservedBy, Is.EqualTo(reader));
    }

    [Test]
    public void testInvalidRenewal()
    {
        var reader = Alice();
        var book = BorrowedCopyFor(reader, LoanPeriod.OfDays(20));
        var renewalPeriod = LoanPeriod.OfDays(11);

        Action act = () => book.Renew(reader, renewalPeriod);

        Assert.Throws<InvalidOperationException>(act);
    }

    [Test]
    public void thirtyDaysIsFineOrWhatever()
    {
        var reader = Alice();
        var book = BorrowedCopyFor(reader, LoanPeriod.OfDays(20));
        var expectedDueDate = DateOnly.FromDateTime(DateTime.Today).AddDays(30);

        var renewedBook = book.Renew(reader, LoanPeriod.OfDays(10));

        Assert.That(renewedBook.DueDate, Is.EqualTo(expectedDueDate));
    }

    [Test]
    public void testInvalidBookTitle()
    {
        var title = " ";
        var isbn = Isbn.Of("978-0201485677");
        var copyId = "BOOK-001";

        Action act = () => Book.Available(title, isbn, copyId);

        Assert.Throws<ArgumentException>(act);
    }

    [Test]
    public void testInvalidBookCopyId()
    {
        var title = "Refactoring";
        var isbn = Isbn.Of("978-0201485677");
        var copyId = " ";

        Action act = () => Book.Available(title, isbn, copyId);

        Assert.Throws<ArgumentException>(act);
    }

    private static Book AvailableCopy() => Book.Available("Refactoring", Isbn.Of("978-0201485677"), "BOOK-001");

    private static Book BorrowedCopyFor(Member member, LoanPeriod loanPeriod) => AvailableCopy().Borrow(member, loanPeriod);

    private static Member Alice() => new("MEMBER-001", "Alice");

    private static Member Bob() => new("MEMBER-002", "Bob");
}
