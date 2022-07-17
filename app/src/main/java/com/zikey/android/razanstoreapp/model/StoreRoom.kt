package com.zikey.android.razanstoreapp.model

import com.example.distributorapp.model.answers.ServerAnswer
import com.google.gson.annotations.SerializedName

object StoreRoom {

    data class StoreRoom(
        @SerializedName("c") val code: Int?,
        @SerializedName("n") val name: String?,
        @SerializedName("t") val type: String?,
    )


    data class StoreRoomsAnswer(

        @SerializedName("s") var storeRooms: List<StoreRoom>?

    ) : ServerAnswer()


}