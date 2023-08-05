using Avalonia;
using Avalonia.Controls;
using Avalonia.Markup.Xaml;

namespace Mia.Views;

public partial class SplashScreen : Window
{
    public SplashScreen()
    {
        InitializeComponent();
#if DEBUG
        this.AttachDevTools();
#endif
    }

    private void InitializeComponent()
    {
        AvaloniaXamlLoader.Load(this);
    }
}