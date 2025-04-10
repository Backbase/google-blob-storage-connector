package com.backbase.blob.storage.connector.controller;

import com.backbase.blob.storage.connector.service.GoogleBlobService;
import com.backbase.c3.storage.api.v1.RepositoryApi;
import com.backbase.c3.storage.api.v1.model.C3Repository;
import com.backbase.c3.storage.api.v1.model.CSRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BlobStorageRepositoryController implements RepositoryApi {

    private final GoogleBlobService blobService;

    @Override
    public ResponseEntity<Void> deleteRepository(String c3RepositoryId) {
        blobService.deleteContainer(c3RepositoryId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<C3Repository> initializeRepository(CSRepository csRepository) {
        return new ResponseEntity<>(blobService.initializeContainer(csRepository), HttpStatus.CREATED);
    }
}
