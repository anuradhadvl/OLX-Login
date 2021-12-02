package com.olx.dto;

import lombok.Data;

@Data
public class User {
	
	private int id;
	private String username;
	private String password;
	private String roles;
	private String active;
	private String firstname;
	private String lastname;

}
