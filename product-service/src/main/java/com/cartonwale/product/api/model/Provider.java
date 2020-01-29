package com.cartonwale.product.api.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;
import com.cartonwale.common.model.SheetLayerType;

@Document(collection = "Provider")
public class Provider extends EntityBase{

	private int providerId;
	private String companyName;
	private String contactName;
	private List<Phone> phones;
	private String email;
	private String website;
	private int foundationYear;
	private double annualIncome;
	private int clientCount;
	private List<CartonType> cartonType;
	private PrintingType printingType;
	private List<CorrugationType> corrugationType;
	private List<SheetLayerType> supportedSheetLayers;
	private boolean isCreditSupported;
	private double creditLimit;
	private int creditDays;
	private int factoryCapacity;
	private boolean isLogisticAvailable;
	private String companyPAN;
	private String gstin;
	private boolean shareCapacity;
	private boolean allowQualityInspect;
	private double dieCuttingChargesperThousand;
	private boolean manufactureWithProvidedMaterial;
	private int maxBoxSizeL;
	private int maxBoxSizeW;
	private boolean isMakeToStock;
	private boolean isMakeToOrder;
	
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
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
	public int getClientCount() {
		return clientCount;
	}
	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	
	public List<CartonType> getCartonType() {
		return cartonType;
	}
	public void setCartonType(List<Integer> cartonType) {
		this.cartonType = cartonType.stream().map(value -> CartonType.getCartonType(value)).collect(Collectors.toList());
	}
	public PrintingType getPrintingType() {
		return printingType;
	}
	
	public void setPrintingType(int value) {
		this.printingType = PrintingType.getPrintingType(value);
	}
	public List<CorrugationType> getCorrugationType() {
		return corrugationType;
	}
	public void setCorrugationType(List<Integer> corrugationType) {
		this.corrugationType = corrugationType.stream().map(value -> CorrugationType.getCorrugationType(value)).collect(Collectors.toList());;
	}
	public List<SheetLayerType> getSupportedSheetLayers() {
		return supportedSheetLayers;
	}
	public void setSupportedSheetLayers(List<Integer> supportedSheetLayers) {
		this.supportedSheetLayers = supportedSheetLayers.stream().map(value -> SheetLayerType.getSheetLayerType(value)).collect(Collectors.toList());
	}
	public boolean isCreditSupported() {
		return isCreditSupported;
	}
	public void setCreditSupported(boolean isCreditSupported) {
		this.isCreditSupported = isCreditSupported;
	}
	public double getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(double creditLimit) {
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
	public void setLogisticAvailable(boolean isLogisticAvailable) {
		this.isLogisticAvailable = isLogisticAvailable;
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
	public double getDieCuttingChargesperThousand() {
		return dieCuttingChargesperThousand;
	}
	public void setDieCuttingChargesperThousand(double dieCuttingChargesperThousand) {
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
	public boolean isMakeToStock() {
		return isMakeToStock;
	}
	public void setMakeToStock(boolean isMakeToStock) {
		this.isMakeToStock = isMakeToStock;
	}
	public boolean isMakeToOrder() {
		return isMakeToOrder;
	}
	public void setMakeToOrder(boolean isMakeToOrder) {
		this.isMakeToOrder = isMakeToOrder;
	}
	
	
}
