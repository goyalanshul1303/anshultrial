package com.application.onboarding.providersob;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggarwal.swati on 12/20/18.
 */

public class ProviderRequest extends RequestData {
    int operatingHours;
    String companyName;
    int clientCount;
    int printingType;
    List<Integer> corrugationType = new ArrayList();
    List<Integer> supportedSheetLayers = new ArrayList();
    boolean isCreditSupported;
    int creditLimit;
    int creditDays;
    int factoryCapacity;
    boolean shareCapacity;
    boolean allowQualityInspect;
    int dieCuttingChargesperThousand;
    boolean manufactureWithProvidedMaterial;
    int maxBoxSizeL;
    int maxBoxSizeW;
    boolean isLogisticAvailable;


    public int getOperatingHours() {
        return operatingHours;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setOperatingHours(int operatingHours) {
        this.operatingHours = operatingHours;
    }

    public int getPrintingType() {
        return printingType;
    }

    public void setPrintingType(int printingType) {
        this.printingType = printingType;
    }


    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }

    public List<Integer> getCorrugationType() {
        return corrugationType;
    }

    public void setCorrugationType(List<Integer> corrugationType) {
        this.corrugationType = corrugationType;
    }

    public List<Integer> getSupportedSheetLayers() {
        return supportedSheetLayers;
    }

    public void setSupportedSheetLayers(List<Integer> supportedSheetLayers) {
        this.supportedSheetLayers = supportedSheetLayers;
    }

    public boolean isCreditSupported() {
        return isCreditSupported;
    }

    public void setCreditSupported(boolean creditSupported) {
        isCreditSupported = creditSupported;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(int creditDays) {
        this.creditDays = creditDays;
    }

    public int getFactoryCapacity() {
        return factoryCapacity;
    }

    public void setFactoryCapacity(int factoryCapacity) {
        this.factoryCapacity = factoryCapacity;
    }

    public boolean isShareCapacity() {
        return shareCapacity;
    }

    public void setShareCapacity(boolean shareCapacity) {
        this.shareCapacity = shareCapacity;
    }

    public boolean isAllowQualityInspect() {
        return allowQualityInspect;
    }

    public void setAllowQualityInspect(boolean allowQualityInspect) {
        this.allowQualityInspect = allowQualityInspect;
    }

    public int getDieCuttingChargesperThousand() {
        return dieCuttingChargesperThousand;
    }

    public void setDieCuttingChargesperThousand(int dieCuttingChargesperThousand) {
        this.dieCuttingChargesperThousand = dieCuttingChargesperThousand;
    }

    public boolean isManufactureWithProvidedMaterial() {
        return manufactureWithProvidedMaterial;
    }

    public void setManufactureWithProvidedMaterial(boolean manufactureWithProvidedMaterial) {
        this.manufactureWithProvidedMaterial = manufactureWithProvidedMaterial;
    }

    public int getMaxBoxSizeL() {
        return maxBoxSizeL;
    }

    public void setMaxBoxSizeL(int maxBoxSizeL) {
        this.maxBoxSizeL = maxBoxSizeL;
    }

    public int getMaxBoxSizeW() {
        return maxBoxSizeW;
    }

    public void setMaxBoxSizeW(int maxBoxSizeW) {
        this.maxBoxSizeW = maxBoxSizeW;
    }

    public boolean isLogisticAvailable() {
        return isLogisticAvailable;
    }

    public void setLogisticAvailable(boolean logisticAvailable) {
        isLogisticAvailable = logisticAvailable;
    }
}
