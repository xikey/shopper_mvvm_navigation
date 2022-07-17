package com.zikey.android.razanstoreapp.repo.api.tools

import android.content.Context
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import com.zikey.android.razanstoreapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ServerApiClient {

    companion object {
        private var retrofitWithHeader: Retrofit? = null
        private var retrofitWithoutHeader: Retrofit? = null


        private fun initHeader(context: Context): OkHttpClient.Builder? {
            val httpClient = OkHttpClient.Builder()

            val token: String? = ""
            val session: SessionManagement = SessionManagement.getInstance(context)!!


            httpClient.addInterceptor { chain ->
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
                    .header("xAthorize", session.getToken())
                    .method(original.method(), original.body())
                    .build()
                val response: Response = chain.proceed(request)
                response
            }


            httpClient.connectTimeout(30, TimeUnit.SECONDS);
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            return httpClient
        }


        fun getClient(context: Context?): Retrofit? {
            return try {

                if (retrofitWithoutHeader == null) {
                    retrofitWithoutHeader = Retrofit.Builder()
                        .baseUrl(
                            BuildConfig.IP
                        )
                        .addConverterFactory(GsonConverterFactory.create())
                        /**
                         * **
                         * Add a call adapter factory for supporting service method return types other than.
                         *
                         * A CallAdapter.Factory call adapter which uses RxJava 3 for creating observables.
                         *
                         * Adding this class to Retrofit allows you to return an Observable, Flowable, Single, Completable
                         * or Maybe from service methods.
                         */
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .build()
                }
                retrofitWithoutHeader
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


        fun getClientWithHeader(context: Context?): Retrofit? {
            if (retrofitWithHeader == null) {
                retrofitWithHeader = Retrofit.Builder()
                    .baseUrl(
                        BuildConfig.IP
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    /**
                     * **
                     * Add a call adapter factory for supporting service method return types other than.
                     *
                     * A CallAdapter.Factory call adapter which uses RxJava 3 for creating observables.
                     *
                     * Adding this class to Retrofit allows you to return an Observable, Flowable, Single, Completable
                     * or Maybe from service methods.
                     */
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(initHeader(context!!)!!.build())
                    .build()
            }
            return retrofitWithHeader
        }


        fun clearRetrofit() {
            retrofitWithHeader = null
            retrofitWithoutHeader = null
        }

    }
}