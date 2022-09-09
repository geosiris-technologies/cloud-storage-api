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

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.geosiris.storage.cloud.api.property.S3Properties;
import com.geosiris.storage.cloud.api.service.StorageService;
import com.geosiris.storage.cloud.api.service.impl.S3Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class S3Config {
    public static Logger logger = LogManager.getLogger(S3Config.class);

    private static AmazonS3 S3Client(final S3Properties s3Properties) {
        AmazonS3 client;

        if (s3Properties.isLocalstackEnabled()) {
            String host = s3Properties.getLocalstackEndpoint();
            if (!host.startsWith("http")) {
                host = "http://" + host;
            }

            BasicAWSCredentials s3Credential = new BasicAWSCredentials(s3Properties.getAccessKey(), s3Properties.getSecretKey());
            logger.info("Registering S3Client (with Localstack)");
            client = AmazonS3ClientBuilder.standard().withClientConfiguration(new ClientConfiguration())
                    .withEndpointConfiguration(new EndpointConfiguration(host, s3Properties.getLocalstackRegion()))
                    // .withCredentials(new AWSStaticCredentialsProvider(s3Credential))
                    .withCredentials(new AWSStaticCredentialsProvider(s3Credential)).withPathStyleAccessEnabled(true)
                    .disableChunkedEncoding().withForceGlobalBucketAccessEnabled(true).build();
        } else {
            logger.info("Registering S3Client");
            client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(s3Properties.getAccessKey(), s3Properties.getSecretKey())))
                    .withRegion(s3Properties.getRegion()).build();
        }
        return client;
    }

    public static StorageService S3Service() {
        return new S3Service(S3Client(new S3Properties()));
    }

}
