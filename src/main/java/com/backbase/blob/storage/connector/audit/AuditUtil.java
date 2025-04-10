package com.backbase.blob.storage.connector.audit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuditUtil {

    public static final String EVENT_CATEGORY = "Document Store Azure Blob Connector";
    public static final String REPOSITORY_OBJECT_TYPE = "Repository";
    public static final String CONTENT_OBJECT_TYPE = "Content Object";
    public static final String MOVE_EVENT = "Move";
    public static final String CREATE_SIGNED_URL = "Create Signed URL";

    /**
     * Creates metadata map by input key list and argument map.
     *
     * @param arguments all the operation argument
     * @param keyList   needed keys to be in metadata
     * @return map of key list and argument values
     */
    public static Map<String, String> createAuditMetadata(Map<String, Object> arguments,
        List<AuditMetadataKey> keyList) {
        return keyList.stream().filter(item -> arguments.containsKey(item.getKey()))
            .collect(Collectors.toMap(AuditMetadataKey::getKey,
                auditMetadataKey -> (String) arguments.get(auditMetadataKey.getKey())));
    }
}
