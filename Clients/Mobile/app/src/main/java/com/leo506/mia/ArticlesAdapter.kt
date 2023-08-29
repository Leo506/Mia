package com.leo506.mia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leo506.mia.databinding.ArticleLayoutBinding
import domain.Article

class ArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {
    class ArticlesViewHolder(val bindings: ArticleLayoutBinding) : RecyclerView.ViewHolder(bindings.root)

    var articles: List<Article> = emptyList()
    set(newValue) {
        field = newValue
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ArticleLayoutBinding.inflate(inflater, parent, false)

        return ArticlesViewHolder(binding)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val article = articles[position]

        with(holder.bindings) {
            title.text = article.Title
            subtitle.text = article.Author
        }
    }
}