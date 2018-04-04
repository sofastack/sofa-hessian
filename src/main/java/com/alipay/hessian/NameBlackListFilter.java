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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 名字黑名单过滤器
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class NameBlackListFilter implements ClassNameFilter {

    /**
     * 黑名单 包名前缀
     */
    protected List<String>                   blackPrefixList;

    /**
     * 类名是否在黑名单中结果缓存。{className:true/false}
     */
    protected ConcurrentMap<String, Boolean> resultOfInBlackList;

    /**
     * 指定黑名单前缀
     *
     * @param blackPrefixList 黑名单前缀
     */
    public NameBlackListFilter(List<String> blackPrefixList) {
        this(blackPrefixList, 8192);
    }

    /**
     * 指定黑名单前缀和缓存大小
     *
     * @param blackPrefixList 黑名单前缀
     * @param maxCacheSize    最大缓存大小
     */
    public NameBlackListFilter(List<String> blackPrefixList, int maxCacheSize) {
        this.blackPrefixList = blackPrefixList;
        buildCache(maxCacheSize);
    }

    /**
     * 初始化缓存
     *
     * @param maxCacheSize 最大缓存
     */
    protected void buildCache(int maxCacheSize) {
        if (blackPrefixList != null && !blackPrefixList.isEmpty()) {
            int min = Math.min(256, maxCacheSize);
            int max = Math.min(10240, maxCacheSize);
            ConcurrentLinkedHashMap.Builder<String, Boolean> builder = new ConcurrentLinkedHashMap.Builder<String, Boolean>()
                .initialCapacity(min).maximumWeightedCapacity(max);
            this.resultOfInBlackList = builder.build();
        } else {
            this.resultOfInBlackList = null;
        }
    }

    public int order() {
        return 0;
    }

    public String resolve(String className) throws IOException {
        if (blackPrefixList == null || resultOfInBlackList == null) {
            return className;
        }
        Boolean result = resultOfInBlackList.get(className);
        if (result == null) {
            result = inBlackList(className);
            resultOfInBlackList.putIfAbsent(className, result);
        }
        if (result) {
            throw new IOException("Class " + className + " is in blacklist. ");
        } else {
            return className;
        }
    }

    /**
     * 检测类名是否不在黑名单中
     *
     * @param className
     * @return 是否在黑名单中
     */
    private boolean inBlackList(String className) {
        for (String prefix : blackPrefixList) {
            if (className.startsWith(prefix)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
