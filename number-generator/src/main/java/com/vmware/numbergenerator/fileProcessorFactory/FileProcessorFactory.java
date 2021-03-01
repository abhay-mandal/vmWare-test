package com.vmware.numbergenerator.fileProcessorFactory;

import com.vmware.numbergenerator.entities.EntityType;
import com.vmware.numbergenerator.exceptions.InvalidEntityTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileProcessorFactory {

    @Autowired
    private EmployeeFileProcessor employeeFileProcessor;

    public FileProcessor getFileProcessor(EntityType entityType) throws InvalidEntityTypeException {
        if (entityType == EntityType.Employee) {
            return employeeFileProcessor;
        }
        throw new InvalidEntityTypeException("Not a Valid Entity Type, Please connect with your Admin");
    }
}
