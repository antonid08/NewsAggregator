package antonid.newsaggregator.data.remote.newsapi.retrofit

import antonid.newsaggregator.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NewsApiProvider {

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .addHeader("x-api-key", BuildConfig.NEWSAPI_ORG_API_KEY)
            .build()

        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        .create()

    val api: NewsApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.NEWSAPI_ORG_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NewsApi::class.java)
}