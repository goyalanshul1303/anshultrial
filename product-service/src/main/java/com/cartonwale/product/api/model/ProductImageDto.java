package com.cartonwale.product.api.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ProductImageDto {

	private List<MultipartFile> productImagesFiles = new ArrayList<>();
	
	public List<MultipartFile> getProductImagesFiles() {
		return productImagesFiles;
	}
	public void setProfileImagesFiles(List<MultipartFile> productImagesFiles) {
		this.productImagesFiles = productImagesFiles;
	}
}
