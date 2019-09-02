package io.pavelshackih.giphy.data.network

import io.pavelshackih.giphy.BuildConfig
import io.pavelshackih.giphy.model.data.network.GiphyResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0
    ): GiphyResponse

    companion object {

        private const val ENABLE_LOGGING = false
        private const val API_KEY_PARAM = "api_key"
        private const val BASE_URL = "https://API.giphy.com/v1/gifs/"

        val API: GiphyApi by lazy {
            val clientBuilder = OkHttpClient.Builder()

            if (ENABLE_LOGGING && BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(interceptor)
            }

            clientBuilder.addInterceptor { chain ->
                var request = chain.request()
                val url = request.url
                    .newBuilder()
                    .addQueryParameter(API_KEY_PARAM, BuildConfig.GIPHY_KEY)
                    .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .baseUrl(BASE_URL)
                .build()

            retrofit.create(GiphyApi::class.java)
        }
    }
}