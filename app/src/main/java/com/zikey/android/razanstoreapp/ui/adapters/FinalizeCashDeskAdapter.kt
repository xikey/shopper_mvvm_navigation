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
import com.zikey.android.razanstoreapp.model.Terminal
import com.zikey.android.razanstoreapp.tools.NumberSeperator


class FinalizeCashDeskAdapter(
    private val fragment: Fragment,
    private val onClickListener: OnItemClickListener?
) :
    ListAdapter<Terminal.Terminal, RecyclerView.ViewHolder>(FinalizeCashDeskDiffCallback()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as ZikeyViewHolder).bind(plant, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ZikeyViewHolder(
            RowCashDeskAvailabilityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), fragment, this, onClickListener
        )
    }

    class ZikeyViewHolder(
        private val binding: RowCashDeskAvailabilityBinding,
        private val fragment: Fragment,
        private val adapter: FinalizeCashDeskAdapter,
        private val onChangeListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Terminal.Terminal, position: Int) {
            binding.apply {
                FontChanger().applyMainFont(lyHeader)

                if (position % 2 == 0) {
                    lyContainer.setBackgroundColor(Color.parseColor("#FFFFFF"))
                } else {
                    lyContainer.setBackgroundColor(Color.parseColor("#ECEFF1"))
                }


                if (item.bank != null && !TextUtils.isEmpty(item.bank))
                    txtBank.setText(item.bank) else txtBank.setText("-")

                if (item.terminalType != null && !TextUtils.isEmpty(item.terminalType))
                    txtPaymentType.setText(item.terminalType) else txtPaymentType.setText("-")

                if (item.accountNumber != null && !TextUtils.isEmpty(item.accountNumber))
                    txtAccountNumber.setText(item.accountNumber) else txtAccountNumber.setText("-")

                if (item.amount != null && !TextUtils.isEmpty(item.amount))
                    txtAvailable.setText(item.amount) else txtAvailable.setText("0")

                try {
                    txtAvailable.setText(NumberSeperator.separate( item.amount.toDouble()))
                }catch (e:Exception){
                    e.printStackTrace()
                }

                lyHeader.setOnClickListener {
                    onChangeListener!!.onClick(item)
                }

            }
        }
    }
}

private class FinalizeCashDeskDiffCallback : DiffUtil.ItemCallback<Terminal.Terminal>() {

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

public interface OnItemClickListener {
    fun onRemove(terminal: Terminal.Terminal)
    fun onClick(terminal: Terminal.Terminal)
}