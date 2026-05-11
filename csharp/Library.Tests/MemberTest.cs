using NUnit.Framework;
using SammanCoaching.Library;

namespace Library.Tests;

public class MemberTest
{
    [Test]
    public void testMemberNoId()
    {
        var memberId = " ";
        var name = "Alice";

        Action act = () => new Member(memberId, name);

        Assert.Throws<ArgumentException>(act);
    }

    [Test]
    public void testMemberNoName()
    {
        var memberId = "MEMBER-001";
        var name = " ";

        Action act = () => new Member(memberId, name);

        Assert.Throws<ArgumentException>(act);
    }
}
