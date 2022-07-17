package com.zikey.android.razanstoreapp.ui.custom_dialog

import android.R.string
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AlertDialog.Builder
import com.zikey.android.razanstoreapp.R

class DialogBuilder {

    fun showYesNOAlert(
        context: AppCompatActivity,
        title: String?,
        question: String?,
        yesAction: OnClickListener?,
        noAction: OnClickListener?
    ): AlertDialog? {
        var title = title
        if (TextUtils.isEmpty(title)) {
            title = context.getString(R.string.atention)
        }
        return Builder(context).setTitle(title).setMessage(question)
            .setPositiveButton(context.getString(R.string.yes), yesAction)
            .setNegativeButton(context.getString(R.string.no), noAction).show()
    }

    fun showAlert(context: AppCompatActivity, message: String?): AlertDialog? {
        return Builder(context).setMessage(message)
            .setNeutralButton(context.getString(string.ok), null as OnClickListener?).show()
    }

    fun showAlert(context: AppCompatActivity, error: Throwable?): AlertDialog? {
        return if (error == null) {
            null
        } else {
            if (TextUtils.isEmpty(error.message)) Builder(context).setMessage(error.toString())
                .setNeutralButton(context.getString(string.ok), null as OnClickListener?)
                .show() else Builder(context).setMessage(error.message)
                .setNeutralButton(context.getString(string.ok), null as OnClickListener?).show()
        }
    }

    fun showAlert(
        context: AppCompatActivity,
        message: String?,
        okAction: OnClickListener?
    ): AlertDialog? {
        return Builder(context).setMessage(message)
            .setNeutralButton(context.getString(string.ok), okAction).show()
    }

}