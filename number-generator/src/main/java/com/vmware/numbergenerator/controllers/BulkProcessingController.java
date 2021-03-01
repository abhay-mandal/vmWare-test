package com.vmware.numbergenerator.controllers;

import com.vmware.numbergenerator.entities.BulkProcessingMetadata;
import com.vmware.numbergenerator.mapper.BulkProcessingMapper;
import com.vmware.numbergenerator.models.BulkProcessingResponse;
import com.vmware.numbergenerator.processor.BulkUploadProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class BulkProcessingController {

    @Autowired
    private BulkUploadProcessor bulkUploadProcessor;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @GetMapping(path = "/bulk/{id}")
    public BulkProcessingResponse getBulkProcessingStatus(@PathVariable("id") String id) {
        BulkProcessingMetadata bulkProcessingEntity = bulkUploadProcessor.getBulkProcessingStatus(id);
        return BulkProcessingMapper.bulkProcessingResponseFrom(bulkProcessingEntity);
    }

    @PostMapping(path = "/bulk/{id}")
    public String triggerBulkProcessing(@PathVariable("id") String id) {
        executorService.execute(() -> {
            bulkUploadProcessor.triggerBulkProcessing(id);
        });
        return "Request Submitted";
    }
}
