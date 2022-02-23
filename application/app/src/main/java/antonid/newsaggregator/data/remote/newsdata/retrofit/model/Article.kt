package antonid.newsaggregator.data.remote.newsdata.retrofit.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Article(
    val title: String,
    val url: String,
    @SerializedName("image_url")
    val imageUrl: String?,
    val source: String,
    @SerializedName("published_at")
    val publishedAt: Date,
)