using RssReader.Domain.Models;

namespace RssReader.Domain.Abstractions;

public interface IArticlesService
{
    Task<IEnumerable<Article>> GetArticles(string feed);    
}