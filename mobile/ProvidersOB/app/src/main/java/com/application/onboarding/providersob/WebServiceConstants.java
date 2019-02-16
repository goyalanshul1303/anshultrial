package com.application.onboarding.providersob;

/**
 * Created by aggarwal.swati on 1/28/19.
 */

public class WebServiceConstants {
    private static String BASE_URL = "https://cartonwale-api-gateway.appspot.com/api";
    public static final String LOGIN = BASE_URL
            + "/auth-service/auth";
    public static final String CHANGE_PASSWORD = BASE_URL
            + "/auth-service/users/changePassword";
    public static final String CREATE_CONSUMER = BASE_URL + "/consumer-service/consumers";
    public static final String CREATE_PROVIDER = BASE_URL + "/provider-service/providers";
    public static final String GET_ALL_PRODUCTS = BASE_URL + "/product-service/product/consumer/";


}
