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
package com.alipay.hessian.internal;

import com.alipay.hessian.Constants;
import com.alipay.hessian.NameBlackListFilter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 内置黑名单列表过滤器
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class InternalNameBlackListFilter extends NameBlackListFilter {

    private static final String BLACKLIST_FILE      = System
                                                        .getProperty(Constants.SERIALIZE_BLACKLIST_FILE,
                                                            Constants.DEFAULT_SERIALIZE_BLACK_LIST);

    static final List<String>   INTERNAL_BLACK_LIST = readBlackList(BLACKLIST_FILE);

    /**
     * 构造函数
     */
    public InternalNameBlackListFilter() {
        super(INTERNAL_BLACK_LIST);
    }

    /**
     * 构造函数指定缓存大小
     *
     * @param maxCacheSize 最大缓存大小
     */
    public InternalNameBlackListFilter(int maxCacheSize) {
        super(INTERNAL_BLACK_LIST, maxCacheSize);
    }

    static List<String> readBlackList(String blackListFile) {

        List<String> result = new ArrayList<String>();
        //Get file from resources folder
        ClassLoader classLoader;

        if (blackListFile.equals(Constants.DEFAULT_SERIALIZE_BLACK_LIST)) {
            classLoader = InternalNameBlackListFilter.class.getClassLoader();
        } else {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        final InputStream inputStream = classLoader.getResourceAsStream(blackListFile);
        if (inputStream != null) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()) {
                    final String nextLine = scanner.nextLine();
                    if (!isBlank(nextLine)) {
                        result.add(nextLine);
                    }
                }
            } catch (Exception e) {
                //ignore
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
            //不存在使用内置的
        } else {
            result = readBlackList(Constants.DEFAULT_SERIALIZE_BLACK_LIST);
        }

        return result;
    }

    //is blank
    static boolean isBlank(String cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
