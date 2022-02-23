package antonid.newsaggregator.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArticleRoomEntity(
    @PrimaryKey
    val title: String,
    val imageUrl: String?,
    val source: String,
    val publishTimestamp: Long,
)
