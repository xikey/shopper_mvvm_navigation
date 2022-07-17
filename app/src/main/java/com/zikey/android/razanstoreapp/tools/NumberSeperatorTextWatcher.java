package com.zikey.android.razanstoreapp.tools;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by Zikey on 22/05/2018.
 */

public class NumberSeperatorTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;
    private NumberChangeListener numberChangeListener;


    private EditText et;


    public Long getNumber() {


        try {
            return dfnd.parse(et.getText().toString()).longValue();
        } catch (Exception e) {
            return null;
        }
    }

    public String getString() {

        return (et.getText().toString());
    }

    public NumberSeperatorTextWatcher(EditText et, NumberChangeListener numberChangeListener) {

        df = new DecimalFormat("#,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###");
        this.et = et;
        hasFractionalPart = false;

        this.numberChangeListener = numberChangeListener;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberSeperatorTextWatcher";

    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        if (TextUtils.isEmpty(s)){
            if (numberChangeListener!=null)
                numberChangeListener.afterChange(null);
        }

        try {


            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
//_____________________________get the none Seprated Number_________________________________________


            Number n = df.parse(v);
            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                et.setText(df.format(n));
            } else {
                et.setText(dfnd.format(n));
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }



            if (numberChangeListener!=null)
                numberChangeListener.afterChange(v);

        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (Exception e) {
            // do nothing?
        }

        et.addTextChangedListener(this);


    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            hasFractionalPart = true;


        } else {
            hasFractionalPart = false;
        }
    }


    public interface NumberChangeListener {

        void afterChange(String noneSeperated);

    }
}