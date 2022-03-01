package antonid.newsaggregator.ui.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import antonid.newsaggregator.databinding.ArticleItemBinding
import antonid.newsaggregator.domain.model.Article
import com.bumptech.glide.Glide

class ArticlesRecyclerAdapter : RecyclerView.Adapter<ArticlesRecyclerAdapter.ArticleViewHolder>() {

    private val articles = mutableListOf<Article>()

    fun getArticles(): List<Article> = articles.toList()

    fun addArticles(articles: List<Article>) {
        this.articles.addAll(articles)
        notifyItemRangeInserted(this.articles.size - articles.size, articles.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ArticleItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    class ArticleViewHolder(
        private val binding: ArticleItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val circularProgressDrawable = CircularProgressDrawable(itemView.context).apply {
            strokeWidth = 8f
            centerRadius = 40f
            start()
        }


        fun bind(article: Article) {
            Glide.with(binding.root)
                .load(article.imageUrl)
                .placeholder(circularProgressDrawable)
                .into(binding.image)
            binding.title.text = article.title
            binding.source.text = article.source
        }

    }
}