package com.wcwy.oprnfeign.service;

import com.wcwy.oprnfeign.config.FeignConfig;
import org.redisson.api.RLock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: TestService
 * @Description: TODO
 * @Author: tangzhuo
 * @Date: 2022-08-26 9:44
 */
@FeignClient(value = "wcwy-company",configuration = FeignConfig.class)
@Component
public interface TestService {
    @GetMapping("/tUser/userRole/{name}")
    public Object UserRole (@PathVariable("name") String name);

    @GetMapping("/tPermission/selectByid/{id}")
    public Object selectByid(@PathVariable("id") Long id);

    @GetMapping("/test")
    public String  testc();

    @GetMapping("/testRLock")
    public String  RLock(@RequestParam("rLock") RLock rLock) throws InterruptedException;
}
