package antonid.newsaggregator.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import antonid.newsaggregator.data.local.room.entity.ArticleRoomEntity

fun getDatabase(context: Context): NewsAggregatorDatabase =
    Room.databaseBuilder(
        context,
        NewsAggregatorDatabase::class.java, "NewsAggregatorDatabase"
    )
        .fallbackToDestructiveMigration()
        .build()

@Database(
    entities = [
        ArticleRoomEntity::class,
    ], version = 2
)
abstract class NewsAggregatorDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao

}