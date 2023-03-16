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
package com.caucho.hessian.test.java8;

import java.io.Serializable;

/**
 *
 * @author junyuan
 * @version AbstractWrapper.java, v 0.1 2023年03月16日 15:38 junyuan Exp $
 */
public abstract class AbstractWrapper implements Serializable {

    abstract protected Object[] getFields();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractWrapper) {
            Object[] targets = ((AbstractWrapper) obj).getFields();
            Object[] src = this.getFields();
            if (src.length != targets.length) {
                return false;
            }

            for (int i = 0, len = targets.length; i < len; i++) {
                if (!targets[i].equals(src[i])) {
                    return false;
                }
            }
            return true;
        }

        return super.equals(obj);
    }
}