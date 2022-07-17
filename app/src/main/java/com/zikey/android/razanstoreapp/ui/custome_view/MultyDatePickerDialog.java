package com.zikey.android.razanstoreapp.ui.custome_view;


import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.razanpardazesh.com.resturantapp.tools.FontChanger;
import com.zikey.android.razanstoreapp.R;
import com.zikey.android.razanstoreapp.tools.CalendarWrapper;
import com.zikey.android.razanstoreapp.tools.PersianDateConverter;


/**
 * Created by Zikey on 08/03/2017.
 */

public class MultyDatePickerDialog extends DialogFragment {

    private final int Key_FROM_DATE = 1;
    private final int Key_END_DATE = 2;


    PersianCalendar calendar;
    private ViewGroup root;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog datePickerDialog;
    private TextView txtFarvardin;
    private TextView txtOrdibehesh;
    private TextView txtKhordad;
    private TextView txtTir;
    private TextView txtMordad;
    private TextView txtShahrivar;
    private TextView txtMehr;
    private TextView txtAban;
    private TextView txtAzar;
    private TextView txtDey;
    private TextView txtBahman;
    private TextView txtEsfand;
    private TextView txtStartsDate;
    private TextView txtEndDate;
    private LinearLayout lyFromDate;
    private LinearLayout lyEndDate;
    private String fromDate = new CalendarWrapper().getFirstDayOfThisMonth();
    private String endDate = new CalendarWrapper().getCurrentPersianDate();
    private LinearLayout lyScrollView;
    private TextView txtOK;
    private TextView txtDecline;
    private IDatePickerListener iDatePickerListener;
    private View.OnClickListener onEndDateClickListener;

    // itemClickedLable for find which one Date must change; fromDate or Enddate
    private int itemClickedLable = 0;

    private static final String KEY_FRAGMENT_LABLE = "DATE_PICKER_FRAGMENT";

