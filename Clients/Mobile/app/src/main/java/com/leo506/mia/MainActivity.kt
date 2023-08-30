package com.leo506.mia

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.leo506.mia.databinding.ActivityMainBinding
import utils.RssFeedParser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticlesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nothingText.visibility = View.VISIBLE

        val manager = LinearLayoutManager(this)
        adapter = ArticlesAdapter()

        binding.articlesList.layoutManager = manager
        binding.articlesList.adapter = adapter

        binding.rssInputLayout.isEndIconVisible = false
        binding.rssInputLayout.isEndIconCheckable = true
        binding.rssInput.doAfterTextChanged {
            binding.rssInputLayout.isEndIconVisible = it?.any() == true
            binding.rssInputLayout.error = null
        }

        binding.rssInputLayout.setEndIconOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            binding.rssInput.clearFocus()
            val t = Thread {
                try {
                    val articles = RssFeedParser.parseRssFeed(binding.rssInput.text.toString())
                    runOnUiThread {
                        adapter.articles = articles
                        when {
                            articles.isNotEmpty() -> binding.nothingText.visibility = View.GONE
                            else -> binding.nothingText.visibility = View.VISIBLE
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        binding.rssInputLayout.error = "Can not parse rss"
                    }
                }
            }
            t.start()
        }
    }
}