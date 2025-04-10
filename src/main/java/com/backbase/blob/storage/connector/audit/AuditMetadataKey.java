package com.backbase.blob.storage.connector.audit;

public enum AuditMetadataKey {

    CS_REPOSITORY("csRepository"),
    C3_REPOSITORY_ID_KEY("c3RepositoryId"),
    C3_OBJECT_ID("c3ObjectId"),
    CS_PATH_KEY("csPath");

    private final String key;

    AuditMetadataKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
