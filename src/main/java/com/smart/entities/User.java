package com.smart.entities;

import java.util.ArrayList;
import java.util.List;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class User {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private int id;
   @NotBlank(message="{Name field is required!!}")
   @Size(min=2,max=20,message="min 2 and max 20 char are require")
   private String name;
   @Column(unique=true)
   private String email;
   
   private String password;
   private String role;
   private boolean enabled;
   private String imageUrl;
   @Column(length=300)
   private String about;
@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="user")
private List<Contact>contacts=new ArrayList<>();


	public List<Contact> getContacts() {
	return contacts;
}


public void setContacts(List<Contact> contacts) {
	this.contacts = contacts;
}


	public int getId() {
	return id;
}


public String getName() {
	return name;
}




public String getEmail() {
	return email;
}




public String getPassword() {
	return password;
}




public String getRole() {
	return role;
}




public boolean getEnabled() {
	return enabled;
}




public String getImageUrl() {
	return imageUrl;
}




public String getAbout() {
	return about;
}




public void setId(int id) {
	this.id = id;
}




public void setName(String name) {
	this.name = name;
}




public void setEmail(String email) {
	this.email = email;
}




public void setPassword(String password) {
	this.password = password;
}




public void setRole(String role) {
	this.role = role;
}




public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}




public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}




public void setAbout(String about) {
	this.about = about;
}




	@Override
public String toString() {
	return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
			+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", contacts=" + contacts + "]";
}




	public User() {
		// TODO Auto-generated constructor stub
	}

}
