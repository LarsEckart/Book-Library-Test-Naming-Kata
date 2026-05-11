using System.Text.RegularExpressions;

namespace SammanCoaching.Library;
public sealed record Isbn
{
    private Isbn(string digits)
    {
        Digits = digits;
    }
    public string Digits { get; }
    public static Isbn Of(string value)
    {
        ArgumentNullException.ThrowIfNull(value);

        var digits = value.Replace("-", "").Replace(" ", "");
        if (!Regex.IsMatch(digits, "^\\d{13}$"))
        {
            throw new ArgumentException("ISBN must contain 13 digits", nameof(value));
        }

        if (!HasValidChecksum(digits))
        {
            throw new ArgumentException("ISBN has an invalid checksum", nameof(value));
        }

        return new Isbn(digits);
    }
    public override string ToString() => Digits;

    private static bool HasValidChecksum(string digits)
    {
        var sum = 0;
        for (var index = 0; index < 12; index++)
        {
            var digit = digits[index] - '0';
            sum += index % 2 == 0 ? digit : digit * 3;
        }

        var expectedCheckDigit = (10 - sum % 10) % 10;
        var actualCheckDigit = digits[12] - '0';
        return expectedCheckDigit == actualCheckDigit;
    }
}
