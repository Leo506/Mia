using System;

namespace Mia.Models;

public record Article(
    string Title,
    string Author,
    //string Description,
    DateTime PublishTime,
    string Uri
);