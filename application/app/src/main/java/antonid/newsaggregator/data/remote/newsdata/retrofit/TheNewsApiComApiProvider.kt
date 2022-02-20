package antonid.newsaggregator.data.remote.newsdata.retrofit

import antonid.newsaggregator.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TheNewsApiComApiProvider {

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val url = originalRequest.url().newBuilder()
            .addQueryParameter("api_token", BuildConfig.THE_NEWS_API_COM_API_KEY)
            .build()

        chain.proceed(originalRequest.newBuilder().url(url).build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        .create()

    val api: TheNewsApiComApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.THE_NEWS_API_COM_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TheNewsApiComApi::class.java)
}