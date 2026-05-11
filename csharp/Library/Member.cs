namespace SammanCoaching.Library;
public sealed record Member
{
    public Member(string memberId, string name)
    {
        MemberId = RequireText(memberId, nameof(memberId));
        Name = RequireText(name, nameof(name));
    }
    public string MemberId { get; }
    public string Name { get; }

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
