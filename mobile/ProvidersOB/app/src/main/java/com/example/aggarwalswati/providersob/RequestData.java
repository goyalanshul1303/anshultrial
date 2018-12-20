package com.example.aggarwalswati.providersob;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggarwal.swati on 12/6/18.
 */

public class RequestData {

    String contactName;
    String email;
    String website;
    int foundationYear;
    int annualIncome;
    List<Integer> cartontype = new ArrayList();

    List<JSONObject> phones = new ArrayList<>();

    public List<AddressClass> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressClass> addresses) {
        this.addresses = addresses;
    }

    List<AddressClass> addresses = new ArrayList<>();




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


    public List<Integer> getCartontype() {
        return cartontype;
    }

    public void setCartontype(List<Integer> cartontype) {
        this.cartontype = cartontype;
    }


    public List<JSONObject> getPhones() {
        return phones;
    }

    public void setPhones(List<JSONObject> phones) {
        this.phones = phones;
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


    String companyPAN;
    String gstin;


}
