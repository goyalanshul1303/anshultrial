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
    public static final String AWARD_QUOTATION = BASE_URL +"/order-service/quotes/award/";
    public static final String GET_CONSUMER_REQUIREMENTS_LIST = BASE_URL + "/order-service/orders/consumer/requirements";
    public static final String GET_DASHBOARD_DETAILS = BASE_URL +"/order-service/orders/consumer/dashboard";
    public static final String GET_COMPLETED_ORDERS = BASE_URL + "/order-service/orders/consumer/completed";
    public static final String INSERT_ITEM_TO_CART = BASE_URL + "/order-service/cart/add";
    public static final String REMOVE_ITEM_FROM_CART = BASE_URL + "/order-service/cart/remove";
    public static final String GET_ALL_CART = BASE_URL + "/order-service/cart";



}
