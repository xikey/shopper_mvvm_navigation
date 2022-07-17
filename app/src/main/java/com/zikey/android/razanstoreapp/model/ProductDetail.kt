package com.zikey.android.razanstoreapp.model

import com.google.gson.annotations.SerializedName

object ProductDetail {

    data class ProductDetails(
        @SerializedName("dt") var date: String,
        @SerializedName("srh") var shomare_resid_havale: Long,
        @SerializedName("tj") var type_joz: String,
        @SerializedName("ca") var code_anbar: Long,
        @SerializedName("na") var name_anbar: String,
        @SerializedName("sm") var shomare_mostanad: String,
        @SerializedName("cth") var code_taraf_hesab: String,
        @SerializedName("st") var sharh_tafzili: String,
        @SerializedName("st2") var sharh_tafzili2: String,
        @SerializedName("nm") var name_markaz: String,
        @SerializedName("nm2") var name_markaz2: String,
        @SerializedName("t") var tozihat: String,

        )

}