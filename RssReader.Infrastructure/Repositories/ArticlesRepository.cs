using System.Xml.Linq;
using RssReader.Domain.Abstractions;
using RssReader.Domain.Models;

namespace RssReader.Infrastructure.Repositories;

internal class ArticlesRepository : IArticlesRepository
{
    public async Task<IEnumerable<Article>> GetAll(string feed)
    {
        var feedInfo = await GetFeedInfo(feed).ConfigureAwait(false);
        return new List<Article>(GetArticles(feedInfo));
    }

    private static IEnumerable<Article> GetArticles(string feedInfo)
    {
        XNamespace xNameSpace = "http://www.w3.org/2005/Atom";
        var doc = XDocument.Parse(feedInfo);
        foreach (var element in doc.Root?.Descendants(xNameSpace + "entry") ?? Enumerable.Empty<XElement>())
        {
            yield return new Article
            {
                Title = element.Element(xNameSpace + "title")?.Value ?? string.Empty,
                Author = element.Element(xNameSpace + "author")?.Element(xNameSpace + "name")?.Value ?? string.Empty,
                PublishedTime = DateTime.Parse(element.Element(xNameSpace + "published")?.Value ?? "00:00:00")
            };
        }
    }

    private static async Task<string> GetFeedInfo(string feed)
    {
        var httpClient = new HttpClient();
        var response = await httpClient.SendAsync(new HttpRequestMessage(HttpMethod.Get, feed)
        {
            Headers = { { "User-Agent", "MyRssReader" } }
        }).ConfigureAwait(false);

        return await response.Content.ReadAsStringAsync().ConfigureAwait(false);
    }
}