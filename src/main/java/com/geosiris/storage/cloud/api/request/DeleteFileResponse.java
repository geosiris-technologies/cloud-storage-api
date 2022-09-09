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
public class DeleteFileResponse extends BaseResponse {
	protected boolean result;

	private DeleteFileResponse(int status, String cause, Exception exception, boolean result) {
		super(status, cause, exception);
		this.result = result;
	}

	public static DeleteFileResponseBuilder builder() {
		return new DeleteFileResponseBuilder();
	}

	/**
	 * @return boolean return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	public static class DeleteFileResponseBuilder {
		private int _status;
		private String _cause;
		private Exception _exception;
		private boolean _result;

		public DeleteFileResponseBuilder() {
		}

		public DeleteFileResponseBuilder status(int status) {
			this._status = status;
			return this;
		}

		public DeleteFileResponseBuilder cause(String cause) {
			this._cause = cause;
			return this;
		}

		public DeleteFileResponseBuilder exception(Exception exception) {
			this._exception = exception;
			return this;
		}

		public DeleteFileResponseBuilder result(boolean result) {
			this._result = result;
			return this;
		}

		public DeleteFileResponse build() {
			DeleteFileResponse response = new DeleteFileResponse(_status, _cause, _exception, _result);
			return response;
		}
	}
}