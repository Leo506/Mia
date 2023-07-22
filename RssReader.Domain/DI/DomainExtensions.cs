using Microsoft.Extensions.DependencyInjection;
using RssReader.Domain.Abstractions;
using RssReader.Domain.Services;

namespace RssReader.Domain.DI;

public static class DomainExtensions
{
    public static IServiceCollection AddDomain(this IServiceCollection collection) =>
        collection.AddTransient<IArticlesService, ArticlesService>();
}