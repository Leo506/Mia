package viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.Article
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles = _articles.asStateFlow()

    fun refresh() = viewModelScope.launch {
        _isRefreshing.update { true }
        delay(2000)
        _articles.update {
            listOf(Article("Reddit title", "u/leo506", "https://www.reddit.com/r/programming/comments/155nvnp/what_does_a_cto_actually_do/"))
        }
        _isRefreshing.update { false }
    }
}