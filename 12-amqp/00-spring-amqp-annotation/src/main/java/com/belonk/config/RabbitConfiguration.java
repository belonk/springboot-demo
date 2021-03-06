package com.belonk.config;

import com.belonk.domain.User;
import com.belonk.util.Printer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sun on 2019/6/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class RabbitConfiguration {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final String ANONYMOUS_QUEUE_NAME = "spring.amqp.anonymous.queue";
    public static final String ANONYMOUS_QUEUE_NAME_1 = "spring.amqp.anonymous.queue1";
    public static final String ANONYMOUS_QUEUE_NAME_2 = "spring.amqp.anonymous.queue2";
    public static final String ANONYMOUS_QUEUE_NAME_3 = "spring.amqp.anonymous.queue3";
    public static final String ANONYMOUS_QUEUE_NAME_4 = "spring.amqp.anonymous.queue4";
    public static final String ANONYMOUS_QUEUE_NAME_5 = "spring.amqp.anonymous.queue5";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("192.168.0.27", 5672);
        cachingConnectionFactory.setUsername("admin");
        cachingConnectionFactory.setPassword("123456");
        // 消息确认
        cachingConnectionFactory.setPublisherConfirms(true);
        // 消息返回
        cachingConnectionFactory.setPublisherReturns(true);
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public RabbitTemplate callbackRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        // 消息返回
        rabbitTemplate.setMandatory(true);
        // 设置消息返回回调，一个RabbitTemplate只能设置一次返回回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                Printer.p("Message returned : " + replyCode + ", " + replyText);
            }
        });
        // 消息确认回调，一个RabbitTemplate只能设置一次确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                Printer.p("Message confirmed : " + ack + ", " + cause + ", " + correlationData);
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate jsonRabbitTemplate() {
        RabbitTemplate jsonRabbitTemplate = new RabbitTemplate(connectionFactory());
        jsonRabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return jsonRabbitTemplate;
    }

    // @Bean
    // public SimpleMessageListenerContainer simpleMessageListenerContainer() {
    //     SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
    //     container.setQueueNames(RabbitConfiguration.ANONYMOUS_QUEUE_NAME_5);
    //     container.setMessageConverter(jackson2JsonMessageConverter());
    //     return container;
    // }

    /**
     * 设置异步监听时的消息转换器，非JsonMessageDemo时注释掉
     */
    // @Bean
    // public RabbitListenerContainerFactory rabbitListenerContainerFactory() {
    //     SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
    //     rabbitListenerContainerFactory.setConnectionFactory(connectionFactory());
    //     rabbitListenerContainerFactory.setMessageConverter(jackson2JsonMessageConverter());
    //     return rabbitListenerContainerFactory;
    // }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        //
        // DefaultClassMapper classMapper = new DefaultClassMapper();
        // Map<String, Class<?>> idClassMapping = new HashMap<>();
        // idClassMapping.put("user", User.class);
        // classMapper.setIdClassMapping(idClassMapping);
        // jackson2JsonMessageConverter.setClassMapper(classMapper);
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue anonymousQueue() {
        // 匿名队列
        return new AnonymousQueue(() -> ANONYMOUS_QUEUE_NAME);
    }

    @Bean
    public Queue anonymousQueue1() {
        return new AnonymousQueue(() -> ANONYMOUS_QUEUE_NAME_1);
    }

    @Bean
    public Queue anonymousQueue2() {
        return new AnonymousQueue(() -> ANONYMOUS_QUEUE_NAME_2);
    }

    @Bean
    public Queue anonymousQueue3() {
        return new AnonymousQueue(() -> ANONYMOUS_QUEUE_NAME_3);
    }

    @Bean
    public Queue anonymousQueue4() {
        return new AnonymousQueue(() -> ANONYMOUS_QUEUE_NAME_4);
    }

    @Bean
    public Queue anonymousQueue5() {
        return new AnonymousQueue(() -> ANONYMOUS_QUEUE_NAME_5);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Protected Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Property accessors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Inner classes
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}