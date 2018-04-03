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

import com.alipay.hessian.generic.model.GenericMap;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public class GenericMapSerializer extends AbstractSerializer {
    private static final GenericMapSerializer instance = new GenericMapSerializer();

    public static GenericMapSerializer getInstance() {
        return instance;
    }

    private GenericMapSerializer() {

    }

    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {

        if (out.addRef(obj)) {
            return;
        }

        GenericMap genericMap = (GenericMap) obj;

        out.writeMapBegin(genericMap.getType());

        Iterator iter = genericMap.getMap().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            out.writeObject(entry.getKey());
            out.writeObject(entry.getValue());
        }
        out.writeMapEnd();
    }
}
