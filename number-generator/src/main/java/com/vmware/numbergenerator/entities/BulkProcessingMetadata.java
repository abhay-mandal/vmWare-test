package com.vmware.numbergenerator.entities;

import com.vmware.numbergenerator.models.BulkProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkProcessingMetadata {

    @Id
    private String id;

    private BulkProcessingStatus status;

    private Date requestedAt;

    private Date lastProcessedAt;

    private EntityType entityType;

}
