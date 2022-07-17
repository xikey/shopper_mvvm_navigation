package com.zikey.android.razanstoreapp.ui.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.razanpardazesh.com.resturantapp.tools.FontChanger;
import com.zikey.android.razanstoreapp.R;
import com.zikey.android.razanstoreapp.databinding.FragmentAddToBasketBinding;
import com.zikey.android.razanstoreapp.model.Order;
import com.zikey.android.razanstoreapp.model.OrderedProduct;
import com.zikey.android.razanstoreapp.model.Product;
import com.zikey.android.razanstoreapp.model.tools.SessionManagement;
import com.zikey.android.razanstoreapp.tools.Convertor;
import com.zikey.android.razanstoreapp.tools.NumberSeperator;
import com.zikey.android.razanstoreapp.ui.custom_dialog.CustomDialogBuilder;
import com.zikey.android.razanstoreapp.viewmodel.OrderViewModel;

import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

/**
 * Created by Zikey on 20/06/2017.
 */

public class AddToBasketFragment extends DialogFragment {

    private static final String KEY_ADD_TO_BASKET_FRAGMENT = "ADD_TO_BASKET_FRAGMENT";
    private FragmentAddToBasketBinding mBinding;

    public OrderViewModel orderViewModel;


    //Global
    private double total = 1;
    private double packageCount = 0;
    private double singleCount = 0;
    private Long orderID = null;
    private boolean isReturnedProduct;

    //use for Edit Mode
    private OnBasketChangeListener onBasketChangeListener;
    private Product.Product product;
    private OrderedProduct.OrderedProduct orderedProduct;

    private long fi = 0;
    private String expireDate = "";
    private boolean editableMode = false;
    private Fragment ownerFragment;

    public void setOwnerFragment(Fragment ownerFragment) {
        this.ownerFragment = ownerFragment;
    }

    public void setOrderedProduct(OrderedProduct.OrderedProduct orderedProduct) {
        this.orderedProduct = orderedProduct;
    }


    public void setReturnedProduct(boolean returnedProduct) {
        isReturnedProduct = returnedProduct;
    }


    public void setProduct(Product.Product product) {
        this.product = product;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_FRAME,
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

        mBinding = FragmentAddToBasketBinding.inflate(inflater, container, false);
        new FontChanger().applyMainFont(mBinding.getRoot());
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDialog();
        initViewModels();
        initOrderApiService();
        initViews();
        initClickListeners();
        editableMode();
        updateContent();
        initTabIndex();
        initFirstCount();

    }

