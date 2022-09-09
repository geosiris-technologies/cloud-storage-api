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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseResponse {

    public static Logger logger = LogManager.getLogger(BaseResponse.class);

    protected int status;
    protected String cause;
    protected Exception exception;

    public BaseResponse() {
        this.status = -1;
        this.cause = "";
        this.exception = null;
    }

    public BaseResponse(int status, String cause, Exception exception) {
        this.status = status;
        this.cause = cause;
        this.exception = exception;
    }

    /**
     * @return int return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return String return the cause
     */
    public String getCause() {
        return cause;
    }

    /**
     * @param cause the cause to set
     */
    public void setCause(String cause) {
        this.cause = cause;
    }

    /**
     * @return Exception return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        className = className.replaceAll("Propert[yi](es)*", "");
        String result = "[" + className.toLowerCase() + "]\n";
        for (Field f : this.getClass().getDeclaredFields()) {
            try {
                Object obj = f.get(this);
                if (obj.getClass().isArray()) {
                    result += "" + f.getName() + "= [";
                    try {
                        for (Object o : (Object[]) obj) {
                            result += o + ",";
                        }
                    } catch (Exception e) {
                        // int length = obj.getClass().getField("length").getInt(obj);
                        List<?> objList = Arrays.asList(obj);
                        for (int i = 0; i < objList.size(); i++) {
                            result += objList.get(i) + ",";
                        }
                    }
                    result += "]";
                } else if (obj instanceof List) {
                    result += "" + f.getName() + "= [";
                    for (Object o : (List<Object>) obj) {
                        result += o + ",";
                    }
                    result += "]";
                } else {
                    result += "" + f.getName() + "=" + obj + " \n";
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }
}
