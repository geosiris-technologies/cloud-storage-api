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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.amazonaws.util.IOUtils;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.specialized.BlockBlobClient;
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

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AzureBlobStorageService extends AbstractStorageService implements StorageService {

    public static Logger logger = LogManager.getLogger(AzureBlobStorageService.class);

    private String defaultContainerName;

    private final BlobServiceClient blobServiceClient;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public AzureBlobStorageService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    @Override
    public UploadFileResponse uploadFile(final UploadFileRequest uploadFileRequest) {
        UploadFileResponse result;

        try {
            final InputStream streamToUpload = clone(uploadFileRequest.getStream());

            final BlockBlobClient blockBlobClient = getBlobClient(uploadFileRequest.getBucketName(),
                    getFilePath(uploadFileRequest));

            BlobHttpHeaders headers = new BlobHttpHeaders();
            headers.setContentType(uploadFileRequest.getContentType());
            blockBlobClient.upload(streamToUpload, IOUtils.toByteArray(uploadFileRequest.getStream()).length);
            blockBlobClient.setHttpHeaders(headers);

            result = UploadFileResponse.builder().fileName(uploadFileRequest.getName()).status(HttpStatus.SC_OK)
                    .comment(blockBlobClient.getBlobUrl()).build();
        } catch (IOException | NoBucketException e) {
            logger.error("Error creating blob");
            logger.debug(e.getMessage(), e);
            result = UploadFileResponse.builder().fileName(uploadFileRequest.getName())
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).cause("Error creating blob").exception(e).build();
        }
        return result;
    }

    @Override
    public Future<UploadFileResponse> uploadFileAsync(final UploadFileRequest request) {
        // return new AsyncResult<>(uploadFile(request));
        return executor.submit(() -> {
            return uploadFile(request);
        });
    }

    @Override
    public GetFileResponse getFile(final GetFileRequest request) {
        logger.info("Reading file from Azure {}", request.getPath());
        GetFileResponse result;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final BlockBlobClient blockBlobClient = getBlobClient(request.getBucketName(), request.getPath());
            blockBlobClient.download(outputStream);
            result = GetFileResponse.builder().content(outputStream.toByteArray()).status(HttpStatus.SC_OK).build();
        } catch (IOException | NoBucketException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            result = GetFileResponse.builder().cause(e.getMessage()).exception(e)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return result;
    }

    @Override
    public DeleteFileResponse deleteFile(final DeleteFileRequest request) {
        logger.info("Deleting file from Azure {}", request.getPath());
        DeleteFileResponse result;
        try {
            final BlockBlobClient blockBlobClient = getBlobClient(request.getBucketName(), request.getPath());
            blockBlobClient.delete();
            result = DeleteFileResponse.builder().status(HttpStatus.SC_OK).build();
        } catch (NoBucketException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            result = DeleteFileResponse.builder().cause(e.getMessage()).exception(e)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return result;
    }

    private BlockBlobClient getBlobClient(final String bucketName, final String path) throws NoBucketException {
        return blobServiceClient.getBlobContainerClient(getBucketName(bucketName, defaultContainerName))
                .getBlobClient(path).getBlockBlobClient();
    }

    @Override
    public ListFilesResponse listFiles(ListFilesRequest request) {
        // TODO Auto-generated method stub
        return null;
    }
}
