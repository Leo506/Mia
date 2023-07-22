using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using RssReader.Domain.Abstractions;
using RssReader.WebApi.DTO;

namespace RssReader.WebApi.Controllers;

[ApiController]
[Route("[controller]")]
public class RssController : ControllerBase
{
    private readonly IArticlesService _articlesService;

    public RssController(IArticlesService articlesService) => _articlesService = articlesService;

    [HttpPost("articles")]
    public async Task<IActionResult> GetArticles([FromBody] FeedDto feedDto)
    {
        var articles = await _articlesService.GetArticles(feedDto.FeedUrl).ConfigureAwait(false);
        return Ok(articles);
    }
}