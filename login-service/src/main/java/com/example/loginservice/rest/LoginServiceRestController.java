package com.example.loginservice.rest;

import com.example.loginservice.dto.EmployeeDto;
import com.example.loginservice.dto.UserCreds;
import com.example.loginservice.response.LoginResponse;
import com.example.loginservice.response.RegisterResponse;
import com.example.loginservice.response.UpdateResponse;
import com.example.loginservice.service.LoginService;
import com.example.loginservice.service.TokenValidationService;
import com.example.loginservice.service.delegate.HysterixServiceDelegate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
public class LoginServiceRestController {
   private static final Logger logger= LoggerFactory.getLogger(LoginServiceRestController.class);
    @Autowired
    private HysterixServiceDelegate serviceDelegate;
    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody @Valid EmployeeDto employee){
        logger.info("Employee Details recieved {}", employee);
        RegisterResponse registerResponse = loginService.register(employee);
        if(registerResponse.getStatus()!=-1)
            return ResponseEntity.ok().body(registerResponse);
        else
            return ResponseEntity.badRequest().body(registerResponse);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody UserCreds userCredential) {
        logger.info("Trying to login {}", userCredential.getUsername());
        LoginResponse loginResponse = loginService.validateLoginCredentials(userCredential);
        if(loginResponse.getStatus().equalsIgnoreCase("success"))
            return ResponseEntity.ok().body(loginResponse);
        else
            return ResponseEntity.badRequest().body(loginResponse);
    }

    @DeleteMapping(value = "/remove")
    public ResponseEntity<?> removeUser(@RequestBody UserCreds userCredential, HttpServletResponse response){
        logger.info("Request to remove user {}", userCredential.getUsername());
        String result =  loginService.remove(userCredential);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<?> updateUser(@RequestBody EmployeeDto employee, HttpServletResponse response){
        logger.info("Request to update user {}", employee.getUserId());
        UpdateResponse updateResponse = loginService.update(employee);
        if(updateResponse.getUpdateStatus().equalsIgnoreCase("updated")){
            return ResponseEntity.ok().body(updateResponse);
        }
        else
            return ResponseEntity.badRequest().body(updateResponse);
    }

    @GetMapping("/healthCheck")
    public String method2() {
        return "UP";
    }

    //TODO: We can use FeignClient here
    @HystrixCommand(fallbackMethod = "healthIsDown")
    @GetMapping("/healthCheck-Api")
    public String method3() {
        String response = serviceDelegate.healthCheck();
        return "Instance called is : " + "http:/login-service/healthCheck" + " <br/><br/> And Response : " + response;
    }
    private String healthIsDown(){
        return serviceDelegate.healthCheckDown();
    }
    @Autowired
    private RestTemplate restTemplate;
}
