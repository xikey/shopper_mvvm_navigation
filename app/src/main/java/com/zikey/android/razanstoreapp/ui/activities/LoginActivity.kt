package com.zikey.android.razanstoreapp.ui.activities

import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razanpardazesh.com.resturantapp.tools.FontChanger
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.databinding.ActivityLoginBinding
import com.zikey.android.razanstoreapp.model.CashDesk
import com.zikey.android.razanstoreapp.model.Member
import com.zikey.android.razanstoreapp.model.tools.SessionManagement
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomAlertDialog
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder
import com.zikey.android.razanstoreapp.viewmodel.CashDeskViewModel
import com.zikey.android.razanstoreapp.viewmodel.MemberViewModel
import es.dmoral.toasty.Toasty

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private  val mCashDeskViewModel: CashDeskViewModel  by viewModels()
    private val mMemberViewModel: MemberViewModel by viewModels()
    private var cashDeskList: List<CashDesk.CashDesk>? = null
    private var selectedCashDesk: CashDesk.CashDesk? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }




    override fun onStart() {
        super.onStart()
        initFont()
        initViews()
        initViewModel()
        initUserDatas()
    }

    private fun initUserDatas() {

        val setting = SessionManagement.getInstance(this)?.loadMember() ?: return

        if (setting.code_markaz != 0L) {
            binding.txtUser.setText(setting.code_markaz.toString())
        }

    }


    private fun initViewModel() {


        mCashDeskViewModel.getCashDeskFromApi(this, 0)

        cashDeskViewModelObserver()
        memberViewModelObserver()


    }

    private fun memberViewModelObserver() {
        mMemberViewModel.dataResponse.observe(
            this,
            Observer { memberResponse ->
                memberResponse?.let {

                    Log.i("MemberResponse ", "${memberResponse}")

                    if (memberResponse != null) {
                        if (!TextUtils.isEmpty(memberResponse.getMessage())) {
                            CustomDialogBuilder().showCustomAlert(
                                this,
                                "خطا",
                                memberResponse.getMessage(),
                                "بستن"
                            )
                            binding.lyProgres.lyProgress.visibility = View.GONE
                            return@Observer
                        }
                        if (memberResponse.member == null || TextUtils.isEmpty(memberResponse.member.name)) {
                            CustomDialogBuilder().showCustomAlert(
                                this,
                                "خطا",
                                "خطا در دریافت اطلاعات کاربر",
                                "بستن"
                            )
                            binding.lyProgres.lyProgress.visibility = View.GONE
                            return@Observer

                        }

                        memberResponse.member.cashDeskId = selectedCashDesk!!.id
                        memberResponse.member.cashDeskName = selectedCashDesk!!.name
                        SessionManagement.getInstance(this)
                            ?.saveMemberData(this, memberResponse.member)
                        MainActivity.start(this)
                     //   binding.lyProgres.lyProgress.visibility = View.GONE
                        finish()
                    }

                }
            })

        mMemberViewModel.dataLoadingError.observe(
            this,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Member API Error", "$dataError")
                    if (dataError) {
                        binding.lyProgres.lyProgress.visibility = View.GONE
                        CustomDialogBuilder().showCustomAlert(
                            this,
                            "خطا",
                            "${dataError} \n\n خطا در ورود کاربر",
                            "بستن"
                        )

                    }

                }
            })

        mMemberViewModel.loadData.observe(this, Observer { loadMember ->
            loadMember?.let {
                Log.i("member Loading", "$loadMember")
                binding.lyProgres.lyProgress.visibility = View.VISIBLE


            }
        })

    }

    /**
     * A function to get the data in the observer after the API is triggered.
     */
    private fun cashDeskViewModelObserver() {

        mCashDeskViewModel.cashDeskResponse.observe(
            this,
            Observer { cashDeskResponse ->
                cashDeskResponse?.let {

                    Log.i("cashDeskResponse ", "${cashDeskResponse}")
                    if (cashDeskResponse != null && !cashDeskResponse.cashDesks.isNullOrEmpty()) {

                        cashDeskList = cashDeskResponse.cashDesks
                        binding.lyProgres.lyProgress.visibility = View.GONE
                        initCashDesksList()
                    } else {
                        CustomDialogBuilder().showYesNOCustomAlert(
                            this,
                            "خطا",
                            "خطا در دریافت لیست صندوق، لطفا مجددا تلاش نمایید.",
                            "تلاش مجددا",
                            "انصراف",
                            { fragment ->
                                mCashDeskViewModel.getCashDeskFromApi(this@LoginActivity, 0)
                                fragment?.dismiss()
                            }, object : CustomAlertDialog.OnCancelClickListener {
                                override fun onClickCancel(fragment: DialogFragment?) {
                                    fragment?.dismiss()
                                    finish()
                                }

                                override fun onClickOutside(fragment: DialogFragment?) {
                                    fragment?.dismiss()
                                    finish()
                                }

                            }
                        )

                    }

                    // setRandomDishResponseInUI(randomDishResponse.recipes[0])
                }
            })

        mCashDeskViewModel.cashDeskLoadingError.observe(
            this,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Cash Desk API Error", "$dataError")
                    if (dataError) {
                        binding.lyProgres.lyProgress.visibility = View.GONE
                        CustomDialogBuilder().showYesNOCustomAlert(
                            this,
                            "خطا",
                            "خطا در دریافت لیست صندوق، لطفا مجددا تلاش نمایید.",
                            "تلاش مجددا",
                            "انصراف",
                            { fragment ->
                                mCashDeskViewModel.getCashDeskFromApi(this@LoginActivity, 0)
                                fragment?.dismiss()
                            }, object : CustomAlertDialog.OnCancelClickListener {
                                override fun onClickCancel(fragment: DialogFragment?) {
                                    fragment?.dismiss()
                                    finish()
                                }

                                override fun onClickOutside(fragment: DialogFragment?) {
                                    fragment?.dismiss()
                                    finish()
                                }

                            }
                        )

                    }

                }
            })

        mCashDeskViewModel.loadCashDesk.observe(this, Observer { loadCashDesk ->
            loadCashDesk?.let {
                Log.i("Cash Desk Loading", "$loadCashDesk")
                binding.lyProgres.lyProgress.visibility = View.VISIBLE


            }
        })
    }


    private fun initViews() {

        binding.button.setOnClickListener(this@LoginActivity)

    }

    fun initCashDesksList() {
        val setting = SessionManagement.getInstance(this)?.loadMember()
        var savedCashDesk: Long = 0L
        if (setting != null) {
            savedCashDesk = setting.cashDeskId!!
        }

        val items: MutableList<String> = mutableListOf()
        if (cashDeskList.isNullOrEmpty())
            return
        cashDeskList!!.forEach {

            if (it.id == savedCashDesk) {
                selectedCashDesk = it
                binding.txtSelectDesk.setText(it.name)
            }

            items += it.name.toString()
        }
        val adapter = ArrayAdapter(this, R.layout.text_selector_list_item, items)
        val txtSelectCashDesk = (binding.txtSelectDesk as? AutoCompleteTextView)
        if (txtSelectCashDesk != null) {
            txtSelectCashDesk.setAdapter(adapter)
            txtSelectCashDesk.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    selectedCashDesk = cashDeskList!![position]
                }
        }
    }

    private fun initFont() {
        FontChanger().applyMainFont(binding.root)

    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button -> {
                val user = binding.txtUser.text.toString()
                val pass = binding.txtPass.text.toString()

                if (TextUtils.isEmpty(user)) {
                    binding.txtUser.setError(getString(R.string.please_insert_user_code))
                    binding.txtUser.requestFocus()

                    return
                }


                if (TextUtils.isEmpty(pass)) {

                    binding.txtPass.setError(getString(R.string.please_insert_password))
                    binding.txtPass.requestFocus()
                    return
                }

                if (selectedCashDesk == null) {
                    Toasty.error(this, "صندوق کاربر را انتخاب نمایید").show()
                    return
                }

                val member = Member.Member(
                    "",
                    binding.txtUser.text.toString().toLong(),
                    binding.txtPass.text.toString(),
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    selectedCashDesk!!.id,
                    selectedCashDesk!!.name
                )

                mMemberViewModel.loginMember(this, member)


            }
        }
    }


}