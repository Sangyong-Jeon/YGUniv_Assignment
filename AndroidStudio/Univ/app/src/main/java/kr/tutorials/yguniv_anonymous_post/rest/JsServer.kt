package kr.tutorials.yguniv_anonymous_post.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JsServer {
    companion object {
        var url = "http://10.0.2.2:8080"
        private var server: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var postApi: PostApi = server.create(PostApi::class.java)

        // 서버 Url 변경
        fun renewServerUrl() {
            server = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            postApi = server.create(PostApi::class.java)
        }
    }
}