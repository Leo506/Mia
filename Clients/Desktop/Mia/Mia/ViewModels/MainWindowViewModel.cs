using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Xml.Linq;
using Mia.Models;
using ReactiveUI;

namespace Mia.ViewModels;

public class MainWindowViewModel : ViewModelBase
{
    private string _rssUrl = string.Empty;
    public string RssUrl
    {
        get => _rssUrl;
        set => this.RaiseAndSetIfChanged(ref _rssUrl, value);
    }

    public ObservableCollection<Article> Articles { get; private set; } = new();

    public async Task UpdateArticles()
    {
        if (string.IsNullOrWhiteSpace(_rssUrl))
            return;
        
        var feedInfo = await GetFeedInfo().ConfigureAwait(false);

        Articles = new ObservableCollection<Article>(GetArticles(feedInfo));
        this.RaisePropertyChanged(nameof(Articles));
    }

    private async Task<string> GetFeedInfo()
    {
        var httpClient = new HttpClient();
        var response = await httpClient.SendAsync(new HttpRequestMessage(HttpMethod.Get, RssUrl)
        {
            Headers = { { "User-Agent", "Mia" } }
        }).ConfigureAwait(false);

        var feedInfo = await response.Content.ReadAsStringAsync().ConfigureAwait(false);
        return feedInfo;
    }

    private static IEnumerable<Article> GetArticles(string feedInfo)
    {
        XNamespace xNameSpace = "http://www.w3.org/2005/Atom";
        var doc = XDocument.Parse(feedInfo);
        foreach (var xElement in doc.Root?.Descendants(xNameSpace+"entry") ?? Enumerable.Empty<XElement>())
        {
            yield return new Article(
                xElement.Element(xNameSpace + "title")?.Value ?? string.Empty,
                xElement.Element(xNameSpace + "author")?.Element(xNameSpace + "name")?.Value ?? string.Empty,
                DateTime.Parse(xElement.Element(xNameSpace + "published")?.Value ?? "00:00:00"),
                xElement.Element(xNameSpace + "link")?.Attribute("href")?.Value ?? string.Empty);
        }
    }
}