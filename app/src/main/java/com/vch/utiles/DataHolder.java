package com.vch.utiles;


import com.vch.bean.OrderHistory;
import com.vch.bean.TiffinOrder;
import com.vch.response.UserDetail;

public class DataHolder {

    private static String count;
    private static String subTotal;
    private static UserDetail userData;
    private static TiffinOrder tiffinOrder;
    private static OrderHistory orderHistory;

    public static void setCount(String count) {
        DataHolder.count = count;
    }

    public static void setSubTotal(String subTotal) {
        DataHolder.subTotal = subTotal;
    }

    public static String getCount() {
        return count;
    }

    public static String getSubTotal() {
        return subTotal;
    }

    public static void setUserData(UserDetail userData) {
        DataHolder.userData = userData;
    }

    public static TiffinOrder getTiffinOrder() {
        return tiffinOrder;
    }

    public static void setTiffinDetails(TiffinOrder tiffinOrder) {
        DataHolder.tiffinOrder = tiffinOrder;
    }

    public static OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public static void setOrderHistory(OrderHistory orderHistory) {
        DataHolder.orderHistory = orderHistory;
    }
}
