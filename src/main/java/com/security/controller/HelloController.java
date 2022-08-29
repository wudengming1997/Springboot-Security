package com.security.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@GetMapping("/admin/hello")
	public String admin() {
		return "hello admin";
	}

	@GetMapping("/user/hello")
	public String user() {
		return "hello user";
	}

	@GetMapping("/db/hello")
	public String db() {
		return "hello db";
	}

	@GetMapping("/test")
	public String test() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(15);
		String password = encoder.encode("123");
		System.out.println("---------password-----------> " + password);
		return password;
	}
}
