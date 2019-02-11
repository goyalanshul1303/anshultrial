package com.application.onboarding.providersob;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 2/11/19.
 */

public class ConsumerListData {

    public ArrayList<ConsumerDetailsItem> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(ArrayList<ConsumerDetailsItem> detailsList) {
        this.detailsList = detailsList;
    }

    ArrayList<ConsumerDetailsItem> detailsList = new ArrayList<>();
}
