package viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.RssFeedParser

class MainViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    val hasError = MutableLiveData<Boolean>()

    init {
        _articles.value = emptyList()
        hasError.value = false
    }

    fun updateArticlesList(feedUrl: String) {
        _articles.value = emptyList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val updatedArticlesList = RssFeedParser.parseRssFeed(feedUrl)
                    _articles.postValue(updatedArticlesList)
                } catch (e: Exception) {
                    Log.e("MIA", e.message.toString())
                    hasError.postValue(true)
                }
            }
        }
    }
}