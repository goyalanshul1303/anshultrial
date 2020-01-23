package com.cartonwale.common.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.image.ImageContainer;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection="User")
public class User extends EntityBase{

    private String email;
    
	private String username;

    private String password;
    
    private String firstName;

    private String lastName;
    
    private ImageContainer profileImageContainer = new ImageContainer();

    private ImageContainer coverImageContainer = new ImageContainer();

    private Date lastLoggedOn;

    private Date registeredOn;

    private Integer attempts;
    
    private Date lastPasswordResetDate;
    
    private String entityId;
    
    private String companyName;
    
    private String contactNumber;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
	public ImageContainer getProfileImageContainer() {
		return profileImageContainer;
	}

	public void setProfileImageContainer(ImageContainer profileImageContainer) {
		this.profileImageContainer = profileImageContainer;
	}

	public ImageContainer getCoverImageContainer() {
		return coverImageContainer;
	}

	public void setCoverImageContainer(ImageContainer coverImageContainer) {
		this.coverImageContainer = coverImageContainer;
	}

	public Date getLastLoggedOn() {
        return lastLoggedOn;
    }

    public void setLastLoggedOn(Date lastLoggedOn) {
        this.lastLoggedOn = lastLoggedOn;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	@JsonIgnore
	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

}
