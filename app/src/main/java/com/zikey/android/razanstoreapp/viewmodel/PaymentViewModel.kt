package com.zikey.android.razanstoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.Payment
import com.zikey.android.razanstoreapp.model.Terminal
import com.zikey.android.razanstoreapp.repo.api.OrderApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class PaymentViewModel : ViewModel() {

    private val orderApiService = OrderApiService()

    private val compositeDisposable = CompositeDisposable()
    private val terminalCompositeDisposable = CompositeDisposable()
    private val finalizeFactorCompositeDisposable = CompositeDisposable()
    private val factorCompositeDisposable = CompositeDisposable()
    private val deletePaymentRowCompositeDisposable = CompositeDisposable()


    val loadData = MutableLiveData<Boolean>()
    val dataResponse = MutableLiveData<Order.OrderAnswer>()
    val dataLoadingError = MutableLiveData<Boolean>()

    val loadPaymentTypeData = MutableLiveData<Boolean>()
    val paymentTypeResponse = MutableLiveData<Terminal.TerminalsWrapper>()
    val paymentTypeLoadingError = MutableLiveData<Boolean>()

    val loadSubmitPaymentData = MutableLiveData<Boolean>()
    val submitPaymentResponse = MutableLiveData<Terminal.TerminalsWrapper>()
    val submitPaymentLoadingError = MutableLiveData<Boolean>()

    val loadFinalizeFactorData = MutableLiveData<Boolean>()
    val finalizeFactorResponse = MutableLiveData<Order.OrderAnswer>()
    val finalizeFactorLoadingError = MutableLiveData<Boolean>()

    val loadFactorData = MutableLiveData<Boolean>()
    val factorResponse = MutableLiveData<Order.OrderAnswer>()
    val factorLoadingError = MutableLiveData<Boolean>()

    val deletePaymentRowData = MutableLiveData<Boolean>()
    val deletePaymentRowResponse = MutableLiveData<Terminal.TerminalsWrapper>()
    val deletePaymentRowLoadingError = MutableLiveData<Boolean>()


    fun get(context: Context, orderId: Long) {
        // Define the value of the load cash desk.
        if (compositeDisposable.size() > 0)
            compositeDisposable.clear()

        loadData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDisposable.add(

            orderApiService.getOrderPayments(context, orderId)
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

    fun getTerminals(context: Context) {
        // Define the value of the load cash desk.
        if (terminalCompositeDisposable.size() > 0)
            terminalCompositeDisposable.clear()

        loadPaymentTypeData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        terminalCompositeDisposable.add(

            orderApiService.getTerminals(context)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Terminal.TerminalsWrapper>() {
                    override fun onSuccess(value: Terminal.TerminalsWrapper) {
                        // Update the values with response in the success method.
                        loadPaymentTypeData.value = false
                        paymentTypeResponse.value = value
                        paymentTypeLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadPaymentTypeData.value = false
                        paymentTypeLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun submitPayment(context: Context, payment: Payment.Payment) {
        if (compositeDisposable.size() > 0)
            compositeDisposable.clear()

        loadSubmitPaymentData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDisposable.add(

            orderApiService.submitPayment(context, payment)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Terminal.TerminalsWrapper>() {
                    override fun onSuccess(value: Terminal.TerminalsWrapper) {
                        // Update the values with response in the success method.
                        loadSubmitPaymentData.value = false
                        submitPaymentResponse.value = value
                        submitPaymentLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadSubmitPaymentData.value = false
                        submitPaymentLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun finalizeFactor(context: Context, orderId: Long) {
        // Define the value of the load cash desk.
        if (finalizeFactorCompositeDisposable.size() > 0)
            finalizeFactorCompositeDisposable.clear()

        loadFinalizeFactorData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        finalizeFactorCompositeDisposable.add(

            orderApiService.finalizeFactor(context, orderId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Order.OrderAnswer>() {
                    override fun onSuccess(value: Order.OrderAnswer?) {
                        // Update the values with response in the success method.
                        loadFinalizeFactorData.value = false
                        finalizeFactorResponse.value = value
                        finalizeFactorLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadFinalizeFactorData.value = false
                        finalizeFactorLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun getOrderDetail(context: Context, orderId: Long) {
        // Define the value of the load cash desk.
        if (factorCompositeDisposable.size() > 0)
            factorCompositeDisposable.clear()


        loadFactorData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        factorCompositeDisposable.add(

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
                        loadFactorData.value = false
                        factorResponse.value = value
                        factorLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadFactorData.value = false
                        factorLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun deletePaymentRow(context: Context, orderId: Long, paymentId: Long) {
        // Define the value of the load cash desk.
        if (deletePaymentRowCompositeDisposable.size() > 0)
            deletePaymentRowCompositeDisposable.clear()

        deletePaymentRowData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        deletePaymentRowCompositeDisposable.add(

            orderApiService.deletePaymentRow(context,orderId,paymentId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Terminal.TerminalsWrapper>() {
                    override fun onSuccess(value: Terminal.TerminalsWrapper) {
                        // Update the values with response in the success method.
                        deletePaymentRowData.value = false
                        deletePaymentRowResponse.value = value
                        deletePaymentRowLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        deletePaymentRowData.value = false
                        deletePaymentRowLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }


}