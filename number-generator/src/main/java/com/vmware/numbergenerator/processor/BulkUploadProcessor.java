package com.vmware.numbergenerator.processor;

import com.vmware.numbergenerator.entities.BulkProcessingMetadata;
import com.vmware.numbergenerator.entities.EntityType;
import com.vmware.numbergenerator.models.BulkProcessingStatus;
import com.vmware.numbergenerator.repositories.BulkProcessingMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BulkUploadProcessor {

    @Autowired
    private BulkProcessingMetadataRepository bulkProcessingMetadataRepository;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private BulkProcessorTask bulkProcessorTask;

    public BulkProcessingMetadata process(UUID fileId, EntityType entityType) {
        BulkProcessingMetadata bulkProcessingEntity = new BulkProcessingMetadata(
                fileId.toString(),
                BulkProcessingStatus.QUEUED,
                new Date(),
                null,
                entityType);
        bulkProcessingMetadataRepository.save(bulkProcessingEntity);
        executorService.execute(() -> {
            triggerBulkProcessing(bulkProcessingEntity.getId());
        });
        return bulkProcessingEntity;
    }

    public BulkProcessingMetadata getBulkProcessingStatus(String fileId) {
        Optional<BulkProcessingMetadata> bulkProcessingMetadata = bulkProcessingMetadataRepository.findById(fileId);
        if (!bulkProcessingMetadata.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job Not Found");
        }
        return bulkProcessingMetadata.get();
    }

    public void triggerBulkProcessing(String fileId) {
        bulkProcessorTask.process(fileId);
    }
}
