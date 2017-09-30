package com.example.xililive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class XililiveApplication {

	@RequestMapping("/xililive")
	@ResponseBody
	String home() {
		return MainActivity.domain();
	}
	@RequestMapping("/index")
	@ResponseBody
	String index(){
		return "hello world";
	}
	public static void main(String[] args) {
		SpringApplication.run(XililiveApplication.class, args);
	}
}
