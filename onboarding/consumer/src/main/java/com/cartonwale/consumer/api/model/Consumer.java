package com.cartonwale.consumer.api.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;

@Document(collection = "Consumer")
public class Consumer extends EntityBase{

	private String companyName;
	private String contactName;
	private List<Address> addresses;
	private List<Phone> phones;
	private String email;
	private String website;
	private int foundationYear;
	private double annualIncome;
	private String companyPAN;
	private String gstin;
	
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
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
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
	
}
