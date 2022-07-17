package com.zikey.android.razanstoreapp.model

import com.example.distributorapp.model.answers.ServerAnswer
import com.google.gson.annotations.SerializedName

object Shelf {

    data class Shelf(
        @SerializedName("id") var id: Long,
        @SerializedName("c") var code: String,
        @SerializedName("r") var radif: String,
        @SerializedName("g") var ghesmat: String,
    )


    data class ShelvesWrapper(

        @SerializedName("s") var shelves: ArrayList<Shelf>?

    ) : ServerAnswer()

 data class ShelfWrapper(

        @SerializedName("s") var shelvf: Shelf

    ) : ServerAnswer()


}