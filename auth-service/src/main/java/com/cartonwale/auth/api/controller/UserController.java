package com.cartonwale.auth.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.auth.api.dto.PasswordDto;
import com.cartonwale.auth.api.dto.UserImageDto;
import com.cartonwale.auth.api.model.User;
import com.cartonwale.auth.api.service.UserService;
import com.cartonwale.common.exception.ServiceException;
import com.cartonwale.common.response.CommonResponse;
import com.cartonwale.common.sse.ListenerType;
import com.cartonwale.common.util.ControllerBase;

@RestController
@RequestMapping("/users")
public class UserController extends ControllerBase {

	@Autowired
    private UserService userService;
	
	public UserController() {
		super(ListenerType.USER);
	}
	
	@RequestMapping
    public ResponseEntity<List<User>> getAll() {
		return makeResponse(userService.getAll());
    }
	
	@RequestMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") String id) {
		return makeResponse(userService.getById(id));
    }
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse> passwordChange(@RequestBody PasswordDto passwordDto) {
		return makeResponse(
				/*userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword())
				.map(r->*/new CommonResponse(1, userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword())))/*)*/;
	}
	
	@RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<User> update(@ModelAttribute User user) {
		try {
			user = userService.edit(user, new UserImageDto());
			publishMessage(user.getId(), user);
			return makeResponse(/*userService.edit(user, new UserImageDto()).map(i->{*/
					user
					/*return i;
				})*/, HttpStatus.ACCEPTED);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
    	
    }
    
    @RequestMapping(value="/updateFullProfile", method = RequestMethod.POST)
    public ResponseEntity<User> updateFullProfile(@ModelAttribute User user, @ModelAttribute UserImageDto imageDto) {
    	try {
			user = userService.edit(user, new UserImageDto());
			publishMessage(user.getId(), user);
			return makeResponse(/*userService.edit(user, new UserImageDto()).map(i->{*/
					user
					/*return i;
				})*/, HttpStatus.ACCEPTED);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

}
