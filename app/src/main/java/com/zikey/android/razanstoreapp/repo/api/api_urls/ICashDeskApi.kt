package com.zikey.android.razanstoreapp.repo.api.api_urls

import com.zikey.android.razanstoreapp.model.CashDesk
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICashDeskApi {

    @GET("Members/userCashDesks")
    fun getCashDesk(@Query("user") code: Long): Single<CashDesk.CashDesksAnswer>

}