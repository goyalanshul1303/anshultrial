package com.cartonwale.product.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cartonwale.common.constants.ConfigConstants;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.messages.InfoMessage;
import com.cartonwale.common.model.image.Image;
import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.ServiceUtil;
import com.cartonwale.common.util.image.ImageUtil;
import com.cartonwale.product.api.dao.ProductDao;
import com.cartonwale.product.api.exception.DuplicateProductException;
import com.cartonwale.product.api.model.Order;
import com.cartonwale.product.api.model.Product;
import com.cartonwale.product.api.model.ProductImageDto;
import com.cartonwale.product.api.model.ProductPrice;
import com.cartonwale.product.api.service.ProductPriceService;
import com.cartonwale.product.api.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductServiceImpl extends GenericServiceImpl<Product> implements ProductService {
	
	private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductPriceService productPriceService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostConstruct
	void init() {
		init(Product.class, productDao);
	}
	
	@Value(ConfigConstants.IMAGE_UPLOAD_LOCATION)
	private String IMAGE_LOCATION;
	
	@Override
	public Product add(Product product) {
		
		List<Product> existingProduct = productDao.getByName(product.getName(), product.getConsumerId());
		
		if(existingProduct != null && !existingProduct.isEmpty())
			
			throw new DuplicateProductException("Product with this name already exists");
		
		Product productNew = super.add(product);
		
		productPriceService.add(new ProductPrice(productNew.getId()));
		return productNew;
	};
	
	@Override
	public Product edit(Product product) {
		
		return super.edit(product);
	}
	
	@Override
	public List<Product> getAll(String authToken){
		
		List<Product> products = null;
		if(SecurityUtil.getAuthUserDetails().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER_ADMIN")))
			try {
				products = productDao.getAll();
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
			}
		else
			products = productDao.getAllByConsumer(SecurityUtil.getAuthUserDetails().getEntityId());
		
		products.stream().peek(p -> logger.debug("Product Price: " + p.getPrice()));
		
		addRecentOrdersToProducts(products, authToken);
		
		addPriceToProducts(products);
		
		return products ;
	}

	
	@Override
	public Product getById(String consumerId, String id) {

		List<Product> products = Arrays.asList(productDao.getById(consumerId, id));
		addPriceToProducts(products);
		return products.get(0);
	}

	@Override
	public List<Product> getAllByConsumer(String consumerId) {
		List<Product> products = productDao.getAllByConsumer(consumerId);
		
		return products ;
	}

	private void addPriceToProducts(List<Product> products) {
		
		List<String> productIds = products.stream().map(product -> product.getId()).collect(Collectors.toList());
		
		List<ProductPrice> productPrices = productPriceService.getByProductIds(productIds);
		Map<String, ProductPrice> productPriceMap = productPrices.stream().collect(Collectors.toMap(ProductPrice::getProductId, pp -> pp));
		
		products.stream().forEach(p -> p.setPrice(productPriceMap.getOrDefault(p.getId(), new ProductPrice()).getPrice()));
		
	}

	private void addRecentOrdersToProducts(List<Product> products, String authToken) {
		
		List<String> productIds = products.stream().map(product -> product.getId()).collect(Collectors.toList());
		
		ResponseEntity<List<Order>> responseEntity = ServiceUtil.callByType(HttpMethod.PUT,
				authToken, Arrays.asList(MediaType.APPLICATION_JSON), null, "http://ORDER-SERVICE/orders/abc/recentOrders",
				getProviderUserAsString(productIds), restTemplate, new ParameterizedTypeReference<List<Order>>() {});
		
		List<Order> orders = responseEntity.getBody();
		
		Map<String, Order> recentOrderProductMap = orders.stream().collect(Collectors.toMap(Order::getProductId, o -> o));
		products.stream().forEach(p -> p.setLastOrder(recentOrderProductMap.get(p.getId())));
		
	}
	
	private String getProviderUserAsString(List<String> productIds) {
		ObjectMapper mapper = new ObjectMapper();

		String json = null;
		try {
			json = mapper.writeValueAsString(productIds);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		logger.error(json);
		return json;
	}

	@Override
	public List<Product> getProductsAcceptingOffers() {
		
		List<String> productIds = productPriceService.getProductsAcceptingOffers();
		return productDao.getByProductIds(productIds);
	}
	
	@Override
	public Boolean uploadProductImage(ProductImageDto imageDto, String id) {
		
		ImageUtil imageUtil = ImageUtil.createDropBoxStorageImageUtil();
		//AuthUser authUser = SecurityUtil.getAuthUserDetails();
		Product product = getById(id);
		//Prepare Product Picture File Names
        List<Image> tmpProductPictures = new ArrayList<>();
    	for(MultipartFile file : imageDto.getProductImagesFiles()){
    		String fileName = imageUtil.genarateFileName(file);
    		tmpProductPictures.add(new Image(fileName));
    	}
		
		IntStream.range(0, imageDto.getProductImagesFiles().size()).forEach(idx->{
			
			//String id = authUser.getUserId();
			String fileName = tmpProductPictures.get(idx).getFileName();
			MultipartFile file = imageDto.getProductImagesFiles().get(idx);
			
			try{
				String s = imageUtil.storeFile(String.valueOf(id), fileName, IMAGE_LOCATION, ConfigConstants.IMAGE_PROFILE_UPLOAD_LOCATION, file);
				logger.info(InfoMessage.USER_PROFILE_IMAGE_SAVED, s);
			}catch(Exception e){
				logger.error("Error while uploading file" + e);
			}
			product.getImages().add(fileName);
		});
		
		edit(product);
		
		return Boolean.TRUE;
	}

	@Override
	public byte[] getProductImage(String id) {
		ImageUtil imageUtil = ImageUtil.createDropBoxStorageImageUtil();
		return imageUtil.readFile(IMAGE_LOCATION, ConfigConstants.IMAGE_PROFILE_UPLOAD_LOCATION, id, getById(id).getImages().get(0));
		
	}
}
