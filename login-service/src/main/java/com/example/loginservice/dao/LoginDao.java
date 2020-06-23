package com.example.loginservice.dao;

import com.example.loginservice.dto.EmployeeDto;
import com.example.loginservice.response.LoginResponse;
import com.example.loginservice.response.UpdateResponse;
import com.example.loginservice.dto.UserCreds;
import com.example.loginservice.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public LoginDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public RegisterResponse register(EmployeeDto employeeDto) {

        int loginTableStatus = jdbcTemplate.update("insert into login ( user_id ,password) values(?,?)", employeeDto.getUserId(), passwordEncoder().encode(employeeDto.getPassword()));
        int ratingTableStatus = jdbcTemplate.update("insert into rating (user_id,manager_id,firstName,LastName,department) values(?,?,?,?,?)", employeeDto.getUserId(), employeeDto.getManager_id(), employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getDepartment());
        RegisterResponse response = new RegisterResponse();
        int result = loginTableStatus & ratingTableStatus;
        response.setResponse(result == 1 ? "Success" : "Failure");
        response.setStatus(result);
        return response;
    }

    public LoginResponse login(UserCreds userCreds) {
        String sql = "select password from Login where user_id=?";
        String password = (String) jdbcTemplate.queryForObject(
                sql, new Object[]{userCreds.getUsername()}, String.class);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatus(passwordEncoder().matches(userCreds.getPassword(), password) ? "Success" : "Failure");
        return loginResponse;
    }
    public String removeUser(UserCreds userCreds) {
        int loginRemoveStatus = jdbcTemplate.update("delete from login where user_id=?", userCreds.getUsername());
        int ratingRemoveStatus = jdbcTemplate.update("delete from rating where user_id=?", userCreds.getUsername());
        return (loginRemoveStatus & ratingRemoveStatus) == 1 ? "User removed successfully" : "Error during removing user";
    }

    public UpdateResponse updateUser(EmployeeDto employeeDto) {
        int loginStatus = jdbcTemplate.update("update login set password =? where user_id=?", passwordEncoder().encode(employeeDto.getPassword()), employeeDto.getUserId());
        int ratingStatus = jdbcTemplate.update("update rating set firstName=?,lastName=?,department=? ,manager_id=? where user_id=?", employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getDepartment(),employeeDto.getManager_id(),employeeDto.getUserId());
        int result = loginStatus & ratingStatus;
        UpdateResponse response = new UpdateResponse();
        if (result == 1) {

            response.setEmployeeDto(employeeDto);
            response.setUpdateStatus("Updated");
        } else {
            response.setUpdateStatus("No user with mentioned user_id "+employeeDto.getUserId());
            //throw Exception
        }
        return response;
    }
}
