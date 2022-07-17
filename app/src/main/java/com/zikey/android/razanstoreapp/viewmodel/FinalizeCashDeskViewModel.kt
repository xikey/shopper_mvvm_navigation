package com.zikey.android.razanstoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.Terminal
import com.zikey.android.razanstoreapp.repo.api.OrderApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class FinalizeCashDeskViewModel : ViewModel() {

    private val orderApiService = OrderApiService()
    private val compositeDisposable = CompositeDisposable()
    private val detailsCompositeDisposable = CompositeDisposable()
    private val updateAmountCompositeDisposable = CompositeDisposable()
    private val calculatorCompositeDisposable = CompositeDisposable()

    val createFinalizeData = MutableLiveData<Boolean>()
    val createFinalizeResponse = MutableLiveData<Order.OrderAnswer>()
    val createFinalizeLoadingError = MutableLiveData<Boolean>()

    val finalizePaymentsData = MutableLiveData<Boolean>()
    val finalizePaymentsResponse = MutableLiveData<Terminal.TerminalsWrapper>()
    val finalizePaymentsLoadingError = MutableLiveData<Boolean>()


    val finalizePaymentsUpdateAmountData = MutableLiveData<Boolean>()
    val finalizePaymentsUpdateAmountResponse = MutableLiveData<Terminal.TerminalsWrapper>()
    val finalizePaymentsUpdateAmountLoadingError = MutableLiveData<Boolean>()


    val calculateCashDeskData = MutableLiveData<Boolean>()
    val calculateCashDeskResponse = MutableLiveData<Terminal.TerminalsWrapper>()
    val calculateCashDeskLoadingError = MutableLiveData<Boolean>()

    fun create(context: Context, cashDeskID: Long, date: String) {
        // Define the value of the load cash desk.
        if (compositeDisposable.size() > 0)
            compositeDisposable.clear()

        createFinalizeData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDisposable.add(

            orderApiService.finalizeCashDeskCreate(context, cashDeskID, date, "")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Order.OrderAnswer>() {
                    override fun onSuccess(value: Order.OrderAnswer?) {
                        // Update the values with response in the success method.
                        createFinalizeData.value = false
                        createFinalizeResponse.value = value
                        createFinalizeLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        createFinalizeData.value = false
                        createFinalizeLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun getFinalizePayments(context: Context, finalizedCahDeskID: Long) {
        // Define the value of the load cash desk.
        if (detailsCompositeDisposable.size() > 0)
            detailsCompositeDisposable.clear()

        finalizePaymentsData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        detailsCompositeDisposable.add(

            orderApiService.finalizeCashDesk_getPayments(context, finalizedCahDeskID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Terminal.TerminalsWrapper>() {
                    override fun onSuccess(value: Terminal.TerminalsWrapper?) {
                        // Update the values with response in the success method.
                        finalizePaymentsData.value = false
                        finalizePaymentsResponse.value = value
                        finalizePaymentsLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        finalizePaymentsData.value = false
                        finalizePaymentsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

    fun getFinalizePaymentsUpdateAmounts(context: Context, footerId: Long, amount: String) {
        // Define the value of the load cash desk.
        if (updateAmountCompositeDisposable.size() > 0)
            updateAmountCompositeDisposable.clear()

        finalizePaymentsUpdateAmountData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        updateAmountCompositeDisposable.add(

            orderApiService.finalizeCashDesk_updateAmount(context, footerId, amount)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Terminal.TerminalsWrapper>() {
                    override fun onSuccess(value: Terminal.TerminalsWrapper?) {
                        // Update the values with response in the success method.
                        finalizePaymentsUpdateAmountData.value = false
                        finalizePaymentsUpdateAmountResponse.value = value
                        finalizePaymentsUpdateAmountLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        finalizePaymentsUpdateAmountData.value = false
                        finalizePaymentsUpdateAmountLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }
    fun calculateCashDesk(context: Context, cashDeskID: Long ) {
        // Define the value of the load cash desk.
        if (calculatorCompositeDisposable.size() > 0)
            calculatorCompositeDisposable.clear()

        calculateCashDeskData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        calculatorCompositeDisposable.add(

            orderApiService.finalizeCashDesk_calculator(context, cashDeskID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<Terminal.TerminalsWrapper>() {
                    override fun onSuccess(value: Terminal.TerminalsWrapper?) {
                        // Update the values with response in the success method.
                        calculateCashDeskData.value = false
                        calculateCashDeskResponse.value = value
                        calculateCashDeskLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        calculateCashDeskData.value = false
                        calculateCashDeskLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }


}