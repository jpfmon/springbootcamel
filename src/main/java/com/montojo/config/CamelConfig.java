package com.montojo.config;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.component.ComponentsBuilderFactory;
import org.apache.camel.builder.component.dsl.Sjms2ComponentBuilderFactory;
import org.apache.camel.component.sjms2.Sjms2Component;
import org.apache.camel.spi.Registry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration
public class CamelConfig {

//    public static final String RABBIT_URI = "rabbitmq:amq.direct?queue=%s&routingKey=%s&autoDelete=false";
//
//    @Bean
//    public ConnectionFactory rabbitConnectionFactory(){
//        return factory();
//    }
//
//    public ConnectionFactory factory(){
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        factory.setPort(5672);
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//        return factory;
//    }


    @Bean
    public Sjms2Component sjms2() {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        props.put(Context.PROVIDER_URL, "t3//:localhost:49163");
        props.put(Context.SECURITY_PRINCIPAL, "weblogic");
        props.put(Context.SECURITY_CREDENTIALS, "welcome1");

        InitialContext webLogicServerContext = null;

        try {
            webLogicServerContext = new InitialContext(props);

        } catch (NamingException e) {
            System.out.println("\n\nNAMING EXCEPTION");
        }
        QueueConnectionFactory queueConnectionFactory = null;
        try {
            queueConnectionFactory = (QueueConnectionFactory) webLogicServerContext.lookup("ConnectionFactory");
        } catch (NamingException e) {
            System.out.println("\n\nNAMING EXCEPTION   2222222");
        }
//      comment in main branch
//        another comment in main
//        another comment in main
//        another comment in main
        return ComponentsBuilderFactory.sjms2().connectionFactory(queueConnectionFactory).build();
    }

}
