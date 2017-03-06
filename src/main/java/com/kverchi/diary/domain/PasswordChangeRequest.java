package com.kverchi.diary.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="password_change_requests")
public class PasswordChangeRequest implements Serializable {
	@Id
	private String UUID;
	@Column(name="user_id")
	private int userId;
	@Column(name="created_time")
	private Date createdTime;
	@Column(name="isUUIDused")
	private boolean isUUIDused;
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public boolean isUUIDused() {
		return isUUIDused;
	}
	public void setUUIDused(boolean isUUIDused) {
		this.isUUIDused = isUUIDused;
	}
	

}
