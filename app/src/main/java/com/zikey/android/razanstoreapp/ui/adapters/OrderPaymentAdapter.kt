package com.zikey.android.razanstoreapp.ui.adapters

import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.databinding.RowOrderHistoryItemBinding
import com.zikey.android.razanstoreapp.databinding.RowOrderPaymentsItemBinding
import com.zikey.android.razanstoreapp.databinding.RowOrderProductItemBinding
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.OrderedProduct
import com.zikey.android.razanstoreapp.model.Payment
import com.zikey.android.razanstoreapp.tools.NumberSeperator
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.ui.fragments.AddToBasketFragment
import com.zikey.android.razanstoreapp.ui.fragments.NewFactorFragmentDirections
import com.zikey.android.razanstoreapp.ui.fragments.OrdersFragmentDirections


class OrderPaymentAdapter(
    private val fragment: Fragment,
    private val onChangeListener: OnPaymentRemove?
) :
    ListAdapter<Payment.Payment, RecyclerView.ViewHolder>(ItemDiffCallback()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as PlantViewHolder).bind(plant, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            RowOrderPaymentsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), fragment, this, onChangeListener
        )
    }

    class PlantViewHolder(
        private val binding: RowOrderPaymentsItemBinding,
        private val fragment: Fragment,
        private val adapter: OrderPaymentAdapter,
        private val onChangeListener: OnPaymentRemove?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Payment.Payment, position: Int) {
            binding.apply {

                FontChanger().applyMainFont(lyHeader)

                if (position % 2 == 0) {
                    lyHeader.setBackgroundColor(Color.parseColor("#FFFFFF"))
                } else {
                    lyHeader.setBackgroundColor(Color.parseColor("#ECEFF1"))
                }

                val tempPos = position + 1
                txtRow.setText("" + tempPos)
                txtPrice.setText(NumberSeperator.separate(item.price))
                if (item.bank != null && !TextUtils.isEmpty(item.bank))
                    txtBank.setText(item.bank)

                if (item.terminalName != null && !TextUtils.isEmpty(item.terminalName))
                    txtTerminalName.setText(item.terminalName)

                if (item.terminalType != null && !TextUtils.isEmpty(item.terminalType))
                    txtTerminalType.setText(item.terminalType)

                imgDelete.setOnClickListener {
                    onChangeListener?.onRemove(item)
                }


            }
        }
    }
}

private class ItemDiffCallback : DiffUtil.ItemCallback<Payment.Payment>() {

    override fun areItemsTheSame(
        oldItem: Payment.Payment,
        newItem: Payment.Payment
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Payment.Payment,
        newItem: Payment.Payment
    ): Boolean {
        return oldItem == newItem
    }

}

public interface OnPaymentRemove {
    fun onRemove(payment: Payment.Payment)
}