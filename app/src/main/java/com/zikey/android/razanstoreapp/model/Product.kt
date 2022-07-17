package com.zikey.android.razanstoreapp.model

import com.example.distributorapp.model.answers.ServerAnswer
import com.google.gson.annotations.SerializedName

object Product {

    data class Product(
        @SerializedName("i") var id: Long,
        @SerializedName("ck") var code: Long,
        @SerializedName("n") var name: String,
        @SerializedName("pn") var picNumber: Int,
        @SerializedName("b") var barcode: String,
        @SerializedName("p") var price1: Float,
        @SerializedName("p2") var price2: Float,
        @SerializedName("p3") var price3: Float,
        @SerializedName("p4") var price4: Float,
        @SerializedName("p5") var price5: Float,
        @SerializedName("ra") var raas: Float,
        @SerializedName("ut") var unit: String,
        @SerializedName("cp") var countPerPackage: Double,
        @SerializedName("cd") var cashDiscount: Float,
        @SerializedName("dp") var discountPercent: Double,
        @SerializedName("tx") var hasTax: Boolean,
        @SerializedName("wgt") var weight: Float,
        @SerializedName("dpc") var distributorPrice: Long,
        @SerializedName("b2") var barcode2: String,
        @SerializedName("b3") var barcode3: String,
        @SerializedName("b4") var barcode4: String,
        @SerializedName("b5") var barcode5: String,
        @SerializedName("b6") var barcode6: String,
        @SerializedName("av") var available: Double,
        @SerializedName("inpc") var inputCount: Double,
        @SerializedName("otpc") var outputCount: Double,
        @SerializedName("can") var storeRoomCode: Long,
        @SerializedName("isv") var isValid: Int,
        @SerializedName("siz") var size: Double,
        @SerializedName("isbg") var isBulkGoods: Int,
        @SerializedName("orpt") var orderPoint: Long,
        @SerializedName("ntod") var needToOrder: Long,
        @SerializedName("prdt") var kardex: ProductDetail.ProductDetails?,
        @SerializedName("shelf") var shelf: Shelf?,
        @SerializedName("fi") var fi: Long?,
        @SerializedName("dp1") var discountPrice1: Double?,
        @SerializedName("dp2") var discountPrice2: Double?,
    )

    data class ProductsAnswer(

        @SerializedName("p") var products: List<Product>?

    ) : ServerAnswer()

    data class ProductAnswer(

        @SerializedName("p") var product: Product

    ) : ServerAnswer()


}