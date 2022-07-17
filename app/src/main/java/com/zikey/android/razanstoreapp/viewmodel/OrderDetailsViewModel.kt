package com.zikey.android.razanstoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.repo.api.OrderApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderDetailsViewModel : ViewModel() {


    private val orderApiService = OrderApiService()
    private val compositeDisposable = CompositeDisposable()
    private val compositeEditDisposable = CompositeDisposable()
    private val compositeDeleteDisposable = CompositeDisposable()

    val loadData = MutableLiveData<Boolean>()
    val dataResponse = MutableLiveData<Order.OrderAnswer>()
    val dataLoadingError = MutableLiveData<Boolean>()

    val loadEditDataData     = MutableLiveData<Boolean>()
    val editDataResponse     = MutableLiveData<Order.OrderAnswer>()
    val editDataLoadingError = MutableLiveData<Boolean>()


    val loadDeleteDataData     = MutableLiveData<Boolean>()
    val deleteDataResponse     = MutableLiveData<Order.OrderAnswer>()
    val deleteDataLoadingError = MutableLiveData<Boolean>()

    fun get(context: Context, orderId: Long) {
        // Define the value of the load cash desk.
        if (compositeDisposable.size()>0)
            compositeDisposable.clear()


        loadData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDisposable.add(


            orderApiService.getOrder(context, orderId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Order.OrderAnswer>() {
                    override fun onSuccess(value: Order.OrderAnswer?) {
                        // Update the values with response in the success method.
                        loadData.value = false
                        dataResponse.value = value
                        dataLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadData.value = false
                        dataLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun edidProduct(context: Context, order: Order.OrderAnswer) {

        if (compositeEditDisposable.size()>0)
            compositeEditDisposable.clear()

        loadEditDataData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeEditDisposable.add(
            orderApiService.editProduct(context, order)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Order.OrderAnswer>() {
                    override fun onSuccess(value: Order.OrderAnswer?) {
                        // Update the values with response in the success method.
                        loadEditDataData.value = false
                        editDataResponse.value = value
                        editDataLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadEditDataData.value = false
                        editDataLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun delete(context: Context, order: Order.OrderAnswer) {

        if (compositeDeleteDisposable.size()>0)
            compositeDeleteDisposable.clear()

        loadDeleteDataData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDeleteDisposable.add(
            orderApiService.remove(context, order)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Order.OrderAnswer>() {
                    override fun onSuccess(value: Order.OrderAnswer?) {
                        // Update the values with response in the success method.
                        loadDeleteDataData.value = false
                        deleteDataResponse.value = value
                        deleteDataLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadDeleteDataData.value = false
                        deleteDataLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }



}