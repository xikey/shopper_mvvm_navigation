package com.zikey.android.razanstoreapp.repo.api.api_urls

import com.zikey.android.razanstoreapp.model.Member
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.Payment
import com.zikey.android.razanstoreapp.model.Terminal
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IOrderApi {

    @POST("Orders/insert")
    fun insert(@Body order: Order.OrderAnswer?): Single<Order.OrderAnswer>

    @POST("Orders/InsertProduct")
    fun insertProduct(@Body order: Order.OrderAnswer?): Single<Order.OrderAnswer>

    @POST("Orders/editProduct")
    fun editProduct(@Body order: Order.OrderAnswer?): Single<Order.OrderAnswer>

    @POST("Orders/delete")
    fun remove(@Body order: Order.OrderAnswer?): Single<Order.OrderAnswer>

    @GET("Orders/getOrders")
    fun getOrders(@Query("cashDeskId") cashDeskId: Long): Single<Order.OrdersAnswer>

    @GET("Orders/getOrder")
    fun getOrder(@Query("orderId") orderId: Long): Single<Order.OrderAnswer>

    @GET("Orders/orderPayments")
    fun getOrderPayments(@Query("orderId") orderId: Long): Single<Order.OrderAnswer>

    @GET("Orders/getTerminals")
    fun getTerminals(): Single<Terminal.TerminalsWrapper>

    @POST("Orders/submitPayment")
    fun submitPayment(@Body payment: Payment.Payment): Single<Terminal.TerminalsWrapper>

    @GET("Orders/finilizeFactor")
    fun finilizeFactor(@Query("orderId") orderId: Long): Single<Order.OrderAnswer>

    @GET("Orders/deletePaymentRow")
    fun deletePaymentRow(
        @Query("orderId") orderId: Long,
        @Query("paymentId") paymentId: Long
    ): Single<Terminal.TerminalsWrapper>

    @GET("Orders/finilizeCashDesk")
    fun finalizeCashDesk(
        @Query("cashDeskId") cashDeskId: Long,
        @Query("date") date: String,
        @Query("comment") comment: String,
    ): Single<Order.OrderAnswer>

    @GET("Orders/getFinalizedCashDeskDetails")
    fun finalizeCashDesk_getPayments(
        @Query("finalizedCahDeskID") finalizedCahDeskID: Long,
    ): Single<Terminal.TerminalsWrapper>

    @GET("Orders/finalizedCashDesk_updateAmount")
    fun finalizeCashDesk_updateAmount(
        @Query("footerID") footerID: Long,
        @Query("amount") amount: String,
    ): Single<Terminal.TerminalsWrapper>

    @GET("Orders/finalizedCashDesk_calculate")
    fun finalizeCashDesk_calculator(
        @Query("cashDeskId") cashDeskId: Long,
    ): Single<Terminal.TerminalsWrapper>

}