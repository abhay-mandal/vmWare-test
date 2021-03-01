package com.vmware.numbergenerator.controllers;

import com.vmware.numbergenerator.entities.BulkProcessingMetadata;
import com.vmware.numbergenerator.entities.Employee;
import com.vmware.numbergenerator.entities.EntityType;
import com.vmware.numbergenerator.mapper.BulkProcessingMapper;
import com.vmware.numbergenerator.mapper.EmployeeMapper;
import com.vmware.numbergenerator.models.BulkProcessingResponse;
import com.vmware.numbergenerator.models.EmployeeRequest;
import com.vmware.numbergenerator.models.EmployeeResponse;
import com.vmware.numbergenerator.processor.BulkUploadProcessor;
import com.vmware.numbergenerator.repositories.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@RestController
public class EmployeeControllers {

    private static final Logger logger = LogManager.getLogger(EmployeeControllers.class);
    public static final int ROW_LIMIT = 1000000;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BulkUploadProcessor bulkUploadProcessor;


    @GetMapping(path="/employee")
    public List<EmployeeResponse> getAllUsers() {
        List<EmployeeResponse> employeeResponseList = new ArrayList<>();
        for (Employee employee : employeeRepository.findAll()) {
            employeeResponseList.add(EmployeeMapper.employeeResponseFrom(employee));
        }
        logger.debug(employeeResponseList);
        return employeeResponseList;
    }

    @PostMapping(path="/employee")
    public EmployeeResponse createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        Employee employee = EmployeeMapper.employeeFrom(employeeRequest);
        employeeRepository.save(employee);
        return EmployeeMapper.employeeResponseFrom(employee);
    }

    @GetMapping(path = "/employee/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable("id") Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found");
        }
        return EmployeeMapper.employeeResponseFrom(employee.get());
    }

    @PutMapping(path = "/employee/{id}")
    public EmployeeResponse updateEmployeeById(@PathVariable("id") Integer id,
                                               @RequestBody @Valid EmployeeRequest employeeRequest) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found");
        }
        Employee employeeEntity = employee.get();
        employeeEntity.setName(employeeRequest.getName());
        employeeEntity.setAge(employeeRequest.getAge());
        employeeRepository.save(employeeEntity);
        return EmployeeMapper.employeeResponseFrom(employeeEntity);
    }

    @DeleteMapping(path = "/employee/{id}")
    public EmployeeResponse deleteEmployee(@PathVariable("id") Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found");
        }
        employeeRepository.delete(employee.get());
        return EmployeeMapper.employeeResponseFrom(employee.get());
    }

    @PostMapping(path = "/api/employee")
    public BulkProcessingResponse employeeBulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        UUID guid = UUID.randomUUID();
        String filePath = "./employeeBulkUploadRequest/" + guid.toString() + ".queue";
        //FixMe: This should be uploaded to S3 or some common Storage
        file.transferTo(Paths.get(filePath));

        File fileObject = new File(filePath);
        Stream<String> lines = Files.lines(fileObject.toPath());
        if (lines.count() > ROW_LIMIT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too Many Rows in the File");
        }

        BulkProcessingMetadata bulkProcessingEntity = bulkUploadProcessor.process(guid, EntityType.Employee);
        return BulkProcessingMapper.bulkProcessingResponseFrom(bulkProcessingEntity);
    }
}
