![](logo.svg)

##  google-blob-storage-connector


## Documentation

For a better understanding of how to configure and operate this project please refer to the following resources.
If you need to understand how the component works under the hood, nothing beats having a peek at the code.

1. [Understanding   google-blob-storage-connector][understand-project-url]
2. [  Deployment configuration][project-config-url]

[understand-project-url]: #
## Understanding google-blob-storage-connector
Build a contentservices connector to upload files to Google storage

[project-config-url]: #
## Deployment Configuration

### google-blob-storage-connector
Config google credentials key stored in a mounted volumn and file size limit
````yaml
  env:
    spring.servlet.multipart.max-file-size: 10MB
    spring.servlet.multipart.max-request-size: 10MB  
    GOOGLE_APPLICATION_CREDENTIALS: "/var/secrets/google/key.json"

  volumes:
    - name: gcp-ekyc-sa-creds
      secret:
      secretName: gcp-ekyc-sa-creds
    
  volumeMounts:
    - name: gcp-ekyc-sa-creds
      mountPath: /var/secrets/google
      readOnly: true
````


### contentservices
````yaml
  env:
    contentservices.storage.connectors.gcpStorage.service: google-blob-storage-connector
    contentservices.storage.signedUrl.enabled: "true"
    contentservices.cron.retention.enabled: "true"
````
