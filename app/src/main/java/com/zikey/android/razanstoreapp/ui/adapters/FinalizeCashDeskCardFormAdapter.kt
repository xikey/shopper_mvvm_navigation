package com.zikey.android.razanstoreapp.ui.adapters

import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.databinding.RowCashDeskAvailabilityBinding
import com.zikey.android.razanstoreapp.databinding.RowCashDeskAvailabilityCalculatedBinding
import com.zikey.android.razanstoreapp.model.Payment
import com.zikey.android.razanstoreapp.model.Terminal
import com.zikey.android.razanstoreapp.tools.NumberSeperator


class FinalizeCashDeskCardFormAdapter(
    private val fragment: Fragment
) :
    ListAdapter<Terminal.Terminal, RecyclerView.ViewHolder>(FinalizeCashDeskCalculatedDiffCallback()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as ZikeyViewHolder).bind(plant, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ZikeyViewHolder(
            RowCashDeskAvailabilityCalculatedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), fragment, this
        )
    }

    class ZikeyViewHolder(
        private val binding: RowCashDeskAvailabilityCalculatedBinding,
        private val fragment: Fragment,
        private val adapter: FinalizeCashDeskCardFormAdapter,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Terminal.Terminal, position: Int) {
            binding.apply {
                FontChanger().applyMainFont(lyContainer)


                if (!TextUtils.isEmpty(item.bank))
                    txtBank.setText(item.bank) else txtBank.setText("-")


                txtNumber.setText(item.id.toString())

                if (!TextUtils.isEmpty(item.terminalType))
                    txtPaymentType.setText(item.terminalType) else txtPaymentType.setText("-")

                if (!TextUtils.isEmpty(item.accountNumber))
                    txtAccountNumber.setText(item.accountNumber) else txtAccountNumber.setText("-")

                if (!TextUtils.isEmpty(item.amount))
                    txtUserAvailability.setText(item.amount) else txtUserAvailability.setText("0")

                if (!TextUtils.isEmpty(item.sale))
                    txtSale.setText(item.sale) else txtSale.setText("0")

                if (!TextUtils.isEmpty(item.returned))
                    txtReturn.setText(item.returned) else txtReturn.setText("0")

                if (!TextUtils.isEmpty(item.netSale))
                    txtNetSale.setText(item.netSale) else txtNetSale.setText("0")

                if (!TextUtils.isEmpty(item.difference))
                    txtDifference.setText(item.difference) else txtDifference.setText("0")

                try {
                    if (!TextUtils.isEmpty(item.amount))
                    txtUserAvailability.setText(NumberSeperator.separate(item.amount.toDouble()))
                    if (!TextUtils.isEmpty(item.sale))
                    txtSale.setText(NumberSeperator.separate(item.sale.toDouble()))
                    if (!TextUtils.isEmpty(item.returned))
                    txtReturn.setText(NumberSeperator.separate(item.returned.toDouble()))
                    if (!TextUtils.isEmpty(item.netSale))
                    txtNetSale.setText(NumberSeperator.separate(item.netSale.toDouble()))
                    if (!TextUtils.isEmpty(item.difference))
                    txtDifference.setText(NumberSeperator.separate(item.difference.toDouble()))

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }
    }
}

private class FinalizeCashDeskCalculatedDiffCallback : DiffUtil.ItemCallback<Terminal.Terminal>() {

    override fun areItemsTheSame(
        oldItem: Terminal.Terminal,
        newItem: Terminal.Terminal
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Terminal.Terminal,
        newItem: Terminal.Terminal
    ): Boolean {
        return oldItem == newItem
    }

}
