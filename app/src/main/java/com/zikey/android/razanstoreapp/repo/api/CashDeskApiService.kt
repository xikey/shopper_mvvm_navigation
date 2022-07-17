package com.zikey.android.razanstoreapp.repo.api

import android.content.Context
import com.zikey.android.razanstoreapp.model.CashDesk
import com.zikey.android.razanstoreapp.repo.api.api_urls.ICashDeskApi
import com.zikey.android.razanstoreapp.repo.api.tools.ServerApiClient
import io.reactivex.rxjava3.core.Single

class CashDeskApiService  {

     fun getCashDesks(context: Context, code: Long): Single<CashDesk.CashDesksAnswer> {

        val api = ServerApiClient.getClient(context)!!.create(ICashDeskApi::class.java)
        return api.getCashDesk(code)

    }
}