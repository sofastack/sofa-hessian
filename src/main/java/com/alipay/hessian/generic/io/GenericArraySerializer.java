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
package com.alipay.hessian.generic.io;

import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericObject;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;

/**
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public final class GenericArraySerializer extends AbstractSerializer {

    private static GenericArraySerializer instance = new GenericArraySerializer();

    public static GenericArraySerializer getInstance() {
        return instance;
    }

    private GenericArraySerializer() {
    }

    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (out.addRef(obj)) {
            return;
        }
        GenericArray genericArray;
        if (obj instanceof GenericObject[]) {
            genericArray = new GenericArray(Object.class.getName());
            genericArray.setObjects((GenericObject[]) obj);
        } else {
            genericArray = (GenericArray) obj;
        }

        boolean hasEnd = out.writeListBegin(genericArray.getLength(), genericArray.getType());

        for (int i = 0; i < genericArray.getLength(); i++) {
            out.writeObject(genericArray.get(i));
        }

        if (hasEnd) {
            out.writeListEnd();
        }
    }
}
