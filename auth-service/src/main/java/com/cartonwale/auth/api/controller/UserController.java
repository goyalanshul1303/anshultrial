package com.cartonwale.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.auth.api.dto.PasswordDto;
import com.cartonwale.auth.api.dto.UserImageDto;
import com.cartonwale.auth.api.model.User;
import com.cartonwale.auth.api.service.UserService;
import com.cartonwale.common.async.response.AsyncResponseEntity;
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
    public AsyncResponseEntity<User> getAll() {
		return makeAsyncResponse(userService.getAll());
    }
	
	@RequestMapping("/{id}")
    public AsyncResponseEntity<User> getById(@PathVariable("id") String id) {
		return makeAsyncResponse(userService.getById(id));
    }
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public AsyncResponseEntity<CommonResponse> passwordChange(@ModelAttribute PasswordDto passwordDto) {
		return makeAsyncResponse(
				userService.changePassword(passwordDto.getOldPassword(), passwordDto.getNewPassword())
				.map(r->new CommonResponse(1, r)));
	}
	
	@RequestMapping(method = RequestMethod.PUT)
    public AsyncResponseEntity<User> update(@ModelAttribute User user) {
    	return makeAsyncResponse(userService.edit(user, new UserImageDto()).map(i->{
			publishMessage(i.getId(), i);
			return i;
		}), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value="/updateFullProfile", method = RequestMethod.POST)
    public AsyncResponseEntity<User> updateFullProfile(@ModelAttribute User user, @ModelAttribute UserImageDto imageDto) {
    	return makeAsyncResponse(userService.edit(user, imageDto).map(i->{
			publishMessage(i.getId(), i);
			return i;
		}), HttpStatus.ACCEPTED);
    }

}
