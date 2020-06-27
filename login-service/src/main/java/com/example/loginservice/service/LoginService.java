package com.example.loginservice.service;

import com.example.loginservice.dao.LoginDao;
import com.example.loginservice.dto.EmployeeDto;
import com.example.loginservice.response.LoginResponse;
import com.example.loginservice.response.UpdateResponse;
import com.example.loginservice.dto.UserCreds;
import com.example.loginservice.response.RegisterResponse;
import com.example.loginservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service(value = "loginService")
public class LoginService {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private JwtUtil jwtUtil;

    public RegisterResponse register(EmployeeDto employee) {
        return loginDao.register(employee);

    }

    /**
     * @param userCredential
     * @return
     */
    public LoginResponse validateLoginCredentials(UserCreds userCredential) {
        Boolean validResponse = loginDao.login(userCredential);
        LoginResponse loginResponse = new LoginResponse();
        if (validResponse) {
            appUserDetailsService.setUserCredentials(userCredential);
            final UserDetails userDetails = appUserDetailsService.loadUserByUsername(userCredential.getUsername());
            final String jwtToken = jwtUtil.generateToken(userDetails);
            loginResponse.setJwt(jwtToken);
            loginResponse.setStatus("success");
            loginResponse.setUserName(userCredential.getUsername());
        } else
            loginResponse.setStatus("failure");

        return loginResponse;
    }


    public String remove(UserCreds userCredential) {
        return loginDao.removeUser(userCredential);
    }

    /**
     * @param employee
     * @return
     */
    public UpdateResponse update(EmployeeDto employee) {
        return loginDao.updateUser(employee);
    }
}
