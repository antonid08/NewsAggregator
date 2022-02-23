package antonid.newsaggregator.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import antonid.newsaggregator.data.local.room.entity.ArticleRoomEntity

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM ArticleRoomEntity ORDER BY publishTimestamp DESC LIMIT :count")
    suspend fun getLatest(count: Int): List<ArticleRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithReplace(articles: List<ArticleRoomEntity>)

    @Query("DELETE FROM ArticleRoomEntity")
    suspend fun removeAll()

}