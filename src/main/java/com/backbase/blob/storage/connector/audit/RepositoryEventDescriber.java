package com.backbase.blob.storage.connector.audit;

import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_REPOSITORY_ID_KEY;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.CS_REPOSITORY;

import com.backbase.audit.client.annotation.AuditableOperation;
import com.backbase.audit.client.describer.DefaultAuditEventDescriber;
import com.backbase.audit.client.model.AuditMessage;
import com.backbase.c3.storage.api.v1.model.CSRepository;
import java.util.Collections;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Prepares audit message for adding repository operation.
 * {@link com.backbase.blob.storage.connector.service.AzureBlobService#initializeContainer(CSRepository)}
 */
@Component
public class RepositoryEventDescriber extends DefaultAuditEventDescriber {

    @Override
    public AuditMessage describeInitiated(AuditMessage auditMessage, AuditableOperation auditableOperation,
        Map<String, Object> arguments) {
        return super.describeInitiated(auditMessage, auditableOperation, arguments)
            .withEventMetaData(repositoryEventMetadata(arguments));
    }

    @Override
    public AuditMessage describeSuccessful(AuditMessage auditMessage, AuditableOperation auditableOperation,
        Map<String, Object> arguments, Object response) {
        return super.describeSuccessful(auditMessage, auditableOperation, arguments, response)
            .withEventMetaData(repositoryEventMetadata(arguments));
    }

    private Map<String, String> repositoryEventMetadata(Map<String, Object> arguments) {
        CSRepository csRepository = (CSRepository) arguments.get(CS_REPOSITORY.getKey());
        return Collections.singletonMap(C3_REPOSITORY_ID_KEY.getKey(), csRepository.getCsRepositoryId());
    }
}
