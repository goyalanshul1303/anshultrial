package com.cartonwale.auth.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cartonwale.auth.api.dao.RoleDao;
import com.cartonwale.auth.api.dao.UserDao;
import com.cartonwale.auth.api.dto.UserImageDto;
import com.cartonwale.auth.api.model.Role;
import com.cartonwale.auth.api.model.User;
import com.cartonwale.auth.api.security.SecurityUtil;
import com.cartonwale.auth.api.service.UserService;
import com.cartonwale.common.constants.ConfigConstants;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.exception.DuplicateEmailRegisteredException;
import com.cartonwale.common.exception.OldPasswordNotMatch;
import com.cartonwale.common.exception.RequiredFieldMissingException;
import com.cartonwale.common.exception.ServiceException;
import com.cartonwale.common.messages.ErrorMessage;
import com.cartonwale.common.messages.InfoMessage;
import com.cartonwale.common.model.Mail;
import com.cartonwale.common.model.Permission;
import com.cartonwale.common.model.image.Image;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.MailUtil;
import com.cartonwale.common.util.image.ImageUtil;

import rx.Single;
import rx.exceptions.Exceptions;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService{

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
    
	@Value(ConfigConstants.IMAGE_UPLOAD_LOCATION)
	private String IMAGE_LOCATION;
	
	@PostConstruct
	void init() {
        init(User.class, userDao);
    }

	@Override
	public User findByEmail(String email){
		try{
			return /*Single.just(*/userDao.findByEmail(email)/*)*/;
		}catch (DataAccessException e) {
			System.out.println(e);
			return null;
			/*return Single.error(e);*/
        }
	}
	
	@Override
	public User findByUsername(String username){
		try{
			return /*Single.just(*/userDao.findByUsername(username)/*)*/;
		}catch (DataAccessException e) {
			System.out.println(e);
			return null;
			/*return Single.error(e);*/
        }
	}
	
	@Override
	public User findByUsernameOrEmail(String username, String email){
		try{
			return /*Single.just(*/userDao.findByUsernameOrEmail(username, email)/*)*/;
		}catch (DataAccessException e) {
			System.out.println(e);
			return null;
			/*return Single.error(e);*/
        }
	}
	
	@Override
    public User add(User user){
		
		registerValidation(user);
    	
    	/*return findByEmail(user.getEmail()).flatMap(*/
    			
		User checkUser = findByUsernameOrEmail(user.getUsername(), user.getEmail());
		
		//check email already registered
		if(checkUser != null)
			throw new DuplicateEmailRegisteredException(); 
		
		//setting default username, if username is empty
    	if(StringUtils.isEmpty(user.getUsername())){
    		/*String[] splictedEmail = user.getEmail().split("@");
    		if(splictedEmail.length >= 2) user.setUsername(splictedEmail[0]);*/
    		user.setUsername(user.getContactNumber());
    	}
    		
        //encode password
    	String emailPwd = user.getPassword();
    	user.setPassword(passwordEncoder.encode(emailPwd));
    	user.setRegisteredOn(new Date());
    	user.setLastPasswordResetDate(new Date());
        
    	for(Role r : user.getRoles()){
    	
    		Role role;
			try {
				role = roleDao.getById(r.getId());
				 //set user_permission
	            List<Permission> permissions = role.getPermissions();
	            for(Permission permission : permissions){
	            	user.getPermissions().add(permission);
	            }
	            
			} catch (DataAccessException e) {
				System.out.println(e);
				return null;
			}
    		
    	}
    	
    	user.setStatus(User.STATUS_INACTIVE);
        
    	User created  = super.add(user);
    	
    	MailUtil.sendMail(new Mail(user.getEmail(), "Your Cartonwale Account Details",
				"admin@cartonwale-auth-service.appspotmail.com",
				getMailBody(user.getUsername(), emailPwd)));
        return created;
            
    	/*});*/
    	
    }
	
	private String getMailBody(String userId, String password) {

		return "UserId: " + userId + "\nPassword: " + password;
	}
	
	@Override
    public User edit(User user, UserImageDto imageDto) throws ServiceException{
    	
		User loggedUser = SecurityUtil.getLoggedDbUser();
		
		ImageUtil imageUtil = ImageUtil.createDropBoxStorageImageUtil();
		
    	//Prepare Profile Picture File Names
        List<Image> tmpProfilePictures = new ArrayList<>();
    	for(MultipartFile file : imageDto.getProfileImagesFiles()){
    		String fileName = imageUtil.genarateFileName(file);
    		tmpProfilePictures.add(new Image(fileName));
    	}
    	//Prepare Profile Picture File Names
    	
    	//Prepare Cover Picture File Names
    	List<Image> tmpCoverPictures = new ArrayList<>();
    	for(MultipartFile file : imageDto.getCoverImagesFiles()){
    		String fileName = imageUtil.genarateFileName(file);
    		tmpCoverPictures.add(new Image(fileName));
    	}
    	//Prepare Cover Picture File Names
    	
    	/*return getById(loggedUser.getId()).flatMap(*/
    			
		User savedUser = getById(loggedUser.getId());
		if(savedUser != null){
				
			/**
			 * email, username, password, can't update by common edit function
			 */
			user.setId(savedUser.getId());
			user.setEmail(savedUser.getEmail());
			user.setUsername(savedUser.getUsername());
			user.setPassword(savedUser.getPassword());
			user.setRoles(savedUser.getRoles());
			user.setPermissions(savedUser.getPermissions());
			user.setRegisteredOn(savedUser.getRegisteredOn());
			user.setLastPasswordResetDate(savedUser.getLastPasswordResetDate());
			user.setLastLoggedOn(savedUser.getLastLoggedOn());
			user.setAttempts(savedUser.getAttempts());
			user.setStatus(savedUser.getStatus());
			
			//add images with existing images
			user.setProfileImageContainer(savedUser.getProfileImageContainer());
			user.getProfileImageContainer().getImages().addAll(tmpProfilePictures);
			
			user.setCoverImageContainer(savedUser.getCoverImageContainer());
			user.getCoverImageContainer().getImages().addAll(tmpCoverPictures);
			
			if(Role.checkUserIs_Seller_Admin(savedUser) || Role.checkUserIs_Seller(savedUser)){
				user.setSellerProfile(savedUser.getSellerProfile());
				user.setBuyerProfile(null);
			}
			else if(Role.checkUserIs_Buyer(savedUser)){
				user.setBuyerProfile(savedUser.getBuyerProfile());
				user.setSellerProfile(null);
			}
			
			return super.edit(user);
				
		}else{
			throw new ServiceException(ErrorMessage.USER_NOT_FOUND);
		}	
		/*}).map(u->{
    		
    		IntStream.range(0, imageDto.getProfileImagesFiles().size()).forEach(idx->{
				
				String id = u.getId();
				String fileName = tmpProfilePictures.get(idx).getFileName();
				MultipartFile file = imageDto.getProfileImagesFiles().get(idx);
				
				try{
					imageUtil.storeFile(String.valueOf(id), fileName, IMAGE_LOCATION, ConfigConstants.IMAGE_PROFILE_UPLOAD_LOCATION, file).subscribe(s->{
						logger.info(InfoMessage.USER_PROFILE_IMAGE_SAVED, s);
					});
				}catch(Exception e){
					throw Exceptions.propagate(e);
				}
			});
    		
    		IntStream.range(0, imageDto.getCoverImagesFiles().size()).forEach(idx->{
				
    			String id = u.getId();
				String fileName = tmpCoverPictures.get(idx).getFileName();
				MultipartFile file = imageDto.getCoverImagesFiles().get(idx);
				
				try{
					imageUtil.storeFile(String.valueOf(id), fileName, IMAGE_LOCATION, ConfigConstants.IMAGE_COVER_UPLOAD_LOCATION, file).subscribe(s->{
						logger.info(InfoMessage.USER_COVER_IMAGE_SAVED, s);
					});
				}catch(Exception e){
					throw Exceptions.propagate(e);
				}
			});
    		
    		return u;
    	});*/
    	
    }
	
	@Override
    public Boolean changePassword(String oldPassword, String newPassword){

		User loggedUser = SecurityUtil.getLoggedDbUser();
		
       if(passwordEncoder.matches(oldPassword, loggedUser.getPassword())){
    	   
    	   /*return getById(loggedUser.getId()).flatMap(*/
		   User savedUser = getById(loggedUser.getId());
		   
		   savedUser.setPassword(passwordEncoder.encode(newPassword));
		   savedUser.setLastPasswordResetDate(new Date());
		   savedUser.setStatus(User.STATUS_ACTIVE);
		   super.edit(savedUser);
		   return true;
			   
		   /*});*/
    	   
       }else
    	   throw new OldPasswordNotMatch();
    }

	@Override
	public Single<Boolean> delete(String id){
		User user= new User();
		user.setId(id);
		super.delete(user);
		return Single.just(true);
	}

	@Override
	public Single<Boolean> delete(List<String> idList){
		for(String id : idList){
			this.delete(id);
		}
		return Single.just(true);
	}

	@Override
	public Single<User> findUserByToken(String token){
		try{
			return Single.just(userDao.findUserByToken(token));
		}catch (Exception e) {
			return Single.error(e);
		}
	}
	
	private boolean registerValidation(User user){
		
		List<String> missingFields = new ArrayList<>();
		
		if(StringUtils.isEmpty(user.getEmail()))
			missingFields.add("user.email");
		
		if(StringUtils.isEmpty(user.getPassword()))
			missingFields.add("user.password");
		
		if(!missingFields.isEmpty())
			throw new RequiredFieldMissingException(missingFields.toArray());
		
		return true;
		
	}
	
}
