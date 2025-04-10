package com.backbase.blob.storage.connector.audit;

import com.backbase.audit.client.annotation.AuditableOperation;
import com.backbase.audit.client.model.AuditMessage;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ObjectEventDescriberTest {
    private static final String USER_ID = UUID.randomUUID().toString();
    private static final String USERNAME = "username";
    @InjectMocks
    private ObjectEventDescriber objectEventDescriber;

    @Test
    void objectEventInitializedTest() {
        AuditMessage auditMessage = new AuditMessage();
        AuditableOperation auditableOperation = mock(AuditableOperation.class);
        Map<String, Object> arguments = createArguments();
        AuditMessage finalAuditMessage = objectEventDescriber
                .describeInitiated(auditMessage, auditableOperation, arguments);

        assertEquals(arguments, finalAuditMessage.getEventMetaData());
    }

    @Test
    void objectSuccessfulEventTest() {
        AuditMessage auditMessage = new AuditMessage().withUserId(USER_ID).withUsername(USERNAME);
        AuditableOperation auditableOperation = mock(AuditableOperation.class);
        Map<String, Object> arguments = createArguments();
        AuditMessage finalAuditMessage = objectEventDescriber
                .describeSuccessful(auditMessage, auditableOperation, arguments, null);

        assertEquals(USER_ID, finalAuditMessage.getUserId());
        assertEquals(USERNAME, finalAuditMessage.getUsername());
        assertEquals(arguments, finalAuditMessage.getEventMetaData());
    }

    private Map<String, Object> createArguments() {
        return Map.of("c3RepositoryId", "repo", "c3ObjectId", "obj");
    }
}
