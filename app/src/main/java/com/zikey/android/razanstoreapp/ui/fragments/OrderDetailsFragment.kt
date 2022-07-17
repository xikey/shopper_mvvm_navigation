package com.zikey.android.razanstoreapp.ui.fragments

import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.OrderDetailsFragmentBinding
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.Order.OrderAnswer
import com.zikey.android.razanstoreapp.model.OrderedProduct
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import com.zikey.android.razanstoreapp.model.tools.SessionManagement.Companion.instance
import com.zikey.android.razanstoreapp.tools.NumberSeperator
import com.zikey.android.razanstoreapp.ui.adapters.OnProductChange
import com.zikey.android.razanstoreapp.ui.adapters.OrderAdapter
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomAlertDialog
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.viewmodel.OrderDetailsViewModel
import es.dmoral.toasty.Toasty
import java.util.ArrayList

class OrderDetailsFragment : Fragment(), View.OnClickListener {


    private var _binding: OrderDetailsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter: OrderAdapter

    private val orderViewModel: OrderDetailsViewModel by viewModels()

    var orderId = 0L
    var orderTotalPrice = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val args: OrderDetailsFragmentArgs by navArgs()
        orderId = args.factorId

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OrderDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViews()

        initOrderGetDataViewModel()
        initEditOrderViewModel()
        initDeleteOrderViewModel()
        getOrder()
    }

    private fun initAdapter() {

        adapter = OrderAdapter(this, object : OnProductChange {
            override fun onChange(orderedProduct: OrderedProduct.OrderedProduct) {
                orderedProduct.fi = orderedProduct.product!!.price1.toLong()
                val orderedProducts = ArrayList<OrderedProduct.OrderedProduct>()
                orderedProducts.add(orderedProduct)
                val order = Order.Order(
                    orderId,
                    null,
                    null,
                    null,
                    null,
                    orderedProducts,
                    null,
                    null,
                    null,
                    null,
                    null,
                    instance!!.getCashDeskId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,0.0
                )

                orderViewModel.edidProduct(requireContext(), OrderAnswer(order))

            }

            override fun onRemove(orderedProduct: OrderedProduct.OrderedProduct) {
                orderedProduct.fi = orderedProduct.product!!.price1.toLong()
                val orderedProducts = ArrayList<OrderedProduct.OrderedProduct>()
                orderedProducts.add(orderedProduct)
                val order = Order.Order(
                    orderId,
                    null,
                    null,
                    null,
                    null,
                    orderedProducts,
                    null,
                    null,
                    null,
                    null,
                    null,
                    instance!!.getCashDeskId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,0.0
                )

                orderViewModel.delete(requireContext(), OrderAnswer(order))
            }

        })
    }

    private fun getOrder() {

        initHeaders(null)

        orderViewModel.get(
            requireContext(),
            orderId = orderId
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_factor, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_delete -> {

                CustomDialogBuilder().showYesNOCustomAlert(
                    requireActivity() as AppCompatActivity,
                    "حذف فاکتور",
                    "مایل به حذف فاکتور میباشید؟",
                    "حذف",
                    "انصراف",
                    { fragment ->
                        fragment!!.dismiss()

                        val orderedProducts = ArrayList<OrderedProduct.OrderedProduct>()
                        orderedProducts.add(
                            OrderedProduct.OrderedProduct(
                                null,
                                null,
                                null,
                                0,
                                "",
                                0,
                                false
                            )
                        )

                        val order = Order.Order(
                            orderId,
                            null,
                            null,
                            null,
                            null,
                            orderedProducts,
                            null,
                            null,
                            null,
                            null,
                            null,
                            instance!!.getCashDeskId(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,0.0
                        )

                        orderViewModel.delete(requireContext(), OrderAnswer(order))
                    },
                    null
                )


                return true
            }


        }

        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        FontChanger().applyMainFont(binding.root)
        FontChanger().applyTitleFont(binding.lyContent.lyHeader)
        binding.lyContent.rvItem.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = this@OrderDetailsFragment.adapter
        }

        binding.fabSelectProduct.setOnClickListener(this)
        binding.btnPay.setOnClickListener(this)
    }

    private fun initOrderGetDataViewModel() {

        if (orderViewModel.dataResponse.hasObservers())
            return


        orderViewModel.dataResponse.observe(
            viewLifecycleOwner,
            Observer { ordersResponse ->
                ordersResponse?.let {

                    if (it != null) {
                        if (it.getIsSuccess() != 1) {
                            CustomDialogBuilder().showCustomAlert(
                                requireActivity() as AppCompatActivity,
                                "خطا",
                                it.getMessage(),
                                "بستن",
                                object : CustomAlertDialog.OnCancelClickListener {
                                    override fun onClickCancel(fragment: DialogFragment?) {
                                        fragment!!.dismiss()
                                        findNavController().navigate(OrderDetailsFragmentDirections.actionDismiss())
                                    }

                                    override fun onClickOutside(fragment: DialogFragment?) {
                                        fragment!!.dismiss()
                                        // findNavController().navigate(OrdersFragmentDirections.actionDismiss())
                                    }

                                }
                            )
                            showProgress(false)
                            return@Observer
                        } else {
                            if (it.order == null || it.order!!.orderedProducts.isNullOrEmpty()) {
                                CustomDialogBuilder().showCustomAlert(
                                    requireActivity() as AppCompatActivity,
                                    "خطا",
                                    "اطلاعاتی جهت نمایش وجود ندارد",
                                    "بستن",
                                    object : CustomAlertDialog.OnCancelClickListener {
                                        override fun onClickCancel(fragment: DialogFragment?) {
                                            fragment!!.dismiss()
                                            findNavController().navigate(
                                                OrderDetailsFragmentDirections.actionDismiss()
                                            )
                                        }

                                        override fun onClickOutside(fragment: DialogFragment?) {
                                            fragment!!.dismiss()
                                            // findNavController().navigate(OrdersFragmentDirections.actionDismiss())
                                        }

                                    }
                                )

                                return@Observer
                            }
                            showProgress(false)
                            initHeaders(it.order!!)
                            adapter.submitList(it.order!!.orderedProducts)
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
                        showProgress(false)
                        CustomDialogBuilder().showCustomAlert(
                            requireActivity() as AppCompatActivity,
                            "خطا",
                            "${dataError} \n\n خطا در دریافت اطلاعات",
                            "بستن"
                        )

                    }

                }
            })

        orderViewModel.loadData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }

    private fun initEditOrderViewModel() {

        if (orderViewModel.editDataResponse.hasObservers())
            return


        orderViewModel.editDataResponse.observe(
            viewLifecycleOwner,
            Observer { ordersResponse ->
                ordersResponse?.let {
                    showProgress(false)
                    Log.i("Response ", "${it}")

                    if (it != null) {
                        if (it.getIsSuccess() != 1) {
                            CustomDialogBuilder().showCustomAlert(
                                requireActivity() as AppCompatActivity,
                                "خطا",
                                it.getMessage(),
                                "بستن"
                            )
                            return@Observer
                        } else {
                            Toasty.success(requireContext(), "ویرایش فاکتور با موفقیت انجام شد")
                                .show()
                            adapter.submitList(null)
                            adapter.notifyDataSetChanged()
                            getOrder()
                        }


                    }

                }
            })

        orderViewModel.editDataLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    showProgress(false)
                    Log.i("Order API Error", "$dataError")
                    if (dataError) {

                        CustomDialogBuilder().showCustomAlert(
                            requireActivity() as AppCompatActivity,
                            "خطا",
                            "${dataError} \n\n خطا در دریافت اطلاعات",
                            "بستن"
                        )

                    }

                }
            })

        orderViewModel.loadEditDataData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("edit order Loading", "$it")
                showProgress(true)


            }
        })

    }


    private fun initDeleteOrderViewModel() {

        if (orderViewModel.deleteDataResponse.hasObservers())
            return

        orderViewModel.deleteDataResponse.observe(
            viewLifecycleOwner,
            Observer { ordersResponse ->
                ordersResponse?.let {
                    showProgress(false)
                    Log.i("Response ", "${it}")

                    if (it != null) {
                        if (it.getIsSuccess() != 1) {
                            CustomDialogBuilder().showCustomAlert(
                                requireActivity() as AppCompatActivity,
                                "خطا",
                                it.getMessage(),
                                "بستن"
                            )
                            return@Observer
                        } else {
                            Toasty.success(requireContext(), "ویرایش فاکتور با موفقیت انجام شد")
                                .show()
                            adapter.submitList(null)
                            adapter.notifyDataSetChanged()
                            getOrder()
                        }


                    }

                }
            })

        orderViewModel.deleteDataLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    showProgress(false)
                    Log.i("Delete API Error", "$dataError")
                    if (dataError) {

                        CustomDialogBuilder().showCustomAlert(
                            requireActivity() as AppCompatActivity,
                            "خطا",
                            "${dataError} \n\n خطا در دریافت اطلاعات",
                            "بستن"
                        )

                    }

                }
            })

        orderViewModel.loadDeleteDataData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Delete order Loading", "$it")
                showProgress(true)

            }
        })

    }


    private fun initHeaders(order: Order.Order?) {

        binding.txtRowCount.setText(getString(R.string.dots))
        binding.txtTotalCount.setText(getString(R.string.dots))
        binding.txtTotalDiscount.setText(getString(R.string.dots))
        binding.txtTotalPrice.setText(getString(R.string.dots))
        orderTotalPrice = 0;
        if (order == null)
            return

        try {
            var totalCount = 0.0
            var totalDiscount = 0.0
            order.orderedProducts!!.forEach {
                totalCount += it.count!!
                totalDiscount += it.product!!.cashDiscount
            }
            orderTotalPrice = order.totalPrice!!
            binding.txtRowCount.setText(order.orderedProducts!!.size.toString())
            binding.txtTotalCount.setText(totalCount.toString())
            binding.txtTotalDiscount.setText(NumberSeperator.separate(totalDiscount))
            binding.txtTotalPrice.setText(NumberSeperator.separate(order.totalPrice!!))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.fab_select_product -> {
                findNavController().navigate(
                    OrderDetailsFragmentDirections.actionOrderDetailsFragmentToSelectProductFragment(
                        orderId
                    )
                )
            }

            R.id.btnPay -> {
                if (orderTotalPrice <= 0) {
                    Toasty.error(requireContext(), "مبلغ فاکتور قابل پرداخت نمیباشد!").show()
                    return
                }
                findNavController().navigate(
                    OrderDetailsFragmentDirections.actionOrderDetailsFragmentToPaymentFragment(
                        orderId, orderTotalPrice
                    )
                )
            }


        }

    }

    fun showProgress(show: Boolean) {
        if (show) {
            binding.lyProgres.lyProgress.visibility = View.VISIBLE
            binding.lyContent.lyContent.visibility = View.GONE
        } else {
            binding.lyProgres.lyProgress.visibility = View.GONE
            binding.lyContent.lyContent.visibility = View.VISIBLE
        }

    }

}