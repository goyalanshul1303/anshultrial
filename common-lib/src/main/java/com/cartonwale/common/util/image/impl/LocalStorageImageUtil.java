package com.cartonwale.common.util.image.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cartonwale.common.constants.ImageResizeLevel;
import com.cartonwale.common.util.image.ImageUtil;

public class LocalStorageImageUtil extends ImageUtil {

	private Logger logger = LoggerFactory.getLogger(LocalStorageImageUtil.class);

	@Override
	public String storeFile(String id, String fileName, String fileLocation, String imageDir,
			MultipartFile multipartFile){

		/*return Single.create(s->{*/
			
			try {
				String dirStr;
				if (!StringUtils.isEmpty(id))
					dirStr = fileLocation + File.separator + imageDir + File.separator + id;
				else
					dirStr = fileLocation + File.separator + imageDir;

				Path dir = Paths.get(dirStr);
				if (!Files.exists(dir)) {
					Files.createDirectories(dir);
				}

				Path file = Paths.get(dirStr + File.separator + fileName);

				byte[] bytes = multipartFile.getBytes();
				Files.write(file, bytes);

				resizeImage(bytes, dirStr, fileName, ImageResizeLevel.HIGH.getSize());
				resizeImage(bytes, dirStr, fileName, ImageResizeLevel.MEDIUM.getSize());
				resizeImage(bytes, dirStr, fileName, ImageResizeLevel.LOW.getSize());

				//s.onSuccess(fileName);;
				return fileName;

			} catch (Exception e) {
				logger.error(e.getMessage());
				//s.onError(e);
			}
			
			return null;
			
		/*}).subscribeOn(Schedulers.io());*/
	}
	
	@Override
	public void resizeImage(byte[] bytes, String dirStr, String fileName, int targetSize) throws Exception{
    	
		BufferedImage orginalImage = ImageIO.read(new ByteArrayInputStream(bytes));
		
		String[] splt = splitFileName(fileName);
		String randomFileName = splt[0];
		String fileExtension = splt[1];
		
		String savedImage = targetSize + "-" + randomFileName + "." + fileExtension;
		
		BufferedImage scaledImage = Scalr.resize(orginalImage, targetSize);
	    ImageIO.write(scaledImage, fileExtension, new File(dirStr + File.separator + savedImage));
    	
    }

	@Override
	public byte[] readFile(String fileLocation, String imageDir, String id,
			String fileName) {
		
		return null;
	}

}
