package com.zikey.android.razanstoreapp.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.FragmentSelectStoreRoomBinding
import com.zikey.android.razanstoreapp.model.StoreRoom
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import com.zikey.android.razanstoreapp.viewmodel.StoreRoomViewModel
import es.dmoral.toasty.Toasty


class SelectStoreRoomFragment : DialogFragment() {

    private lateinit var onSelect: OnSelect
    private lateinit var adapter: RecyclerAdapter
    private lateinit var title: String
    private lateinit var searchView: SearchView
    private val storeRoomViewModel: StoreRoomViewModel by viewModels()
    private var _binding: FragmentSelectStoreRoomBinding? = null
    private var type: Int = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var count = 100
    private var firstIndex = 0
    private var keySearch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(
            androidx.fragment.app.DialogFragment.STYLE_NO_FRAME,
            android.R.style.Theme_Light_NoTitleBar
        );
        super.onCreate(savedInstanceState)

        arguments?.let {

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.getWindow()?.getAttributes()!!.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )


    }

    private fun initRecyclerView() {

        adapter = RecyclerAdapter()

       binding.rvItems.isNestedScrollingEnabled = true

        binding.rvItems.let {

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
        // Inflate the layout for this fragment
        _binding = FragmentSelectStoreRoomBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        storeRoomViewModelObserver()
        initRecyclerView()
        initData()

    }

    private fun initData() {
        val cashDeskId = SessionManagement.instance!!.getCashDeskId()

        storeRoomViewModel.getStoreRooms(requireContext(), cashDeskId)

    }

    private fun initViews() {


        FontChanger().applyTitleFont(binding.lyHeader)

        val toolbar: Toolbar = binding.toolbar
        val menu = toolbar.menu
        val searchView: SearchView = menu.findItem(R.id.search).actionView as SearchView
        val edittext = searchView.findViewById<EditText>(R.id.search_src_text)
        edittext.setTextColor(Color.parseColor("#FFFFFF"))


        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        if (title.isNotEmpty()) {
            toolbar.setTitle(title)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (adapter != null) {
                    try {
                        adapter.filter.filter(newText)
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                }
                return true
            }

        })

    }


    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {


        private var datas: List<StoreRoom.StoreRoom>? = null
        private var datasDump: List<StoreRoom.StoreRoom>? = null


        fun setDatas(data: List<StoreRoom.StoreRoom>?) {
            datas = null
            datasDump = null
            datas = data
            datasDump = data

            notifyDataSetChanged()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.row_store_room,
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


                hld.txtRow.setText((position + 1).toString())
                hld.txtName.setText(info?.name)
                hld.txtType.setText(info?.type)
                hld.txtCode.setText((info?.code).toString())
                //  hld.txtName.setText(datas?.get(position).)

                hld.lyHeader.setOnClickListener {

                    if (onSelect != null) {
                        datas?.get(position)?.let { it1 ->
                            onSelect.setOnSelect(info?.code!!, info.name!!)
                            Toasty.success(activity!!, info.name!!).show()
                        }

                        dismiss()
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


        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val lyHeader: LinearLayout = itemView.findViewById(R.id.lyHeader)
            val txtRow: TextView = itemView.findViewById(R.id.txtRow)
            val txtCode: TextView = itemView.findViewById(R.id.txtCode)
            val txtName: TextView = itemView.findViewById(R.id.txtName)
            val txtType: TextView = itemView.findViewById(R.id.txtType)


        }

        override fun getFilter(): Filter {
            return MyFilter()
        }


        inner class MyFilter() : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val temp: ArrayList<StoreRoom.StoreRoom> = ArrayList()
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
                var temp: ArrayList<StoreRoom.StoreRoom>? = null

                try {
                    temp = results!!.values as ArrayList<StoreRoom.StoreRoom>
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

                if (temp == null) temp = ArrayList<StoreRoom.StoreRoom>()

                datas = temp

                notifyDataSetChanged()
            }

        }

    }


    fun setTitle(input: String) {
        title = input
    }

    fun setType(input: Int) {
        type = input
    }

    fun setOnSelect(listener: OnSelect) {
        onSelect = listener
    }


    companion object {

        private const val KEY_STORE_ROOMS_FRAGMENT = "STORE_ROOMS_FRAGMENT"

        @JvmStatic
        fun newInstance(title: String, fragmentManager: FragmentManager, onSelect: OnSelect) =
            SelectStoreRoomFragment().apply {
                setTitle(title)
                setOnSelect(onSelect)

                show(fragmentManager, KEY_STORE_ROOMS_FRAGMENT)
            }

    }


    interface OnSelect {
        fun setOnSelect(code: Int, name: String)
    }

    private fun storeRoomViewModelObserver() {
        storeRoomViewModel.storeRoomsListResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                response?.let {

                    Log.i("Response ", "${response}")
                    if (!response.storeRooms.isNullOrEmpty()) {

                        adapter.setDatas(response.storeRooms)
                        binding.lyProgres.lyProgress.visibility ?: View.GONE
                    } else {
                        Toasty.error(requireContext(), "اطلاعاتی جهت دریافت وججود ندارد").show()
                        dismiss()
                    }

                }
            })

        storeRoomViewModel.storeRoomsLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Cash Desk API Error", "$dataError")
                    if (dataError) {
                        binding.lyProgres?.lyProgress?.visibility = View.GONE
                        Toasty.error(
                            requireContext(),
                            "خطا در دریافت اطلاعات. لطفا مجددا تلاش نمایید"
                        ).show()
                        dismiss()
                    }

                }
            })

        storeRoomViewModel.loadStoreRooms.observe(viewLifecycleOwner, Observer { loadCashDesk ->
            loadCashDesk?.let {
                Log.i("Product Loading", "$loadCashDesk")
                binding?.lyProgres?.lyProgress?.visibility ?: View.VISIBLE

            }
        })
    }


}