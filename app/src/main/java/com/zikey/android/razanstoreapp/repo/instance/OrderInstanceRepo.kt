package com.zikey.android.razanstoreapp.repo.instance

import android.content.Context
import android.text.TextUtils
import androidx.annotation.WorkerThread
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.OrderBasket
import com.zikey.android.razanstoreapp.model.OrderedProduct
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.*

class OrderInstanceRepo {

    @WorkerThread
    suspend fun getOrder(context: Context?) :Order.OrderAnswer = coroutineScope{

        val basket = OrderBasket.getInstance()
        val answer = Order.OrderAnswer(basket.order)
        answer.setIsSuccess(1)


        return@coroutineScope answer
    }



    @WorkerThread
    suspend fun saveProductToOrder(
        context: Context?,
        orderedProduct: OrderedProduct.OrderedProduct?,
        orderID: Long?,
        isReturned: Boolean
    ): Order.OrderAnswer = coroutineScope {

        val basket = OrderBasket.getInstance()
        if (orderedProduct != null) basket.setOrderedProduct(orderedProduct)

        val answer = Order.OrderAnswer(null)
        answer.setIsSuccess(1)


        return@coroutineScope answer

    }

    @WorkerThread
    suspend fun saveCustomerTell(
        context: Context?,
        customerTell: String?,

        ): Order.OrderAnswer = coroutineScope {

        val basket = OrderBasket.getInstance()

        basket.customerTell = customerTell
        val answer = Order.OrderAnswer(null)
        answer.setIsSuccess(1)

        return@coroutineScope answer

    }

    suspend fun removeProduct(
        context: Context?,
        orderedProduct: OrderedProduct.OrderedProduct?,
        orderID: UUID?
    ): Order.OrderAnswer = coroutineScope {

        val answer = Order.OrderAnswer(null)
        val basket: OrderBasket = OrderBasket.getInstance()

        basket.removeOrderedProduct(orderedProduct)
        answer.setIsSuccess(1)
        return@coroutineScope answer
    }


    suspend fun updateProduct(
        context: Context?,
        rowNum: Int,
        orderID: UUID?,
        orderedProduct: OrderedProduct.OrderedProduct?
    ) : Order.OrderAnswer = coroutineScope {
        val basket: OrderBasket = OrderBasket.getInstance()
        if (orderedProduct != null) basket.updateOrderedProduct(rowNum,orderedProduct)

        val answer = Order.OrderAnswer(null)
        answer.setIsSuccess(1)

        return@coroutineScope answer

    }


    suspend fun clearBasket(
        context: Context?,
    ): Order.OrderAnswer = coroutineScope {

        val answer = Order.OrderAnswer(null)
        val basket: OrderBasket = OrderBasket.getInstance()
        basket.clearBasket()
        answer.setIsSuccess(1)
        return@coroutineScope answer
    }

}