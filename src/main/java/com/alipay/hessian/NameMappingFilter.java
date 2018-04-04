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
package com.alipay.hessian;

import com.alipay.hessian.clhm.ConcurrentLinkedHashMap;

/**
 * 名字映射过滤器，某些情况下，需要将OldClass序列化的类，用NewClass去解析
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class NameMappingFilter implements ClassNameFilter {

    /**
     * 保留映射关系 旧类-->新类
     */
    private final ConcurrentLinkedHashMap<String, String> objectMap = new ConcurrentLinkedHashMap.Builder<String, String>()
                                                                        .initialCapacity(100)
                                                                        .maximumWeightedCapacity(100).build();

    /**
     * Register mapping.
     *
     * @param oldClass the old class
     * @param newClass the new class
     */
    public void registerMapping(String oldClass, String newClass) {
        objectMap.put(oldClass, newClass);
    }

    /**
     * Unregister mapping.
     *
     * @param oldClass the old class
     */
    public void unregisterMapping(String oldClass) {
        objectMap.remove(oldClass);
    }

    public int order() {
        return 1;
    }

    public String resolve(String className) {
        if (objectMap.isEmpty()) {
            return className;
        }
        String mapClass = objectMap.get(className);
        return mapClass != null ? mapClass : className;
    }
}
