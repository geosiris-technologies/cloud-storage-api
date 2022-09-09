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

import java.util.concurrent.Future;

import com.geosiris.storage.cloud.api.request.DeleteFileRequest;
import com.geosiris.storage.cloud.api.request.DeleteFileResponse;
import com.geosiris.storage.cloud.api.request.GetFileRequest;
import com.geosiris.storage.cloud.api.request.GetFileResponse;
import com.geosiris.storage.cloud.api.request.ListFilesRequest;
import com.geosiris.storage.cloud.api.request.ListFilesResponse;
import com.geosiris.storage.cloud.api.request.UploadFileRequest;
import com.geosiris.storage.cloud.api.request.UploadFileResponse;

public interface StorageService {

	UploadFileResponse uploadFile(UploadFileRequest request);

	Future<UploadFileResponse> uploadFileAsync(UploadFileRequest request);

	GetFileResponse getFile(GetFileRequest request);

	ListFilesResponse listFiles(ListFilesRequest request);

	DeleteFileResponse deleteFile(DeleteFileRequest request);

}
