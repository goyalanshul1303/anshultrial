package com.cartonwale.order.api.model;

public class ProviderDashboard {

	private int inProgressCount;
	
	private int quotationOrderCount;
	
	private int productPricingCount;
	
	public ProviderDashboard(int inProgressCount, int quotationOrderCount, int productPricingCount) {
		super();
		this.inProgressCount = inProgressCount;
		this.quotationOrderCount = quotationOrderCount;
		this.productPricingCount = productPricingCount;
	}

	public int getInProgressCount() {
		return inProgressCount;
	}

	public void setInProgressCount(int inProgressCount) {
		this.inProgressCount = inProgressCount;
	}

	public int getQuotationOrderCount() {
		return quotationOrderCount;
	}

	public void setQuotationOrderCount(int quotationOrderCount) {
		this.quotationOrderCount = quotationOrderCount;
	}

	public int getProductPricingCount() {
		return productPricingCount;
	}

	public void setProductPricingCount(int productPricingCount) {
		this.productPricingCount = productPricingCount;
	}
}
