package com.zikey.android.razanstoreapp.model;

import android.text.TextUtils;
import android.util.Log;


import com.zikey.android.razanstoreapp.model.tools.SessionManagement;
import com.zikey.android.razanstoreapp.tools.CalendarWrapper;

import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class OrderBasket {

    private static OrderBasket ourInstance = null;
    private ArrayList<OrderedProduct.OrderedProduct> orderedProducts = null;
    private String comment;
    private String time;
    private String date;
    private UUID localID;
    private String orderDate;
    private String customerTell;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UUID getLocalID() {
        return localID;
    }

    public void setLocalID(UUID localID) {
        this.localID = localID;
    }

    public String getCustomerTell() {
        return customerTell;
    }

    public void setCustomerTell(String customerTell) {
        this.customerTell = customerTell;
    }

    public OrderBasket setOrderedProduct(OrderedProduct.OrderedProduct item) {
        if (item == null)
            return this;


        if (orderedProducts == null)
            orderedProducts = new ArrayList<>();

        for (int i = 0; i < orderedProducts.size(); i++) {
            OrderedProduct.OrderedProduct o = orderedProducts.get(i);

            if (o.getProduct() != null && item.getProduct() != null) {
//                Log.i("ZIKEY_TAG",o.getProduct().getCode() == item.getProduct().getCode()?"true":"false");
//                Log.i("ZIKEY_TAG",o.getFi() ==((long) item.getProduct().getPrice1())?"true":"false");
//                Log.i("ZIKEY_TAG",o.getFi()+"");
//                Log.i("ZIKEY_TAG",((long) item.getProduct().getPrice1())+"");


                if (o.getProduct().getCode() == item.getProduct().getCode()) {

                    o.setCount(item.getCount() + o.getCount());
                    o.setCost(item.getCost() + o.getCost());
                  //  o.setFi((long) item.getProduct().getPrice1());

                    return this;


                }
            }

        }
        item.setFi((long) item.getProduct().getPrice1());
        orderedProducts.add(item);
        return this;

    }

    public OrderBasket updateOrderedProduct(OrderedProduct.OrderedProduct item) {
        if (item == null)
            return this;


        if (orderedProducts == null)
            orderedProducts = new ArrayList<>();

        for (int i = 0; i < orderedProducts.size(); i++) {
            OrderedProduct.OrderedProduct o = orderedProducts.get(i);

            if (o.getProduct() != null && item.getProduct() != null) {

                if (o.getProduct().getCode() == item.getProduct().getCode() ) {

                    o.setCount(item.getCount());
                    o.setCost(item.getCost());

                    return this;


                }
            }

        }

        orderedProducts.add(item);
        return this;

    }

    public OrderBasket updateOrderedProduct(int rowNumber, OrderedProduct.OrderedProduct item) {
        if (item == null)
            return this;

        if (orderedProducts == null || orderedProducts.size() == 0)
            return this;

        OrderedProduct.OrderedProduct op = orderedProducts.get(rowNumber);
        op = item;


        return this;

    }


    public OrderBasket removeOrderedProduct(OrderedProduct.OrderedProduct item) {
        if (item == null)
            return this;

        if (orderedProducts == null || orderedProducts.size() == 0)
            return this;

        for (int i = 0; i < orderedProducts.size(); i++) {

            OrderedProduct.OrderedProduct o = orderedProducts.get(i);

            if (o.getProduct() != null && item.getProduct() != null) {

                if (o.getProduct().getCode() == item.getProduct().getCode() ) {
                    orderedProducts.remove(i);
                    return this;
                }
            }

        }
        return this;
    }

    public OrderBasket removeAllProducts() {

        orderedProducts = null;
        return this;
    }

    public OrderBasket updateOrderDate(String date) {

        this.orderDate = date;
        return this;
    }

    public static OrderBasket getInstance() {

        CalendarWrapper calendarWrapper = new CalendarWrapper();

        if (ourInstance == null)
            ourInstance = new OrderBasket();

        if (TextUtils.isEmpty(ourInstance.getTime()))
            ourInstance.setTime(calendarWrapper.getCurrentTime_Second());


        if (TextUtils.isEmpty(ourInstance.getDate()))
            ourInstance.setDate(calendarWrapper.getCurrentPersianDate());

        if (ourInstance.localID == null)
            ourInstance.setLocalID(UUID.randomUUID());


        return ourInstance;
    }


    private OrderBasket() {

    }


    public Order.Order getOrder() {

        Order.Order order = new Order.Order(
                0l,
                localID,
                date,
                time,
                0l,
                orderedProducts,
                comment,
                "1",
                orderDate,
                0l,
                customerTell,
                SessionManagement.Companion.getInstance().getCashDeskId(),
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
                null,
                null,0.0

        );


        return order;
    }

    public void setOrder(Order.Order order) {
        if (order == null)
            return;

        time = order.getCreateTime();
        date = order.getCreateDate();
        localID = order.getOrderUUID();
        orderDate = order.getOrderDate();
        orderedProducts = order.getOrderedProducts();
        customerTell = order.getCustomerTell();


    }

    public void clearBasket() {
        ourInstance = null;

    }

}
