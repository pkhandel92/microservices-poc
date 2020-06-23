package com.example.loginservice.response;

import com.example.loginservice.dto.EmployeeDto;

public class UpdateResponse {
    private EmployeeDto employeeDto;
    private String updateStatus;

    public EmployeeDto getEmployeeDto() {
        return employeeDto;
    }

    public void setEmployeeDto(EmployeeDto employeeDto) {
        this.employeeDto = employeeDto;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }
}
