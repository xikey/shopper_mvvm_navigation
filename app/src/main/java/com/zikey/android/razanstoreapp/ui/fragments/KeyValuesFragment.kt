package com.zikey.android.razanstoreapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.model.KeyValue


class KeyValuesFragment : DialogFragment() {

    private lateinit var onSelect: OnSelect
    private lateinit var lyRecyclerView: RecyclerView
    private lateinit var adapter: RecyclerAdapter
    private lateinit var title: String

    private var type: Int = 0

    lateinit var keyValues: ArrayList<KeyValue>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )


    }

    private fun initRecyclerView() {

        adapter = RecyclerAdapter()

        lyRecyclerView.isNestedScrollingEnabled = true

        lyRecyclerView.let {

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
        val view: View = inflater.inflate(R.layout.fragment_key_values, container, false)
        initViews(view)
        initRecyclerView()
        return view
    }

    private fun initData(inputData: ArrayList<KeyValue>) {

        keyValues = inputData

    }

    private fun initViews(view: View) {

        lyRecyclerView = view.findViewById(R.id.rvItems)

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        if (title.isNotEmpty()) {
            toolbar.setTitle(title)
        }

    }


    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.row_key_value,
                parent, false
            )

            val fontChanger = FontChanger()
            fontChanger.applyMainFont(itemView)


            return ItemHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val hld: ItemHolder = holder as ItemHolder



            try {

                val info = keyValues?.get(position)

                hld.txtRow.setText((position + 1).toString())
                hld.txtKey.setText(info.key)
                if (TextUtils.isEmpty(info.value)) {
                    hld.txtValue.setText("")
                } else
                    hld.txtValue.setText(info.value)

                hld.crdContainer.setOnClickListener {

                    if (onSelect != null) {
                        keyValues?.get(position)?.let { it1 ->
                            onSelect.setOnSelect(it1.value, it1.key, this@KeyValuesFragment)

                        }


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

            return if (keyValues.isNullOrEmpty())
                0
            else
                keyValues.size
        }


        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val crdContainer: CardView = itemView.findViewById(R.id.crdContainer)
            val txtRow: TextView = itemView.findViewById(R.id.txtRow)
            val txtKey: TextView = itemView.findViewById(R.id.txtKey)
            val txtValue: TextView = itemView.findViewById(R.id.txtValue)
            val lyHeader: LinearLayout = itemView.findViewById(R.id.lyHeader)

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

        private const val KEY_FRAGMENT = "KEY_FRAGMENT"

        @JvmStatic
        fun newInstance(
            title: String,
            fragmentManager: FragmentManager,
            keyValues: ArrayList<KeyValue>,
            onSelect: OnSelect
        ) =
            KeyValuesFragment().apply {
                setTitle(title)
                setOnSelect(onSelect)
                initData(keyValues)
                show(fragmentManager, KEY_FRAGMENT)
            }

    }


    interface OnSelect {
        fun setOnSelect(input: String, key: String, fragment: DialogFragment)
    }

}