    public void setiDatePickerListener(IDatePickerListener iDatePickerListener) {
        this.iDatePickerListener = iDatePickerListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_FRAME,
                android.R.style.Theme_Black_NoTitleBar);
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_date_picker, null);

        initDialog();
        initDatePickerListener();
        initView();
        return root;
    }

    private void initDialog() {

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
    }

    private void setDates(String startDate, String endDate) {

        fromDate = startDate;
        this.endDate = endDate;

    }

    private void initView() {
        if (root == null)
            return;

        txtFarvardin = (TextView) root.findViewById(R.id.txtFarvardin);
        txtOrdibehesh = (TextView) root.findViewById(R.id.txtOrdibehesh);
        txtKhordad = (TextView) root.findViewById(R.id.txtKhordad);
        txtTir = (TextView) root.findViewById(R.id.txtTir);
        txtMordad = (TextView) root.findViewById(R.id.txtMordad);
        txtShahrivar = (TextView) root.findViewById(R.id.txtShahrivar);
        txtMehr = (TextView) root.findViewById(R.id.txtMehr);
        txtAban = (TextView) root.findViewById(R.id.txtAban);
        txtAzar = (TextView) root.findViewById(R.id.txtAzar);
        txtDey = (TextView) root.findViewById(R.id.txtDey);
        txtBahman = (TextView) root.findViewById(R.id.txtBahman);
        txtEsfand = (TextView) root.findViewById(R.id.txtEsfand);
        txtStartsDate = (TextView) root.findViewById(R.id.txtFromDate);
        txtEndDate = (TextView) root.findViewById(R.id.txtEndDate);
        lyScrollView = (LinearLayout) root.findViewById(R.id.lyScrollView);
        txtOK = (TextView) root.findViewById(R.id.txtOK);
        txtDecline = (TextView) root.findViewById(R.id.txtDecline);
        FontChanger fontChanger = new FontChanger();
        fontChanger.applyMainFont(root.findViewById(R.id.lyContainer));

        lyFromDate = root.findViewById(R.id.lyFromDate);
        lyEndDate = root.findViewById(R.id.lyEndDate);

        CalendarWrapper calendarWrapper = new CalendarWrapper();
        txtEndDate.setText(endDate);
        txtStartsDate.setText(fromDate);

        fontChanger.applyTitleFont(txtStartsDate);
        fontChanger.applyTitleFont(txtEndDate);


        initClickListeners();


    }


    public static MultyDatePickerDialog Show(FragmentActivity act, String startDate, String endDate, IDatePickerListener datePickerListener) {

        android.app.FragmentManager manager = act.getFragmentManager();
        MultyDatePickerDialog fragment = new MultyDatePickerDialog();
        if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
            fragment.setDates(startDate, endDate);
        }
        if (datePickerListener != null)
            fragment.setiDatePickerListener(datePickerListener);
        fragment.show(manager, KEY_FRAGMENT_LABLE);
        return fragment;

    }


    private void initDatePickerListener() {

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                switch (itemClickedLable) {
                    case 1:
                        fromDate = changeDateFormat(year, monthOfYear, dayOfMonth);
                        txtStartsDate.setText(fromDate);
                        break;

                    case 2:
                        endDate = changeDateFormat(year, monthOfYear, dayOfMonth);
                        txtEndDate.setText(endDate);
                        break;

                    default:
                        break;
                }

            }
        };
    }


    private void showPersianCalendar(DatePickerDialog dialog) {

        if (onDateSetListener == null)
            return;

        if (calendar == null)
            calendar = new PersianCalendar();

        dialog = DatePickerDialog.newInstance(onDateSetListener, calendar.getPersianYear(), calendar.getPersianMonth(),
                calendar.getPersianDay());

        dialog.setThemeDark(false);
        dialog.show(getFragmentManager(), "Datepickerdialog");

    }

    private void initClickListeners() {

        if (lyEndDate == null)
            initView();

        if (calendar == null)
            calendar = new PersianCalendar();

        lyEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickedLable = Key_END_DATE;
                showPersianCalendar(datePickerDialog);
            }
        });

        lyFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickedLable = Key_FROM_DATE;
                showPersianCalendar(datePickerDialog);

            }
        });

        txtFarvardin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 0, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 0, 31));

            }
        });
        txtOrdibehesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 1, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 1, 31));

            }
        });
        txtKhordad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 2, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 2, 31));

            }
        });
        txtTir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 3, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 3, 31));

            }
        });
        txtMordad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 4, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 4, 31));

            }
        });
        txtShahrivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 5, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 5, 31));

            }
        });
        txtMehr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 6, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 6, 30));

            }
        });
        txtAban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 7, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 7, 30));

            }
        });
        txtAzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 8, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 8, 30));

            }
        });
        txtDey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 9, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 9, 30));

            }
        });
        txtBahman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 10, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 10, 30));

            }
        });
        txtEsfand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartsDate.setText(changeDateFormat(calendar.getPersianYear(), 11, 1));
                txtEndDate.setText(changeDateFormat(calendar.getPersianYear(), 11, 30));

            }
        });

        txtDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (iDatePickerListener != null)
                        iDatePickerListener.datePickerListener(txtStartsDate.getText().toString(), txtEndDate.getText().toString(), MultyDatePickerDialog.this);

                } catch (Exception ex) {

                }


//                if (checkBox.isChecked()) {
//                    GlobalFilter.getInstance().setStartDate(txtStartsDate.getText().toString());
//                    GlobalFilter.getInstance().setEndDate(txtEndDate.getText().toString());
//                    dismiss();
//                } else {
//                    GlobalFilter.getInstance().setStartDate(txtStartsDate.getText().toString());
//                    GlobalFilter.getInstance().setEndDate(null);
//                    dismiss();
//                }
            }
        });

        onEndDateClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickedLable = Key_END_DATE;
                showPersianCalendar(datePickerDialog);
            }
        };

    }

    private String changeDateFormat(int year, int monthOfYear, int dayOfMonth) {


        return PersianDateConverter.toPersianFormat(year, monthOfYear, dayOfMonth);

    }


    private void setEnableViewsChild(ViewGroup viewGroup, boolean state) {
        if (viewGroup == null)
            return;

        viewGroup.setClickable(state);
        viewGroup.setEnabled(state);


        int i = 0;
        i = viewGroup.getChildCount();
        if (i != 0) {
            for (int j = 0; j < i; j++) {

                if (viewGroup.getChildAt(j) instanceof ViewGroup) {
                    viewGroup.setEnabled(state);
                    setEnableViewsChild((ViewGroup) viewGroup.getChildAt(j), state);
                } else {
                    viewGroup.getChildAt(j).setClickable(state);
                    viewGroup.setEnabled(state);
                }
            }

        }

    }


    public interface IDatePickerListener {

        public void datePickerListener(String fromDate, String toDate, DialogFragment dialog);
    }

}

