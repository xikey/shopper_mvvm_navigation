package com.example.distributorapp.model.answers

import com.google.gson.annotations.SerializedName

abstract class ServerAnswer {

    @SerializedName("issu")
    private var isSuccess = 0

    @SerializedName("hasm")
    private var hasMore = 0

    @SerializedName("last")
    private var lastIndex: Long = 0

    @SerializedName("mess")
    private var message: String? = null

    open fun getIsSuccess(): Int {
        return isSuccess
    }

    open fun setIsSuccess(isSuccess: Int) {
        this.isSuccess = isSuccess
    }

    open fun getHasMore(): Int {
        return hasMore
    }

    open fun setHasMore(hasMore: Int) {
        this.hasMore = hasMore
    }

    open fun getLastIndex(): Long {
        return lastIndex
    }

    open fun setLastIndex(lastIndex: Long) {
        this.lastIndex = lastIndex
    }

    open fun getMessage(): String? {
        return message
    }

    open fun setMessage(message: String?) {
        this.message = message
    }

    open fun isSuccess(answer: Any?): Boolean? {
        if (answer == null) return false
        if (answer !is ServerAnswer) return false
        return answer.getIsSuccess() == 1
    }

    open fun hasMore(answer: Any?): Boolean? {
        if (answer == null) return false
        if (answer !is ServerAnswer) return false
        return answer.getHasMore() == 1
    }


}