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
package com.geosiris.storage.cloud.api.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;

import com.geosiris.storage.cloud.api.exception.NoBucketException;
import com.geosiris.storage.cloud.api.request.UploadFileRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractStorageService {
    public static Logger logger = LogManager.getLogger(AbstractStorageService.class);

    protected InputStream clone(final InputStream inputStream) {
        InputStream result = null;
        try {
            inputStream.mark(0);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readLength;
            while ((readLength = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readLength);
            }
            inputStream.reset();
            outputStream.flush();
            result = new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    protected String getBucketName(final String bucketName, final String defaultBucketName) throws NoBucketException {
        return Optional.ofNullable(Optional.ofNullable(bucketName).orElse(defaultBucketName))
                .orElseThrow(() -> new NoBucketException("Bucket name not indicated")).toLowerCase();
    }

    protected String getFilePath(final UploadFileRequest request) {
        return request.getFolder().concat("/").concat(request.getName());
    }

}
