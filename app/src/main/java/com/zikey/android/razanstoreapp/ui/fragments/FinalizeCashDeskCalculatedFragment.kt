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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.FinalizeCashDeskCalculatedFragmentBinding
import com.zikey.android.razanstoreapp.databinding.FinalizeCashDeskFragmentBinding
import com.zikey.android.razanstoreapp.model.Payment
import com.zikey.android.razanstoreapp.model.Terminal
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import com.zikey.android.razanstoreapp.tools.CalendarWrapper
import com.zikey.android.razanstoreapp.ui.adapters.FinalizeCashDeskAdapter
import com.zikey.android.razanstoreapp.ui.adapters.FinalizeCashDeskCardFormAdapter
import com.zikey.android.razanstoreapp.ui.adapters.OnItemClickListener
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomAlertDialog
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.viewmodel.FinalizeCashDeskViewModel
import es.dmoral.toasty.Toasty

class FinalizeCashDeskCalculatedFragment : Fragment() {

    private var _binding: FinalizeCashDeskCalculatedFragmentBinding? = null

    private val viewModel: FinalizeCashDeskViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter: FinalizeCashDeskCardFormAdapter
    private var terminals: List<Terminal.Terminal>? = null
    private var cashDeskId = 0L
    private var selectedDate: String = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val args: FinalizeCashDeskCalculatedFragmentArgs by navArgs()
        cashDeskId = args.cashDeskId
        selectedDate = args.date
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FinalizeCashDeskCalculatedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initViews()
        initFinalizeCashDeskPaymentsViewModel()
        loadData()

    }

    private fun loadData() {
        viewModel.getFinalizePayments(requireContext(), cashDeskId)
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


    private fun initViews() {
        FontChanger().applyMainFont(binding.root)
        (activity as AppCompatActivity).supportActionBar!!.title ="پیگیری: "+cashDeskId
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = this@FinalizeCashDeskCalculatedFragment.adapter
        }


    }

    private fun initAdapter() {

        adapter = FinalizeCashDeskCardFormAdapter(this)
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