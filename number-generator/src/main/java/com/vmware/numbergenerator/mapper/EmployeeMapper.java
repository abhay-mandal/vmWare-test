package com.vmware.numbergenerator.mapper;

import com.vmware.numbergenerator.entities.Employee;
import com.vmware.numbergenerator.models.EmployeeRequest;
import com.vmware.numbergenerator.models.EmployeeResponse;

public class EmployeeMapper {
    public static EmployeeResponse employeeResponseFrom(Employee employee) {
        return new EmployeeResponse(employee.getId(), employee.getAge(), employee.getName());
    }

    public static Employee employeeFrom(EmployeeRequest employeeRequest) {
        return new Employee(employeeRequest.getName(), employeeRequest.getAge());
    }
}
