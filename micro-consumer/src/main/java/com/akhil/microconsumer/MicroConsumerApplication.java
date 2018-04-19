package com.akhil.microconsumer;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClientException;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class MicroConsumerApplication {

	public static void main(String[] args) throws RestClientException, IOException {
		ApplicationContext ctx = SpringApplication.run(MicroConsumerApplication.class, args);

		ConsumerControllerClient consumerControllerClient = ctx.getBean(ConsumerControllerClient.class);
		System.out.println(consumerControllerClient);
		
		for(int i=0;i<20;i++)		
			consumerControllerClient.getEmployee();
		
		//Using FeignClient
		consumerControllerClient.getEmployee1();
		
		//Using ZUUL Proxy
		consumerControllerClient.getEmployee2();

	}

	@Bean
	public ConsumerControllerClient consumerControllerClient() {
		return new ConsumerControllerClient();
	}
}
