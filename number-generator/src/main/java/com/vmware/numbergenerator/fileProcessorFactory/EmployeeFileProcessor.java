package com.vmware.numbergenerator.fileProcessorFactory;

import com.vmware.numbergenerator.entities.Employee;
import com.vmware.numbergenerator.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

@Service
public class EmployeeFileProcessor implements FileProcessor {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void process(String id) throws Exception {

        // FixMe: Read this from S3
        String fileName = "./employeeBulkUploadRequest/" + id + ".queue";

        File file = new File(fileName);

        if (!file.exists()) {
            throw new RuntimeException("File Not Found");
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner fileScanner = new Scanner(fileInputStream);

        while (fileScanner.hasNextLine()) {
            String data = fileScanner.nextLine();
            String[] fields = data.split(" ");
            if (fields.length != 2) {
                throw new RuntimeException("Invalid Row");
            }
            employeeRepository.save(new Employee(fields[0], Integer.parseInt(fields[1])));
            Thread.sleep(100); // FixMe: Dummy wait time to simulate processing
        }
    }
}
