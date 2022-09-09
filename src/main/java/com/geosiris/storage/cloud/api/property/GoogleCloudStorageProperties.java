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

public class GoogleCloudStorageProperties extends Properties {

    protected String keyfile;

    public GoogleCloudStorageProperties() {
        super();
    }

    /**
     * @return String return the keyfile
     */
    public String getKeyfile() {
        return keyfile;
    }

    /**
     * @param keyfile the keyfile to set
     */
    public void setKeyfile(String keyfile) {
        this.keyfile = keyfile;
    }

}