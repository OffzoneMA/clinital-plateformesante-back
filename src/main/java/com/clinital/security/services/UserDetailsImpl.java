package com.clinital.security.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.clinital.enums.ERole;
import com.clinital.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import net.bytebuddy.asm.Advice.This;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String email;
	
	private String telephone;

	@JsonIgnore
	private String password;

	private ERole role;

	private Boolean isEnabled;

	public UserDetailsImpl(Long id, String email, String telephone, String password,
	ERole role,Boolean isEnabled) {
		this.id = id;
		this.email = email;
		this.telephone = telephone;
		this.password = password;
		this.role= role;
		this.isEnabled=isEnabled;
	}

	public static UserDetailsImpl build(User user) {
		
		// List<GrantedAuthority> authorities = user.getRole().map(role -> new SimpleGrantedAuthority(role.getName().name()))
		// 		.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(), 
				user.getEmail(),
				user.getTelephone(),
				user.getPassword()
				, user.getRole(),user.isEnabled());
				
	}


	public Long getId() {
		return id;
	}

	
	public ERole getRole() {
		return role;
	}

	public String getEmail() {
		return email;
	}
	
	@Override
	public String getUsername() {
	        return email;
	    }
	@Override
	public String getPassword() {
		return password;
	}

	public String getTelephone() {
		return telephone;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(  ) {
		ERole role = this.getRole();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.name()));
		return authorities;
	}

	// public Set<String> getRoles() {
	// 	return getAuthorities().stream().map(GrantedAuthority::getAuthority)
	// 			.collect(Collectors.toSet());
	// }
	
}
