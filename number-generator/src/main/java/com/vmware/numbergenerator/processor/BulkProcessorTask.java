package com.vmware.numbergenerator.processor;

import com.vmware.numbergenerator.entities.BulkProcessingMetadata;
import com.vmware.numbergenerator.exceptions.InvalidEntityTypeException;
import com.vmware.numbergenerator.fileProcessorFactory.FileProcessor;
import com.vmware.numbergenerator.fileProcessorFactory.FileProcessorFactory;
import com.vmware.numbergenerator.models.BulkProcessingStatus;
import com.vmware.numbergenerator.repositories.BulkProcessingMetadataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BulkProcessorTask {

    private static final Logger logger = LogManager.getLogger(BulkProcessorTask.class);

    @Autowired
    private BulkProcessingMetadataRepository bulkProcessingMetadataRepository;

    @Autowired
    private FileProcessorFactory fileProcessorFactory;

    private final List<BulkProcessingStatus> doNotStartBulkProcessingIfStatusIs = Arrays.asList(
            BulkProcessingStatus.ABORTED,
            BulkProcessingStatus.PROCESSING,
            BulkProcessingStatus.FINISHED);


    public void process(String id) {
        Optional<BulkProcessingMetadata> bulkProcessingEntity = bulkProcessingMetadataRepository.findById(id);
        if (!bulkProcessingEntity.isPresent()) {
            return;
        }

        BulkProcessingMetadata bulkProcessingMetadata = bulkProcessingEntity.get();

        if (doNotStartBulkProcessingIfStatusIs.contains(bulkProcessingMetadata.getStatus())) {
            return;
        }

        try {
            FileProcessor fileProcessor = fileProcessorFactory.getFileProcessor(bulkProcessingMetadata.getEntityType());
            bulkProcessingMetadata.setStatus(BulkProcessingStatus.PROCESSING);
            bulkProcessingMetadataRepository.save(bulkProcessingMetadata);
            fileProcessor.process(bulkProcessingMetadata.getId());
            bulkProcessingMetadata.setStatus(BulkProcessingStatus.FINISHED);
        } catch(InvalidEntityTypeException invalidEntityTypeException) {
            logger.error(invalidEntityTypeException);
            bulkProcessingMetadata.setStatus(BulkProcessingStatus.INVALID_BULK_ENTITY);
        } catch (Exception ex) {
            logger.error(ex);
            bulkProcessingMetadata.setStatus(BulkProcessingStatus.ERROR);
        } finally {
            bulkProcessingMetadata.setLastProcessedAt(new Date());
            bulkProcessingMetadataRepository.save(bulkProcessingMetadata);
        }
    }
}
