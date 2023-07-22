namespace RssReader.Domain.Models;

public class Article
{
    public string Title { get; set; } = default!;

    public string Author { get; set; } = default!;

    public DateTime PublishedTime { get; set; } = default!;
}