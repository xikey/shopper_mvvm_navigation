package com.zikey.android.razanstoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zikey.android.razanstoreapp.model.Product
import com.zikey.android.razanstoreapp.model.StoreRoom
import com.zikey.android.razanstoreapp.repo.api.ProductApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class StoreRoomViewModel : ViewModel() {

    private val productApiService = ProductApiService()

    /**
     * A disposable container that can hold onto multiple other Disposables and
     * offers time complexity for add(Disposable), remove(Disposable) and delete(Disposable)
     * operations.
     */
    private val compositeDisposable = CompositeDisposable()
    private val storeRoomCompositeDisposable = CompositeDisposable()

    val loadData = MutableLiveData<Boolean>()
    val dataResponse = MutableLiveData<Product.ProductsAnswer>()
    val dataLoadingError = MutableLiveData<Boolean>()


    val loadProductPriceList = MutableLiveData<Boolean>()
    val productPriceListResponse = MutableLiveData<Product.ProductsAnswer>()
    val productPriceListLoadingError = MutableLiveData<Boolean>()


    val loadStoreRooms = MutableLiveData<Boolean>()
    val storeRoomsListResponse = MutableLiveData<StoreRoom.StoreRoomsAnswer>()
    val storeRoomsLoadingError = MutableLiveData<Boolean>()

    fun getProducts(context: Context, keySearch: String, firstIndex: Int, count: Int) {
        // Define the value of the load cash desk.
        loadData.value = true

        // Adds a Disposable to this container or disposes it if the container has been disposed.
        compositeDisposable.add(
            // Call the RandomDish method of RandomDishApiService class.
            productApiService.getProducts(context, keySearch, firstIndex, count)
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
                .subscribeWith(object : DisposableSingleObserver<Product.ProductsAnswer>() {
                    override fun onSuccess(value: Product.ProductsAnswer?) {
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


    fun getStoreRooms(context: Context, cashDeskId: Long) {
        loadStoreRooms.value = true

        storeRoomCompositeDisposable.add(
            productApiService.getStoreRooms(context, cashDeskId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StoreRoom.StoreRoomsAnswer>() {

                    override fun onSuccess(value: StoreRoom.StoreRoomsAnswer?) {
                        loadStoreRooms.value = false
                        storeRoomsListResponse.value = value
                        storeRoomsLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadStoreRooms.value = false
                        storeRoomsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }



}