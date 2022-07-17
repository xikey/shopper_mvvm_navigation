package com.zikey.android.razanstoreapp.ui.adapters

import android.graphics.Color
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
import com.zikey.android.razanstoreapp.databinding.RowOrderProductItemBinding
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.OrderedProduct
import com.zikey.android.razanstoreapp.tools.NumberSeperator
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.ui.fragments.AddToBasketFragment
import com.zikey.android.razanstoreapp.ui.fragments.NewFactorFragmentDirections
import com.zikey.android.razanstoreapp.ui.fragments.OrdersFragmentDirections


class OrderAdapter(private val fragment: Fragment, private val onChangeListener: OnProductChange?) :
    ListAdapter<OrderedProduct.OrderedProduct, RecyclerView.ViewHolder>(OrderDiffCallback()) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as PlantViewHolder).bind(plant, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlantViewHolder(
            RowOrderProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), fragment, this,onChangeListener
        )
    }

    class PlantViewHolder(
        private val binding: RowOrderProductItemBinding,
        private val fragment: Fragment,
        private val adapter: OrderAdapter,
        private val onChangeListener: OnProductChange?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderedProduct.OrderedProduct, position: Int) {
            binding.apply {

                FontChanger().applyMainFont(lyHeader)

                if (position % 2 == 0) {
                    lyHeader.setBackgroundColor(Color.parseColor("#FFFFFF"))
                } else {
                    lyHeader.setBackgroundColor(Color.parseColor("#ECEFF1"))
                }

                txtCount.setText(NumberSeperator.separate(item.count!!))
                txtFi.setText(NumberSeperator.separate(item.product!!.fi!!))
                txtName.setText(item.product!!.name!!)
                val tempPos = position + 1
                txtRow.setText("" + tempPos)
                txtPrice.setText(NumberSeperator.separate(item.product!!.price1!!))

                try {
                    txtBarcode.setText(item.product!!.barcode)
                    if (item.product!!.discountPrice1 != null)
                        txtDiscountPrice1.setText(NumberSeperator.separate(item.product!!.discountPrice1!!))
                    if (item.product!!.discountPrice2 != null)
                        txtDiscountPrice2.setText(NumberSeperator.separate(item.product!!.discountPrice2!!))
                    if (item.product!!.discountPercent != null)
                        txtDiscountPercent.setText("" + item.product!!.discountPercent + " % ")
                    if (item.product!!.cashDiscount != null)
                        txtTotalDiscount.setText(NumberSeperator.separate(item.product!!.cashDiscount))

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                lyDetails.visibility = View.GONE

                if (item.isSelcted)
                    lyDetails.visibility = View.VISIBLE

                if (item.isSelcted) {
                    imgOpen.visibility = View.GONE
                    imgColse.visibility = View.VISIBLE
                } else {
                    imgOpen.visibility = View.VISIBLE
                    imgColse.visibility = View.GONE
                }

                lyOpenClose.setOnClickListener {
                    item.isSelcted = !item.isSelcted
                    adapter.notifyDataSetChanged()
                }

                lyHeader.setOnClickListener{
                    var fg = AddToBasketFragment.Show(fragment.activity, item, fragment)
                    fg.enableEditableMode(object : AddToBasketFragment.OnBasketChangeListener {
                        override fun onChange(count: Double, cost: Double) {

                            val temp = item.copy()
                            temp.count=count
                            onChangeListener?.onChange(temp)

                            return
                        }

                        override fun onRemove() {
                            onChangeListener?.onRemove(item)
                        }

                    })
                }


            }
        }
    }
}

private class OrderDiffCallback : DiffUtil.ItemCallback<OrderedProduct.OrderedProduct>() {

    override fun areItemsTheSame(
        oldItem: OrderedProduct.OrderedProduct,
        newItem: OrderedProduct.OrderedProduct
    ): Boolean {
        return oldItem.product!!.code == newItem.product!!.code
    }

    override fun areContentsTheSame(
        oldItem: OrderedProduct.OrderedProduct,
        newItem: OrderedProduct.OrderedProduct
    ): Boolean {
        return oldItem == newItem
    }

}

public interface OnProductChange {
    fun onChange(orderedProduct: OrderedProduct.OrderedProduct)
    fun onRemove(orderedProduct: OrderedProduct.OrderedProduct)
}