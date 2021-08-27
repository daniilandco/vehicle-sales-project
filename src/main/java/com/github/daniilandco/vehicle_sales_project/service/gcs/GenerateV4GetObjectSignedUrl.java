package com.github.daniilandco.vehicle_sales_project.service.gcs;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Component
public class GenerateV4GetObjectSignedUrl {
    private final Storage storage;

    public GenerateV4GetObjectSignedUrl(Storage storage) {
        this.storage = storage;
    }

    public URL generate(
            String bucketName, String objectName) throws StorageException {
        // String bucketName = "my-bucket";
        // String objectName = "my-object";

        // Define resource
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();

        URL url =
                storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

        return url;
    }

}