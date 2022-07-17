package com.zikey.android.razanstoreapp.ui.fragments

import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.PaymentFragmentBinding
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.Payment
import com.zikey.android.razanstoreapp.model.Terminal
import com.zikey.android.razanstoreapp.tools.NumberSeperator
import com.zikey.android.razanstoreapp.ui.adapters.OnPaymentRemove
import com.zikey.android.razanstoreapp.ui.adapters.OrderPaymentAdapter
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomAlertDialog
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.viewmodel.PaymentViewModel
import es.dmoral.toasty.Toasty

class PaymentFragment : Fragment(), View.OnClickListener, SubmitPaymentFragment.OnAddListener {


    private var _binding: PaymentFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: PaymentViewModel by viewModels()
    lateinit var adapter: OrderPaymentAdapter
    var orderId = 0L
    var orderTotalPrice = 0L
    var orderPaidPrice = 0L
    var terminals: List<Terminal.Terminal>? = null
    var orderDetails: Order.OrderAnswer? = null
    var payments: List<Payment.Payment>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val args: PaymentFragmentArgs by navArgs()
        orderId = args.factorId
        orderTotalPrice = args.factorTotalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PaymentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initViews()
        initGetOrderPaymentsViewModel()
        initGetTerminalsViewModel()
        initSubmitPaymentViewModel()
        initFinalizeFactorViewModel()
        initOrderGetDataViewModel()
        initDeletePaymentRowViewModel()
        getData()
        getOrderDetail()
        getTerminals()
    }


    private fun getOrderDetail() {

        viewModel.getOrderDetail(requireContext(), orderId)

    }

    private fun getTerminals() {

        viewModel.getTerminals(requireContext())
    }

    private fun getData() {
        initHeader(null)
        adapter.submitList(null)
        viewModel.get(requireContext(), orderId)
    }

    private fun initViews() {
        FontChanger().applyMainFont(binding.root)
        binding.txtFactorPrice.setText(NumberSeperator.separate(orderTotalPrice))

        FontChanger().applyTitleFont(binding.lyContent.lyHeader)
        binding.lyContent.rvItem.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = this@PaymentFragment.adapter
        }

        binding.fabSelectPayment.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)

    }

    private fun initAdapter() {

        adapter = OrderPaymentAdapter(this, object : OnPaymentRemove {
            override fun onRemove(payment: Payment.Payment) {

                CustomDialogBuilder().showYesNOCustomAlert(
                    requireActivity() as AppCompatActivity,
                    "حذف ردیف",
                    "مایل به حذف ردیف پرداختی میباشید؟",
                    "حذف", "انصراف", object : CustomAlertDialog.OnActionClickListener {
                        override fun onClick(fragment: DialogFragment?) {
                            viewModel.deletePaymentRow(requireContext(), orderId, payment.id)
                            fragment!!.dismiss()
                        }

                    }, null
                )


            }

        })
    }


    private fun showProgress(show: Boolean) {

        if (show) {
            binding.lyProgres.lyProgress.visibility = View.VISIBLE
            binding.lyContent.lyContent.visibility = View.GONE
        } else {
            binding.lyProgres.lyProgress.visibility = View.GONE
            binding.lyContent.lyContent.visibility = View.VISIBLE
        }

    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.fab_select_payment -> {
                showTerminalSelectorDialog()
            }
            R.id.btnSend -> {
                CustomDialogBuilder().showYesNOCustomAlert(
                    requireActivity() as AppCompatActivity,
                    "ثبت نهایی",
                    "مایل به ثبت نهایی میباشید؟",
                    "ثبت نهایی", "انصراف",
                    { fragment ->
                        fragment!!.dismiss()
                        viewModel.finalizeFactor(requireContext(), orderId)


                    }, null
                )

            }


        }

    }

    private fun initOrderGetDataViewModel() {
        orderDetails = null

        viewModel.factorResponse.observe(
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
                                        findNavController().navigate(PaymentFragmentDirections.actionDismiss())
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
                                            findNavController().navigate(PaymentFragmentDirections.actionDismiss())
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
                            orderDetails = it
                            return@Observer
                        }


                    }

                }
            })

        viewModel.factorLoadingError.observe(
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

        viewModel.loadFactorData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }


    private fun initGetOrderPaymentsViewModel() {

        payments = null
        viewModel.dataResponse.observe(
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

                                    }

                                    override fun onClickOutside(fragment: DialogFragment?) {
                                        fragment!!.dismiss()

                                    }

                                }
                            )
                            showProgress(false)
                            return@Observer
                        } else {
                            if (it.order == null || it.order!!.payments.isNullOrEmpty()) {
                                CustomDialogBuilder().showCustomAlert(
                                    requireActivity() as AppCompatActivity,
                                    "خطا",
                                    "اطلاعاتی جهت نمایش وجود ندارد",
                                    "بستن",
                                    object : CustomAlertDialog.OnCancelClickListener {
                                        override fun onClickCancel(fragment: DialogFragment?) {
                                            fragment!!.dismiss()

                                        }

                                        override fun onClickOutside(fragment: DialogFragment?) {
                                            fragment!!.dismiss()

                                        }

                                    }
                                )

                                return@Observer
                            }
                            showProgress(false)
                            adapter.submitList(it.order!!.payments)
                            payments = it.order!!.payments
                            initHeader(it.order!!)
//                            adapter.submitList(it.order!!.orderedProducts)
                            return@Observer
                        }


                    }

                }
            })

        viewModel.dataLoadingError.observe(
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

        viewModel.loadData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }

    private fun initDeletePaymentRowViewModel() {

        viewModel.deletePaymentRowResponse.observe(
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
                                        findNavController().navigate(PaymentFragmentDirections.actionDismiss())

                                    }

                                    override fun onClickOutside(fragment: DialogFragment?) {
                                        fragment!!.dismiss()

                                    }

                                }
                            )
                            showProgress(false)
                            return@Observer
                        } else {
                            showProgress(false)
                            getData()
                            return@Observer
                        }


                    }

                }
            })

        viewModel.deletePaymentRowLoadingError.observe(
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

        viewModel.deletePaymentRowData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }

    private fun initSubmitPaymentViewModel() {

        viewModel.submitPaymentResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                response?.let {

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

                                    }

                                    override fun onClickOutside(fragment: DialogFragment?) {
                                        fragment!!.dismiss()

                                    }

                                }
                            )
                            showProgress(false)
                            return@Observer
                        } else {

                            getData()
                            Toasty.success(requireContext(), "ثبت اطلاعات پرداخت").show()
                            return@Observer
                        }


                    }

                }
            })

        viewModel.submitPaymentLoadingError.observe(
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

        viewModel.loadSubmitPaymentData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }

    private fun initFinalizeFactorViewModel() {

        viewModel.finalizeFactorResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                response?.let {

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

                                    }

                                    override fun onClickOutside(fragment: DialogFragment?) {
                                        fragment!!.dismiss()

                                    }

                                }
                            )
                            showProgress(false)
                            return@Observer
                        } else {
                            Print_SamanKish_Fragment.Show(this, orderDetails, payments)
                            Toasty.success(requireContext(), "ثبت اطلاعات ").show()
                            return@Observer
                        }


                    }

                }
            })

        viewModel.finalizeFactorLoadingError.observe(
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

        viewModel.loadFinalizeFactorData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }

    private fun initGetTerminalsViewModel() {

        viewModel.paymentTypeResponse.observe(
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
                                        findNavController().navigate(PaymentFragmentDirections.actionDismiss())

                                    }

                                    override fun onClickOutside(fragment: DialogFragment?) {
                                        fragment!!.dismiss()

                                    }

                                }
                            )
                            showProgress(false)
                            return@Observer
                        } else {
                            if (it.terminals == null || it.terminals!!.isNullOrEmpty()) {
                                CustomDialogBuilder().showCustomAlert(
                                    requireActivity() as AppCompatActivity,
                                    "خطا",
                                    "اطلاعاتی جهت نمایش وجود ندارد",
                                    "بستن",
                                    object : CustomAlertDialog.OnCancelClickListener {
                                        override fun onClickCancel(fragment: DialogFragment?) {
                                            fragment!!.dismiss()
                                            findNavController().navigate(PaymentFragmentDirections.actionDismiss())
                                        }

                                        override fun onClickOutside(fragment: DialogFragment?) {
                                            fragment!!.dismiss()

                                        }

                                    }
                                )

                                return@Observer
                            }
                            showProgress(false)
                            terminals = it.terminals
//                            adapter.submitList(it.order!!.orderedProducts)
                            return@Observer
                        }


                    }

                }
            })

        viewModel.paymentTypeLoadingError.observe(
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

        viewModel.loadPaymentTypeData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }


    fun initHeader(order: Order.Order?) {
        if (order == null) {
            binding.txtTotalPayment.setText("0")
            return
        }

        if (order!!.payments.isNullOrEmpty()) {
            binding.txtTotalPayment.setText("0")
            return
        }

        var totalPaid = 0L

        order.payments!!.forEach() {

            totalPaid += it.price
        }
        orderPaidPrice = totalPaid

        binding.txtTotalPayment.setText(NumberSeperator.separate(totalPaid))
    }

    fun showTerminalSelectorDialog() {


        if (terminals.isNullOrEmpty())
            return

        val items: MutableList<String> = mutableListOf()

        terminals!!.forEach {

            items += it.terminalName.toString()
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.text_selector_list_item, items)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("ترمینال ها")
            .setAdapter(
                adapter
            ) { dialog, which ->
                SubmitPaymentFragment.Show(
                    requireActivity(), terminals!![which],
                    (orderTotalPrice - orderPaidPrice).toDouble(), this
                )
                Toasty.success(requireContext(), terminals!![which].terminalName).show()
            }

            .show()
    }

    override fun onAdd(terminal: Terminal.Terminal?, price: Double) {

        if (terminal == null)
            return

        if (price <= 0)
            return
        val payment = Payment.Payment(
            orderId,
            price.toLong(),
            0,
            terminal.id,
            terminal.terminalType,
            terminal.terminalName,
            terminal.bank,
            ""
        )

        viewModel.submitPayment(requireContext(), payment)
        Toasty.success(requireContext(), "Add").show()

    }


}