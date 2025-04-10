package com.backbase.blob.storage.connector.audit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_OBJECT_ID;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.C3_REPOSITORY_ID_KEY;
import static com.backbase.blob.storage.connector.audit.AuditMetadataKey.CS_PATH_KEY;

class AuditUtilTest {

    @Test
    void testCreateMetadata() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(C3_REPOSITORY_ID_KEY.getKey(), "repoId");
        arguments.put(C3_OBJECT_ID.getKey(), "objectId");
        arguments.put(CS_PATH_KEY.getKey(), "csPth");
        Map<String, String> auditMetadata = AuditUtil.createAuditMetadata(arguments,
                List.of(C3_REPOSITORY_ID_KEY, CS_PATH_KEY));

        Assertions.assertEquals(2, auditMetadata.size());
        Assertions.assertEquals("repoId", auditMetadata.get(C3_REPOSITORY_ID_KEY.getKey()));
        Assertions.assertEquals("csPth", auditMetadata.get(CS_PATH_KEY.getKey()));
    }
}
