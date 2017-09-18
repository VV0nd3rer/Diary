package com.kverchi.diary.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kverchi.diary.domain.Role;

@Entity
@Table(name="users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private int userId;
	private String username;
    private String password;
    private boolean enabled;
    private String email;
	private String information;
	private ZonedDateTime registration_date;


	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    		name="user_roles",
    		joinColumns = {@JoinColumn(name="user_id", referencedColumnName="user_id")},
    		inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="role_id")}
    		)
    private Collection<Role> roles = new HashSet<Role>();
    public User() {};
	public User(int userId) {
		this.userId = userId;
	};
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public ZonedDateTime getRegistration_date() {
		DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
		String text = registration_date.format(formatter);
		registration_date = ZonedDateTime.parse(text, formatter);
		return registration_date;
	}

	public void setRegistration_date(ZonedDateTime registration_date) {
		this.registration_date = registration_date;
	}

	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
    
}
