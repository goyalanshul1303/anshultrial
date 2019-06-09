package com.app.carton.provider;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 2/11/19.
 */

public class OrdersListDetailsItem {
    String id;
    int status;
    String productName;
    int orderStatus;
    long orderDate;
    int changeDimension;
    String productId;
    String quantity;
    int orderAmount;
    DimensionClass dimension;
    AwardedQuote awardedQuote;
    ArrayList<OrderStatus> statuses = new ArrayList<>();
}
