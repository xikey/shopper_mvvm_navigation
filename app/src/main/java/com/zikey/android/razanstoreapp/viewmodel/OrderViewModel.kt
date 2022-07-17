package com.zikey.android.razanstoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.OrderedProduct
import com.zikey.android.razanstoreapp.model.Product
import com.zikey.android.razanstoreapp.repo.api.OrderApiService
import com.zikey.android.razanstoreapp.repo.api.ProductApiService
import com.zikey.android.razanstoreapp.repo.instance.OrderInstanceRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.*

class OrderViewModel() : ViewModel() {


    private val instanceRepo: OrderInstanceRepo = OrderInstanceRepo()

    private val orderApiService = OrderApiService()
    private val compositeDisposable = CompositeDisposable()

    val loadData = MutableLiveData<Boolean>()
    val dataResponse = MutableLiveData<Order.OrderAnswer>()
    val dataLoadingError = MutableLiveData<Boolean>()


    val loadInsertProductData = MutableLiveData<Boolean>()
    val dataInsertProductResponse = MutableLiveData<Order.OrderAnswer>()
    val dataInsertProductLoadingError = MutableLiveData<Boolean>()

    fun insertOrderApi(context: Context, order: Order.OrderAnswer) {

        loadData.value = true
        if (compositeDisposable.size()>0)
            compositeDisposable.clear()
        compositeDisposable.add(
            // Call
            orderApiService.insert(context, order)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Order.OrderAnswer>() {
                    override fun onSuccess(value: Order.OrderAnswer?) {
                        loadData.value = false
                        dataResponse.value = value
                        dataLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadData.value = false
                        dataLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun insertProductToOrderApi(context: Context, order: Order.OrderAnswer) {
        // Define the value of the load cash desk.
        loadInsertProductData.value = true
        if (compositeDisposable.size()>0)
            compositeDisposable.clear()



        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDisposable.add(
            // Call the RandomDish method of RandomDishApiService class.
            orderApiService.insertProduct(context, order)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Order.OrderAnswer>() {
                    override fun onSuccess(value: Order.OrderAnswer?) {
                        // Update the values with response in the success method.
                        loadInsertProductData.value = false
                        dataInsertProductResponse.value = value
                        dataInsertProductLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadInsertProductData.value = false
                        dataInsertProductLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }



    fun insertProductToBasket(
        context: Context?,
        orderedProduct: OrderedProduct.OrderedProduct?,
        orderID: Long?,
        isReturned: Boolean
    ) = viewModelScope.launch {
        instanceRepo.saveProductToOrder(
            context,
            orderedProduct,
            orderID,
            isReturned
        )
    }

    fun insertCustomerTell(
        context: Context?,
        customerTell: String?,
    ) = viewModelScope.launch {
        instanceRepo.saveCustomerTell(
            context,
            customerTell
        )
    }

    fun clearBasket(
        context: Context?,
    ) = viewModelScope.launch {
        instanceRepo.clearBasket(
            context
        )
    }


    fun updateProduct(
        context: Context?,
        rowNum: Int,
        orderID: UUID?,
        orderedProduct: OrderedProduct.OrderedProduct?
    ) = viewModelScope.launch {
        instanceRepo.updateProduct(context, rowNum, orderID, orderedProduct)
    }

    fun removeProduct(
        context: Context?,
        orderedProduct: OrderedProduct.OrderedProduct?,
        orderID: UUID?
    ) = viewModelScope.launch {
        instanceRepo.removeProduct(context, orderedProduct, orderID)
    }

    suspend fun getOrder(
        context: Context?
    ): Order.OrderAnswer = viewModelScope.run { instanceRepo.getOrder(context) }


    fun clearDataObservers() {
        loadData.value=null
        dataResponse.value=null
        dataLoadingError.value=null

        loadInsertProductData.value=null
        dataInsertProductResponse.value=null
        dataInsertProductLoadingError.value=null
    }


}