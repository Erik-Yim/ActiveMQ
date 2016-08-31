package cn.itcast.activemq.listener.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping("index")
	public String index() {
		return "index" ;
	}
	
	@RequestMapping("demo")
	@ResponseBody
	public String demo() {
		return "Hello ActiveMQ 5.14.0" ;
	}
	
}
