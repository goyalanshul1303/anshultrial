package com.cartonwale.order.api.model;

public class ConsumerDashboard {

	private int requirementsCount;
	
	private int openOrdersCount;

	public ConsumerDashboard(int requirementsCount, int openOrdersCount) {
		super();
		this.requirementsCount = requirementsCount;
		this.openOrdersCount = openOrdersCount;
	}

	public int getRequirementsCount() {
		return requirementsCount;
	}

	public void setRequirementsCount(int requirementsCount) {
		this.requirementsCount = requirementsCount;
	}

	public int getOpenOrdersCount() {
		return openOrdersCount;
	}

	public void setOpenOrdersCount(int openOrdersCount) {
		this.openOrdersCount = openOrdersCount;
	}
}
