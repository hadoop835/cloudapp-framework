package com.alibaba.cloudapp.aliee.demo.service.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="${spring.application.name}")
public interface RemoteService {

    @RequestMapping(value = "/echo/{msg}", method = RequestMethod.GET)
    public String echo(@PathVariable("msg") String msg);
}
