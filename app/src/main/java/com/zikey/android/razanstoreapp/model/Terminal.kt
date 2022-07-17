package com.zikey.android.razanstoreapp.model

import com.example.distributorapp.model.answers.ServerAnswer
import com.google.gson.annotations.SerializedName

object Terminal {

    data class Terminal(

        @SerializedName("id") var id: Long,
        @SerializedName("tt") var terminalType: String,
        @SerializedName("tn") var terminalName: String,
        @SerializedName("b") var bank: String,
        @SerializedName("imei") var imei: String,
        @SerializedName("mn") var mobileNumber: String,
        @SerializedName("acn") var accountNumber: String,
        @SerializedName("amt") var amount: String,
        @SerializedName("sal") var sale: String,
        @SerializedName("ret") var returned: String,
        @SerializedName("nts") var netSale: String,
        @SerializedName("dif") var difference: String,
    )

    data class TerminalsWrapper(
        @SerializedName("t") var terminals: List<Terminal>,
    ) : ServerAnswer()


    data class TerminalWrapper(
        @SerializedName("t") var terminal: Terminal,
    ) : ServerAnswer()

}