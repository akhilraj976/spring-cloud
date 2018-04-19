package com.akhil.microconsumer;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ConsumerControllerClient {

	//@Autowired
	//private DiscoveryClient discoveryClient;
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	//Get Employee using Ribbon client load balance
	public void getEmployee() throws RestClientException, IOException {

		//List<ServiceInstance> instances=discoveryClient.getInstances("micro-producer");
		//ServiceInstance serviceInstance=instances.get(0);
		//String baseUrl=serviceInstance.getUri().toString();
		
		ServiceInstance serviceInstance=loadBalancer.choose("micro-producer");
		//System.out.println(serviceInstance.getUri());
		String baseUrl=serviceInstance.getUri().toString();
		
		baseUrl=baseUrl+"/employee";
		
		//String baseUrl = "http://localhost:8091/employee";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		try{
		response=restTemplate.exchange(baseUrl,
				HttpMethod.GET, getHeaders(),String.class);
		}catch (Exception ex)
		{
			System.out.println(ex);
		}
		System.out.println(response.getBody());
	}

	@Autowired
	private RemoteCallService feignLoadBalancer;
	//Get employee details using Feign Client .. Feign client itself behaves as a LB
	public void getEmployee1() throws RestClientException, IOException {

		try {
			Employee emp = feignLoadBalancer.getData();
			System.out.println("########"+emp.getEmpId());
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	
	//Get employee details using ZUUL Proxy
	public void getEmployee2() throws RestClientException, IOException {
		System.out.println("Using ZUUL Service registered in Eureka to call producer service");

		ServiceInstance serviceInstance=loadBalancer.choose("zuul-service");
		//System.out.println(serviceInstance.getUri());
		String baseUrl=serviceInstance.getUri().toString();
		
		baseUrl=baseUrl+"/producer/employee";
		
		//String baseUrl = "http://localhost:8091/employee";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		try{
		response=restTemplate.exchange(baseUrl,
				HttpMethod.GET, getHeaders(),String.class);
		}catch (Exception ex)
		{
			System.out.println(ex);
		}
		System.out.println(response.getBody());
	}
	
	private static HttpEntity<?> getHeaders() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
}