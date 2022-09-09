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
public class UploadFileResponse extends BaseResponse {

	protected String fileName;
	protected String comment;

	private UploadFileResponse(int status, String cause, Exception exception, String fileName, String comment) {
		super(status, cause, exception);
		this.fileName = fileName;
		this.comment = comment;
	}

	/**
	 * @return String return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return String return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	public static UploadFileResponseBuilder builder() {
		return new UploadFileResponseBuilder();
	}

	public static class UploadFileResponseBuilder {
		private int _status;
		private String _cause;
		private Exception _exception;
		private String _fileName;
		private String _comment;

		public UploadFileResponseBuilder() {
		}

		public UploadFileResponseBuilder status(int status) {
			this._status = status;
			return this;
		}

		public UploadFileResponseBuilder cause(String cause) {
			this._cause = cause;
			return this;
		}

		public UploadFileResponseBuilder exception(Exception exception) {
			this._exception = exception;
			return this;
		}

		public UploadFileResponseBuilder fileName(String fileName) {
			this._fileName = fileName;
			return this;
		}

		public UploadFileResponseBuilder comment(String comment) {
			this._comment = comment;
			return this;
		}

		public UploadFileResponse build() {
			UploadFileResponse response = new UploadFileResponse(_status, _cause, _exception, _fileName, _comment);
			return response;
		}
	}

}