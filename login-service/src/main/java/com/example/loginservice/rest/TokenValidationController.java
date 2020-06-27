package com.example.loginservice.rest;

import com.example.loginservice.service.TokenValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class TokenValidationController {
    private static final Logger logger= LoggerFactory.getLogger(TokenValidationController.class);

    @Autowired
    private TokenValidationService tokenValidationService;

    @GetMapping(value = "/token/validate")
    public @ResponseBody
    String validateToken(HttpServletRequest request){
        logger.info("Call to validate toke {} ", request.getHeader("Authorization"));
        return tokenValidationService.validateToken(request.getHeader("Authorization"));
    }
}
