package com.vmware.numbergenerator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EmployeeRequest {
    @Min(value = 0, message = "Age must be greater than or equal to zero")
    @Max(value = 120, message = "Age must be less than or equal to 120")
    private int age;
    @NotEmpty(message="Field name Must not be empty")
    private String name;
}
