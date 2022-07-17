package com.zikey.android.razanstoreapp.ui.fragments

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.FragmentSelectProductBinding
import com.zikey.android.razanstoreapp.databinding.RowSelectProduct2Binding
import com.zikey.android.razanstoreapp.model.Product
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import com.zikey.android.razanstoreapp.tools.NumberSeperator
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomAlertDialog
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.viewmodel.ProductViewModel
import es.dmoral.toasty.Toasty


class SelectProductFragment : Fragment() {

    private var mBinding: FragmentSelectProductBinding? = null
    private lateinit var productViewModel: ProductViewModel


    private lateinit var adapter: RecyclerAdapter

    private var count = 100
    private var firstIndex = 0
    private var keySearch = ""
    private var hasMore: Boolean = false

    /**
     * order id
     * هنگامی که اطلاعات سفارش از سرور گرفته شده باشد ای دی سفارش نیاز میباشد برای
     * چون کالا مستقیما روی سرور درج میشود
     */
    private var orderID: Long = 0

    private var datas: ArrayList<Product.Product>? = null
    private var datasDump: ArrayList<Product.Product>? = null
    var searchEditText: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val args: OrderDetailsFragmentArgs by navArgs()
        orderID = args.factorId

    }


    private fun initRecyclerView() {

        adapter = RecyclerAdapter()

        mBinding!!.rvItems.isNestedScrollingEnabled = true

        mBinding!!.rvItems.let {

            it.adapter = adapter
            it.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentSelectProductBinding.inflate(inflater, container, false)

        FontChanger().applyMainFont(mBinding!!.root)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initRecyclerView()
        initViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_bscanner, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val myActionMenuItem = menu.findItem(R.id.search)
        val searchView = myActionMenuItem.actionView as SearchView
        val searchEditText = searchView.findViewById<View>(R.id.search_src_text) as EditText
        searchEditText!!.setTextColor(Color.parseColor("#FFFFFF"))
        searchEditText!!.setHint("جستجو")
        searchEditText!!.setHintTextColor(Color.parseColor("#FFFFFF"))


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null) {

                    keySearch = ""
                } else {
                    keySearch = query
                }
                adapter.clearAll()
                initData()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText == null || TextUtils.isEmpty(newText)) {
                    keySearch = ""
                    adapter.clearAll()
                    initData()
                }

                return true
            }

        })

        val menuScanItem = menu.findItem(R.id.scan)
        menuScanItem.setOnMenuItemClickListener {
            getCameraPermission(searchView, menu)
            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.search -> {

                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }


    private fun initViewModel() {
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.getProducts(
            activity as AppCompatActivity,
            keySearch,
            firstIndex,
            count
        )
        productViewModelObserver()
        productPriceListViewModelObserver()

    }

    private fun productViewModelObserver() {
        productViewModel.dataResponse.observe(
            viewLifecycleOwner,
            Observer { productResponse ->
                productResponse?.let {

                    Log.i("productResponse ", "${productResponse}")
                    if (productResponse != null && !productResponse.products.isNullOrEmpty()) {

                        adapter.setDatas(productResponse.products)
                        mBinding?.lyProgres?.lyProgress?.visibility ?: View.GONE
                    } else {
                        CustomDialogBuilder().showYesNOCustomAlert(
                            activity as AppCompatActivity,
                            "خطا",
                            "خطا در دریافت لیست کالاها، لطفا مجددا تلاش نمایید.",
                            "تلاش مجددا",
                            "انصراف",
                            { fragment ->
                                productViewModel.getProducts(
                                    activity as AppCompatActivity,
                                    keySearch,
                                    firstIndex,
                                    count
                                )
                                fragment?.dismiss()
                            }, object : CustomAlertDialog.OnCancelClickListener {
                                override fun onClickCancel(fragment: android.app.DialogFragment?) {
                                    fragment?.dismiss()

                                }

                                override fun onClickOutside(fragment: android.app.DialogFragment?) {
                                    fragment?.dismiss()

                                }

                            }
                        )

                    }

                }
            })

        productViewModel.dataLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Cash Desk API Error", "$dataError")
                    if (dataError) {
                        mBinding?.lyProgres?.lyProgress?.visibility ?: View.GONE
                        CustomDialogBuilder().showYesNOCustomAlert(
                            activity as AppCompatActivity,
                            "خطا",
                            "خطا در دریافت لیست کالا، لطفا مجددا تلاش نمایید.",
                            "تلاش مجددا",
                            "انصراف",
                            { fragment ->
                                productViewModel.getProducts(
                                    activity as AppCompatActivity,
                                    keySearch,
                                    firstIndex,
                                    count
                                )
                                fragment?.dismiss()
                            }, object : CustomAlertDialog.OnCancelClickListener {
                                override fun onClickCancel(fragment: android.app.DialogFragment?) {
                                    fragment?.dismiss()

                                }

                                override fun onClickOutside(fragment: android.app.DialogFragment?) {
                                    fragment?.dismiss()

                                }

                            }
                        )

                    }

                }
            })

        productViewModel.loadData.observe(viewLifecycleOwner, Observer { loadCashDesk ->
            loadCashDesk?.let {
                Log.i("Product Loading", "$loadCashDesk")
                mBinding?.lyProgres?.lyProgress?.visibility ?: View.VISIBLE


            }
        })
    }

    private fun productPriceListViewModelObserver() {
        productViewModel.productPriceListResponse.observe(
            viewLifecycleOwner,
            Observer { productsResponse ->
                productsResponse?.let {
                    mBinding!!.lyProgres.lyProgress.visibility = View.GONE
                    Log.i("productsResponse ", "${productsResponse}")
                    if (!productsResponse.products.isNullOrEmpty()) {
                        showPricePickerDialog(productsResponse.products!!)
                        return@Observer
                    }
                    if (productsResponse.getIsSuccess() == 0) {
                        if (!TextUtils.isEmpty(productsResponse.getMessage())) {
                            CustomDialogBuilder().showCustomAlert(
                                activity as AppCompatActivity,
                                "خطا",
                                productsResponse.getMessage(),
                                "بستن"
                            )

                        } else {
                            CustomDialogBuilder().showCustomAlert(
                                activity as AppCompatActivity,
                                "خطا",
                                "خطا در دریافت لیست قیمت و موجودی کالا، لطفا مجددا تلاش نمایید.",
                                "بستن"
                            )

                        }
                    } else {
                        mBinding!!.lyProgres.lyProgress.visibility = View.GONE
                        CustomDialogBuilder().showCustomAlert(
                            activity as AppCompatActivity,
                            "خطا",
                            "خطا در دریافت لیست قیمت و موجودی کالا، لطفا مجددا تلاش نمایید.",
                            "بستن"
                        )

                    }

                }
            })

        productViewModel.productPriceListLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("PriceList API Error", "$dataError")
                    if (dataError) {
                        mBinding?.lyProgres?.lyProgress?.visibility ?: View.GONE
                        CustomDialogBuilder().showCustomAlert(
                            activity as AppCompatActivity,
                            "خطا",
                            "خطا در دریافت لیست قیمت و موجودی کالا، لطفا مجددا تلاش نمایید.",
                            "بستن"
                        )

                    }

                }
            })

        productViewModel.loadProductPriceList.observe(viewLifecycleOwner, Observer { loadCashDesk ->
            loadCashDesk?.let {
                Log.i("PriceList Loading", "$loadCashDesk")
                mBinding?.lyProgres?.lyProgress?.visibility ?: View.VISIBLE


            }
        })
    }

    private fun showPricePickerDialog(products: List<Product.Product>) {
        if (products.size > 1) {

            var singleItems = arrayOf<String>()
            products.forEach { product ->
                singleItems += NumberSeperator.separate(product.price1)
            }
            var checkedItem = 0

            activity?.let {
                MaterialAlertDialogBuilder(it)
                    .setTitle(resources.getString(R.string.selectPriceLevel))
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                        // Respond to neutral button press
                    }
                    .setPositiveButton(resources.getString(R.string.select)) { dialog, which ->
                        run {

                            Toasty.success(requireActivity(), singleItems[checkedItem]).show()
                            val product = adapter.findProduct(products[checkedItem])

                            if (product == null)
                                return@run


                            product.price1 = products[checkedItem].price1
                            product.available = products[checkedItem].available

                            AddToBasketFragment.Show(
                                activity,
                                product,
                                false,
                                orderID,
                                this
                            )
                        }

                    }

                    // Single-choice items (initialized with checked item)
                    .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->

                        checkedItem = which

                        // Respond to item chosen
                    }
                    .show()
            }

        } else {
            val product = adapter.findProduct(products[0])

            if (product == null)
                return


            product.price1 = products[0].price1
            product.available = products[0].available

            if ((products[0].price1) == 0f) {

                CustomDialogBuilder().showCustomAlert(
                    activity as AppCompatActivity,
                    "خطا",
                    "امکان افزودن کالا با قیمت صفر وجود ندارد!",
                    "بستن"
                )

                return
            }

            AddToBasketFragment.Show(
                activity,
                product,
                false,
                orderID,
                this
            )
            Toasty.success(requireActivity(), NumberSeperator.separate(products[0].price1)).show()
        }
    }


    private fun initData() {
        productViewModel.getProducts(
            activity as AppCompatActivity,
            keySearch,
            firstIndex,
            count
        )

    }

    private fun initViews() {


        mBinding!!.rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (recyclerView.layoutManager is LinearLayoutManager) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    val pos = layoutManager!!.findLastVisibleItemPosition()
                    if (datas != null && pos == datas!!.size - 1 && hasMore) {
                        firstIndex = count + 1
                        initData()
                    }
                }


            }
        })


    }

    fun getCameraPermission(searchView: SearchView, menu: Menu) {

        Dexter.withContext(activity).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    activity?.let { it1 ->
                        BarcodeScannerFragment.newInstance(it1.supportFragmentManager,
                            object : BarcodeScannerFragment.OnSelect {
                                override fun setOnSelect(barcode: String) {

                                    if (searchView != null) {
                                        searchView.setQuery(barcode, true)
                                        searchView.isIconified = false
                                        menu.performIdentifierAction(R.id.search, 0);
                                        searchEditText!!.setText(barcode)
                                        searchEditText!!.selectAll()
                                    }

                                }

                            })
                    }
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    activity?.let {
                        Toasty.error(
                            it,
                            "مجوز دسترسی به دوربین به نرم افزار داده نشده است, لطفا لیست مجوزها را بررسی نمایید"
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                    return
                }

            }).onSameThread().check()


    }


    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {


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


            notifyDataSetChanged()
        }

        fun findProduct(searchedProduct: Product.Product): Product.Product? {


            if (datas.isNullOrEmpty())
                return null

            datas!!.forEach { item ->

                if (searchedProduct.code == item.code)
                    return item.copy()

            }

            return null

        }

        fun clearAll() {
            datas = null
            datasDump = null
            firstIndex = 0
            notifyDataSetChanged()

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val binding: RowSelectProduct2Binding =
                RowSelectProduct2Binding.inflate(LayoutInflater.from(context), parent, false)

            FontChanger().applyMainFont(binding.root)


            return ItemHolder(binding)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val hld: ItemHolder = holder as ItemHolder

            try {

                val info = datas?.get(position)


                hld.txtRow.setText((position + 1).toString())
                hld.txtName.setText(info?.name)
                hld.txtBarcode.setText(info?.barcode.toString())
                hld.lyHeader.setOnClickListener {
                    mBinding!!.lyProgres.lyProgress.visibility = View.VISIBLE
                    activity?.let { it1 ->
                        productViewModel.getProductPriceList(
                            it1,
                            SessionManagement.getInstance(it1)!!.getCashDeskId(), info!!.code
                        )
                    }
                }


                if (position % 2 == 0) {
                    holder.lyHeader.setBackgroundColor(Color.parseColor("#FFFFFF"))
                } else {
                    holder.lyHeader.setBackgroundColor(Color.parseColor("#ECEFF1"))
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


        inner class ItemHolder(itemView: RowSelectProduct2Binding) :
            RecyclerView.ViewHolder(itemView.root) {

            val lyHeader = itemView.lyHeader
            val txtRow = itemView.txtRow
            val txtName = itemView.txtName
            val txtBarcode = itemView.txtBarcode

        }

        override fun getFilter(): Filter {
            return MyFilter()
        }


        inner class MyFilter() : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val temp: ArrayList<Product.Product> = ArrayList()
                if (TextUtils.isEmpty(constraint)) {
                    val filter = FilterResults()
                    filter.values = datasDump
                    filter.count = 0
                    return filter
                }

                if (datasDump != null && datasDump!!.size !== 0) {
                    for (item in datasDump!!) {
                        try {
                            if (!TextUtils.isEmpty(item.name) && item.name!!.contains(constraint!!)
                            ) {
                                temp.add(item)
                            }
                        } catch (ex: java.lang.Exception) {
                            ex.printStackTrace()
                        }
                        try {
                            if (item.code.toString().contains(constraint!!)) temp.add(
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
                var temp: ArrayList<Product.Product>? = null

                try {
                    temp = results!!.values as ArrayList<Product.Product>
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

                if (temp == null) temp = ArrayList<Product.Product>()

                datas = temp

                notifyDataSetChanged()
            }

        }
    }


    override fun onDestroy() {
        mBinding = null
        super.onDestroy()

    }

//    companion object {
//
//        private const val KEY_SELECT_PRODUCT_FRAGMENT = "SELECT_PRODUCT_FRAGMENT"
//
//        @JvmStatic
//        fun newInstance(
//            title: String,
//            fragmentManager: FragmentManager,
//            selectMode: Boolean,
//            orderId: Long?,
//            fragment: Fragment,
//        ) =
//            SelectProductFragment().apply {
//                setTitle(title)
//                setOnSelect(onSelect)
//                setOnChange(onChange)
//                setIsSelectMode(selectMode)
//                setOrderId(orderId)
//                setOwnerFragment(fragment)
//
//                show(fragmentManager, KEY_SELECT_PRODUCT_FRAGMENT)
//            }
//
//
//    }


}