package com.zikey.android.razanstoreapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
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
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.tools.NumberSeperator
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.ui.fragments.NewFactorFragmentDirections
import com.zikey.android.razanstoreapp.ui.fragments.OrdersFragmentDirections


/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class OrdersAdapter(private val fragment: Fragment) :
    ListAdapter<Order.Order, RecyclerView.ViewHolder>(PlantDiffCallback()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as PlantViewHolder).bind(plant, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            RowOrderHistoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), fragment
        )
    }

    class PlantViewHolder(
        private val binding: RowOrderHistoryItemBinding,
        private val fragment: Fragment
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Order.Order, position: Int) {
            binding.apply {

                FontChanger().applyMainFont(lyHeader)

                if (position % 2 == 0) {
                    lyHeader.setBackgroundColor(Color.parseColor("#FFFFFF"))
                } else {
                    lyHeader.setBackgroundColor(Color.parseColor("#ECEFF1"))
                }

                txtDate.setText(item.createDate + "\n" + item.createTime)
                if (item.totalPrice == null)
                    txtPrice.setText(NumberSeperator.separate(0))
                else
                    txtPrice.setText(NumberSeperator.separate(item.totalPrice!!))
                txtTell.setText(item.customerTell)
                txtRow.setText("" + item.id)
                //Using DataBinding

                lyHeader.setOnClickListener {
                    fragment.findNavController().navigate(
                        OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(
                            item.id!!
                        )
                    )
                }

            }
        }
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<Order.Order>() {

    override fun areItemsTheSame(oldItem: Order.Order, newItem: Order.Order): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Order.Order, newItem: Order.Order): Boolean {
        return oldItem == newItem
    }
}