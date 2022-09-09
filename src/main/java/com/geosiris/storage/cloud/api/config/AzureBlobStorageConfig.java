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

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.geosiris.storage.cloud.api.property.AzureBlobStorageProperties;
import com.geosiris.storage.cloud.api.service.StorageService;
import com.geosiris.storage.cloud.api.service.impl.AzureBlobStorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AzureBlobStorageConfig {
    public static Logger logger = LogManager.getLogger(AzureBlobStorageConfig.class);

    public AzureBlobStorageProperties azureBlobStorageProperties() {
        return new AzureBlobStorageProperties();
    }

    public BlobServiceClient blobServiceClient(final AzureBlobStorageProperties azureBlobStorageProperties) {
        logger.info("Registering Azure Blob Storage client");
        return new BlobServiceClientBuilder().connectionString(azureBlobStorageProperties.getConnectionString())
                .buildClient();
    }

    public StorageService azureBlobStorageService(final BlobServiceClient blobServiceClient) {
        return new AzureBlobStorageService(blobServiceClient);
    }

}
