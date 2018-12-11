package com.example.aggarwalswati.providersob;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggarwal.swati on 12/6/18.
 */

public class RequestData {
    public int getPrintingType() {
        return printingType;
    }

    public void setPrintingType(int printingType) {
        this.printingType = printingType;
    }

    int printingType;

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    String operatingHours;
    String companyName;
    String contactName;
    String email;
    String website;
    int foundationYear;
    int annualIncome;
    int clientCount;
    List<Integer> cartontype = new ArrayList();
    List<Integer> corrugationType = new ArrayList();
    List<Integer> supportedSheetLayers = new ArrayList();
    List<JSONObject> phones = new ArrayList<>();

    public List<AddressClass> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressClass> addresses) {
        this.addresses = addresses;
    }

    List<AddressClass> addresses = new ArrayList<>();
    boolean isCreditSupported;
    int creditLimit;
    int creditDays;
    int factoryCapacity;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getFoundationYear() {
        return foundationYear;
    }

    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    public int getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(int annualIncome) {
        this.annualIncome = annualIncome;
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }

    public List<Integer> getCartontype() {
        return cartontype;
    }

    public void setCartontype(List<Integer> cartontype) {
        this.cartontype = cartontype;
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

    public List<JSONObject> getPhones() {
        return phones;
    }

    public void setPhones(List<JSONObject> phones) {
        this.phones = phones;
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

    public boolean isLogisticAvailable() {
        return isLogisticAvailable;
    }

    public void setLogisticAvailable(boolean logisticAvailable) {
        isLogisticAvailable = logisticAvailable;
    }

    public String getCompanyPAN() {
        return companyPAN;
    }

    public void setCompanyPAN(String companyPAN) {
        this.companyPAN = companyPAN;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
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

    boolean isLogisticAvailable;
    String companyPAN;
    String gstin;
    boolean shareCapacity;
    boolean allowQualityInspect;
    int dieCuttingChargesperThousand;
    boolean manufactureWithProvidedMaterial;
    int maxBoxSizeL;
    int maxBoxSizeW;



}
