package com.zikey.android.razanstoreapp.ui.fragments

import android.app.DialogFragment
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.FragmentNewFactorBinding
import com.zikey.android.razanstoreapp.databinding.RowInctanceOrderedProdictsItemBinding
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.OrderedProduct
import com.zikey.android.razanstoreapp.tools.NumberSeperator
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomAlertDialog
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.ui.fragments.home.HomeFragmentDirections
import com.zikey.android.razanstoreapp.viewmodel.OrderViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.regex.Matcher
import java.util.regex.Pattern


class NewFactorFragment : Fragment(), View.OnClickListener {

    private var mBinding: FragmentNewFactorBinding? = null

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var adapter: RecyclerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentNewFactorBinding.inflate(inflater, container, false)
        return mBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFonts()
        initListeners()
        initRecycler()
        iniOrderViewModel()
        getOrders()



    }

    private fun initRecycler() {


        adapter = RecyclerAdapter()

        mBinding!!.rvItems.isNestedScrollingEnabled = true

        mBinding!!.rvItems.let {

            it.adapter = adapter
            it.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
        }
    }

    private fun iniOrderViewModel() {

        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.clearDataObservers()
        initOrderApiService()
    }

    private fun initOrderApiService() {


        orderViewModel.dataResponse.observe(
            viewLifecycleOwner,
            Observer { orderResponse ->
                orderResponse?.let {

                    Log.i("MemberResponse ", "${it}")

                    if (it != null) {
                        if (it.getIsSuccess() != 1) {
                            CustomDialogBuilder().showCustomAlert(
                                requireActivity() as AppCompatActivity,
                                "خطا",
                                it.getMessage(),
                                "بستن"
                            )
                            mBinding!!.lyProgres.lyProgress.visibility = View.GONE
                            return@Observer
                        } else {
                            Toasty.success(requireActivity(), "فاکتور شما با موفقیت ثبت گردید!")
                                .show()
                            mBinding!!.lyProgres.lyProgress.visibility = View.GONE
                            clearBasket()
                            try {

                                openFactorDetail(it.order!!.id!!)
                            } catch (e: Exception) {

                            }

                            return@Observer
                        }


                    }

                }
            })

        orderViewModel.dataLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Order API Error", "$dataError")
                    if (dataError) {
                        mBinding!!.lyProgres.lyProgress.visibility = View.GONE
                        CustomDialogBuilder().showCustomAlert(
                            requireActivity() as AppCompatActivity,
                            "خطا",
                            "${dataError} \n\n خطا در ثبت فاکتور جدید",
                            "بستن"
                        )

                    }

                }
            })

        orderViewModel.loadData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("member Loading", "$it")


            }
        })

    }

    private fun initListeners() {
        mBinding!!.button.setOnClickListener(this)
        mBinding!!.fabSelectProduct.setOnClickListener(this)


        mBinding!!.txtCustomerTel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty() || s.isBlank()) {
                    saveCustomerTell("")
                } else {
                    saveCustomerTell(s.toString())
                }
            }

        })

    }

    private fun saveCustomerTell(tell: String) {

        orderViewModel.insertCustomerTell(requireContext(), tell)
    }

    private fun initFonts() {
        FontChanger().applyTitleFont(mBinding!!.fabSelectProduct)
        FontChanger().applyTitleFont(mBinding!!.lyHeader)
        FontChanger().applyTitleFont(mBinding!!.button)

    }

    private fun getOrders() = runBlocking<Order.Order?> {

        adapter.clearAll()
        var order: Order.Order? = null;

        orderViewModel.getOrder(requireContext()).let { order ->

            order.order.let {
                if (it == null)
                    return@runBlocking null

                if (!TextUtils.isEmpty(it.customerTell)) {
                    //  Toasty.success(requireContext(), "customer Tell Updated").show()
                    mBinding!!.txtCustomerTel.setText(it.customerTell)
                }

                if (it.orderedProducts.isNullOrEmpty()) {
                    //  Toasty.success(requireContext(), "order is Empty").show()
                    return@runBlocking null
                }
                mBinding!!.txtError.visibility = View.GONE
                adapter.setDatas(it.orderedProducts)
                return@runBlocking it
//                        Toasty.success(requireContext(), "order Size ${it.orderedProducts!!.size}")
//                            .show()
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_factor, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_delete -> {
                clearBasket()
                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearBasket() {

        adapter.clearAll()
        mBinding!!.txtCustomerTel.setText("")
        orderViewModel.clearBasket(requireContext())


    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    fun checkMobileValidation(number: String): Boolean {
        if (TextUtils.isEmpty(number)) {

            CustomDialogBuilder().showYesNOCustomAlert(
                activity as AppCompatActivity,
                "تلفن مشتری",
                "شماره تلفن مشتری وارد نشده است\n\nمایل به ادامه ثبت فرایند فاکتور میباشید؟",
                "ادامه",
                "انصراف",
                { fragment ->
                    createAndSendOrder()
                    fragment?.dismiss()
                }, object : CustomAlertDialog.OnCancelClickListener {
                    override fun onClickCancel(fragment: DialogFragment?) {
                        fragment?.dismiss()
                    }

                    override fun onClickOutside(fragment: DialogFragment?) {
                        fragment?.dismiss()
                    }

                }
            )
            return false
        }
        val p: Pattern = Pattern.compile("(0|91)?[7-9][0-9]{9}")
        val m: Matcher = p.matcher(number)
        if (m.find() && m.group().equals(number)) {
            createAndSendOrder()
            return true
        }
        CustomDialogBuilder().showCustomAlert(
            activity as AppCompatActivity,
            "تلفن مشتری",
            "شماره تلفن وارد شده نادرست میباشد", "بستن"
        )
        return false
    }

    fun createAndSendOrder() {
        val order: Order.Order = getOrders() ?: return
        val wrapper = Order.OrderAnswer(order)
        orderViewModel.insertOrderApi(requireContext(), wrapper)
    }


    override fun onClick(v: View) {

        when (v.id) {

            R.id.button -> {
                (!checkMobileValidation(mBinding?.txtCustomerTel?.text.toString()))

            }
            R.id.fab_select_product -> {

                findNavController().navigate(
                    NewFactorFragmentDirections.actionNewFactorFragmentToSelectProductFragment(
                        0
                    )
                )
            }
        }

    }


    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

        private var datas: ArrayList<OrderedProduct.OrderedProduct>? = null
        private var datasDump: ArrayList<OrderedProduct.OrderedProduct>? = null

        fun setDatas(data: List<OrderedProduct.OrderedProduct>?) {
            if (data.isNullOrEmpty()) {
                datas = null
                datasDump = null
                notifyDataSetChanged()
                return

            }
            if (datas == null)
                datas = ArrayList()

            if (datasDump == null)
                datasDump = ArrayList()

            datas!!.addAll(data)
            datasDump!!.addAll(data)


            notifyDataSetChanged()
        }


        fun clearAll() {
            datas = null
            datasDump = null
            notifyDataSetChanged()

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val binding: RowInctanceOrderedProdictsItemBinding =
                RowInctanceOrderedProdictsItemBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )

            FontChanger().applyMainFont(binding.root)


            return ItemHolder(binding)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val hld: ItemHolder = holder as ItemHolder

            try {

                val info = datas?.get(position)


                hld.txtRow.setText((position + 1).toString())
                hld.txtName.setText(info?.product?.name)
                hld.txtBarcode.setText(info?.product?.barcode.toString())
                hld.txtCount.setText(info!!.count.toString())
                hld.txtFi.setText(NumberSeperator.separate(info!!.fi))


                if (position % 2 == 0) {
                    holder.lyHeader.setBackgroundColor(Color.parseColor("#FFFFFF"))
                } else {
                    holder.lyHeader.setBackgroundColor(Color.parseColor("#ECEFF1"))
                }

                hld.lyHeader.setOnClickListener {
                    openEditableFragment(info, position)
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }


        }


        override fun getItemCount(): Int {

            return if (datas.isNullOrEmpty())
                0
            else
                datas!!.size
        }


        inner class ItemHolder(itemView: RowInctanceOrderedProdictsItemBinding) :
            RecyclerView.ViewHolder(itemView.root) {

            val lyHeader = itemView.lyHeader
            val txtRow = itemView.txtRow
            val txtName = itemView.txtName
            val txtBarcode = itemView.txtBarcode
            val txtCount = itemView.txtCount
            val txtFi = itemView.txtFi

        }

        override fun getFilter(): Filter {
            return MyFilter()
        }


        inner class MyFilter() : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val temp: ArrayList<OrderedProduct.OrderedProduct> = ArrayList()
                if (TextUtils.isEmpty(constraint)) {
                    val filter = FilterResults()
                    filter.values = datasDump
                    filter.count = 0
                    return filter
                }

                if (datasDump != null && datasDump!!.size !== 0) {
                    for (item in datasDump!!) {
                        try {
                            if (!TextUtils.isEmpty(item.product!!.name) && item.product!!.name!!.contains(
                                    constraint!!
                                )
                            ) {
                                temp.add(item)
                            }
                        } catch (ex: java.lang.Exception) {
                            ex.printStackTrace()
                        }
                        try {
                            if (item.product!!.code.toString().contains(constraint!!)) temp.add(
                                item
                            )
                        } catch (ex: java.lang.Exception) {
                            ex.printStackTrace()
                        }
                    }
                }

                val filter = FilterResults()
                filter.values = temp
                filter.count = temp.size

                return filter

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                var temp: ArrayList<OrderedProduct.OrderedProduct>? = null

                try {
                    temp = results!!.values as ArrayList<OrderedProduct.OrderedProduct>
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

                if (temp == null) temp = ArrayList()

                datas = temp

                notifyDataSetChanged()
            }

        }
    }

    private fun openEditableFragment(order: OrderedProduct.OrderedProduct, position: Int) {

        var fragment = AddToBasketFragment.Show(activity, order, this)
        fragment.enableEditableMode(object : AddToBasketFragment.OnBasketChangeListener {
            override fun onChange(count: Double, cost: Double) {
                order.cost = cost
                order.count = count

                orderViewModel.updateProduct(activity, position, null, order)
                getOrders()

                return
            }

            override fun onRemove() {
                orderViewModel.removeProduct(activity, order, null)
                getOrders()
            }

        })

    }

    fun openFactorDetail(factorId: Long) {
        findNavController().navigate(
            NewFactorFragmentDirections.actionNewFactorFragmentToOrderDetailsFragment(
                factorId
            )
        )
    }

}