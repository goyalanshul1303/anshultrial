package com.cartonwale.product.api.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;

@Document(collection = "Consumer")
public class Consumer extends EntityBase{

	private String consumerName;
	private String contactName;
	private List<Phone> phones;
	private String email;
	private String website;
	private int foundationYear;
	private double annualIncome;
	private String companyPAN;
	private String gstin;
	private String boundedProviderId;
	private List<CartonType> cartonType;
	private int expectedQuantity;
	private boolean sampleCollection;
	private String currentVendor;
	private String otherVendor;
	private double prepaymentPercent;
	private int maxCreditDays;
	
	public String getConsumerName() {
		return consumerName;
	}
	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
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
	public double getAnnualIncome() {
		return annualIncome;
	}
	public void setAnnualIncome(double annualIncome) {
		this.annualIncome = annualIncome;
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
	public List<CartonType> getCartonType() {
		return cartonType;
	}
	public void setCartonType(List<Integer> cartonType) {
		this.cartonType = cartonType.stream().map(value -> CartonType.getCartonType(value)).collect(Collectors.toList());;
	}
	public int getExpectedQuantity() {
		return expectedQuantity;
	}
	public void setExpectedQuantity(int expectedQuantity) {
		this.expectedQuantity = expectedQuantity;
	}
	public boolean isSampleCollection() {
		return sampleCollection;
	}
	public void setSampleCollection(boolean sampleCollection) {
		this.sampleCollection = sampleCollection;
	}
	public String getCurrentVendor() {
		return currentVendor;
	}
	public void setCurrentVendor(String currentVendor) {
		this.currentVendor = currentVendor;
	}
	public String getOtherVendor() {
		return otherVendor;
	}
	public void setOtherVendor(String otherVendor) {
		this.otherVendor = otherVendor;
	}
	public double getPrepaymentPercent() {
		return prepaymentPercent;
	}
	public void setPrepaymentPercent(double prepaymentPercent) {
		this.prepaymentPercent = prepaymentPercent;
	}
	public int getMaxCreditDays() {
		return maxCreditDays;
	}
	public void setMaxCreditDays(int maxCreditDays) {
		this.maxCreditDays = maxCreditDays;
	}
	public String getBoundedProviderId() {
		return boundedProviderId;
	}
	public void setBoundedProviderId(String boundedProviderId) {
		this.boundedProviderId = boundedProviderId;
	}
	
}
