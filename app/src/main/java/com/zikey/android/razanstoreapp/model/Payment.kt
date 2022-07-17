package com.zikey.android.razanstoreapp.model

import com.google.gson.annotations.SerializedName

object Payment {

    data class Payment(

        @SerializedName("id") val id: Long = 0,
        @SerializedName("p") val price: Long = 0,
        @SerializedName("c") val control: Long = 0,
        @SerializedName("ti") val terminalId: Long = 0,
        @SerializedName("tt") val terminalType: String? = null,
        @SerializedName("tn") val terminalName: String? = null,
        @SerializedName("b") val bank: String? = null,
        @SerializedName("an") val accountNumber: String? = null,

        )


}