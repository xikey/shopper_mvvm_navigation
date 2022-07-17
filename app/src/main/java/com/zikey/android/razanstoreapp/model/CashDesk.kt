package com.zikey.android.razanstoreapp.model

import com.example.distributorapp.model.answers.ServerAnswer
import com.google.gson.annotations.SerializedName

object CashDesk {

    data class CashDesk(
        @SerializedName("i") val id: Long,
        @SerializedName("n") val name: String?,
    )

    data class CashDesksAnswer(
        @SerializedName("c") val cashDesks: List<CashDesk>
    ) : ServerAnswer()

    data class CashDeskAnswer(@SerializedName("c") val cashDesk: CashDesk) : ServerAnswer()

}
