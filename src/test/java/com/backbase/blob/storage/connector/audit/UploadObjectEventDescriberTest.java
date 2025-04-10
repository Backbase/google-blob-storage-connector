package com.backbase.blob.storage.connector.audit;

import com.backbase.audit.client.annotation.AuditableOperation;
import com.backbase.audit.client.model.AuditMessage;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_OBJECT_ID;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_REPOSITORY_ID_KEY;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.CS_PATH_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UploadObjectEventDescriberTest {

    private static final String USER_ID = UUID.randomUUID().toString();
    private static final String USERNAME = "username";
    private static final String REPO = "repo";
    private static final String PATH = "path";
    private static final String OBJECT = "obj";
    @InjectMocks
    private UploadObjectEventDescriber uploadObjectEventDescriber;

    @Test
    void uploadObjectEvent() {
        AuditMessage auditMessage = new AuditMessage();
        AuditableOperation auditableOperation = mock(AuditableOperation.class);
        Map<String, Object> arguments = createArguments(true);

        AuditMessage finalAuditMessage = uploadObjectEventDescriber
                .describeInitiated(auditMessage, auditableOperation, arguments);

        assertEquals(arguments, finalAuditMessage.getEventMetaData());
    }
    @Test
    void updateObjectEvent() {
        AuditMessage auditMessage = new AuditMessage();
        AuditableOperation auditableOperation = mock(AuditableOperation.class);
        Map<String, Object> arguments = createArguments(false);

        AuditMessage finalAuditMessage = uploadObjectEventDescriber
                .describeInitiated(auditMessage, auditableOperation, arguments);

        assertEquals(arguments, finalAuditMessage.getEventMetaData());
    }

    @Test
    void uploadObjectSuccessfulEventTest() {
        AuditMessage auditMessage = new AuditMessage().withUserId(USER_ID).withUsername(USERNAME);
        AuditableOperation auditableOperation = mock(AuditableOperation.class);
        Map<String, Object> arguments = createArguments(true);

        AuditMessage finalAuditMessage = uploadObjectEventDescriber
                .describeSuccessful(auditMessage, auditableOperation, arguments, null);

        assertEquals(USER_ID, finalAuditMessage.getUserId());
        assertEquals(USERNAME, finalAuditMessage.getUsername());
        assertEquals(arguments, finalAuditMessage.getEventMetaData());
    }

    private Map<String, Object> createArguments(boolean generatePath) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(C3_REPOSITORY_ID_KEY.getKey(), REPO);
        arguments.put(C3_OBJECT_ID.getKey(), OBJECT);
        if(generatePath) {
            arguments.put(CS_PATH_KEY.getKey(), PATH);
        }
        return arguments;
    }
}
