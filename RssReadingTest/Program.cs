using System.Xml.Linq;

const string url = "https://www.reddit.com/r/programming/.rss";
var httpClient = new HttpClient();
var response = await httpClient.SendAsync(new HttpRequestMessage(HttpMethod.Get, url)
{
    Headers = { { "User-Agent", "MyRssReader" } }
});

XNamespace xNameSpace = "http://www.w3.org/2005/Atom";
var doc = XDocument.Parse(await response.Content.ReadAsStringAsync());
foreach (var element in doc.Root?.Descendants(xNameSpace + "entry") ?? Enumerable.Empty<XElement>())
{
    Console.WriteLine(element.Element(xNameSpace + "title")?.Value);
    Console.WriteLine();
}