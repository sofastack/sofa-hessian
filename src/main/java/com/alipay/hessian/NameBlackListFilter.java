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
import com.alipay.sofa.common.log.LoggerSpaceManager;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * 名字黑名单过滤器
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public abstract class NameBlackListFilter implements ClassNameFilter {

    private static Logger      LOGGER                     = judgeLogger();

    //do not change this
    public static final String HESSIAN_SERIALIZE_LOG_NAME = "HessianSerializeLog";
    public static final String CONFIG_LOG_SPACE_NAME      = "com.alipay.sofa.middleware.config";

    private static Logger judgeLogger() {

        try {
            NameBlackListFilter.class.getClassLoader().loadClass("com.alipay.sofa.middleware.log.ConfigLogFactory");
        } catch (Throwable e) {
            //do nothing
            return null;
        }
        return LoggerSpaceManager.getLoggerBySpace(HESSIAN_SERIALIZE_LOG_NAME, CONFIG_LOG_SPACE_NAME);
    }

    /**
     * 黑名单 包名前缀
     */
    protected static List<String>                   blackPrefixList;

    /**
     * 类名是否在黑名单中结果缓存。{className:true/false}
     */
    protected static ConcurrentMap<String, Boolean> resultOfInBlackList;

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
        NameBlackListFilter.blackPrefixList = blackPrefixList;
        buildCache(blackPrefixList, maxCacheSize);
    }

    /**
     * 初始化缓存
     *
     * @param maxCacheSize 最大缓存
     */
    public static void buildCache(List<String> blackPrefixList, int maxCacheSize) {
        if (blackPrefixList != null && !blackPrefixList.isEmpty()) {
            NameBlackListFilter.blackPrefixList = blackPrefixList;
            int min = Math.min(256, maxCacheSize);
            int max = Math.min(10240, maxCacheSize);
            ConcurrentLinkedHashMap.Builder<String, Boolean> builder = new ConcurrentLinkedHashMap.Builder<String, Boolean>()
                .initialCapacity(min).maximumWeightedCapacity(max);
            resultOfInBlackList = builder.build();
        } else {
            resultOfInBlackList = null;
        }
    }

    public int order() {
        return 0;
    }

    public String resolve(String className) throws IOException {
        if (blackPrefixList == null || resultOfInBlackList == null) {
            return className;
        }
        final String monitorKey = "@" + className;
        Boolean monitorResult = resultOfInBlackList.get(monitorKey);
        if (monitorResult == null) {
            monitorResult = inBlackList(monitorKey);
            resultOfInBlackList.putIfAbsent(monitorKey, monitorResult);
        }

        if (monitorResult && LOGGER != null) {
            LOGGER.info(String.format(
                "[status] %s, [class] %s, [rule] %s, [callStack] %s",
                "watch", className, monitorKey, CallStackUtil
                    .getCurrentCallStack()));

        }

        Boolean result = resultOfInBlackList.get(className);
        if (result == null) {
            result = inBlackList(className);
            resultOfInBlackList.putIfAbsent(className, result);
        }
        if (result && LOGGER != null) {
            LOGGER.info(String.format(
                "[status] %s, [class] %s, [rule] %s, [callStack] %s",
                "control", className, className, CallStackUtil
                    .getCurrentCallStack()));

            throw new IOException("Class " + className + " is in blacklist. ");
        } else {
            return className;
        }
    }

    /**
     * 检测类名是否不在黑名单中
     *
     * @param className
     * @return
     */
    protected boolean inBlackList(String className) {
        for (String prefix : blackPrefixList) {
            if (className.startsWith(prefix)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
