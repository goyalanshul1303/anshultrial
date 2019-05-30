package com.application.onboarding.providersob;

/**
 * Created by aggarwal.swati on 5/24/19.
 */

public class OrderStatus {
    public long getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(long statusDate) {
        this.statusDate = statusDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    long statusDate;
    int status;
}
