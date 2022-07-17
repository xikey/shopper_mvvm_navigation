package com.zikey.android.razanstoreapp.ui.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.razanpardazesh.com.resturantapp.tools.FontChanger;
import com.zikey.android.razanstoreapp.R;
import com.zikey.android.razanstoreapp.model.Order;
import com.zikey.android.razanstoreapp.model.OrderedProduct;
import com.zikey.android.razanstoreapp.model.Payment;
import com.zikey.android.razanstoreapp.model.tools.SessionManagement;
import com.zikey.android.razanstoreapp.tools.CalendarWrapper;
import com.zikey.android.razanstoreapp.tools.NumberSeperator;
import com.zikey.android.razanstoreapp.ui.custome_view.CustomPrintLayout;

import java.util.List;

import ir.sep.android.Service.IProxy;

/**
 * Created by Zikey on 06/05/2018.
 */

public class Print_SamanKish_Fragment extends DialogFragment {

    private static final String KEY_PRINT_FACTOR = "PRINT_FACTOR";

    private Print_SamanKish_Fragment fragment;
    private String printerLogicalName = null;
    private TextView txtPrinterState;

    //Views
    private ViewGroup root;
    private RelativeLayout lyContent;
    private TextView txtTitle;
    private LinearLayout lyProgress;

    private CardView lyActionPrint;
    private CustomPrintLayout customPrintLayout;

    IProxy service;
    private MyServiceConnection connection;

    private Order.OrderAnswer orderData;
    private List<Payment.Payment> payments;

    public void setOrderData(Order.OrderAnswer orderData) {
        this.orderData = orderData;
    }

    public void setPayments(List<Payment.Payment> payments) {
        this.payments = payments;
    }

    public void setFragment(Print_SamanKish_Fragment fragment) {
        this.fragment = fragment;
    }

    public void setPrinterLogicalName(String printerLogicalName) {
        this.printerLogicalName = printerLogicalName;
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
        dialog.setCanceledOnTouchOutside(true);
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

        root = (ViewGroup) inflater.inflate(R.layout.fragment_print_saman_kish_factor, null);
        if (root == null)
            return null;

        initDialog();
        initViews();
        initClickListeners();
        initService();
        createCustomView();

        return root;

    }


    private void initService() {
        Log.i("TAG", "initService()");
        connection = new MyServiceConnection();
        Intent i = new Intent();
        i.setClassName("ir.sep.android.smartpos", "ir.sep.android.Service.Proxy");
        boolean ret = getActivity().bindService(i, connection, getActivity().BIND_AUTO_CREATE);
        Log.i("TAG", "initService() bound value: " + ret);
    }


    private void releaseService() {
        getActivity().unbindService(connection);
        connection = null;
        Log.d("TAG", "releaseService(): unbound.");
    }


