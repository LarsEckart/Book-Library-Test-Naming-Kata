using NUnit.Framework;
using SammanCoaching.Library;

namespace Library.Tests;

public class LoanPeriodTest
{
    [Test]
    public void nopeAgain()
    {
        var days = 0;

        Action act = () => LoanPeriod.OfDays(days);

        Assert.Throws<ArgumentException>(act);
    }

    [Test]
    public void nopeAgain2()
    {
        var days = -1;

        Action act = () => LoanPeriod.OfDays(days);

        Assert.Throws<ArgumentException>(act);
    }

    [Test]
    public void testDefaultLoanPeriod()
    {
        var expectedDays = 14;

        var loanPeriod = LoanPeriod.DefaultPeriod();

        Assert.That(loanPeriod.Days, Is.EqualTo(expectedDays));
    }

    [Test]
    public void testMaximumLoanPeriod()
    {
        var days = 30;

        var loanPeriod = LoanPeriod.OfDays(days);

        Assert.That(loanPeriod.Days, Is.EqualTo(days));
    }

    [Test]
    public void tooMuch()
    {
        var days = 31;

        Action act = () => LoanPeriod.OfDays(days);

        Assert.Throws<ArgumentException>(act);
    }
}
