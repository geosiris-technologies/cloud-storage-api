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
package com.geosiris.storage.cloud.api.request;

public class ListFilesRequest {
    protected String subPath;
    protected String bucketName;

    public ListFilesRequest(String subPath, String bucketName) {
        this.subPath = subPath;
        this.bucketName = bucketName;
    }

    /**
     * @return String return the subPath
     */
    public String getSubPath() {
        return subPath;
    }

    /**
     * @param subPath the subPath to set
     */
    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    /**
     * @return String return the bucketName
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * @param bucketName the bucketName to set
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

}
