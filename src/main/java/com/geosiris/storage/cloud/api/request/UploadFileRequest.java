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

import com.amazonaws.services.s3.model.CannedAccessControlList;
import java.io.InputStream;

public class UploadFileRequest {
    protected InputStream stream;
    protected String folder;
    protected String name;
    protected String contentType;
    protected String bucketName;
    protected CannedAccessControlList accessControl;

    public UploadFileRequest(InputStream stream, String folder, String name, String contentType, String bucketName) {
        this.stream = stream;
        this.folder = folder;
        this.name = name;
        this.contentType = contentType;
        this.bucketName = bucketName;
        this.accessControl = CannedAccessControlList.Private;
    }

    public UploadFileRequest(InputStream stream, String folder, String name, String contentType, String bucketName,
            CannedAccessControlList accessControl) {
        this.stream = stream;
        this.folder = folder;
        this.name = name;
        this.contentType = contentType;
        this.bucketName = bucketName;
        this.accessControl = accessControl;
    }

    /**
     * @return InputStream return the stream
     */
    public InputStream getStream() {
        return stream;
    }

    /**
     * @param stream the stream to set
     */
    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    /**
     * @return String return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    /**
     * @return CannedAccessControlList return the accessControl
     */
    public CannedAccessControlList getAccessControl() {
        return accessControl;
    }

    /**
     * @param accessControl the accessControl to set
     */
    public void setAccessControl(CannedAccessControlList accessControl) {
        this.accessControl = accessControl;
    }

}
