package com.zikey.android.razanstoreapp.ui.fragments

import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.FinalizeCashDeskFragmentBinding
import com.zikey.android.razanstoreapp.model.Payment
import com.zikey.android.razanstoreapp.model.Terminal
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import com.zikey.android.razanstoreapp.tools.CalendarWrapper
import com.zikey.android.razanstoreapp.ui.adapters.FinalizeCashDeskAdapter
import com.zikey.android.razanstoreapp.ui.adapters.OnItemClickListener
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomAlertDialog
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.viewmodel.FinalizeCashDeskViewModel
import es.dmoral.toasty.Toasty

class FinalizeCashDeskFragment : Fragment(), View.OnClickListener {

    private var _binding: FinalizeCashDeskFragmentBinding? = null

    private val viewModel: FinalizeCashDeskViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter: FinalizeCashDeskAdapter
    private var terminals: List<Terminal.Terminal>? = null
    private var cashDeskId = 0L
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FinalizeCashDeskFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initViews()
        initFinalizeCashDeskCreatorViewModel()
        initFinalizeCashDeskPaymentsViewModel()
        initFinalizeCashDeskPaymentsUpdateAmountViewModel()
        initCashDeskCalculatorViewModel()
        selectDate()


    }

    private fun initCashDeskCalculatorViewModel() {

        viewModel.calculateCashDeskResponse.observe(
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

                            showProgress(false)
                            findNavController().navigate(
                                FinalizeCashDeskFragmentDirections.actionFinalizeCashDeskFragmentToFinalizeCashDeskCalculatedFragment(
                                    cashDeskId,
                                    selectedDate
                                )
                            )
                            return@Observer
                        }


                    }

                }
            })

        viewModel.calculateCashDeskLoadingError.observe(
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

        viewModel.calculateCashDeskData.observe(
            viewLifecycleOwner,
            Observer { loadOrder ->
                loadOrder?.let {
                    Log.i("Order Loading", "$it")
                    showProgress(true)

                }
            })

    }


    private fun initFinalizeCashDeskPaymentsUpdateAmountViewModel() {

        terminals = null
        viewModel.finalizePaymentsUpdateAmountResponse.observe(
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

                            showProgress(false)
                            terminals = null
                            adapter.submitList(null)

                            viewModel.getFinalizePayments(
                                requireContext(),
                                finalizedCahDeskID = cashDeskId
                            )
                            return@Observer
                        }


                    }

                }
            })

        viewModel.finalizePaymentsUpdateAmountLoadingError.observe(
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

        viewModel.finalizePaymentsUpdateAmountData.observe(
            viewLifecycleOwner,
            Observer { loadOrder ->
                loadOrder?.let {
                    Log.i("Order Loading", "$it")
                    showProgress(true)

                }
            })

    }

    private fun initFinalizeCashDeskPaymentsViewModel() {

        terminals = null
        viewModel.finalizePaymentsResponse.observe(
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
                            if (it.terminals.isNullOrEmpty()) {
                                CustomDialogBuilder().showCustomAlert(
                                    requireActivity() as AppCompatActivity,
                                    "خطا",
                                    "خطا در دریافت اطلاعات صندوق",
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
                            adapter.submitList(it.terminals)
                            terminals = it.terminals

                            return@Observer
                        }


                    }

                }
            })

        viewModel.finalizePaymentsLoadingError.observe(
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

        viewModel.finalizePaymentsData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }

    private fun initFinalizeCashDeskCreatorViewModel() {

        viewModel.createFinalizeResponse.observe(
            viewLifecycleOwner,
            Observer { itemsResponse ->
                itemsResponse?.let {

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
                                        findNavController().navigate(
                                            FinalizeCashDeskFragmentDirections.actionDismiss()
                                        )

                                    }

                                    override fun onClickOutside(fragment: DialogFragment?) {
                                        fragment!!.dismiss()

                                    }

                                }
                            )
                            showProgress(false)
                            return@Observer
                        } else {
                            if (it.order == null || it.order!!.id == null || it.order!!.id == 0L) {
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
                            Toasty.success(requireContext(), it.order!!.id.toString()).show()
                            viewModel.getFinalizePayments(requireContext(), it.order!!.id!!)
                            cashDeskId = it.order!!.id!!
                            return@Observer
                        }


                    }

                }
            })

        viewModel.createFinalizeLoadingError.observe(
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

        viewModel.createFinalizeData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Order Loading", "$it")
                showProgress(true)

            }
        })

    }

    private fun selectDate() {
        CustomDialogBuilder().showDatePicker(activity as AppCompatActivity,
            { view, year, monthOfYear, dayOfMonth ->
                val selectedDate =
                    CalendarWrapper.toPersianFormat(year, monthOfYear, dayOfMonth)
                val dayName = CalendarWrapper.getNameOfDayPersianType(year, monthOfYear, dayOfMonth)
                Toasty.normal(requireContext(), selectedDate.toString()).show()
                this.selectedDate = selectedDate!!
                (activity as AppCompatActivity).supportActionBar!!.title =
                    dayName + " " + selectedDate
                selectedDate?.let {
                    viewModel.create(
                        requireContext(),
                        SessionManagement.instance!!.getCashDeskId(), it
                    )
                }

            }
        ) { findNavController().navigate(FinalizeCashDeskFragmentDirections.actionDismiss()) }
        return

    }

    private fun initViews() {
        FontChanger().applyMainFont(binding.root)
        FontChanger().applyTitleFont(binding.lyHeader)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = this@FinalizeCashDeskFragment.adapter
        }

        binding.btnSend.setOnClickListener(this)


    }

    private fun initAdapter() {

        adapter = FinalizeCashDeskAdapter(this, object : OnItemClickListener {
            override fun onRemove(terminal: Terminal.Terminal) {
                return
            }

            override fun onClick(terminal: Terminal.Terminal) {

                CustomDialogBuilder().showInputTextDialog_NumbersOnly(
                    requireActivity() as AppCompatActivity,
                    "موجودی" + " ${terminal.terminalType} ",
                    object : CustomDialogBuilder.OnDialogListener {
                        override fun onOK(input: String?) {

                            try {
                                input?.let {
                                    viewModel.getFinalizePaymentsUpdateAmounts(
                                        requireContext(), terminal.id,
                                        it
                                    )
                                }
                            } catch (e: Exception) {
                                Toasty.error(requireContext(), "فرمت مبلغ وارد شده نادردست میباشد")
                                    .show()
                                e.printStackTrace()
                            }


                        }

                        override fun onNeutral(input: String?) {
                            input?.let { Toasty.error(requireContext(), it).show() }
                        }

                    }
                )
                return
            }

        })
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btnSend -> {
                CustomDialogBuilder().showYesNOCustomAlert(
                    requireActivity() as AppCompatActivity,
                    "ثبت نهایی",
                    "مایل به ثبت نهایی میباشید؟",
                    "ثبت نهایی", "انصراف",
                    { fragment ->
                        fragment!!.dismiss()


                        viewModel.calculateCashDesk(requireContext(), cashDeskId)

                    }, null
                )

            }


        }

    }

    private fun showProgress(show: Boolean) {

        if (show) {
            binding.lyProgres.lyProgress.visibility = View.VISIBLE
            binding.rvItems.visibility = View.GONE
        } else {
            binding.lyProgres.lyProgress.visibility = View.GONE
            binding.rvItems.visibility = View.VISIBLE
        }

    }


}