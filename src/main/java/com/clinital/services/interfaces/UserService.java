package com.clinital.services.interfaces;

import javax.servlet.http.HttpServletRequest;

import com.clinital.dto.UserDTO;
import com.clinital.models.User;
import com.clinital.security.services.UserDetailsImpl;

public interface UserService {
	
	User updateUser(User newUser, String email, UserDetailsImpl currentUser);

	User save(User user,Object obj);

}
