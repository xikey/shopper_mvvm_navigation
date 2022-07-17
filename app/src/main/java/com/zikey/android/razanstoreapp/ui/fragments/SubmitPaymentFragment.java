package com.zikey.android.razanstoreapp.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.razanpardazesh.com.resturantapp.tools.FontChanger;
import com.zikey.android.razanstoreapp.R;
import com.zikey.android.razanstoreapp.databinding.FragmentSubmitPaymentBinding;
import com.zikey.android.razanstoreapp.model.Terminal;
import com.zikey.android.razanstoreapp.tools.NumberSeperator;
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder;

import java.util.UUID;

import es.dmoral.toasty.Toasty;
import ir.sep.android.Service.IProxy;

/**
 * Created by Zikey on 20/06/2017.
 */

public class SubmitPaymentFragment extends DialogFragment {

    private static final String KEY_SUBMIT_PAYMENT_FRAGMENT = "SUBMIT_PAYMENT_FRAGMENT";
    private FragmentSubmitPaymentBinding mBinding;
    private static final int KEY_REQUEST_PAY = 1;
    private double factorRemainedPrice;
    private Terminal.Terminal selectedTerminal;
    private OnAddListener onAddListener;
    IProxy service;
    private MyServiceConnection connection;

    public OnAddListener getOnAddListener() {
        return onAddListener;
    }

    public void setOnAddListener(OnAddListener onAddListener) {
        this.onAddListener = onAddListener;
    }

    public Terminal.Terminal getSelectedTerminal() {
        return selectedTerminal;
    }

    public void setSelectedTerminal(Terminal.Terminal selectedTerminal) {
        this.selectedTerminal = selectedTerminal;
    }

    public double getFactorRemainedPrice() {
        return factorRemainedPrice;
    }

    public void setFactorRemainedPrice(double factorRemainedPrice) {
        this.factorRemainedPrice = factorRemainedPrice;
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            results -> {
                if (results.getResultCode() == Activity.RESULT_OK) {
                    Intent data = results.getData();
                    // Handle the Intent

                    int state = data.getIntExtra("State", -1);
                    String refNum = data.getStringExtra("RefNum");
                    String notSentAdviceRefNum = data.getStringExtra("NotSentAdviceRefNum");
                    String resNum = data.getStringExtra("ResNum");
                    String result = "faild";
                    if (!TextUtils.isEmpty(data.getStringExtra("result")))
                        result = data.getStringExtra("result");

                    String terminalId = data.getStringExtra("TerminalId");
                    String cardNumber = data.getStringExtra("Pan");
                    String traceNumber = data.getStringExtra("TraceNumber");
                    String dateTime = data.getStringExtra("DateTime");
                    String amount = data.getStringExtra("Amount");
                    String amountAffective = data.getStringExtra("AmountAffective");

                    if (state == 0) {
                        //Done
                        Double price = Double.parseDouble(mBinding.edtCount.getText().toString());
                        if (onAddListener != null)
                            onAddListener.onAdd(selectedTerminal, price);

                        this.dismiss();
                    } else {
                        //Error
                        Toasty.error(requireContext(), "statusCode: " + state + "  result:" + result).show();
                    }

                }
                if (results.getResultCode() == Activity.RESULT_CANCELED) {

                    Toasty.error(requireContext(), "انصراف از پرداخت").show();


                }

            });


