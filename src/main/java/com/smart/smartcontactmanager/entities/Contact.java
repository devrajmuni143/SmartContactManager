package com.smart.smartcontactmanager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	private String name;
	private String nickName;
	private String email;
	private String mob;
	private String work;
	private String imageFile;
	private String description;
	@ManyToOne
	@JsonIgnore
	private User user;
	
	
	public Contact(int cId, String name, String nickName, String email, String mob, String work, String imageFile,
			String description, User user) {
		super();
		this.cId = cId;
		this.name = name;
		this.nickName = nickName;
		this.email = email;
		this.mob = mob;
		this.work = work;
		this.imageFile = imageFile;
		this.description = description;
		this.user = user;
	}
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMob() {
		return mob;
	}
	public void setMob(String mob) {
		this.mob = mob;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getImageFile() {
		return imageFile;
	}
	public void setImage(String imageFile) {
		this.imageFile = imageFile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", nickName=" + nickName + ", email=" + email + ", mob=" + mob
				+ ", work=" + work + ", image=" + imageFile + ", description=" + description + ", user=" + user + "]";
	}
	
	
}
