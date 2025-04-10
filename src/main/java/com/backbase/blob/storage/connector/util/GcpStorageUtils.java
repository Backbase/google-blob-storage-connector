package com.backbase.blob.storage.connector.util;

import com.google.cloud.spring.storage.GoogleStorageResource;
import com.google.cloud.storage.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@UtilityClass
public class GcpStorageUtils {

    public GoogleStorageResource retrieveObject(String projectId, String bucketName, String objectId) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        return new GoogleStorageResource(storage, String.format("gs://%s/%s", bucketName, objectId));
    }

    public void testAuthenticateImplicitWithAdc(String projectId, String bucketName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var bucket = storage.get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.values()));
        log.info("Bucket info {}", bucket);
    }
    /**
     *
     * @param projectId projectId
     * @param bucketName bucketName
     * @param objectId objectId
     * @param contents contents
     * @param contentType contentType
     * @return object blob
     */
    public BlobId uploadObject(
            String projectId, String bucketName, String objectId, byte[] contents, String contentType) {
        log.debug("Attempt to upload {} to bucket {} with contents {}, contentType {}",objectId, bucketName, Arrays.toString(contents), contentType);
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectId);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();
        Storage.BlobTargetOption precondition;
        if (storage.get(bucketName, objectId) == null) {
            precondition = Storage.BlobTargetOption.doesNotExist();
        } else {
            precondition = Storage.BlobTargetOption.generationMatch(
                            storage.get(bucketName, objectId).getGeneration());
        }
        storage.create(blobInfo, contents, precondition);
        log.debug("Object {} uploaded to bucket {} with contents {}", objectId, bucketName, Arrays.toString(contents));
        return blobId;
    }

    /**
     * <a href="https://cloud.google.com/storage/docs/copying-renaming-moving-objects">move object</a>
     * @param projectId projectId
     * @param bucketName bucketName
     * @param sourceObjectId sourceObjectId
     * @param targetObjectId targetObjectId
     * @return object blob
     */
    public BlobId moveObject(
            String projectId,
            String bucketName,
            String sourceObjectId,
            String targetObjectId) {

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId source = BlobId.of(bucketName, sourceObjectId);
        BlobId target = BlobId.of(bucketName, targetObjectId);

        Storage.BlobTargetOption precondition;
        if (storage.get(bucketName, targetObjectId) == null) {
            precondition = Storage.BlobTargetOption.doesNotExist();
        } else {
            precondition =
                    Storage.BlobTargetOption.generationMatch(
                            storage.get(bucketName, targetObjectId).getGeneration());
        }

        // Copy source object to target object
        storage.copy(Storage.CopyRequest.newBuilder().setSource(source).setTarget(target, precondition).build());
        Blob copiedObject = storage.get(target);
        // Delete the original blob now that we've copied to where we want it, finishing the "move"
        // operation
        storage.get(source).delete();

        log.debug("Moved object {} from bucket {} to {} in bucket {}",
                sourceObjectId, bucketName, targetObjectId, copiedObject.getBucket());
        return target;

    }

    /**
     * <a href="https://cloud.google.com/storage/docs/deleting-objects">Delete gcp object</a>
     * @param projectId projectId
     * @param bucketName bucketName
     * @param objectName objectName
     */
    public void deleteObject(String projectId, String bucketName, String objectName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Blob blob = storage.get(bucketName, objectName);
        if (blob == null) {
            log.debug("The object {} wasn't found in {}" , objectName, bucketName);
            return;
        }
        BlobId idWithGeneration = blob.getBlobId();
        storage.delete(idWithGeneration);
        log.debug("Object {} was permanently deleted from {}", objectName, bucketName);
    }

    /**
     * Signing a URL requires Credentials which implement ServiceAccountSigner. These can be set
     * explicitly using the Storage.SignUrlOption.signWith(ServiceAccountSigner) option. If you don't,
     * you could also pass a service account signer to StorageOptions, i.e.
     * StorageOptions().newBuilder().setCredentials(ServiceAccountSignerCredentials). In this example,
     * neither of these options are used, which means the following code only works when the
     * credentials are defined via the environment variable GOOGLE_APPLICATION_CREDENTIALS, and those
     * credentials are authorized to sign a URL. See the documentation for Storage.signUrl for more
     * details.
     * You can use this URL with any user agent, for example:
     * curl <signedUrl>
     */
    public URL generateV4GetObjectSignedUrl(
            String projectId, String bucketName, String objectId, long timeoutInMinutes) throws StorageException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        // Define resource
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectId)).build();
        URL url = storage.signUrl(blobInfo, timeoutInMinutes, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());
        log.debug("Generated GET signed URL: {}", url);
        return url;
    }

    /**
     * <a href="https://cloud.google.com/storage/docs/creating-buckets">creating-bucket</a>
     * @param projectId projectId
     * @param bucketName bucketName
     * @return bucket
     */
    public Bucket createBucket(String projectId, String bucketName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        StorageClass storageClass = StorageClass.COLDLINE;
        String location = "ASIA";
        Bucket bucket = storage.create(
                        BucketInfo.newBuilder(bucketName)
                                .setStorageClass(storageClass)
                                .setLocation(location)
                                .build());
        log.debug("Created bucket {} in {} with storage class {}", bucketName, bucket.getLocation(), bucket.getStorageClass());
        return bucket;
    }

    /**
     * <a href="https://cloud.google.com/storage/docs/deleting-buckets">deleting-buckets</a>
     * @param projectId projectId
     * @param bucketName bucketName
     */
    public void deleteBucket(String projectId, String bucketName) {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Bucket bucket = storage.get(bucketName);
        bucket.delete();
        log.debug("Bucket {} was deleted", bucket.getName());
    }
}
