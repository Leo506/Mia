using System.ComponentModel.DataAnnotations;

namespace RssReader.WebApi.DTO;

public record FeedDto
{
    [Required]
    [Url]
    public string FeedUrl { get; set; } = default!;
}