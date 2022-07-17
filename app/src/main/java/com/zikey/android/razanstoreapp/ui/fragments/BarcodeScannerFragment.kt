package com.zikey.android.razanstoreapp.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.zxing.Result
import com.zikey.android.razanstoreapp.R
import me.dm7.barcodescanner.zxing.ZXingScannerView


class BarcodeScannerFragment : DialogFragment(), ZXingScannerView.ResultHandler {

    private lateinit var mScannerView: ZXingScannerView
    private lateinit var onSelect: OnSelect


    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(
            androidx.fragment.app.DialogFragment.STYLE_NO_FRAME,
            android.R.style.Theme_Light_NoTitleBar
        )
        super.onCreate(savedInstanceState)

        arguments?.let {

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.getWindow()?.getAttributes()!!.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )


    }

    private fun setOnSelect(input: OnSelect) {

        onSelect = input

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mScannerView = ZXingScannerView(activity)

        return mScannerView
    }


    companion object {

        private const val KEY_BARCODE_SCANNER_FRAGMENT = "BARCODE_SCANNER_FRAGMENT"

        @JvmStatic
        fun newInstance(
            fragmentManager: FragmentManager,
            onSelect: OnSelect
        ) =
            BarcodeScannerFragment().apply {

                setOnSelect(onSelect)
                show(fragmentManager, KEY_BARCODE_SCANNER_FRAGMENT)
            }


    }


    interface OnSelect {
        fun setOnSelect(barcode: String)
    }

    override fun onDestroy() {
        super.onDestroy()
        mScannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {


        Log.v("TAG", rawResult!!.text) // Prints scan results

        Log.v(
            "TAG", rawResult.barcodeFormat.toString()
        ) // Prints the scan format (qrcode, pdf417 etc.)


        if (onSelect != null)
            onSelect.setOnSelect(rawResult!!.text)

        dismiss()


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

}