package com.backbase.blob.storage.connector.controller;

import com.backbase.blob.storage.connector.service.GoogleBlobService;
import com.backbase.c3.storage.api.v1.ObjectApi;
import com.backbase.c3.storage.api.v1.model.C3Object;
import com.backbase.c3.storage.api.v1.model.C3ObjectCreateSignedUrlRequest;
import com.backbase.c3.storage.api.v1.model.C3ObjectMoveRequest;
import com.backbase.c3.storage.api.v1.model.C3SignedUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@RestController
public class BlobConnectorObjectController implements ObjectApi {

    private final GoogleBlobService blobService;

    @Override
    public ResponseEntity<C3Object> uploadObject(String c3RepositoryId, String csObjectId, String csPath,
        String contentType, MultipartFile contentStream, Boolean createSignedUrl) {
        return new ResponseEntity<>(blobService.uploadObject(
            decode(c3RepositoryId), decode(csObjectId), decode(csPath),
            contentType, contentStream, Optional.ofNullable(createSignedUrl)), HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Resource> retrieveObject(String c3RepositoryId, String c3ObjectId) {
        var result = blobService.retrieveObject(decode(c3RepositoryId), decode(c3ObjectId));
        var headers = new HttpHeaders();
        headers.setContentLength(result.contentLength());
        return new ResponseEntity<>(result.resource(), headers, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<C3Object> moveObject(String c3RepositoryId, String c3ObjectId,
        C3ObjectMoveRequest c3ObjectMoveRequest) {
        return new ResponseEntity<>(blobService.moveObject(decode(c3RepositoryId), decode(c3ObjectId),
            c3ObjectMoveRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteObject(String c3RepositoryId, String c3ObjectId) {
        blobService.deleteObject(decode(c3RepositoryId), decode(c3ObjectId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<C3Object> updateObject(String c3RepositoryId, String c3ObjectId, String contentType,
        MultipartFile contentStream, Boolean createSignedUrl) {
        return new ResponseEntity<>(
            blobService.updateObject(decode(c3RepositoryId), decode(c3ObjectId), contentType, contentStream,
                Optional.ofNullable(createSignedUrl)),
            HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<C3SignedUrl> createSignedUrl(String c3RepositoryId, String c3ObjectId, C3ObjectCreateSignedUrlRequest createSignedUrlRequest) {
        return new ResponseEntity<>(blobService.createSignedUrl(decode(c3RepositoryId), decode(c3ObjectId),  createSignedUrlRequest==null ? Boolean.FALSE : createSignedUrlRequest.getDownload()), HttpStatus.CREATED);
    }

    private String decode(String encoded) {
        return URLDecoder.decode(encoded, UTF_8);
    }
}
