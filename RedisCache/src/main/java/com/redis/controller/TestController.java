package com.redis.controller;


import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.resource.DefaultClientResources;



@RestController
public class TestController {
//	private StatefulRedisConnection<String, String> connection;
//	private StatefulRedisPubSubConnection<String, String> connectionPubSub;
//	
//	
//	public TestController(StatefulRedisConnection<String, String> connection,
//			StatefulRedisPubSubConnection<String, String> connectionPubSub) {
//		this.connection = connection;
//		this.connectionPubSub = connectionPubSub;
//		this.connectionPubSub.addListener(new RedisPubSubAdapter<String, String>() {
//			@Override
//			public void message(String channel, String message) {
//			}
//		});
//	}

	@RequestMapping("/test")
	public String testController() {
		try {
		
			RedisClient redisClient= RedisClient.create(DefaultClientResources.create(), RedisURI.create("10.244.3.6", 6379));
			StatefulRedisConnection<String, String> redis= redisClient.connect();
		
		return "Success";}
		
		catch(Exception e) {
			return "failed to connect";
		}
	}
	

}
