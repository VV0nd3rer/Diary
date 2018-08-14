package com.kverchi.diary.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kverchi.diary.model.entity.User;
import com.kverchi.diary.model.entity.Role;

public class UserDetailsImpl implements UserDetails {
	private User user;
	
	public UserDetailsImpl(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> users = new HashSet<GrantedAuthority>();
		for (Role role : user.getRoles()) {
			users.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return users;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
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
		return user.isEnabled();
	}

}
