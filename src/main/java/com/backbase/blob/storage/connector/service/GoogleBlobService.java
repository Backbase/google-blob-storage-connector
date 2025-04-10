package com.backbase.blob.storage.connector.service;

import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.c3.storage.api.v1.model.*;
import com.backbase.blob.storage.connector.config.StorageConfiguration;
import com.backbase.blob.storage.connector.domain.ObjectResult;
import com.backbase.blob.storage.connector.util.GcpStorageUtils;
import com.google.cloud.spring.storage.GoogleStorageResource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
public class GoogleBlobService {
    private final String projectId ;
    private final String rootBucketName;
    private final int timeoutInMinutes;


    public GoogleBlobService(StorageConfiguration storageConfiguration) {
        this.projectId = storageConfiguration.getProjectId();
        this.rootBucketName = storageConfiguration.getRootBucketName();
        this.timeoutInMinutes = storageConfiguration.getTimeoutInMinutes();
    }

    @PostConstruct
    public void testGscConnection() {
        log.debug("test GSC Connection with project {}, bucket {} ", projectId, rootBucketName);
        GcpStorageUtils.testAuthenticateImplicitWithAdc(projectId, rootBucketName);
    }

    @PreAuthorize("permitAll()")
    public C3Object uploadObject(String c3RepositoryId, String csObjectId, String csPath, String contentType, MultipartFile contentStream, Optional<Boolean> createSignedUrl) {
        String c3GeneratedId = StringUtils.stripStart(csPath, "/");
        log.debug("Uploading object [{}] to blob container [{}] as blob", c3GeneratedId, c3RepositoryId);
        return upload(c3RepositoryId, c3GeneratedId, contentType, contentStream, createSignedUrl);
    }

    private C3Object upload(String c3RepositoryId, String c3GeneratedId, String contentType, MultipartFile contentStream, Optional<Boolean> createSignedUrl) {
        try {
            var blobId = GcpStorageUtils.uploadObject(projectId, rootBucketName, getObjectName(c3RepositoryId, c3GeneratedId) , contentStream.getBytes(), contentType);
            var c3Object = new C3Object(c3GeneratedId).externalURL(blobId.toGsUtilUri());
            if (createSignedUrl.orElse(false)) {
                var expiryTime = OffsetDateTime.now().plusMinutes(timeoutInMinutes);
                URL signedUrl = GcpStorageUtils.generateV4GetObjectSignedUrl(projectId, rootBucketName, getObjectName(c3RepositoryId, c3GeneratedId), timeoutInMinutes);
                var c3SignedUrl = new C3SignedUrl(signedUrl.toString(), expiryTime.toString());
                c3Object.setSignedUrl(c3SignedUrl);
            }
            return c3Object;
        } catch (IOException e) {
            log.error("failed to upload {}", e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    @PreAuthorize("permitAll()")
    public ObjectResult retrieveObject(String c3RepositoryId, String c3ObjectId) {
        log.debug("Downloading object [{}] from blob container [{}]", c3ObjectId, c3RepositoryId);
        GoogleStorageResource resource = GcpStorageUtils.retrieveObject(projectId, rootBucketName, getObjectName(c3RepositoryId, c3ObjectId));
        return new ObjectResult(resource, resource.getBlob().getSize());
    }
    @PreAuthorize("permitAll()")
    public C3Object moveObject(String c3RepositoryId, String c3ObjectId, C3ObjectMoveRequest moveRequest) {
        String destinationC3GeneratedId = StringUtils.stripStart(moveRequest.getCsPath(), "/");
        String sourceC3GeneratedId = StringUtils.stripStart(c3ObjectId, "/");
        log.debug("Moving source object [{}] from blob container [{}] to destination [{}]", c3ObjectId,
                c3RepositoryId, destinationC3GeneratedId);
        var destinationBlob = GcpStorageUtils.moveObject(projectId, rootBucketName,
                getObjectName(c3RepositoryId, sourceC3GeneratedId),
                getObjectName(c3RepositoryId, destinationC3GeneratedId));
        return new C3Object(moveRequest.getCsPath())
                .externalURL(destinationBlob.toGsUtilUri());
    }

    @PreAuthorize("permitAll()")
    public void deleteObject(String c3RepositoryId, String c3ObjectId) {
        log.debug("Deleting object [{}] from blob container [{}]", c3ObjectId, c3RepositoryId);
        GcpStorageUtils.deleteObject(projectId, rootBucketName, getObjectName(c3RepositoryId, c3ObjectId));
    }

    @PreAuthorize("permitAll()")
    public C3Object updateObject(String c3RepositoryId, String c3ObjectId, String contentType, MultipartFile contentStream, Optional<Boolean> createSignedUrl) {
        log.debug("Updating object [{}] from blob container [{}]", c3ObjectId, c3RepositoryId);
        return upload(c3RepositoryId, c3ObjectId, contentType, contentStream, createSignedUrl);
    }

    @PreAuthorize("permitAll()")
    public C3SignedUrl createSignedUrl(String c3RepositoryId, String c3ObjectId, Boolean download) {
        log.debug("Creating singedUrl for object [{}] to blob container [{}]", c3ObjectId, c3RepositoryId);
        var expiryTime = OffsetDateTime.now().plusMinutes(timeoutInMinutes);
        URL signedUrl = GcpStorageUtils.generateV4GetObjectSignedUrl(projectId, rootBucketName, getObjectName(c3RepositoryId, c3ObjectId), timeoutInMinutes);
        return new C3SignedUrl(signedUrl.toString(), expiryTime.toString());
    }


    @PreAuthorize("permitAll()")
    public C3Repository initializeContainer(CSRepository csRepository) {
        log.debug("Initializing container [{}]", csRepository);
        return new C3Repository().c3RepositoryId(csRepository.getCsRepositoryId());
    }
    @PreAuthorize("permitAll()")
    public void deleteContainer(String c3RepositoryId) {
        log.debug("No need to delete container [{}]", c3RepositoryId);
    }

    private String getObjectName(String c3RepositoryId, String c3ObjectId) {
        return String.format("%s/%s", c3RepositoryId, c3ObjectId);
    }
}