    @Override
    public void onCreate(Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NO_FRAME,
                android.R.style.Theme_Black_NoTitleBar);
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));

        }

        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = FragmentSubmitPaymentBinding.inflate(inflater, container, false);
        new FontChanger().applyMainFont(mBinding.getRoot());
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDialog();
        initViews();
        initClickListeners();
        initTabIndex();
        initFirstCount();
        initService();
    }


    private void initFirstCount() {
        try {
            if (TextUtils.isEmpty(mBinding.edtCount.getText())) {
                mBinding.edtCount.setText("1");
            }
        } catch (Exception e) {
            mBinding.edtCount.setText("1");
        }

        mBinding.edtCount.selectAll();

    }


    private void initTabIndex() {
        try {
            mBinding.edtCount.requestFocus();
            //   edtFi.setNextFocusLeftId(R.id.edtCount);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void add() {

        double price = 0;

        if (mBinding.edtCount == null) {
            Toasty.error(requireContext(), "مبلغ واد شده نادرست میباشد").show();
            return;
        }
        if (mBinding.edtCount.getText() == null) {
            Toasty.error(requireContext(), "مبلغ واد شده نادرست میباشد").show();
            return;
        }

        try {
            price = Double.parseDouble(mBinding.edtCount.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (price == 0) {
            Toasty.error(requireContext(), "مبلغ واد شده نادرست میباشد").show();
            return;
        }

        if (price>factorRemainedPrice){
            Toasty.error(requireContext(), "مبلغ واد شده بیشتر از مبلغ فاکتور میباشد").show();
            return;
        }

        if (selectedTerminal.getTerminalName().contains("آنلاین")) {

            payPose((long) price);
            return;
        }


        if (onAddListener != null)
            onAddListener.onAdd(selectedTerminal, price);


        this.dismiss();
    }


    private void initClickListeners() {

        mBinding.btnCancel.setOnClickListener(v -> dismiss());
        mBinding.imgClose.setOnClickListener(v -> dismiss());

        mBinding.btnAdd.setOnClickListener(v -> {

            add();


        });


        mBinding.edtCount.setOnClickListener(v -> mBinding.edtCount.selectAll());

        mBinding.edtCount.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                add();

            }
            return false;
        });


    }


    private void initDialog() {

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
    }

    void initViews() {

        // fitLayoutSize(mBinding.lyContent);
        FontChanger fontChanger = new FontChanger();
        fontChanger.applyMainFont(mBinding.lyContent);
        fontChanger.applyTitleFont(mBinding.lyBottom);

        try {
            mBinding.txtName.setText(selectedTerminal.getTerminalName());
            mBinding.edtRemainedPrice.setText(NumberSeperator.separate(factorRemainedPrice));
            mBinding.edtCount.setText("" + factorRemainedPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static SubmitPaymentFragment Show(FragmentActivity act, Terminal.Terminal terminal, double remainedPrice, OnAddListener onAdListener) {

        FragmentManager manager = act.getSupportFragmentManager();
        SubmitPaymentFragment fragment = new SubmitPaymentFragment();

        fragment.setSelectedTerminal(terminal);
        fragment.setFactorRemainedPrice(remainedPrice);
        fragment.setOnAddListener(onAdListener);
        fragment.show(manager, KEY_SUBMIT_PAYMENT_FRAGMENT);


        return fragment;
    }


    public interface OnAddListener {
        void onAdd(Terminal.Terminal terminal, double price);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void initService() {

        try {

            Log.i("TAG", "initService()");
            connection = new MyServiceConnection();
            Intent i = new Intent();
            i.setClassName("ir.sep.android.smartpos", "ir.sep.android.Service.Proxy");
            boolean ret = requireContext().bindService(i, connection, requireContext().BIND_AUTO_CREATE);
            Log.i("TAG", "initService() bound value: " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        releaseService();
        super.onDismiss(dialog);
    }

    private void releaseService() {
        requireContext().unbindService(connection);
        connection = null;
        Log.d("TAG", "releaseService(): unbound.");
    }


    private void payPose(long payableAmount) {


        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();


        Intent intent = new Intent();
        intent.putExtra("TransType", KEY_REQUEST_PAY);
        intent.putExtra("ResNum", randomUUIDString);
        intent.putExtra("Amount", String.valueOf(payableAmount));
        intent.putExtra("AppId", "1");
        intent.putExtra("Timer", 60_000);
        intent.setComponent(new ComponentName("ir.sep.android.smartpos", "ir.sep.android.smartpos.ThirdPartyActivity"));

        try {
            mStartForResult.launch(intent);
        } catch (Exception e) {
            new CustomDialogBuilder().showCustomAlert((AppCompatActivity) requireActivity(), "خطا", "خطا در ارتباط با نرم افزار smartPos \n\n\n       **از نصب بودن نرم افزار smartPos روی دستگاه خود اطمینان حاصل نمایید.**", "بستن");
            e.printStackTrace();
        }


    }

    class MyServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IProxy.Stub.asInterface((IBinder) boundService);
            Log.i("TAG", "onServiceConnected(): Connected");
            Toasty.info(requireContext(), "AIDLExample Service connected").show();

        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.i("TAG", "onServiceDisconnected(): Disconnected");
            Toasty.info(requireContext(), "AIDLExample Service disconnected").show();
        }
    }

}
