package com.zikey.android.razanstoreapp.ui.fragments

import android.app.DialogFragment
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.OrdersFragmentBinding
import com.zikey.android.razanstoreapp.model.Order
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import com.zikey.android.razanstoreapp.ui.adapters.OrdersAdapter
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomAlertDialog
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.viewmodel.OrdersViewModel
import es.dmoral.toasty.Toasty

class OrdersFragment : Fragment() {

    private var _binding: OrdersFragmentBinding? = null


    private val binding get() = _binding!!
    var adapter: OrdersAdapter = OrdersAdapter(this)


    private val orderViewModel: OrdersViewModel by viewModels()

    lateinit var noFilteredList: List<Order.Order>
    lateinit var filteredList: List<Order.Order>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OrdersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOrdersViewModel()
        initViews()
        getData()
    }

    private fun initViews() {
        FontChanger().applyTitleFont(binding.lyHeader)
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = this@OrdersFragment.adapter
        }

    }

    private fun getData() {

        SessionManagement.getInstance(requireContext())?.let {
            orderViewModel.get(
                requireContext(),
                it.getCashDeskId()
            )
        }
    }

    private fun initOrdersViewModel() {

        orderViewModel.dataResponse.observe(
            viewLifecycleOwner,
            Observer { ordersResponse ->
                ordersResponse?.let {

                    Log.i("Response ", "${it}")

                    if (it != null) {
                        if (it.getIsSuccess() != 1) {
                            CustomDialogBuilder().showCustomAlert(
                                requireActivity() as AppCompatActivity,
                                "خطا",
                                it.getMessage(),
                                "بستن"
                            )
                            binding!!.lyProgres.lyProgress.visibility = View.GONE
                            return@Observer
                        } else {
                            if (it.orders.isNullOrEmpty()) {
                                CustomDialogBuilder().showCustomAlert(
                                    requireActivity() as AppCompatActivity,
                                    "خطا",
                                    "اطلاعاتی جهت نمایش وجود ندارد",
                                    "بستن",
                                    object : CustomAlertDialog.OnCancelClickListener {
                                        override fun onClickCancel(fragment: DialogFragment?) {
                                            fragment!!.dismiss()
                                            findNavController().navigate(OrdersFragmentDirections.actionDismiss())
                                        }

                                        override fun onClickOutside(fragment: DialogFragment?) {
                                            fragment!!.dismiss()
                                            // findNavController().navigate(OrdersFragmentDirections.actionDismiss())
                                        }

                                    }
                                )

                                return@Observer
                            }

                            binding!!.lyProgres.lyProgress.visibility = View.GONE
                            noFilteredList = it.orders!!
                            filteredList=noFilteredList.toList()
                            adapter.submitList(noFilteredList)
                            return@Observer
                        }


                    }

                }
            })

        orderViewModel.dataLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Orders API Error", "$dataError")
                    if (dataError) {
                        binding!!.lyProgres.lyProgress.visibility = View.GONE
                        CustomDialogBuilder().showCustomAlert(
                            requireActivity() as AppCompatActivity,
                            "خطا",
                            "${dataError} \n\n خطا در ورود کاربر",
                            "بستن"
                        )

                    }

                }
            })

        orderViewModel.loadData.observe(viewLifecycleOwner, Observer { loadOrder ->
            loadOrder?.let {
                Log.i("Orders Loading", "$it")
                binding!!.lyProgres.lyProgress.visibility = View.VISIBLE

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val myActionMenuItem = menu.findItem(R.id.search)
        val searchView = myActionMenuItem.actionView as SearchView
        val searchEditText = searchView.findViewById<View>(R.id.search_src_text) as EditText
        searchEditText!!.setTextColor(Color.parseColor("#FFFFFF"))
        searchEditText!!.setHint("جستجو")
        searchEditText!!.setHintTextColor(Color.parseColor("#FFFFFF"))


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    adapter.submitList(noFilteredList)
                } else {

                    filteredList=noFilteredList.filter { it.customerTell.toString().contains(newText!!) }
                    adapter.submitList(filteredList)

                }

                Toasty.error(requireActivity(), "searched").show()
                return true
            }

        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.search -> {

                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }


}

