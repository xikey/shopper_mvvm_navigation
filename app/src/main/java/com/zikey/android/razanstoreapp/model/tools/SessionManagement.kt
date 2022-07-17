package com.zikey.android.razanstoreapp.model.tools


import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.zikey.android.razanstoreapp.R
import com.zikey.android.razanstoreapp.model.Member
import com.zikey.android.razanstoreapp.ui.custom_dialog.DialogBuilder


class SessionManagement(context: Context) {

    private val KEY_PREFERENCES_NAME = "RAZAN_STORE_APP_APPLICATION"

    private val PRINTER_BIXOLON = "BIXOLON"
    private val PRINTER_WOOSIM = "WOOSIM"

    private val KEY_CODE_MARKAZ = "CODE_MARKAZ"
    private val KEY_TOKEN = "TOKEN"
    private val KEY_PASS = "PASS"
    private val KEY_ID = "ID"
    private val KEY_NAME = "NAME"
    private val KEY_TEL = "TEL"
    private val KEY_CASH_DESK_ID = "CASH_DESK_ID"
    private val KEY_CASH_DESK_NAME = "CASH_DESK_NAME"
    private val KEY_PRINTER_MODEL = "PRINTER_MODEL"
    private val KEY_PRINTER_NAME = "PRINTER_NAME"


    private var preferences: SharedPreferences? = context.getSharedPreferences(
        KEY_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    companion object {

        var instance: SessionManagement? = null

        fun getInstance(context: Context): SessionManagement? {
            if (instance == null) instance = SessionManagement(context)
            return instance
        }
    }


    fun getPass(): String? {
        return if (preferences == null) null else preferences!!.getString(KEY_PASS, null)
    }


    /**
     * @param bixolon
     * @param woosim
     * @param modelName modelname is mot important is woosim Printers;
     */
    fun setupPrinter(bixolon: Boolean, woosim: Boolean, modelName: String?) {
        if (preferences == null) return
        if (bixolon) {
            preferences!!.edit().putString(KEY_PRINTER_MODEL, PRINTER_BIXOLON).apply()
            preferences!!.edit().putString(KEY_PRINTER_NAME, modelName).apply()
        } else if (woosim) {
            preferences!!.edit().putString(KEY_PRINTER_MODEL, PRINTER_WOOSIM).apply()
            preferences!!.edit().putString(KEY_PRINTER_NAME, PRINTER_BIXOLON).apply()
        }
    }

    fun getPrinterModel(): String? {
        return if (preferences == null) null else preferences!!.getString(KEY_PRINTER_MODEL, null)
    }

    fun getPrinterName(): String? {
        return if (preferences == null) null else preferences!!.getString(KEY_PRINTER_NAME, null)
    }

    fun setCodeMarkaz(codeMarkaz: Long) {
        if (preferences == null) return
        preferences!!.edit().putLong(KEY_CODE_MARKAZ, codeMarkaz).apply()
    }

    fun getCodeMarkaz(): Long {
        return if (preferences == null) 0 else preferences!!.getLong(KEY_CODE_MARKAZ, 0)
    }

    fun setToken(token: String?) {
        if (preferences == null) return
        preferences!!.edit().putString(KEY_TOKEN, token).apply()
    }


    fun getToken(): String? {
        return if (preferences == null) "" else preferences!!.getString(KEY_TOKEN, "")
    }

    fun setName(name: String?) {
        if (preferences == null) return
        preferences!!.edit().putString(KEY_NAME, name).apply()
    }

    fun getName(): String? {
        return if (preferences == null) "" else preferences!!.getString(KEY_NAME, "")
    }


    fun setTel(tel: String?) {
        if (preferences == null) return
        preferences!!.edit().putString(KEY_TEL, tel).apply()
    }

    fun getTel(): String? {
        return if (preferences == null) "" else preferences!!.getString(KEY_TEL, "")
    }


    fun setID(id: Long) {
        if (preferences == null) return
        preferences!!.edit().putLong(KEY_ID, id).apply()
    }

    fun getID(): Long {
        return if (preferences == null) 0 else preferences!!.getLong(KEY_ID, 0)
    }


    fun setCashDeskId(id: Long) {
        if (preferences == null) return
        preferences!!.edit().putLong(KEY_CASH_DESK_ID, id).apply()
    }

    fun getCashDeskId(): Long {
        return if (preferences == null) 0 else preferences!!.getLong(KEY_CASH_DESK_ID, 0)
    }

 fun setCashDeskName(name: String) {
        if (preferences == null) return
        preferences!!.edit().putString(KEY_CASH_DESK_NAME, name).apply()
    }

    fun getCashDeskName(): String? {
        return if (preferences == null) "" else preferences!!.getString(KEY_CASH_DESK_NAME, "")
    }

    fun clearMemberData() {
        setToken("")
        setName("")
        setCodeMarkaz(0)
        setID(0)
    }


    fun saveMemberData(context: AppCompatActivity, member: Member.Member?): Boolean {
        if (member == null) return false
        val dialog = DialogBuilder()
        if (member.code_markaz!! < 1) {
            dialog.showAlert(context, context.getString(R.string.server_code_markaz_eror))
            return false
        }
        if (TextUtils.isEmpty(member.token)) {
            dialog.showAlert(context, context.getString(R.string.server_token_eror))
            return false
        }
        if (TextUtils.isEmpty(member.name)) {
            dialog.showAlert(context, context.getString(R.string.server_name_eror))
            return false
        }

        setCodeMarkaz(member.code_markaz)
        setName(member.name)
        setToken(member.token)
        setTel(member.tel)
        member.cashDeskId?.let { setCashDeskId(it) }
        member.cashDeskName?.let { setCashDeskName(it) }
        return true
    }

    fun loadMember(): Member.Member? {

        if (getCodeMarkaz() == 0L) return null else if (TextUtils.isEmpty(getName())) return null else if (TextUtils.isEmpty(
                getToken()
            )
        ) return null
        val member = Member.Member(
            name = getName(),
            code_markaz = getCodeMarkaz(),
            password = getPass(),
            token = getToken(),
            id = getID(),
            deviceInfo = null,
            osInfo = null,
            navigationIpAddress = "",
            tel = getTel(),
            salesCenterName = "",
            getCashDeskId(),
            getCashDeskName()
        )

        return member
    }

    fun clearSession(context: Context?) {
        if (preferences == null) return
        preferences!!.edit().clear().commit()
        preferences = null
    }


}