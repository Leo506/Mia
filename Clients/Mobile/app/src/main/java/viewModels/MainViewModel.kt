package viewModels

import android.util.Xml
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainViewModel: ViewModel() {
    private val _isPageLoading = MutableStateFlow(true)
    val isPageLoading = _isPageLoading.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateArticlesList()
            _isPageLoading.update { false }
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles = _articles.asStateFlow()

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _isRefreshing.update { true }
            updateArticlesList()
        }
        finally {
            _isRefreshing.update { false }
        }
    }

    private fun updateArticlesList() {
        val newArticlesList = getArticles()
        _articles.update {
            newArticlesList
        }
    }

    private fun getArticles(): List<Article> {
        getFeedStream().use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    private fun getFeedStream(): InputStream {
        val url = URL("https://www.reddit.com/r/programming/.rss")
        return (url.openConnection() as HttpURLConnection).run {
            requestMethod = "GET"
            doInput = true
            connect()
            inputStream
        }
    }

    private fun readFeed(parser: XmlPullParser): List<Article> {
        val articles = mutableListOf<Article>()

        parser.require(XmlPullParser.START_TAG, null, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG)
                continue
            if (parser.name == "entry") {
                articles.add(readArticle(parser))
            }
            else {
                skip(parser)
            }
        }

        return articles
    }

    private fun readArticle(parser: XmlPullParser): Article {
        parser.require(XmlPullParser.START_TAG, null, "entry")

        var title = ""
        var author = ""
        var url = ""

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG)
                continue
            when (parser.name) {
                "title" -> title = readTitle(parser)
                "author" -> author = readAuthor(parser)
                "link" -> url = readUrl(parser)
                else -> skip(parser)
            }
        }
        return Article(title, author, url)
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, "title")
        return title
    }

    private fun readAuthor(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, "author")
        var authorName = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG)
                continue
            when (parser.name) {
                "name" -> authorName = readText(parser)
                else -> skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, null, "author")
        return authorName
    }

    private fun readUrl(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, null, "link")
        val tag = parser.name
        if (tag == "link") {
            link = parser.getAttributeValue(null, "href")
            parser.nextTag()
        }
        parser.require(XmlPullParser.END_TAG, null, "link")
        return link
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG)
            throw java.lang.IllegalStateException()
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

}