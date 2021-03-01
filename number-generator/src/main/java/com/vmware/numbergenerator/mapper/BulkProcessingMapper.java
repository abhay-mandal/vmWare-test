package com.vmware.numbergenerator.mapper;

import com.vmware.numbergenerator.entities.BulkProcessingMetadata;
import com.vmware.numbergenerator.models.BulkProcessingResponse;

public class BulkProcessingMapper {
    public static BulkProcessingResponse bulkProcessingResponseFrom(BulkProcessingMetadata bulkProcessingMetadata) {
        return new BulkProcessingResponse(bulkProcessingMetadata.getId(),
                bulkProcessingMetadata.getStatus(),
                bulkProcessingMetadata.getRequestedAt(),
                bulkProcessingMetadata.getLastProcessedAt());
    }
}
