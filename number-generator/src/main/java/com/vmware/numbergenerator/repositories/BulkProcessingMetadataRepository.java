package com.vmware.numbergenerator.repositories;

import com.vmware.numbergenerator.entities.BulkProcessingMetadata;
import org.springframework.data.repository.CrudRepository;

public interface BulkProcessingMetadataRepository extends CrudRepository<BulkProcessingMetadata, String> {
}
