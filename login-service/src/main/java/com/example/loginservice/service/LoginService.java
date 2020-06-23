package com.example.loginservice.service;

import com.example.loginservice.dao.LoginDao;
import com.example.loginservice.dto.EmployeeDto;
import com.example.loginservice.response.LoginResponse;
import com.example.loginservice.response.UpdateResponse;
import com.example.loginservice.dto.UserCreds;
import com.example.loginservice.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "loginService")
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    public RegisterResponse register(EmployeeDto employeeDto) {
        return loginDao.register(employeeDto);

    }

    public LoginResponse login(UserCreds userCreds) {
        return loginDao.login(userCreds);
    }

    public String remove(UserCreds userCreds) {
        return loginDao.removeUser(userCreds);
    }

    public UpdateResponse update(EmployeeDto employeeDto) {
        return loginDao.updateUser(employeeDto);
    }
}
