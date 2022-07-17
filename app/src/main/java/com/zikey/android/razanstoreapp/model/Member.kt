package com.zikey.android.razanstoreapp.model

import com.example.distributorapp.model.answers.ServerAnswer
import com.google.gson.annotations.SerializedName

object Member{

    data class Member(
        @SerializedName("n") val name: String?,
        @SerializedName("c") val code_markaz: Long?,
        @SerializedName("p") val password: String?,
        @SerializedName("t") val token: String?,
        @SerializedName("i") val id: Long?,
        @SerializedName("d") val deviceInfo: String?,
        @SerializedName("o") val osInfo: String?,
        @SerializedName("nip") val navigationIpAddress: String?,
        @SerializedName("tl") val tel: String?,
        @SerializedName("scc") val salesCenterName: String?,
        @SerializedName("cdi") var cashDeskId: Long?,
        @SerializedName("cdn") var cashDeskName: String?,

        )

    data class MemberAnswer(
        @SerializedName("m") val member: Member
    ):ServerAnswer()

}
