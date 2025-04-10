![](logo.svg)

[![Jenkins][jenkins-image]][jenkins-url]
[![Pull Requests][pr-image]][pr-url]
[![Slack][slack-image]][slack-url]
[![Contributors][contributors-image]][contributors-url]
[![Issue][issue-image]][issue-url]

##   google-blob-storage-connector


## Documentation

For a better understanding of how to configure and operate this project please refer to the following resources.
If you need to understand how the component works under the hood, nothing beats having a peek at the code.

1. [Understanding   google-blob-storage-connector][understand-project-url]
2. [  google-blob-storage-connector configuration][project-config-url]

[understand-project-url]: #
[project-config-url]: #


### config google credentials and file size limit
````yaml
  env:
    spring.servlet.multipart.max-file-size: 10MB
    spring.servlet.multipart.max-request-size: 10MB  
    GOOGLE_APPLICATION_CREDENTIALS: "/var/secrets/google/key.json"

  volumes:
    - name: apm-agent
      emptyDir: {}
    - name: gcp-ekyc-sa-creds
      secret:
      secretName: gcp-ekyc-sa-creds
    
  volumeMounts:
    - name: apm-agent
      mountPath: /tmp/apm-agent
    - name: gcp-ekyc-sa-creds
      mountPath: /var/secrets/google
      readOnly: true
````


### contentservices config 

````yaml
  env:
    contentservices.storage.connectors.gcpStorage.service: google-blob-storage-connector
    contentservices.storage.signedUrl.enabled: "true"
    contentservices.cron.retention.enabled: "true"
````
