using sb_backend.Schemas;

public class Query
{
    public string Hello() => "Hello, world!";
    public Book GetBook() =>
        new Book
        {
            Title = "C# in depth.",
            Author = new Author
            {
                Name = "Jon Skeet"
            }
        };
}