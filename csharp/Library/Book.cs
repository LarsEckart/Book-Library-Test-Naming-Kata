namespace SammanCoaching.Library;
public sealed class Book
{
    private Book(
        string title,
        Isbn isbn,
        string copyId,
        Member? borrowedBy,
        Member? reservedBy,
        DateOnly? borrowedOn,
        DateOnly? dueDate)
    {
        Title = RequireText(title, nameof(title));
        Isbn = isbn ?? throw new ArgumentNullException(nameof(isbn));
        CopyId = RequireText(copyId, nameof(copyId));
        BorrowedBy = borrowedBy;
        ReservedBy = reservedBy;
        BorrowedOn = borrowedOn;
        DueDate = dueDate;
    }
    public string Title { get; }
    public Isbn Isbn { get; }
    public string CopyId { get; }
    public Member? BorrowedBy { get; }
    public Member? ReservedBy { get; }
    public DateOnly? DueDate { get; }

    private DateOnly? BorrowedOn { get; }
    public static Book Available(string title, Isbn isbn, string copyId) =>
        AvailableCopy(title, isbn, copyId, null);
    public Book ReservedByMember(Member member) =>
        new(Title, Isbn, CopyId, BorrowedBy, member ?? throw new ArgumentNullException(nameof(member)), BorrowedOn, DueDate);
    public Book Borrow(Member member) => Borrow(member, LoanPeriod.DefaultPeriod());
    public Book Borrow(Member member, LoanPeriod loanPeriod)
    {
        ArgumentNullException.ThrowIfNull(member);
        ArgumentNullException.ThrowIfNull(loanPeriod);

        if (IsBorrowed())
        {
            throw new InvalidOperationException("Book is already borrowed");
        }

        if (ReservedBy is not null && ReservedBy != member)
        {
            throw new InvalidOperationException("Book is reserved by another reader");
        }

        var today = DateOnly.FromDateTime(DateTime.Today);
        return new Book(Title, Isbn, CopyId, member, null, today, today.AddDays(loanPeriod.Days));
    }
    public Book ReturnBook(Member member)
    {
        if (!IsBorrowed())
        {
            throw new InvalidOperationException("Book is not borrowed");
        }

        if (BorrowedBy != (member ?? throw new ArgumentNullException(nameof(member))))
        {
            throw new InvalidOperationException("Book is borrowed by another reader");
        }

        return AvailableCopy(Title, Isbn, CopyId, ReservedBy);
    }
    public Book Renew(Member member, LoanPeriod loanPeriod)
    {
        ArgumentNullException.ThrowIfNull(loanPeriod);

        if (!IsBorrowed())
        {
            throw new InvalidOperationException("Book is not borrowed");
        }

        if (BorrowedBy != (member ?? throw new ArgumentNullException(nameof(member))))
        {
            throw new InvalidOperationException("Book is borrowed by another reader");
        }

        if (ReservedBy is not null && ReservedBy != member)
        {
            throw new InvalidOperationException("Book is reserved by another reader");
        }

        var renewedDueDate = DueDate!.Value.AddDays(loanPeriod.Days);
        if (renewedDueDate > BorrowedOn!.Value.AddDays(LoanPeriod.MaxDays))
        {
            throw new InvalidOperationException($"Book cannot be borrowed longer than {LoanPeriod.MaxDays} days");
        }

        return new Book(Title, Isbn, CopyId, BorrowedBy, ReservedBy, BorrowedOn, renewedDueDate);
    }
    public bool IsBorrowed() => BorrowedBy is not null;

    private static Book AvailableCopy(string title, Isbn isbn, string copyId, Member? reservedBy) =>
        new(title, isbn, copyId, null, reservedBy, null, null);

    private static string RequireText(string value, string fieldName)
    {
        ArgumentNullException.ThrowIfNull(value, fieldName);
        if (string.IsNullOrWhiteSpace(value))
        {
            throw new ArgumentException($"{fieldName} must not be blank", fieldName);
        }

        return value;
    }
}
