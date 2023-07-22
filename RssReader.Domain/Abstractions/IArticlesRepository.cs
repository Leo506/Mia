using RssReader.Domain.Models;

namespace RssReader.Domain.Abstractions;

public interface IArticlesRepository
{
    Task<IEnumerable<Article>> GetAll(string feed);
}