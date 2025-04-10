package com.backbase.blob.storage.connector.audit;

import com.backbase.audit.client.annotation.AuditableOperation;
import com.backbase.audit.client.model.AuditMessage;
import com.backbase.c3.storage.api.v1.model.CSRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_REPOSITORY_ID_KEY;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.CS_REPOSITORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RepositoryEventDescriberTest {
    private static final String USER_ID = UUID.randomUUID().toString();
    private static final String USERNAME = "username";
    private static final String REPO = "repo";

    @InjectMocks
    private RepositoryEventDescriber repositoryEventDescriber;

    @Test
    void repositoryEventTest() {
        AuditMessage auditMessage = new AuditMessage();
        AuditableOperation auditableOperation = mock(AuditableOperation.class);

        AuditMessage finalAuditMessage = repositoryEventDescriber
                .describeInitiated(auditMessage, auditableOperation, createArguments());

        assertEquals(REPO, finalAuditMessage.getEventMetaData().get(C3_REPOSITORY_ID_KEY.getKey()));
    }

    @Test
    void repositorySuccessfulEventTest() {
        AuditMessage auditMessage = new AuditMessage().withUserId(USER_ID).withUsername(USERNAME);
        AuditableOperation auditableOperation = mock(AuditableOperation.class);

        AuditMessage finalAuditMessage = repositoryEventDescriber
                .describeSuccessful(auditMessage, auditableOperation, createArguments(), null);

        assertEquals(USER_ID, finalAuditMessage.getUserId());
        assertEquals(USERNAME, finalAuditMessage.getUsername());
        assertEquals(REPO, finalAuditMessage.getEventMetaData().get(C3_REPOSITORY_ID_KEY.getKey()));
    }

    private Map<String, Object> createArguments() {
        return Map.of(CS_REPOSITORY.getKey(), new CSRepository().csRepositoryId(REPO));
    }
}
