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
package com.alipay.sofa.rpc.core.request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Based on RequestBase, add some extensional properties, such as requestProps
 * 
 * INFO: this object will create in every RPC request
 *
 * @author hongwei.yhw
 * @since 2014-07-02
 */
public class SofaRequest extends RequestBase {

    private static final long         serialVersionUID = 7329530374415722876L;

    /** save the request method to avoid reflection */
    private transient Method          method;

    /** target app name */
    private String                    targetAppName;

    /** extensional properties */
    private final Map<String, Object> requestProps     = new HashMap<String, Object>();

    public Object getRequestProp(String key) {
        return requestProps.get(key);
    }

    public void addRequestProps(String key, Object value) {
        if (value != null) {
            requestProps.put(key, value);
        }
    }

    public Map<String, Object> getRequestProps() {
        return requestProps;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getTargetAppName() {
        return targetAppName;
    }

    public void setTargetAppName(String targetAppName) {
        this.targetAppName = targetAppName;
    }

}
