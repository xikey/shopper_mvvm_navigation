package com.zikey.android.razanstoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zikey.android.razanstoreapp.model.CashDesk
import com.zikey.android.razanstoreapp.repo.api.CashDeskApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class CashDeskViewModel : ViewModel() {


    private val cashDeskApiService = CashDeskApiService()

    /**
     * A disposable container that can hold onto multiple other Disposables and
     * offers time complexity for add(Disposable), remove(Disposable) and delete(Disposable)
     * operations.
     */
    private val compositeDisposable = CompositeDisposable()

    val loadCashDesk = MutableLiveData<Boolean>()
    val cashDeskResponse = MutableLiveData<CashDesk.CashDesksAnswer>()
    val cashDeskLoadingError = MutableLiveData<Boolean>()

    fun getCashDeskFromApi(context: Context, userCode: Long) {
        // Define the value of the load cash desk.
        loadCashDesk.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDisposable.add(
            // Call the RandomDish method of RandomDishApiService class.
            cashDeskApiService.getCashDesks(context, userCode)
                // Asynchronously subscribes SingleObserver to this Single on the specified Scheduler.
                /**
                 * Static factory methods for returning standard Scheduler instances.
                 *
                 * The initial and runtime values of the various scheduler types can be overridden via the
                 * {RxJavaPlugins.setInit(scheduler name)SchedulerHandler()} and
                 * {RxJavaPlugins.set(scheduler name)SchedulerHandler()} respectively.
                 */
                .subscribeOn(Schedulers.newThread())
                /**
                 * Signals the success item or the terminal signals of the current Single on the specified Scheduler,
                 * asynchronously.
                 *
                 * A Scheduler which executes actions on the Android main thread.
                 */
                .observeOn(AndroidSchedulers.mainThread())
                /**
                 * Subscribes a given SingleObserver (subclass) to this Single and returns the given
                 * SingleObserver as is.
                 */
                .subscribeWith(object : DisposableSingleObserver<CashDesk.CashDesksAnswer>() {
                    override fun onSuccess(value: CashDesk.CashDesksAnswer?) {
                        // Update the values with response in the success method.
                        loadCashDesk.value = false
                        cashDeskResponse.value = value
                        cashDeskLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        // Update the values in the response in the error method
                        loadCashDesk.value = false
                        cashDeskLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }

}