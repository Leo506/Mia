using System;
using System.Diagnostics;

namespace Mia.Models;

public record Article(
    string Title,
    string Author,
    //string Description,
    DateTime PublishTime,
    string Uri
)
{
    public void OpenArticle()
    {
        Process.Start(new ProcessStartInfo(Uri) { UseShellExecute = true });
    }
}