package com.feedback.poc.controller;

import com.feedback.poc.domain.Rating;
import com.feedback.poc.domain.Response;
import com.feedback.poc.service.FeedbackService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
@Slf4j
@RestController("feedback")
public class FeedbackController {

    @Value("${login.service.url}")
    private String loginUrl;
    @PostConstruct
    public void init(){
        log.info("Property set  {}",loginUrl);
    }
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private RestTemplate restTemplate;
    @HystrixCommand(fallbackMethod = "failedPostFeedback")
    @PostMapping
    public ResponseEntity<Response> postFeedback(@RequestParam(value = "rating") String rate, @RequestBody Rating rating,
                                               HttpServletRequest request) throws URISyntaxException
    {
        String username = validUser(request);
            if(!(rating.getEmployee_id().equals(rating.getManager_id())) && null != username)
        {
            if (username.equals(rating.getEmployee_id())) {
                rating.setEmployee_rating(rate);
            } else if (username.equals(rating.getManager_id())) {
                rating.setManager_rating(rate);
            } else {
                return createErrorResponse(rating, HttpStatus.BAD_REQUEST);
            }
            boolean result = feedbackService.saveRating(rating);
            if(result){
                Response response = new Response();
                response.setMessage("Rating record successfully");
                return ResponseEntity.created(new URI(rating.getEmployee_id())).body(response);
            }
            return createErrorResponse(rating, HttpStatus.BAD_REQUEST);
        }
        else{
            return createErrorResponse(rating, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public Response getRating(@RequestParam("employeeId") String id){
        return feedbackService.retrieveRating(id);
    }

    public ResponseEntity<Response> createErrorResponse(Rating rating, HttpStatus status){
        Response response = new Response();
        response.setMessage("Invalid Request");
        return ResponseEntity.status(status).body(response);
    }

    /**
     *
     * @param request
     * @return
     */
    private String validUser(HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", request.getHeader("Authorization"));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
    private ResponseEntity<Response> failedPostFeedback(String rate,Rating rating,HttpServletRequest request){
        return createFailedResponse(HttpStatus.BAD_GATEWAY);
    }
    public ResponseEntity<Response> createFailedResponse(HttpStatus status){
        Response response = new Response();
        response.setMessage("Service is temporarily Down. Please check in some time");
        return ResponseEntity.status(status).body(response);
    }
    @GetMapping("/health")
    public String getHealth(){
        return restTemplate.getForObject("http://login-service/healthCheck",String.class);
    }
}
