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
package com.geosiris.storage.cloud.api.config;

import java.io.FileInputStream;
import java.io.IOException;

import com.geosiris.storage.cloud.api.property.GoogleCloudStorageProperties;
import com.geosiris.storage.cloud.api.service.StorageService;
import com.geosiris.storage.cloud.api.service.impl.GoogleCloudStorageService;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ConditionalOnCloudStorageProperty(value = "gcp.storage.enabled")
public class GoogleCloudStorageConfig {
    public static Logger logger = LogManager.getLogger(GoogleCloudStorageConfig.class);
    public GoogleCloudStorageProperties googleCloudStorageProperties() {
        return new GoogleCloudStorageProperties();
    }

    public Storage storageClient(final GoogleCloudStorageProperties googleCloudStorageProperties) throws IOException {
        logger.info("Registering Google Storage client");
        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials
                        .fromStream(new FileInputStream(googleCloudStorageProperties.getKeyfile())))
                .build().getService();
    }

    public StorageService googleCloudStorageService(final Storage storageClient) {
        return new GoogleCloudStorageService(storageClient);
    }

}
