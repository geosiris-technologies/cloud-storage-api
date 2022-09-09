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
package com.geosiris.storage.cloud.api.property;

public class S3Properties extends Properties {
    protected String accessKey;
    protected String secretKey;
    protected String region;

    protected Boolean localstackEnabled;
    protected String localstackEndpoint;
    protected String localstackRegion;

    public S3Properties() {
        super();
    }

    public S3Properties(String iniFileContent) {
        super(iniFileContent);
    }

    /**
     * @return String return the accessKey
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * @param accessKey the accessKey to set
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * @return String return the secretKey
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey the secretKey to set
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * @return String return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return Boolean return the localstackEnabled
     */
    public Boolean isLocalstackEnabled() {
        return localstackEnabled;
    }

    /**
     * @param localstackEnabled the localstackEnabled to set
     */
    public void setLocalstackEnabled(Boolean localstackEnabled) {
        this.localstackEnabled = localstackEnabled;
    }

    /**
     * @return String return the localstackEndpoint
     */
    public String getLocalstackEndpoint() {
        return localstackEndpoint;
    }

    /**
     * @param localstackEndpoint the localstackEndpoint to set
     */
    public void setLocalstackEndpoint(String localstackEndpoint) {
        this.localstackEndpoint = localstackEndpoint;
    }

    /**
     * @return String return the localstackRegion
     */
    public String getLocalstackRegion() {
        return localstackRegion;
    }

    /**
     * @param localstackRegion the localstackRegion to set
     */
    public void setLocalstackRegion(String localstackRegion) {
        this.localstackRegion = localstackRegion;
    }


    public static void main(String[] arv){
//        System.out.println(new S3Properties("[s3]\n"
//                                            + "localstackEnabled=false\n"
//                                            + "; must not have underscore in the url !\n"
//                                            + "localstackEndpoint=http://coucou:800\n"
//                                            + "; localstackEndpoint=http://localhost:9997\n"
//                                            + "localstackRegion=us-east-1\n"
//                                            + "accessKey=minioadmin\n"
//                                            + "secretKey=minioadmin\n").toJson());

        System.out.println(new S3Properties());
//        System.out.println(S3Properties.parseJson("{\"accessKey\":\"minioadmin\",\"secretKey\":\"minioadmin\",\"localstackEnabled\":false,\"localstackEndpoint\":\"http://coucou:800\",\"localstackRegion\":\"us-east-1\"}", S3Properties.class));
    }
}