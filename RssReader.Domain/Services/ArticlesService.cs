using RssReader.Domain.Abstractions;
using RssReader.Domain.Models;

namespace RssReader.Domain.Services;

internal class ArticlesService : IArticlesService
{
    private readonly IArticlesRepository _articlesRepository;

    public ArticlesService(IArticlesRepository articlesRepository) => _articlesRepository = articlesRepository;

    public Task<IEnumerable<Article>> GetArticles(string feed) => _articlesRepository.GetAll(feed);
}