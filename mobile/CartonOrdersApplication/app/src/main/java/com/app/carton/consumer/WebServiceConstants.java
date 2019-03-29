package com.app.carton.consumer;

/**
 * Created by aggarwal.swati on 1/28/19.
 */

public class WebServiceConstants {
    private static String BASE_URL = "https://cartonwale-api-gateway.appspot.com/api";
    public static final String LOGIN = BASE_URL
            + "/auth-service/auth";
    public static final String CHANGE_PASSWORD = BASE_URL
            + "/auth-service/users/changePassword";
    public static final String CREATE_ORDER = BASE_URL + "/order-service/orders";
    public static final String GET_ORDERS = BASE_URL + "/order-service/orders";
    public static final String PERMISSION = BASE_URL +"/auth-service/auth/current";
    public static final String GET_ALL_PRODUCTS = BASE_URL + "/product-service/product";
    public static final String GET_SINGLE_PRODUCT = BASE_URL + "/product-service/product/";
    public static final String ADD_PRODUCT = BASE_URL +"/product-service/product";

    public static final String GET_ORDER_QUOTATIONS = BASE_URL +"/order-service/quotes/order/";


}
