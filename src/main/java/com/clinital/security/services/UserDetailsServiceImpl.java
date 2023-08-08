package com.clinital.security.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinital.enums.ProviderEnum;
import com.clinital.models.User;
import com.clinital.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
            throw new UsernameNotFoundException(email);
        }
		//show 
        if (user.isEnabled()==false) {
            throw new BadCredentialsException("Compte bloqué, Merci de contacter le support");
        }
		
		else return UserDetailsImpl.build(user);

		
	}

	@Transactional
	public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
		User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User" + id));

		return UserDetailsImpl.build(user);
	}

	@Transactional
	public boolean isAccountVerified(String email) {
		boolean isVerified = userRepository.findEmailVerifiedByEmail(email);
		return isVerified;
	}

	public User processOAuthPostLogin(String username, ProviderEnum provider) {
		User user = null;
		user = userRepository.findByEmail(username);

		if (user == null) {
			user = new User();
			user.setEmail(username);
			user.setProvider(provider);
			user.setEmailVerified(true);

			userRepository.save(user);
		}

		updateLastLoginDate(user.getId());

		return user;

	}

	public void updateLastLoginDate(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));

		user.setLastLogin(new Date());

		userRepository.save(user);
	}
}
