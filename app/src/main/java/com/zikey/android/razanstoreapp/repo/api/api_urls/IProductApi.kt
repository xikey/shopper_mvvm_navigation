package com.zikey.android.razanstoreapp.repo.api.api_urls

import com.zikey.android.razanstoreapp.model.CashDesk
import com.zikey.android.razanstoreapp.model.Product
import com.zikey.android.razanstoreapp.model.StoreRoom
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IProductApi {


    @GET("Products/getProductsWithoutStoreRoom")
    fun getProducts(
        @Query("keySearch") keySearch: String,
        @Query("firstIndex") firstIndex: Int,
        @Query("count") count: Int,
    ):  Single<Product.ProductsAnswer>


    @GET("Products")
    fun getProductsWithStoreRoom(
        @Query("keySearch") keySearch: String,
        @Query("firstIndex") firstIndex: Int,
        @Query("count") count: Int,
        @Query("storeRoomCode") storeRoomCode: Long,
    ):  Single<Product.ProductsAnswer>



    @GET("Products/getProductPriceList")
    fun getProductPriceList(
        @Query("cashDeskId") cashDeskId: Long,
        @Query("productCode") productCode: Long,
    ):  Single<Product.ProductsAnswer>

    @GET("Products/getStoreRooms")
    fun getStoreRooms(
        @Query("cashDeskId") cashDeskId: Long,
    ):  Single<StoreRoom.StoreRoomsAnswer>


    @GET("Products/kardex")
    fun kardex(
        @Query("productCode") productCode: Long,
        @Query("storeRoomCode") storeRoomCode: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<Product.ProductsAnswer>



}