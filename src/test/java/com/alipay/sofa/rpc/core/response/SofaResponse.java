/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.rpc.core.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Sofa RPC Response class
 *
 * @author hongwei.yhw
 * @since 2014-07-02
 */
public final class SofaResponse implements Serializable {

    static private final long   serialVersionUID = -4364536436151723421L;

    /**
     * this error indicates framework has meet exception
     */
    private boolean             isError          = false;

    /**
     * error message
     */
    private String              errorMsg;

    /**
     * application response or exception object
     */
    private Object              appResponse;

    /**
     * extensional properties
     */
    private Map<String, String> responseProps;

    public Object getAppResponse() {
        return appResponse;
    }

    public void setAppResponse(Object response) {
        appResponse = response;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String error) {
        if (error == null) {
            return;
        }
        errorMsg = error;
        isError = true;
    }

    public Object getResponseProp(String key) {
        return responseProps == null ? null : responseProps.get(key);
    }

    public void addResponseProps(String key, String value) {
        if (responseProps == null) {
            responseProps = new HashMap<String, String>();
        }
        if (responseProps != null && value != null) {
            responseProps.put(key, value);
        }
    }

    public void removeResponseProps(String key) {
        if (responseProps != null && key != null) {
            responseProps.remove(key);
        }
    }

    public Map<String, String> getResponseProps() {
        return responseProps;
    }

    public void setResponseProps(Map<String, String> responseProps) {
        this.responseProps = responseProps;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SofaResponse[");
        sb.append("sofa-rpc exception=").append(isError).append(", ");
        sb.append("sofa-rpc errorMsg=").append(errorMsg).append(", ");
        sb.append("appResponse=").append(appResponse).append("]");
        return sb.toString();
    }
}
