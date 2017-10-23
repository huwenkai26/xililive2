package com.example.xililive;

import com.example.xililive.meme.MemeMainActivity;
import com.example.xililive.tianjiao.MainActivity;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class XililiveApplication {

	@RequestMapping("/tianjiao")
	@ResponseBody
	String home() {
		return new MainActivity().domain();
	}


	@RequestMapping("/xingguang")
	@ResponseBody
	String home2() {
		return new MainActivity2().domain();
	}

	@RequestMapping("/meme")
	@ResponseBody
	String home3() {
		return new MemeMainActivity().domain();
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(XililiveApplication.class, args);
	}
}