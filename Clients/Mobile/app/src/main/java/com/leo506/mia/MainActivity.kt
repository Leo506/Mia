package com.leo506.mia

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.leo506.mia.databinding.ActivityMainBinding
import viewModels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticlesAdapter
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModelObservers()

        setupRecyclerView()

        setupInputEndIcon()
    }

    private fun setupViewModelObservers() {
        vm.articles.observe(this) {
            adapter.articles = it
            when {
                it.isNotEmpty() -> binding.nothingText.visibility = View.GONE
                else -> binding.nothingText.visibility = View.VISIBLE
            }
        }

        vm.hasError.observe(this) {
            when {
                it -> binding.rssInputLayout.error = "Can not parse rss"
                else -> binding.rssInputLayout.error = null
            }
        }

        vm.isUpdating.observe(this) {
            when {
                it -> binding.progressIndicator.visibility = View.VISIBLE
                else -> binding.progressIndicator.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(this)
        adapter = ArticlesAdapter()

        binding.articlesList.layoutManager = manager
        binding.articlesList.adapter = adapter
    }

    private fun setupInputEndIcon() {
        binding.rssInputLayout.isEndIconVisible = false
        binding.rssInputLayout.isEndIconCheckable = true
        binding.rssInput.doAfterTextChanged {
            binding.rssInputLayout.isEndIconVisible = it?.any() == true
            vm.hasError.value = false
        }

        binding.rssInputLayout.setEndIconOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            binding.rssInput.clearFocus()
            vm.updateArticlesList(binding.rssInput.text.toString())
        }
    }
}