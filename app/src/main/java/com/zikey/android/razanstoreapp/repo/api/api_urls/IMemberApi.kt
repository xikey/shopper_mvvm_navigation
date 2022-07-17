package com.zikey.android.razanstoreapp.repo.api.api_urls

import com.zikey.android.razanstoreapp.model.Member
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface IMemberApi {

    @POST("Members/login")
    fun loginMember(@Body member: Member.Member?): Single<Member.MemberAnswer>

}