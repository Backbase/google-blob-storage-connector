package com.backbase.blob.storage.connector.audit;

import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_OBJECT_ID;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_REPOSITORY_ID_KEY;

import com.backbase.audit.client.annotation.AuditableOperation;
import com.backbase.audit.client.describer.DefaultAuditEventDescriber;
import com.backbase.audit.client.model.AuditMessage;
import com.backbase.c3.storage.api.v1.model.C3ObjectMoveRequest;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Prepares audit message for deleting and moving objects.
 * {@link com.backbase.blob.storage.connector.service.AzureBlobService#moveObject(String, String, C3ObjectMoveRequest)}
 * {@link com.backbase.blob.storage.connector.service.AzureBlobService#deleteObject(String, String)}
 */
@Component
public class ObjectEventDescriber extends DefaultAuditEventDescriber {

    @Override
    public AuditMessage describeInitiated(AuditMessage auditMessage, AuditableOperation auditableOperation,
        Map<String, Object> arguments) {
        return super.describeInitiated(auditMessage, auditableOperation, arguments)
            .withEventMetaData(objectEventMetadata(arguments));
    }

    @Override
    public AuditMessage describeSuccessful(AuditMessage auditMessage, AuditableOperation auditableOperation,
        Map<String, Object> arguments, Object response) {
        return super.describeSuccessful(auditMessage, auditableOperation, arguments, response)
            .withEventMetaData(objectEventMetadata(arguments));
    }

    private Map<String, String> objectEventMetadata(Map<String, Object> arguments) {
        return AuditUtil.createAuditMetadata(arguments, List.of(C3_REPOSITORY_ID_KEY, C3_OBJECT_ID));
    }
}
