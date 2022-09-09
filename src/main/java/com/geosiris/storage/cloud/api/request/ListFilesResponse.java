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

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ListFilesResponse extends BaseResponse {
	protected String[] fileList;

	public ListFilesResponse(int status, String cause, Exception exception, String[] fileList) {
		super(status, cause, exception);
		this.fileList = fileList;
	}

	/**
	 * @return String[] return the fileList
	 */
	public String[] getFileList() {
		return fileList;
	}

	/**
	 * @param fileList the fileList to set
	 */
	public void setFileList(String[] fileList) {
		this.fileList = fileList;
	}

	public static ListFilesResponseBuilder builder() {
		return new ListFilesResponseBuilder();
	}

	public static class ListFilesResponseBuilder {
		private int _status;
		private String _cause;
		private Exception _exception;
		private String[] _fileList;

		public ListFilesResponseBuilder() {
		}

		public ListFilesResponseBuilder status(int status) {
			this._status = status;
			return this;
		}

		public ListFilesResponseBuilder cause(String cause) {
			this._cause = cause;
			return this;
		}

		public ListFilesResponseBuilder exception(Exception exception) {
			this._exception = exception;
			return this;
		}

		public ListFilesResponseBuilder fileList(String[] fileList) {
			this._fileList = fileList;
			return this;
		}

		public ListFilesResponse build() {
			ListFilesResponse response = new ListFilesResponse(_status, _cause, _exception, _fileList);
			return response;
		}
	}
}