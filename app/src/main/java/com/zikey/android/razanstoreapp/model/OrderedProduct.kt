package com.zikey.android.razanstoreapp.model

import com.google.gson.annotations.SerializedName

object OrderedProduct {

    data class OrderedProduct(
        @SerializedName("p") var product: Product.Product?,
        @SerializedName("cn") var count: Double?,
        @SerializedName("cs") var cost: Double?,
        @SerializedName("fi") var fi: Long,
        @SerializedName("exp") var expireDate: String,
        @SerializedName("fid") var id: Long,
        var isSelcted: Boolean,

        )

}