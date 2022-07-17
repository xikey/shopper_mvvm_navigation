package com.zikey.android.razanstoreapp.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.FragmentKardexBinding
import com.zikey.android.razanstoreapp.model.KeyValue
import com.zikey.android.razanstoreapp.model.Product
import com.zikey.android.razanstoreapp.tools.CalendarWrapper
import com.zikey.android.razanstoreapp.tools.NumberSeperator
import com.zikey.android.razanstoreapp.ui.custome_view.MultyDatePickerDialog
import com.zikey.android.razanstoreapp.viewmodel.KardexViewModel
import es.dmoral.toasty.Toasty

class KardexFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentKardexBinding? = null

    private val viewModel: KardexViewModel by viewModels()
    private var storeRoomCode: Int? = null
    private var productCode: Long? = null
    //  private lateinit var adapter: RecyclerAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var startDate = CalendarWrapper().getFirstDayOfThisYear()
    private var endDate = CalendarWrapper().getCurrentPersianDate()
    private var firstIndex = 0
    private lateinit var adapter: RecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKardexBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initContent()
        initRecyclerView()
        productViewModelObserver()

    }

    private fun initListeners() {

        binding.lyDates.setOnClickListener(this)
        binding.lyRepo.setOnClickListener(this)
        binding.lyProduct.setOnClickListener(this)

    }

    private fun initRecyclerView() {

        adapter = RecyclerAdapter()

        binding.lyContent.rvItems.isNestedScrollingEnabled = true

        binding.lyContent.rvItems.let {

            it.adapter = adapter
            it.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
        }


    }

    private fun initContent() {

        binding.txtDates.setText("از" + " " + startDate + " " + "تا" + " " + endDate)

    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.lyDates -> {

                MultyDatePickerDialog.Show(
                    activity, startDate, endDate,
                ) { fromDate, toDate, dialog ->
                    this@KardexFragment.startDate = fromDate
                    endDate = toDate
                    dialog.dismiss()
                    initContent()
//                        keySearch = ""
//                        adapter.clearAll()
//                        initData()
                }

            }

            R.id.lyRepo -> {
                selectStoreRoom()

            }
            R.id.lyProduct -> {
                if (storeRoomCode == null || storeRoomCode == 0) {
                    Toasty.error(requireContext(), "انبار انتخاب نشده است").show()
                } else
                    openSelectProductFragment()


            }


        }
    }


    private fun openSelectProductFragment() {

        storeRoomCode?.let {
            SelectProductByRepoDialogFragment.newInstance(
                "انتخاب کالا",
                requireActivity().supportFragmentManager,
                it,
                object : SelectProductByRepoDialogFragment.OnSelect {
                    override fun setOnSelect(code: Long, name: String) {
                        Toasty.success(requireContext(), name).show()
                        binding.txtProduct.setText(name)
                        productCode = code
adapter.clearAll()
                        viewModel.kardex(
                            requireContext(), productCode!!, it, startDate!!,
                            endDate!!
                        )
                        //initData()
                    }

                })
        }
    }

    fun selectStoreRoom() {

        activity?.let {
            SelectStoreRoomFragment.newInstance(
                "انتخاب انبار",
                it.supportFragmentManager,
                object : SelectStoreRoomFragment.OnSelect {
                    override fun setOnSelect(code: Int, name: String) {
                        Toasty.success(it, name).show()
                        storeRoomCode = code
                        binding.txtRepo.setText(name)

                        clearProducts()
                    }

                })
        }
    }

    private fun productViewModelObserver() {

        viewModel.dataResponse.observe(
            viewLifecycleOwner,
            Observer { productResponse ->
                productResponse?.let {

                    Log.i("productResponse ", "${productResponse}")
                    if (productResponse != null && !productResponse.products.isNullOrEmpty()) {

                        adapter.setDatas(productResponse.products)
                        binding?.lyProgres?.lyProgress?.visibility ?: View.GONE
                    } else {
                        Toasty.error(requireContext(), "اطلاعاتی جهت نمایش وجود ندارد").show()

                    }

                }
            })

        viewModel.dataLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Cash Desk API Error", "$dataError")
                    if (dataError) {

                    }

                }
            })

        viewModel.loadData.observe(viewLifecycleOwner, Observer { loadCashDesk ->
            loadCashDesk?.let {
                Log.i("Product Loading", "$loadCashDesk")
                binding?.lyProgres?.lyProgress?.visibility ?: View.VISIBLE


            }
        })
    }

    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var datas: ArrayList<Product.Product>? = null
        private var datasDump: ArrayList<Product.Product>? = null

        fun setDatas(data: List<Product.Product>?) {
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

            for (item: Product.Product in data) {
                // body of loop
            }

            notifyDataSetChanged()
        }

        fun clearAll() {
            datas = null
            datasDump = null
            firstIndex = 0
            notifyDataSetChanged()

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.row_kardex,
                parent, false
            )

            val fontChanger = FontChanger()
            fontChanger.applyMainFont(itemView)


            return ItemHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val hld: ItemHolder = holder as ItemHolder

            try {

                val info = datas?.get(position)


                hld.txtDate.setText(info?.kardex?.date)
                hld.txtStoreRoom.setText(info?.kardex?.name_anbar.toString())
                hld.txtNumber.setText((info?.kardex?.shomare_resid_havale).toString())
                hld.txtType.setText((info?.kardex?.type_joz).toString())
                hld.txtInputCount.setText((info?.inputCount).toString())
                hld.txtOutputCount.setText((info?.outputCount).toString())
                hld.txtAvailability.setText((info?.available).toString())




                hld.lyMore.setOnClickListener {
                    info?.let { it1 -> showProductInfo(it1) }
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


        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val txtDate: TextView = itemView.findViewById(R.id.txtDate)
            val txtNumber: TextView = itemView.findViewById(R.id.txtNumber)
            val txtType: TextView = itemView.findViewById(R.id.txtType)
            val txtStoreRoom: TextView = itemView.findViewById(R.id.txtStoreRoom)
            val txtInputCount: TextView = itemView.findViewById(R.id.txtInputCount)
            val txtOutputCount: TextView = itemView.findViewById(R.id.txtOutputCount)
            val txtAvailability: TextView = itemView.findViewById(R.id.txtAvailability)
            val lyMore: LinearLayout = itemView.findViewById(R.id.lyMore)


        }

    }


    fun showProductInfo(product: Product.Product) {

        var list: ArrayList<KeyValue> = ArrayList()
        list.add(KeyValue("نام کالا", product.name))
        list.add(KeyValue("کد کالا", product.code.toString()))
        list.add(KeyValue("شماره مستند", product.kardex?.shomare_mostanad.toString()))
        list.add(KeyValue("نام مرکز", product.kardex?.name_markaz.toString()))
        list.add(KeyValue("نام مرکز2", product.kardex?.name_markaz2.toString()))
        list.add(KeyValue("توضیحات", product.kardex?.tozihat.toString()))
        list.add(KeyValue("قیمت فروش", NumberSeperator.separate(product.price1).toString()))
        list.add(KeyValue("قیمت مصرف کننده", NumberSeperator.separate(product.price5).toString()))
        list.add(KeyValue("موجودی", product.available.toString()))
        list.add(KeyValue("تعداد وارده", product.inputCount.toString()))
        list.add(KeyValue("تعداد صادره", product.outputCount.toString()))

        try {
            if (!TextUtils.isEmpty(product.barcode))
                list.add(KeyValue("بارکد 1", product.barcode.toString()))
            if (!TextUtils.isEmpty(product.barcode2))
                list.add(KeyValue("بارکد 2", product.barcode2.toString()))
            if (!TextUtils.isEmpty(product.barcode3))
                list.add(KeyValue("بارکد 3", product.barcode3.toString()))
            if (!TextUtils.isEmpty(product.barcode4))
                list.add(KeyValue("بارکد 4", product.barcode4.toString()))
            if (!TextUtils.isEmpty(product.barcode5))
                list.add(KeyValue("بارکد 5", product.barcode5.toString()))
            if (!TextUtils.isEmpty(product.barcode6))
                list.add(KeyValue("بارکد 6", product.barcode6.toString()))
        } catch (e: Exception) {

        }



        activity?.let {
            KeyValuesFragment.newInstance(
                "اطلاعات کالا",
                it.supportFragmentManager,
                list,
                object : KeyValuesFragment.OnSelect {
                    override fun setOnSelect(input: String, key: String, fragment: DialogFragment) {
                        return
                    }

                })
        }

    }

    fun clearProducts(){
        binding.txtProduct.setText("...")
        adapter.clearAll()
    }

}