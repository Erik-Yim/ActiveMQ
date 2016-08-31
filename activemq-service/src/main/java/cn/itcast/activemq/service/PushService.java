package cn.itcast.activemq.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: PushService  
 * (推送的接口)
 * @author zhangtian  
 * @version
 */
public interface PushService {
	public final ExecutorService pushExecutor = Executors.newFixedThreadPool(10) ;
	
	public void push(Object info) ;
}
