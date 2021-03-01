package com.vmware.numbergenerator.models;

import com.vmware.numbergenerator.entities.EntityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class BulkProcessingResponse {
    private String id;

    private BulkProcessingStatus status;

    private Date requestedAt;

    private Date lastProcessedAt;

    public String getStatusUrl() {
        return "GET /bulk/" + id;
    }

    public String getReTriggerUrl() {
        if (status == BulkProcessingStatus.ABORTED || status == BulkProcessingStatus.FINISHED)
            return "Can not re trigger processing because current status is " + status;
        return "POST /bulk/" + id;
    }
}
