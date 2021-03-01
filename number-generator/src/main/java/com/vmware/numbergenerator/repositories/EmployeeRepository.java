package com.vmware.numbergenerator.repositories;

import com.vmware.numbergenerator.entities.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}
