/*
Copyright 2019 GEOSIRIS

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.geosiris.storage.cloud.api.service.impl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.amazonaws.util.IOUtils;
import com.geosiris.storage.cloud.api.exception.NoBucketException;
import com.geosiris.storage.cloud.api.request.DeleteFileRequest;
import com.geosiris.storage.cloud.api.request.DeleteFileResponse;
import com.geosiris.storage.cloud.api.request.GetFileRequest;
import com.geosiris.storage.cloud.api.request.GetFileResponse;
import com.geosiris.storage.cloud.api.request.ListFilesRequest;
import com.geosiris.storage.cloud.api.request.ListFilesResponse;
import com.geosiris.storage.cloud.api.request.UploadFileRequest;
import com.geosiris.storage.cloud.api.request.UploadFileResponse;
import com.geosiris.storage.cloud.api.service.AbstractStorageService;
import com.geosiris.storage.cloud.api.service.StorageService;
import com.google.api.client.util.Value;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GoogleCloudStorageService extends AbstractStorageService implements StorageService {
    public static Logger logger = LogManager.getLogger(GoogleCloudStorageService.class);

    @Value("${gcp.storage.bucket.name}")
    private String defaultBucketName;

    private final Storage storageClient;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public GoogleCloudStorageService(Storage storageClient) {
        this.storageClient = storageClient;
    }

    @Override
    public UploadFileResponse uploadFile(final UploadFileRequest uploadFileRequest) {
        UploadFileResponse result;

        try {
            final String bucketName = getBucketName(uploadFileRequest.getBucketName(), defaultBucketName);

            final BlobInfo blobInfo = storageClient
                    .create(BlobInfo.newBuilder(bucketName, getFilePath(uploadFileRequest))
                            // .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(),
                            // Acl.Role.READER))))
                            .build(), IOUtils.toByteArray(uploadFileRequest.getStream()));
            result = UploadFileResponse.builder().fileName(uploadFileRequest.getName()).status(HttpStatus.SC_OK)
                    .comment(blobInfo.getMediaLink()).build();
        } catch (NoBucketException | IOException e) {
            logger.info("Error creating blob");
            result = UploadFileResponse.builder().fileName(uploadFileRequest.getName())
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).cause("Error creating blob").exception(e).build();
        }
        return result;
    }

    @Override
    public Future<UploadFileResponse> uploadFileAsync(final UploadFileRequest request) {
        // return new AsyncResult<>(uploadFile(uploadFileRequest));
        return executor.submit(() -> {
            return uploadFile(request);
        });
    }

    @Override
    public GetFileResponse getFile(final GetFileRequest request) {
        logger.info("Reading file from GoogleCloudStorage {}", request.getPath());
        GetFileResponse result;
        try {
            final byte[] file = storageClient.readAllBytes(
                    BlobId.of(getBucketName(request.getBucketName(), defaultBucketName), request.getPath()));
            result = GetFileResponse.builder().content(file).status(HttpStatus.SC_OK).build();
        } catch (NoBucketException e) {
            logger.error(e.getMessage(), e);
            result = GetFileResponse.builder().cause(e.getMessage()).exception(e)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return result;
    }

    @Override
    public DeleteFileResponse deleteFile(DeleteFileRequest request) {
        logger.info("Deleting file from path {}", request.getPath());
        DeleteFileResponse result;
        try {
            storageClient
                    .delete(BlobId.of(getBucketName(request.getBucketName(), defaultBucketName), request.getPath()));
            result = DeleteFileResponse.builder().result(true).status(HttpStatus.SC_OK).build();
        } catch (NoBucketException e) {
            logger.error(e.getMessage(), e);
            result = DeleteFileResponse.builder().cause(e.getMessage()).exception(e)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return result;
    }

    @Override
    public ListFilesResponse listFiles(ListFilesRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

}
