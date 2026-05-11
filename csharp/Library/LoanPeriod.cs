namespace SammanCoaching.Library;
public sealed record LoanPeriod
{
    public const int DefaultDays = 14;
    public const int MaxDays = 30;
    public LoanPeriod(int days)
    {
        if (days < 1)
        {
            throw new ArgumentException("Loan period must be at least one day", nameof(days));
        }

        if (days > MaxDays)
        {
            throw new ArgumentException($"Loan period must not be longer than {MaxDays} days", nameof(days));
        }

        Days = days;
    }
    public int Days { get; }
    public static LoanPeriod DefaultPeriod() => new(DefaultDays);
    public static LoanPeriod OfDays(int days) => new(days);
}
