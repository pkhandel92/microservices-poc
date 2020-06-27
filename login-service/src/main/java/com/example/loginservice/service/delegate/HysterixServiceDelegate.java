package com.example.loginservice.service.delegate;

import com.example.loginservice.rest.LoginServiceRestController;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class HysterixServiceDelegate {
    Logger logger= LoggerFactory.getLogger(LoginServiceRestController.class);
    @Value("${login.client}")
    public String client;
    @PostConstruct
    public void init(){
       logger.debug("Loaded client "+client);
    }
    @Autowired
    private RestTemplate restTemplate;
    //TODO: We can use FeignClient here
//    @HystrixCommand(fallbackMethod = "healthCheckDown")
    public String healthCheck() {
        String response = restTemplate.getForObject("http://login-service-client/",
                String.class);
        return "Instance called is : " + "http:/login-service/healthCheck" + " <br/><br/> And Response : " + response;
    }

    public String healthCheckDown(){
        return "LOGIN-SERVICE-CLIENT is down";
    }

}
