package com.olx.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olx.dto.AuthenticateRequest;
import com.olx.dto.User;
import com.olx.security.JwtUtil;



@RestController
@RequestMapping (value = "/olx")
public class UserController {
	
	
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	UserDetailsService userDetailsService;
	
	@PostMapping(value="/user/authenticate",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<String> authenticate(@RequestBody AuthenticateRequest authenticateRequest) {
		try {
			authenticationManager.authenticate(new  UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(), authenticateRequest.getPassword()));
		}catch(BadCredentialsException ex){
			return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
			
		}
		String jwtToken= jwtUtil.generateToken(authenticateRequest.getUsername());
		return new ResponseEntity<String>(jwtToken,HttpStatus.OK);
		
	}
	@GetMapping(value="/user/validate/token")
	public ResponseEntity<Boolean>authenticateToken(@RequestHeader("Authorization") String authToken){
		//UserDetails userDetails= ;
		String token = authToken.replace("Bearer", "");
		String clientUsername = jwtUtil.extractUsername(token);
		String databaseUsername = userDetailsService.loadUserByUsername(clientUsername).getUsername();
		boolean isValidToken = jwtUtil.validateToken(token, databaseUsername);
	    if(isValidToken)
	    	return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	    else
	    	return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
	}
	/*@PostMapping(value="/user",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}, produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<User> createNewUser(@RequestBody User user) {
		System.out.println("in controller class");
		return new ResponseEntity(user, HttpStatus.FOUND);
	}*/

}
