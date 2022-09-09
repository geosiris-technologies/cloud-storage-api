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
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
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

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class S3Service extends AbstractStorageService implements StorageService {

    public static Logger logger = LogManager.getLogger(S3Service.class);

    @Value("${aws.s3.bucket.name}")
    private String defaultBucketName;

    private final AmazonS3 S3Client;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public S3Service(AmazonS3 S3Client) {
        this.defaultBucketName = "defaultBucketName";
        this.S3Client = S3Client;
    }

    public S3Service(String defaultBucketName, AmazonS3 S3Client) {
        this.defaultBucketName = defaultBucketName;
        this.S3Client = S3Client;
    }

    @Override
    public UploadFileResponse uploadFile(final UploadFileRequest request) {
        UploadFileResponse result;

        try {
            InputStream streamToUpload = clone(request.getStream());

            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(IOUtils.toByteArray(request.getStream()).length);

            if (StringUtils.hasValue(request.getContentType())) {
                metadata.setContentType(request.getContentType());
                metadata.setCacheControl("s-maxage");
            }

            String currentBucketName = getBucketName(request.getBucketName(), defaultBucketName);

            if (!S3Client.doesBucketExistV2(currentBucketName)) {
                S3Client.createBucket(new CreateBucketRequest(currentBucketName));
            }

            final PutObjectRequest putObjectRequest = new PutObjectRequest(currentBucketName, getFilePath(request),
                    streamToUpload, metadata).withCannedAcl(request.getAccessControl());

            logger.info("Uploading file to " + getFilePath(request));

            S3Client.putObject(putObjectRequest);

            result = UploadFileResponse.builder().fileName(request.getName()).status(HttpStatus.SC_OK).build();
        } catch (AmazonServiceException ase) {
            showAmazonServiceExceptionUploadFileLogs(ase);
            result = UploadFileResponse.builder().fileName(request.getName())
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).cause(ase.getErrorMessage()).exception(ase).build();
        } catch (AmazonClientException ace) {
            showAmazonClientExceptionUploadFileLogs(ace);
            result = UploadFileResponse.builder().fileName(request.getName())
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).cause(ace.getMessage()).exception(ace).build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = UploadFileResponse.builder().fileName(request.getName())
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).cause(e.getMessage()).exception(e).build();
        }
        return result;
    }

    public Future<UploadFileResponse> uploadFileAsync(final UploadFileRequest request) {
        // return new AsyncResult<>(uploadFile(request));
        return executor.submit(() -> {
            return uploadFile(request);
        });
    }

    @Override
    public GetFileResponse getFile(final GetFileRequest request) {
        logger.info("Reading file from S3 " + request.getPath());
        GetFileResponse result;
        try (S3Object s3Object = S3Client.getObject(
                new GetObjectRequest(getBucketName(request.getBucketName(), defaultBucketName), request.getPath()))) {
            final byte[] file = IOUtils.toByteArray(s3Object.getObjectContent());
            result = GetFileResponse.builder().content(file).status(HttpStatus.SC_OK).build();
        } catch (NoBucketException | IOException e) {
            logger.error(e.getMessage(), e);
            result = GetFileResponse.builder().cause(e.getMessage()).exception(e)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return result;
    }

    @Override
    public DeleteFileResponse deleteFile(final DeleteFileRequest request) {
        logger.info("Deleting file from path " + request.getPath());
        DeleteFileResponse result;
        try {
            String currentBucketName = getBucketName(request.getBucketName(), defaultBucketName);
            if (request.getPath().endsWith("/*")) {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(currentBucketName)
                        .withPrefix(request.getPath().substring(0, request.getPath().length() - 2));

                ObjectListing objectListing = S3Client.listObjects(listObjectsRequest);

                while (true) {
                    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                        S3Client.deleteObject(currentBucketName, objectSummary.getKey());
                    }
                    if (objectListing.isTruncated()) {
                        objectListing = S3Client.listNextBatchOfObjects(objectListing);
                    } else {
                        break;
                    }
                }
            } else {
                final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(currentBucketName,
                        request.getPath());
                S3Client.deleteObject(deleteObjectRequest);
            }
            result = DeleteFileResponse.builder().result(true).status(HttpStatus.SC_OK).build();
        } catch (AmazonServiceException ase) {
            showAmazonServiceExceptionUploadFileLogs(ase);
            result = DeleteFileResponse.builder().cause(ase.getMessage()).exception(ase)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        } catch (AmazonClientException ace) {
            showAmazonClientExceptionUploadFileLogs(ace);
            result = DeleteFileResponse.builder().cause(ace.getMessage()).exception(ace)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = DeleteFileResponse.builder().cause(e.getMessage()).exception(e)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
        return result;
    }

    @Override
    public ListFilesResponse listFiles(ListFilesRequest request) {
        try {
            logger.info("listing file from path" + request.getSubPath() + " in bucket "
                    + getBucketName(request.getBucketName(), defaultBucketName));
        } catch (NoBucketException e1) {
            logger.error(e1.getMessage(), e1);
        }
        ListFilesResponse result = null;
        try {
            for (Bucket bucket : S3Client.listBuckets()) {
                // bucket.

                String[] tmpRes = (String[]) S3Client.listObjects(bucket.getName(), request.getSubPath())
                        .getObjectSummaries().stream().map(x -> x.getKey()).collect(Collectors.toList()).stream()
                        .toArray(String[]::new);
                if (tmpRes != null && tmpRes.length > 0) {
                    result = ListFilesResponse.builder().fileList(tmpRes).status(HttpStatus.SC_OK).build();
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = ListFilesResponse.builder().cause(e.getMessage()).exception(e)
                    .status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }

        return result;
    }

    /* Private methods */

    private void showAmazonServiceExceptionUploadFileLogs(final AmazonServiceException ase) {
        logger.error("Caught an AmazonServiceException, which means your request made it "
                + "to S3, but was rejected with an error response for some reason.");
        logger.error("Error Message:    " + ase.getMessage());
        logger.error("HTTP Status Code: " + ase.getStatusCode());
        logger.error("AWS Error Code:   " + ase.getErrorCode());
        logger.error("Error Type:       " + ase.getErrorType());
        logger.error("Request ID:       " + ase.getRequestId());
    }

    private void showAmazonClientExceptionUploadFileLogs(final AmazonClientException ace) {
        logger.error("Caught an AmazonClientException, which means the client encountered "
                + "an internal error while trying to communicate with S3, such as not being able to access the network.");
        logger.error("Error Message: " + ace.getMessage());
    }

}
