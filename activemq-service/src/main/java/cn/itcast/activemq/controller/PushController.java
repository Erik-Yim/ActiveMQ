package cn.itcast.activemq.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.activemq.domain.Client;
import cn.itcast.activemq.domain.News;
import cn.itcast.activemq.domain.User;
import cn.itcast.activemq.result.ResultResponse;
import cn.itcast.activemq.service.PushService;

@Controller
@RequestMapping("/push/")
public class PushController {
	@Resource(name="userPushService")
	private PushService userPushService;
	@Resource(name="newsPushService")
	private PushService newsPushService;
	@Resource(name="clientPushService")
	private PushService clientPushService;
	
	@RequestMapping("index")
	public String index() {
		return "index" ;
	}
	
	@RequestMapping("demo")
	@ResponseBody
	public String demo() {
		return "Hello Spring 4.3" ;
	}
	
	/**
	 * 用户推送
	 * @param info
	 * @return
	 * @create 2016-8-10 下午4:22:28
	 */
	@RequestMapping(value="user",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse userPush(User info){
		ResultResponse respone = new ResultResponse();
		try {
			userPushService.push(info);
			respone.setData(info);
		} catch (Exception e) {
			e.printStackTrace();
			respone = new ResultResponse(false, e.getMessage());
		}
		return respone;
	}
	
	/**
	 * 新闻推送
	 * @param info
	 * @return
	 * @create 2016-8-10 下午4:22:38
	 */
	@RequestMapping(value="news",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse newsPush(News info){
		ResultResponse respone = new ResultResponse();
		try {
			newsPushService.push(info);
			respone.setData(info);
		} catch (Exception e) {
			e.printStackTrace();
			respone = new ResultResponse(false, e.getMessage());
		}
		return respone;
	}
	/**
	 * 客户推送
	 * @param info
	 * @return
	 * @create 2016-8-10 下午4:22:48
	 */
	@RequestMapping(value="client",method=RequestMethod.POST)
	@ResponseBody
	public ResultResponse clientPush(Client info){
		ResultResponse respone = new ResultResponse();
		try {
			clientPushService.push(info);
			respone.setData(info);
		} catch (Exception e) {
			e.printStackTrace();
			respone = new ResultResponse(false, e.getMessage());
		}
		return respone;
	}
}
