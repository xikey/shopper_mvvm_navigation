package com.zikey.android.razanstoreapp.model

import com.example.distributorapp.model.answers.ServerAnswer
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

object Order {

    data class Order(

        @SerializedName("i") var id: Long?,
        @SerializedName("uuid") var orderUUID: UUID?,
        @SerializedName("crd") var createDate: String?,
        @SerializedName("crt") var createTime: String?,
        @SerializedName("ttp") var totalPrice: Long?,
        @SerializedName("ops") var orderedProducts: ArrayList<OrderedProduct.OrderedProduct>?,
        @SerializedName("dsc") var description: String?,
        @SerializedName("otp") var orderType: String?,
        @SerializedName("ord") var orderDate: String?,
        @SerializedName("rwn") var rowNum: Long?,
        @SerializedName("ctl") var customerTell: String?,
        @SerializedName("cdi") var cashDeskId: Long?,
        @SerializedName("pays") var payments: List<Payment.Payment>?,
        @SerializedName("scn") var saleCenterName: String?,
        @SerializedName("ordnum") var orderNumber: String?,
        @SerializedName("cmpnam") var companyName: String?,
        @SerializedName("brcnam") var branchName: String?,
        @SerializedName("sctell") var saleCenterTell: String?,
        @SerializedName("scadd") var saleCenterAddress: String?,
        @SerializedName("pstc") var postalCode: String?,
        @SerializedName("cunm") var customerName: String?,
        @SerializedName("cuad") var customerAddress: String?,
        //کد اشتراک
        @SerializedName("subc") var subscriptionCode: String?,
        //کد اقتصادی
        @SerializedName("ecc") var economicCode: String?,
        @SerializedName("cds") var cashDiscount: Double?,

        )

    data class OrderAnswer(@SerializedName("o") var order: Order?) : ServerAnswer()

    data class OrdersAnswer(@SerializedName("o") var orders: List<Order>?) : ServerAnswer()

}