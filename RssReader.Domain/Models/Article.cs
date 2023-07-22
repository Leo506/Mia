namespace RssReader.Domain.Models;

public record Article
{
    public string Title { get; set; } = default!;

    public string Author { get; set; } = default!;

    public DateTime PublishedTime { get; set; }

    public string Link { get; set; } = default!;
}