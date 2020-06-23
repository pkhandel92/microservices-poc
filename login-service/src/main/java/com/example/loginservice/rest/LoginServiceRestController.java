package com.example.loginservice.rest;

import com.example.loginservice.dto.EmployeeDto;
import com.example.loginservice.response.LoginResponse;
import com.example.loginservice.response.UpdateResponse;
import com.example.loginservice.dto.UserCreds;
import com.example.loginservice.response.RegisterResponse;
import com.example.loginservice.service.LoginService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;


@RestController
public class LoginServiceRestController {
    Logger logger= LoggerFactory.getLogger(LoginServiceRestController.class);
    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public RegisterResponse register(@RequestBody EmployeeDto employeeDto, HttpServletResponse httpServletResponse){
        logger.info("Employee Details recieved "+employeeDto);

        return loginService.register(employeeDto);
    }

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public LoginResponse login(@RequestBody UserCreds userCreds, HttpServletResponse response){
        logger.info("Trying to login {}",userCreds.getUsername());
        return loginService.login(userCreds);
    }

    @DeleteMapping(value = "/remove",consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON)
    public String removeUser(@RequestBody UserCreds userCreds, HttpServletResponse response){
        logger.info("Request to remove user {}",userCreds.getUsername());
        return loginService.remove(userCreds);
    }
    @PutMapping(value = "/update",consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    public UpdateResponse updateUser(@RequestBody EmployeeDto employeeDto, HttpServletResponse response){
        logger.info("Request to remove user {}",employeeDto.getUserId());
        return loginService.update(employeeDto);
    }

    @Autowired
    private EurekaClient eurekaClient;

    @Bean
    public RestTemplate RestTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/healthCheck")
    public String method2() {
        return "UP";
    }
    //TODO: We can use FeignClient here
    @GetMapping("/healthCheckApi")
    public String method() {

        InstanceInfo instance = eurekaClient.getNextServerFromEureka("LOGIN-SERVICE", false);
        String response = restTemplate.getForObject(instance.getHomePageUrl() + "/healthCheck", String.class);
        return "Instance called is : " + instance.getHomePageUrl() + " <br/><br/> And Response : " + response;
    }

    @Autowired
    private RestTemplate restTemplate;
}
