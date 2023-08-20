package com.leo506.mia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.leo506.mia.databinding.ArticleLayoutBinding

class ArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {


    class ArticlesViewHolder(val bindings: ArticleLayoutBinding) : RecyclerView.ViewHolder(bindings.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ArticleLayoutBinding.inflate(inflater, parent, false)

        return ArticlesViewHolder(binding);
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
    };
}