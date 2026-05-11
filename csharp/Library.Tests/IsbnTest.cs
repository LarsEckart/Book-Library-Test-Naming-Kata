using NUnit.Framework;
using SammanCoaching.Library;

namespace Library.Tests;

public class IsbnTest
{
    [Test]
    public void testIsbnDigits()
    {
        var value = "978-0201485677";

        var isbn = Isbn.Of(value);

        Assert.That(isbn.Digits, Is.EqualTo("9780201485677"));
    }

    [Test]
    public void testIsbnFormat()
    {
        var value = "978 0201485677";

        var isbn = Isbn.Of(value);

        Assert.That(isbn.Digits, Is.EqualTo("9780201485677"));
    }

    [Test]
    public void testIsbnEquality()
    {
        var firstValue = "978-0201485677";
        var secondValue = "9780201485677";

        var firstIsbn = Isbn.Of(firstValue);
        var secondIsbn = Isbn.Of(secondValue);

        Assert.That(firstIsbn, Is.EqualTo(secondIsbn));
    }

    [Test]
    public void testIsbnInequality()
    {
        var firstValue = "978-0201485677";
        var secondValue = "978-0134757599";

        var firstIsbn = Isbn.Of(firstValue);
        var secondIsbn = Isbn.Of(secondValue);

        Assert.That(firstIsbn, Is.Not.EqualTo(secondIsbn));
    }

    [Test]
    public void testInvalidInput()
    {
        var value = "978-0201485670";

        Action act = () => Isbn.Of(value);

        Assert.Throws<ArgumentException>(act);
    }

    [Test]
    public void testInvalidInput2()
    {
        var value = "abc";

        Action act = () => Isbn.Of(value);

        Assert.Throws<ArgumentException>(act);
    }

    [Test]
    public void badInputIsBadIguess()
    {
        var value = "978-020148567";

        Action act = () => Isbn.Of(value);

        Assert.Throws<ArgumentException>(act);
    }
}
