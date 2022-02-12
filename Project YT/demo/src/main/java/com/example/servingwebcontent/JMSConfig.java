package com.example.servingwebcontent;

import javax.jms.ConnectionFactory;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.util.ErrorHandler;


@Configuration
@ComponentScan(basePackages = ".com.example")
public class JMSConfig {

	@Bean
	 public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) 
	  {
			DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
			factory.setErrorHandler(
				      (ErrorHandler) new ErrorHandler() {
				        @Override
				        public void handleError(Throwable t) {
				          //log.warn("An error has occurred in the transaction", t);
				        }
				      });
			configurer.configure(factory, connectionFactory);
		return factory;
	  }

	@Bean // Serialize message content to json using TextMessage
	  public MessageConverter jacksonJmsMessageConverter() {
	    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	    converter.setTargetType(MessageType.TEXT);
	    converter.setTypeIdPropertyName("_type");
	    return converter;
	  }

}