    private void createCustomView() {

        if (payments == null)
            return;
        if (orderData == null)
            return;

        long totalCashPaid = 0;
        long totalPosPaid = 0;

        for (int i = 0; i < payments.size(); i++) {
            Payment.Payment p = payments.get(i);

            if (p.getTerminalName().contains("آنلاین")) {

                totalPosPaid += p.getPrice();
            } else {
                totalCashPaid += p.getPrice();
            }
        }

//        customPrintLayout.addImage(R.drawable.img_daity_logo);

        int printTextSize = 13;
        int printTextSize_big = 15;
        if (!TextUtils.isEmpty(orderData.getOrder().getCompanyName()))
            customPrintLayout.addTextView(orderData.getOrder().getCompanyName(), "#000000", Gravity.CENTER, 5, printTextSize, null, true);
        if (!TextUtils.isEmpty(orderData.getOrder().getSaleCenterName()))
            customPrintLayout.addTextView(orderData.getOrder().getSaleCenterName(), "#000000", Gravity.CENTER, 5, printTextSize, null, true);
        customPrintLayout.addTextView("صورتحساب فروش کالا", "#000000", Gravity.RIGHT, 5, printTextSize_big, null, false);
        customPrintLayout.addTextView(orderData.getOrder().getOrderNumber() + " ش.ف ", "#000000", Gravity.LEFT, 5, printTextSize, null, false);
        customPrintLayout.addTextView(orderData.getOrder().getCreateDate() + " تاریخ ", "#000000", Gravity.LEFT, 5, printTextSize, null, false);

        customPrintLayout.addLine(10);
        customPrintLayout.addTableOneRow("فروشنده");
        customPrintLayout.addLine(0);
        String sellerName = "";
        String economicCode = "";
        String postalCode = "";
        String sellerAddress = "";
        if (!TextUtils.isEmpty(orderData.getOrder().getSaleCenterName()))
            sellerName = orderData.getOrder().getSaleCenterName();

        if (!TextUtils.isEmpty(orderData.getOrder().getEconomicCode()))
            economicCode = orderData.getOrder().getEconomicCode();

        if (!TextUtils.isEmpty(orderData.getOrder().getPostalCode()))
            postalCode = orderData.getOrder().getPostalCode();

        if (!TextUtils.isEmpty(orderData.getOrder().getSaleCenterAddress()))
            sellerAddress = orderData.getOrder().getSaleCenterAddress();


        customPrintLayout.addTableTwoRow("نام شخص:", sellerName);
        customPrintLayout.addTableTwoRow("کد اقتصادی:", economicCode);
        customPrintLayout.addTableTwoRow("کد پستی:", postalCode);
        customPrintLayout.addTableTwoRow("آدرس:", sellerAddress);
        customPrintLayout.addLine(0);


        String customerName = "";
        String customerSubscriptionCode = "";
        String customerTell = "";
        String customerAddress = "";

        if (!TextUtils.isEmpty(orderData.getOrder().getCustomerName()))
            customerName = orderData.getOrder().getCustomerName();

        if (!TextUtils.isEmpty(orderData.getOrder().getSubscriptionCode()))
            customerSubscriptionCode = orderData.getOrder().getSubscriptionCode();

        if (!TextUtils.isEmpty(orderData.getOrder().getCustomerTell()))
            customerTell = orderData.getOrder().getCustomerTell();

        if (!TextUtils.isEmpty(orderData.getOrder().getCustomerAddress()))
            customerAddress = orderData.getOrder().getCustomerAddress();

        customPrintLayout.addLine(10);
        customPrintLayout.addTableOneRow("مشتری");
        customPrintLayout.addLine(0);
        customPrintLayout.addTableTwoRow("نام مشتری:", customerName);
        customPrintLayout.addTableTwoRow("کد اشتراک:", customerSubscriptionCode);
        customPrintLayout.addTableTwoRow("شماره تماس:", customerTell);
        customPrintLayout.addTableTwoRow("آدرس:", customerAddress);
        customPrintLayout.addLine(0);

        customPrintLayout.addLine(10);
        customPrintLayout.addTableFiveRow_black("#", "عنوان", "تعداد", "فی", "جمع کل" + "\n-------------\n" + "بعد تخفیف");
        customPrintLayout.addLine(0);


        long totalCount = 0;
        for (int i = 0; i < orderData.getOrder().getOrderedProducts().size(); i++) {

            OrderedProduct.OrderedProduct details = orderData.getOrder().getOrderedProducts().get(i);

            totalCount += details.getCount();
            String rowNum = String.valueOf(i + 1);
            String name = details.getProduct().getName();
            String count = String.valueOf(details.getCount());
            String fi = NumberSeperator.separate(details.getProduct().getFi());
            //  String price = NumberSeperator.separate(details.getCost());
            double priceWithoutDiscount = details.getCount() * details.getProduct().getFi();
            String price = NumberSeperator.separate(priceWithoutDiscount);
            price += "\n-------------\n";
            price += NumberSeperator.separate(details.getProduct().getPrice1());

            customPrintLayout.addTableFiveRow(rowNum, name, count, fi, price);
            customPrintLayout.addLine(0);

        }


        customPrintLayout.addTableTwoRow("تعداد اقلام ", NumberSeperator.separate(totalCount));
        customPrintLayout.addLine(0);
//
//
        customPrintLayout.addTableTwoRow("نقد", NumberSeperator.separate(totalCashPaid));
        customPrintLayout.addLine(0);
//

        customPrintLayout.addTableTwoRow("کارتخوان", NumberSeperator.separate(totalPosPaid));
        customPrintLayout.addLine(0);


        customPrintLayout.addTableTwoRow("قابل پرداخت", NumberSeperator.separate(orderData.getOrder().getTotalPrice()));
        customPrintLayout.addLine(0);
        customPrintLayout.addEmptyRow(15);


        customPrintLayout.addLeftAndRightTextRow("سود حاصل از خرید", NumberSeperator.separate(orderData.getOrder().getCashDiscount()));
        customPrintLayout.addEmptyRow(10);
        customPrintLayout.addLeftAndRightTextRow("شماره تراکنش", String.valueOf(orderData.getOrder().getId()));

        customPrintLayout.addEmptyRow(30);

        try {
            customPrintLayout.addTextView(orderData.getOrder().getSaleCenterName() + "/" + orderData.getOrder().getBranchName(), "#000000", Gravity.CENTER, 5, printTextSize, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        customPrintLayout.addEmptyRow(100);

        customPrintLayout.addTextView("Parseh Total system", "#000000", Gravity.LEFT, 3, 10, "#FFFFFF", false);
        customPrintLayout.addLine(0);
        customPrintLayout.addTableTwoRow("صندوقدار" + ":" + SessionManagement.Companion.getInstance().getCodeMarkaz(), new CalendarWrapper().getCurrentTime());
    }


    private void initClickListeners() {


        lyActionPrint.setOnClickListener(v -> printLayout());


    }


    private void printLayout() {

        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.lyRoot);

        // convert view group to bitmap
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.buildDrawingCache();
        Bitmap bm = linearLayout.getDrawingCache();

        if (bm == null) {
            linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(linearLayout.getMeasuredWidth(),
                    linearLayout.getHeight(),
                    Bitmap.Config.ARGB_8888);

            Canvas c = new Canvas(bitmap);
            linearLayout.layout(linearLayout.getLeft(), linearLayout.getTop(), linearLayout.getRight(), linearLayout.getBottom());
            linearLayout.draw(c);
            bm = bitmap;

        }

        try {
            service.PrintByBitmap(bm);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }


    private void initDialog() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    void initViews() {

        lyContent = (RelativeLayout) root.findViewById(R.id.lyContent);
        txtTitle = (TextView) root.findViewById(R.id.txtTitle);
        txtPrinterState = (TextView) root.findViewById(R.id.txtPrinterState);
        lyProgress = (LinearLayout) root.findViewById(R.id.lyProgress);


        customPrintLayout = (CustomPrintLayout) root.findViewById(R.id.customPrintLayout);
        lyActionPrint = (CardView) root.findViewById(R.id.lyActionPrint);


        new FontChanger().applyMainFont(lyContent);
        new FontChanger().applyTitleFont(txtTitle);


    }


    public static void Show(Fragment ownerFragment, Order.OrderAnswer orderData, List<Payment.Payment> payments) {

        android.app.FragmentManager manager = ownerFragment.getActivity().getFragmentManager();
        Print_SamanKish_Fragment fragment = new Print_SamanKish_Fragment();
        fragment.setOrderData(orderData);
        fragment.setPayments(payments);
        fragment.setFragment(fragment);

        fragment.show(manager, KEY_PRINT_FACTOR);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        releaseService();
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class MyServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IProxy.Stub.asInterface((IBinder) boundService);
            Log.i("TAG", "onServiceConnected(): Connected");
            Toast.makeText(getActivity(), "AIDLExample Service connected", Toast.LENGTH_LONG).show();
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.i("TAG", "onServiceDisconnected(): Disconnected");
            Toast.makeText(getActivity(), "AIDLExample Service Connected", Toast.LENGTH_LONG).show();
        }
    }


}
