FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
WORKDIR /src
COPY ["RssReader.WebApi/RssReader.WebApi.csproj", "RssReader.WebApi/"]
RUN dotnet restore "RssReader.WebApi/RssReader.WebApi.csproj"
COPY . .
WORKDIR "/src/RssReader.WebApi"
RUN dotnet build "RssReader.WebApi.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "RssReader.WebApi.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "RssReader.WebApi.dll"]
