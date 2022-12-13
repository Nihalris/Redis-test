package com.realnet.redis.redisconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.realnet.redis.subscriber.ComponentSubscriber;
import com.realnet.redis.subscriber.ProjectSubscriber;
import com.realnet.redis.subscriber.RepositorySubscriber;
import com.realnet.redis.subscriber.SureBoardSubscriber;
import com.realnet.redis.subscriber.WelcomeSubscriber;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

@Configuration
public class RedisConfiguration {
	
	
	
	
	

    @Bean(destroyMethod = "shutdown")
    ClientResources clientResources() {
        return DefaultClientResources.create();
    }

    @Bean(destroyMethod = "shutdown")
    RedisClient redisClient(ClientResources clientResources) {
        return RedisClient.create(clientResources, RedisURI.create("10.101.58.10", 6379));
    }

    @Bean(destroyMethod = "close")
    StatefulRedisConnection<String, String> connection(RedisClient redisClient) {
        return redisClient.connect();
    }

    @Bean(destroyMethod = "close")
    StatefulRedisPubSubConnection<String, String> connectionPubSub(RedisClient redisClient) {
        return redisClient.connectPubSub();
    }
	
	
//	http://54.227.230.160:32766
    
    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setHostName("my-redis-store.5w9upe.0001.use2.cache.amazonaws.com");
        configuration.setHostName("10.101.58.10");
        configuration.setPort(6379);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> template() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
	
	@Bean
	public ChannelTopic topic() {
		
		return new ChannelTopic("welcome");
	}
	
	@Bean
	public ChannelTopic topic1() {
		
		return new ChannelTopic("project");
	}
	
	@Bean
	public ChannelTopic topic2() {
		return new ChannelTopic("sureboard");
	}
		
	@Bean
	public ChannelTopic topic3() {
		return new ChannelTopic("repository");
	}
	
	
	@Bean
	public ChannelTopic topic4() {
		return new ChannelTopic("component");
	}
	
	
	@Bean
	MessageListenerAdapter welcomeMessagelistenerAdapter() {
		return new MessageListenerAdapter(new WelcomeSubscriber());
		
	}
	
	
	
	@Bean
	MessageListenerAdapter sureboardMessagelistenerAdapter() {
		return new MessageListenerAdapter(new SureBoardSubscriber());
		
	}
	
	
	
	@Bean
	MessageListenerAdapter repositoryMessagelistenerAdapter() {
		return new MessageListenerAdapter(new RepositorySubscriber());
		
		
	}	
	@Bean
	MessageListenerAdapter componentMessagelistenerAdapter() {
		return new MessageListenerAdapter(new ComponentSubscriber());
		
	}
	@Bean
	public MessageListenerAdapter projectListenerAdapter() {
		return new MessageListenerAdapter(new ProjectSubscriber());
	}
	
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer() {
		
		
		ListOfPreSubscribedChannel.presubscribedLlist.add(topic().toString());
		ListOfPreSubscribedChannel.presubscribedLlist.add(topic1().toString());
		ListOfPreSubscribedChannel.presubscribedLlist.add(topic2().toString());
		ListOfPreSubscribedChannel.presubscribedLlist.add(topic3().toString());
		ListOfPreSubscribedChannel.presubscribedLlist.add(topic4().toString());
		
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();	
		container.setConnectionFactory(connectionFactory());
		
		container.addMessageListener(welcomeMessagelistenerAdapter(), topic());
		container.addMessageListener(projectListenerAdapter(), topic1());
		container.addMessageListener(sureboardMessagelistenerAdapter(), topic2());
		container.addMessageListener(repositoryMessagelistenerAdapter(), topic3());
		container.addMessageListener(componentMessagelistenerAdapter(), topic4());
		System.out.println(container.getConnectionFactory());
		return container;
	}

	
	


		
}
