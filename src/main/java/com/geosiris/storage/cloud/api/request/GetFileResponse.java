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
public class GetFileResponse extends BaseResponse {
	protected byte[] content;

	private GetFileResponse(int status, String cause, Exception exception, byte[] content) {
		super(status, cause, exception);
		this.content = content;
	}

	/**
	 * @return byte[] return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	public static GetFileResponseBuilder builder() {
		return new GetFileResponseBuilder();
	}

	public static class GetFileResponseBuilder {
		private int _status;
		private String _cause;
		private Exception _exception;
		private byte[] _content;

		public GetFileResponseBuilder() {
		}

		public GetFileResponseBuilder status(int status) {
			this._status = status;
			return this;
		}

		public GetFileResponseBuilder cause(String cause) {
			this._cause = cause;
			return this;
		}

		public GetFileResponseBuilder exception(Exception exception) {
			this._exception = exception;
			return this;
		}

		public GetFileResponseBuilder content(byte[] content) {
			this._content = content;
			return this;
		}

		public GetFileResponse build() {
			GetFileResponse response = new GetFileResponse(_status, _cause, _exception, _content);
			return response;
		}
	}

}