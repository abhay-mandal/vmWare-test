package com.vmware.numbergenerator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeResponse {
    private int id;
    private int age;
    private String name;
}
