package com.app.carton.provider;

/**
 * Created by aggarwal.swati on 1/28/19.
 */

public class WebServiceConstants {
    private static String BASE_URL = "https://cartonwale-api-gateway.appspot.com/api";
    public static final String LOGIN = BASE_URL
            + "/auth-service/auth";
    public static final String CHANGE_PASSWORD = BASE_URL
            + "/auth-service/users/changePassword";
    public static final String GET_PLACED_ORDERS = BASE_URL + "/order-service/orders/placedOrders";
    public static final String GET_ORDERS = BASE_URL + "/order-service/orders";
    public static final String PERMISSION = BASE_URL +"/auth-service/auth/current";
    public static final String GET_SINGLE_PRODUCT = BASE_URL + "/product-service/product/";
    public static final String ADD_PRODUCT = BASE_URL +"/product-service/product";
    public static final String ADD_QUOTATION = BASE_URL + "/order-service/quotes";
    public static final String GET_ALL_IN_PROGRESS_ORDERS = BASE_URL + "/order-service/orders/provider";
    public static final String GET_COMPLETED_ORDERS = BASE_URL + "/order-service/orders/provider/completed";
    public static final String UPDATE_ORDER_STATUS = BASE_URL + "/order-service/orders/changeOrderStatus";
    public static final String OPEN_PRODUCT_ACCEPTING_OFFERS = BASE_URL +"/product-service/product/acceptingOffers";
    public static final String ADD_PRODUCT_OFFER = BASE_URL +"/product-service/price/addOffer/";
    public static final String GET_DASHBOARD_DETAILS = BASE_URL +"/order-service/orders/provider/dashboard";
    public static final String CREATE_ONLY_CONSUMER = BASE_URL + "/consumer-service/consumers/consumerOnly";
    public static final String CREATE_PRODUCTS_RAW = BASE_URL + "/product-service/product/raw";
    public static final String GET_ALL_PRODUCTS = BASE_URL + "/product-service/product/";
    public static final String GET_ALL_BOUNDED_CONSUMERS = BASE_URL + "/consumer-service/consumers/boundedConsumers/";

}
