package com.backbase.blob.storage.connector.audit;

import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_OBJECT_ID;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_REPOSITORY_ID_KEY;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.CS_PATH_KEY;

import com.backbase.audit.client.annotation.AuditableOperation;
import com.backbase.audit.client.describer.DefaultAuditEventDescriber;
import com.backbase.audit.client.model.AuditMessage;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Prepares audit message for uploading objects operation.
 * {@link com.backbase.blob.storage.connector.service.AzureBlobService#uploadObject(String, String, String, String,
 * MultipartFile)}
 */
@Component
public class UploadObjectEventDescriber extends DefaultAuditEventDescriber {

    @Override
    public AuditMessage describeInitiated(AuditMessage auditMessage, AuditableOperation auditableOperation,
        Map<String, Object> arguments) {
        return super.describeInitiated(auditMessage, auditableOperation, arguments)
            .withEventMetaData(uploadObjectEventMetadata(arguments));
    }

    @Override
    public AuditMessage describeSuccessful(AuditMessage auditMessage, AuditableOperation auditableOperation,
        Map<String, Object> arguments, Object response) {
        return super.describeSuccessful(auditMessage, auditableOperation, arguments, response)
            .withEventMetaData(uploadObjectEventMetadata(arguments));
    }

    private Map<String, String> uploadObjectEventMetadata(Map<String, Object> arguments) {
        return AuditUtil.createAuditMetadata(arguments, List.of(C3_REPOSITORY_ID_KEY, C3_OBJECT_ID, CS_PATH_KEY));
    }
}
