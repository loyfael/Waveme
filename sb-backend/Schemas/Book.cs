namespace sb_backend.Schemas;

public class Book
{
    public required string Title { get; set; }

    public required Author Author { get; set; }
}

public class Author
{
    public string Name { get; set; } = "";

    public static implicit operator Author(string v)
    {
        throw new NotImplementedException();
    }
}
