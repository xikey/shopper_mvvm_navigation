package com.zikey.android.razanstoreapp.repo.api

import android.content.Context
import com.zikey.android.razanstoreapp.model.Member
import com.zikey.android.razanstoreapp.repo.api.api_urls.ICashDeskApi
import com.zikey.android.razanstoreapp.repo.api.api_urls.IMemberApi
import com.zikey.android.razanstoreapp.repo.api.tools.ServerApiClient
import io.reactivex.rxjava3.core.Single

class MemberApiService {

     fun login(context: Context, member: Member.Member): Single<Member.MemberAnswer> {
        val api = ServerApiClient.getClient(context)!!.create(IMemberApi::class.java)
        return api.loginMember(member = member)
    }
}