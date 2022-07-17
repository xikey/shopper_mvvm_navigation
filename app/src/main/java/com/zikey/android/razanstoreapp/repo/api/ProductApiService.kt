package com.zikey.android.razanstoreapp.repo.api

import android.content.Context
import com.zikey.android.razanstoreapp.model.Product
import com.zikey.android.razanstoreapp.model.StoreRoom
import com.zikey.android.razanstoreapp.repo.api.api_urls.IProductApi
import com.zikey.android.razanstoreapp.repo.api.tools.ServerApiClient

import io.reactivex.rxjava3.core.Single

class ProductApiService {

    fun getProducts(
        context: Context?,
        keySearch: String,
        firstIndex: Int,
        count: Int
    ): Single<Product.ProductsAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IProductApi::class.java)
        return api.getProducts(keySearch, firstIndex, count)
    }

    fun getProductsWithStoreRoom(
        context: Context?,
        keySearch: String,
        firstIndex: Int,
        count: Int,
        storeRoomCode: Long
    ): Single<Product.ProductsAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IProductApi::class.java)
        return api.getProductsWithStoreRoom(keySearch, firstIndex, count, storeRoomCode)
    }

    fun getProductPriceList(
        context: Context?,
        cashDeskID: Long,
        productCode: Long
    ): Single<Product.ProductsAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IProductApi::class.java)
        return api.getProductPriceList(cashDeskID, productCode)
    }


    fun getStoreRooms(
        context: Context?,
        cashDeskID: Long,
    ): Single<StoreRoom.StoreRoomsAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IProductApi::class.java)
        return api.getStoreRooms(cashDeskID)
    }


    fun kardex(
        context: Context?,
        productCode: Long,
        storeRoomCode: Int,
        startDate: String,
        endDate: String
    ): Single<Product.ProductsAnswer> {
        val api = ServerApiClient.getClientWithHeader(context)!!.create(IProductApi::class.java)
        return api.kardex(productCode, storeRoomCode, startDate, endDate)
    }


}