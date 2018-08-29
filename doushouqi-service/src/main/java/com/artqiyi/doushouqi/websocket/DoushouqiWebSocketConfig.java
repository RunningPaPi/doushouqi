package com.artqiyi.doushouqi.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.artqiyi.doushouqi.websocket.service.WebSocketHandlerService;

/**
 * WebSocket配置类
 *
 * @author wushuang
 * @since 2018-05-02
 */
@Configuration
//@EnableWebMvc//这个标注可以不加，如果有加，要extends WebMvcConfigurerAdapter
@EnableWebSocket
public class DoushouqiWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	@Autowired
	private DoushouqiWebSocketHandler doushouqiWebSocketHandler;


	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// 1.注册WebSocket
		String websocket_url = "/websocket/socketServer/{group}/{userId}"; // 设置websocket的地址
		registry.addHandler(doushouqiWebSocketHandler, websocket_url) // 注册Handler
				.setAllowedOrigins("*"); // 注册Interceptor

		// 2.注册SockJS，提供SockJS支持(主要是兼容ie8)
		String sockjs_url = "/sockjs/socketServer/{group}/{userId}"; // 设置sockjs的地址
		registry.addHandler(doushouqiWebSocketHandler, sockjs_url)
				.withSockJS();
    }

}
