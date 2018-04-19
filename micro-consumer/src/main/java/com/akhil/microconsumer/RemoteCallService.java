package com.akhil.microconsumer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name="micro-producer")
public interface RemoteCallService {
	@RequestMapping(method=RequestMethod.GET, value="/employee")
	public Employee getData();

}