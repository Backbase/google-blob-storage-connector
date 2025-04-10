package com.backbase.blob.storage.connector.domain;

import org.springframework.core.io.Resource;

public record ObjectResult(Resource resource, Long contentLength) {
}
