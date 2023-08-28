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
package com.caucho.hessian.test.stacktrace;

import java.io.Serializable;

/**
 *
 * @author junyuan
 * @version ExceptionWrapper.java, v 0.1 2023年04月04日 15:40 junyuan Exp $
 */
public class ExceptionWrapper implements Serializable {
    private static final long serialVersionUID = 4065571790594438646L;

    Throwable                 t;

    /**
     * Getter method for property <tt>t</tt>.
     *
     * @return property value of t
     */
    public Throwable getT() {
        return t;
    }

    /**
     * Setter method for property <tt>t</tt>.
     *
     * @param t  value to be assigned to property t
     */
    public void setT(Throwable t) {
        this.t = t;
    }
}