    private void initOrderApiService() {


        if (!orderViewModel.getDataInsertProductLoadingError().hasObservers())
            orderViewModel.getDataInsertProductLoadingError().observe(
                    ownerFragment.getViewLifecycleOwner(),
                    (Observer<Boolean>) dataError -> {
                        Log.i("Order API Error", "$dataError");
                        if (dataError != null && dataError) {
                            mBinding.lyProgres.lyProgress.setVisibility(View.GONE);
                            new CustomDialogBuilder().showCustomAlert(
                                    (AppCompatActivity) ownerFragment.requireActivity(),
                                    "خطا",
                                    "${dataError} \n\n خطا در ثبت اطلاعات فاکتور",
                                    "بستن"
                            );

                        }
                    });

        if (!orderViewModel.getLoadInsertProductData().hasObservers())
            orderViewModel.getLoadInsertProductData().observe(ownerFragment.getViewLifecycleOwner(), (Observer<Boolean>) o -> {
                Log.i("member Loading", "$it");

            });

        if (!orderViewModel.getDataInsertProductResponse().hasObservers())
            orderViewModel.getDataInsertProductResponse().observe(
                    ownerFragment.getViewLifecycleOwner(),
                    (Observer<Order.OrderAnswer>) o -> {
                        Log.i("MemberResponse ", "${it}");
                        if (o == null)
                            return;


                        Order.OrderAnswer tempAnswer = (Order.OrderAnswer) o;

                        if (tempAnswer.getIsSuccess() != 1) {
                            new CustomDialogBuilder().showCustomAlert(
                                    (AppCompatActivity) ownerFragment.requireActivity(),
                                    "خطا",
                                    tempAnswer.getMessage(),
                                    "بستن"
                            );
                            mBinding.lyProgres.lyProgress.setVisibility(View.GONE);
                            return;
                        } else {
                            Toasty.success(ownerFragment.requireActivity(), "فاکتور شما با موفقیت بروز گردید!")
                                    .show();
                            mBinding.lyProgres.lyProgress.setVisibility(View.GONE);


                            return;
                        }

                    });


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

    private void initViewModels() {
        orderViewModel = new ViewModelProvider(ownerFragment).get(OrderViewModel.class);

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

        if (orderedProduct == null)
            orderedProduct = new OrderedProduct.OrderedProduct(null, 0d, 0d, fi, expireDate, 0, false);
        if (total == 0)
            total = 1;

        orderedProduct.setCount(total);
        orderedProduct.setCost(total * fi);
        orderedProduct.setFi(fi);
        orderedProduct.setProduct(product);
        if (total > orderedProduct.getProduct().getAvailable()) {

            new CustomDialogBuilder().showCustomAlert((AppCompatActivity) requireActivity(), "حطا", "موجودی کالا کافی نمیباشد.", "بستن");
            return;
        }

        insertProductToOrder(orderedProduct, orderID, isReturnedProduct);

    }

    private void insertProductToOrder(OrderedProduct.OrderedProduct orderedProduct, Long orderID, boolean isReturnedProduct) {
        if (orderID == null || orderID <= 0)
            orderViewModel.insertProductToBasket(getActivity(), orderedProduct, orderID, isReturnedProduct);
        else {
            orderedProduct.setFi((long) orderedProduct.getProduct().getPrice1());
            ArrayList<OrderedProduct.OrderedProduct> orderedProducts = new ArrayList<>();
            orderedProducts.add(orderedProduct);
            Order.Order order = new Order.Order(orderID,
                    null,
                    null,
                    null,
                    null,
                    orderedProducts,
                    null,
                    null,
                    null,
                    null,
                    null,
                    SessionManagement.Companion.getInstance().getCashDeskId(),     null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,0.0);
            Order.OrderAnswer answer = new Order.OrderAnswer(order);
            orderViewModel.insertProductToOrderApi(requireContext(), answer);
        }
    }


    private void initClickListeners() {

        mBinding.btnCancel.setOnClickListener(v -> dismiss());
        mBinding.imgClose.setOnClickListener(v -> dismiss());

        mBinding.btnAdd.setOnClickListener(v -> {

            add();
            dismiss();

        });


        mBinding.edtCount.setOnClickListener(v -> mBinding.edtCount.selectAll());

        mBinding.edtCount.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                add();
                dismiss();
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


    }

    /**
     * @param act     FragmentActivity
     * @param product selected Product for add to basket
     * @return
     */
    public static AddToBasketFragment Show(FragmentActivity act, Product.Product product, boolean isReturnedProduct, Long orderID, Fragment ownerFragment) {

        FragmentManager manager = act.getSupportFragmentManager();
        AddToBasketFragment fragment = new AddToBasketFragment();
        if (product != null) {
            fragment.setProduct(product);
        }
        fragment.setOwnerFragment(ownerFragment);
        fragment.show(manager, KEY_ADD_TO_BASKET_FRAGMENT);
        fragment.setReturnedProduct(isReturnedProduct);
        fragment.setOrderID(orderID);


        return fragment;
    }

    public static AddToBasketFragment Show(FragmentActivity act, OrderedProduct.OrderedProduct orderedProduct, Fragment ownerFragment) {

        FragmentManager manager = act.getSupportFragmentManager();
        AddToBasketFragment fragment = new AddToBasketFragment();
        fragment.setOwnerFragment(ownerFragment);
        if (orderedProduct != null) {
            fragment.setOrderedProduct(orderedProduct);


            if (orderedProduct.getProduct() != null) {
                fragment.setProduct(orderedProduct.getProduct());

            }
        }
        fragment.show(manager, KEY_ADD_TO_BASKET_FRAGMENT);

        return fragment;
    }


    public void enableEditableMode(final OnBasketChangeListener basketChangeListener) {
        if (basketChangeListener != null)
            this.onBasketChangeListener = basketChangeListener;

    }

    private void updateContent() {
        if (product == null)
            return;


        mBinding.edtAvailability.setText(NumberSeperator.separate(product.getAvailable()));
        mBinding.edtFi.setText(NumberSeperator.separate(product.getPrice1()));

        if (!TextUtils.isEmpty(product.getName()))
            mBinding.txtName.setText(product.getName());


        try {
            if (total == 0)
                total = 1;

            double totalPrice = total * fi;


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        mBinding.edtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    singleCount = 0;
                    total = packageCount + singleCount;


                }
                if (mBinding.edtCount.getText().toString().equals(".")) {
                    mBinding.edtCount.setText("");
                    mBinding.edtCount.append("0.");
                    return;
                }

                if (s.length() > 0) {
                    double value = Double.valueOf(mBinding.edtCount.getText().toString());
                    singleCount = value;
                    total = packageCount + singleCount;

                }

                if (mBinding.edtCount.getText().toString().contains(".")) {

                    try {
                        String tmp = mBinding.edtCount.getText().toString();
                        int index = tmp.indexOf(".");
                        int lenght = tmp.length();
                        if (lenght > index + 3) {
                            String m = tmp.subSequence(0, index + 3).toString();
                            mBinding.edtCount.setText("");
                            mBinding.edtCount.append(m);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }
        });


        if (orderedProduct != null) {

            mBinding.edtCount.setText("" + orderedProduct.getCount());

        }

    }


    private void editableMode() {

        if (this.onBasketChangeListener == null)
            return;
        editableMode = true;
        try {

            if (!TextUtils.isEmpty(orderedProduct.getExpireDate())) {
                expireDate = orderedProduct.getExpireDate();
            }

            fi = orderedProduct.getFi();

        } catch (Exception e) {
            e.printStackTrace();
        }

        mBinding.btnCancel.setText("حذف");
        mBinding.btnCancel.setOnClickListener(v -> {

            onBasketChangeListener.onRemove();
            dismiss();

        });

        mBinding.btnAdd.setOnClickListener(v -> {

            if (total == 0)
                total = 1;

            orderedProduct.setCost(total * fi);
            orderedProduct.setFi(fi);
            orderedProduct.setExpireDate(expireDate);


            try {

                onBasketChangeListener.onChange(total, total * fi);

            } catch (Exception e) {
                e.printStackTrace();

            }

            dismiss();

        });


        mBinding.edtCount.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (total == 0)
                    total = 1;

                try {
                    onBasketChangeListener.onChange(total, total * fi);

                } catch (Exception e) {
                    e.printStackTrace();

                }

                dismiss();
            }
            return false;
        });


    }

    private void fitLayoutSize(ViewGroup v) {


        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = v.getLayoutParams();
        double d = Convertor.toDp(width, getActivity());
        if (d > 350) {
            int p = (int) Convertor.toDp(110, getActivity());
            if (params instanceof RelativeLayout.LayoutParams)
                ((RelativeLayout.LayoutParams) params).setMargins(p, p / 4, p, p);

        } else {
            int p = (int) Convertor.toDp(5, getActivity());
            if (params instanceof RelativeLayout.LayoutParams)
                ((RelativeLayout.LayoutParams) params).setMargins(p, p / 5, p, p);
        }
    }

    public interface OnBasketChangeListener {
        void onChange(double count, double cost);

        void onRemove();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
