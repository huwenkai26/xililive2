package com.example.xililive;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class XililiveApplication {

	@RequestMapping("/xililive")
	@ResponseBody
	String home() {
		return MainActivity.domain();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(XililiveApplication.class, args);
	}
}