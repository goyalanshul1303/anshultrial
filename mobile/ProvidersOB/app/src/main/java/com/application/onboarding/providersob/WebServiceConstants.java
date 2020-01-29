package com.application.onboarding.providersob;

/**
 * Created by aggarwal.swati on 1/28/19.
 */

public class WebServiceConstants {
    private static String BASE_URL = "https://cartonwale-api-gateway.appspot.com/api";
    public static final String LOGIN = BASE_URL
            + "/auth-service/auth";
    public static final String PERMISSION = BASE_URL +"/auth-service/auth/current";
    public static final String CHANGE_PASSWORD = BASE_URL
            + "/auth-service/users/changePassword";
    public static final String CREATE_CONSUMER = BASE_URL + "/consumer-service/consumers";
    public static final String CREATE_PROVIDER = BASE_URL + "/provider-service/providers";
    public static final String GET_ALL_PRODUCTS = BASE_URL + "/product-service/product/consumer/";
    public static final String ADD_PRODUCT = BASE_URL +"/product-service/product";
    public static final String GET_SINGLE_PRODUCT = BASE_URL + "/product-service/product/";
    public static  final  String START_ACCEPTING_OFFERS = BASE_URL + "/product-service/price/startAcceptingOffers";
    public static  final  String ADD_PRICE = BASE_URL + "/product-service/price/";
    public static final String GET_SINGLE_PRODUCT_OFFERS = BASE_URL + "/product-service/price/";
    public static final String GET_ORDERS = BASE_URL + "/order-service/orders";
    public static final String UPDATE_ORDER_STATUS = BASE_URL + "/order-service/orders/changeOrderStatus";
    public static final String GET_ORDER_QUOTATIONS = BASE_URL +"/order-service/quotes/order/";
    public static final String AWARD_QUOTATION = BASE_URL +"/order-service/quotes/award/";
    public static final String GET_PLACED_ORDERS = BASE_URL + "/order-service/orders/placedOrders";
    public static final String CREATE_ONLY_CONSUMER = BASE_URL + "/consumer-service/consumers/consumerOnly";
    public static final String UPLOAD_IMAGE = BASE_URL + "/product-service/product/uploadProductImage/";
    public static final String CREATE_PRODUCTS_RAW = BASE_URL + "/product-service/product/raw";



}
