using Microsoft.Extensions.DependencyInjection;
using RssReader.Domain.Abstractions;
using RssReader.Infrastructure.Repositories;

namespace RssReader.Infrastructure.DI;

public static class InfrastructureExtensions
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection collection) =>
        collection.AddTransient<IArticlesRepository, ArticlesRepository>();
}