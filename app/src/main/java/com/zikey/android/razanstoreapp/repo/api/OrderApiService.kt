package com.zikey.android.razanstoreapp.repo.api

import android.content.Context
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.Payment
import com.zikey.android.razanstoreapp.model.Terminal
import com.zikey.android.razanstoreapp.repo.api.api_urls.IOrderApi
import com.zikey.android.razanstoreapp.repo.api.tools.ServerApiClient
import io.reactivex.rxjava3.core.Single

class OrderApiService {

    fun insert(context: Context, order: Order.OrderAnswer): Single<Order.OrderAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.insert(order = order)
    }

    fun insertProduct(context: Context, order: Order.OrderAnswer): Single<Order.OrderAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.insertProduct(order = order)
    }

    fun getOrders(context: Context, cashDesk: Long): Single<Order.OrdersAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.getOrders(cashDesk)
    }

    fun getOrder(context: Context, orderId: Long): Single<Order.OrderAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.getOrder(orderId)
    }

    fun editProduct(context: Context, order: Order.OrderAnswer): Single<Order.OrderAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.editProduct(order = order)
    }

    /**
     *    /// در صورتی که ردیف کالا شماره فوتر داشته باشد یک قلم حذف خواهد شد
    /// در غیر این صورت کل فاکتور خذف میشود
    /// orderedProduct.id==idFooter
     */
    fun remove(context: Context, order: Order.OrderAnswer): Single<Order.OrderAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.remove(order = order)
    }

    fun getOrderPayments(context: Context, orderId: Long): Single<Order.OrderAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.getOrderPayments(orderId)
    }

    fun getTerminals(context: Context): Single<Terminal.TerminalsWrapper> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.getTerminals()
    }

    fun submitPayment(
        context: Context,
        payment: Payment.Payment
    ): Single<Terminal.TerminalsWrapper> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.submitPayment(payment)
    }

    fun finalizeFactor(context: Context, orderID: Long): Single<Order.OrderAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.finilizeFactor(orderID)
    }

    fun deletePaymentRow(
        context: Context,
        orderID: Long,
        paymentId: Long
    ): Single<Terminal.TerminalsWrapper> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.deletePaymentRow(orderID, paymentId)
    }

    fun finalizeCashDeskCreate(
        context: Context,
        cashDeskId: Long,
        date: String,
        comment: String,
    ): Single<Order.OrderAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.finalizeCashDesk(cashDeskId, date, comment)
    }

    fun finalizeCashDesk_getPayments(
        context: Context,
        finalizedCahDeskID: Long,
    ): Single<Terminal.TerminalsWrapper> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.finalizeCashDesk_getPayments(finalizedCahDeskID)
    }

    fun finalizeCashDesk_updateAmount(
        context: Context,
        footerID: Long,
        amount: String,
    ): Single<Terminal.TerminalsWrapper> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.finalizeCashDesk_updateAmount(footerID, amount)
    }

    fun finalizeCashDesk_calculator(
        context: Context,
        cashDeskId: Long,
    ): Single<Terminal.TerminalsWrapper> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IOrderApi::class.java)
        return api.finalizeCashDesk_calculator(cashDeskId)
    }

}