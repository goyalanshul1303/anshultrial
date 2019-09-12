package com.cartonwale.common.util.image.impl;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cartonwale.common.constants.ImageResizeLevel;
import com.cartonwale.common.util.image.ImageUtil;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.images.Transform;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class GoogleStorageImageUtil extends ImageUtil {

	private Logger logger = LoggerFactory.getLogger(GoogleStorageImageUtil.class);
	
	// Get an instance of the imagesService we can use to transform images.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
	
	final String bucket = "cartonwale-product-service.appspot.com";
	private final String SEPARATOR = "/";
	
	private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
		      .initialRetryDelayMillis(10)
		      .retryMaxAttempts(10)
		      .totalRetryPeriodMillis(15000)
		      .build());
	
	@Override
	public String storeFile(String id, String fileName, String fileLocation, String imageDir,
			MultipartFile multipartFile) {
		
		//[START original_image]
	    // Read the image.jpg resource into a ByteBuffer.
		try{
			String dirStr;
			if (!StringUtils.isEmpty(id))
				dirStr = SEPARATOR + imageDir + SEPARATOR + id;
			else
				dirStr = SEPARATOR + imageDir;
	
			byte[] bytes = multipartFile.getBytes();
	
		    // Write the original image to Cloud Storage
		    gcsService.createOrReplace(
		        new GcsFilename(bucket, fileName),
		        new GcsFileOptions.Builder().mimeType("image/jpeg").build(),
		        ByteBuffer.wrap(bytes));
		    
		    resizeImage(bytes, dirStr, fileName, ImageResizeLevel.HIGH.getSize());
			resizeImage(bytes, dirStr, fileName, ImageResizeLevel.MEDIUM.getSize());
			resizeImage(bytes, dirStr, fileName, ImageResizeLevel.LOW.getSize());
		    
		} catch (IOException io) {
			logger.error(io.getMessage());
		}
		// [START servingUrl]
	    // Create a fixed dedicated URL that points to the GCS hosted file
	    ServingUrlOptions options = ServingUrlOptions.Builder
	            .withGoogleStorageFileName("/gs/" + bucket + "/" + fileName)
	            .imageSize(150)
	            .crop(true)
	            .secureUrl(true);
	    String url = imagesService.getServingUrl(options);
	    // [END servingUrl]
		return url;
	}

	@Override
	public byte[] readFile(String fileLocation, String imageDir, String id, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resizeImage(byte[] bytes, String dirStr, String fileName, int targetSize) throws IOException {
		
		String[] splt = splitFileName(fileName);
		String randomFileName = splt[0];
		String fileExtension = splt[1];
		
		String savedImageName = targetSize + "-" + randomFileName + "." + fileExtension;
		
		//[START resize]

	    // Make an image directly from a byte array, and transform it.
	    Image image = ImagesServiceFactory.makeImage(bytes);
	    Transform resize = ImagesServiceFactory.makeResize(100, 50);
	    Image resizedImage = imagesService.applyTransform(resize, image);

	    // Write the transformed image back to a Cloud Storage object.
	    gcsService.createOrReplace(
	        new GcsFilename(bucket, savedImageName),
	        new GcsFileOptions.Builder().mimeType("image/jpeg").build(),
	        ByteBuffer.wrap(resizedImage.getImageData()));
		
	}

}
