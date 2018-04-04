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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 类名处理器，包含一组类名过滤器
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class ClassNameResolver {

    /**
     * 锁
     */
    private ReentrantReadWriteLock           lock      = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock  readLock  = lock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    /**
     * 过滤器列表
     */
    List<ClassNameFilter>                    filters   = null;

    /**
     * 增加类名过滤器
     *
     * @param classNameFilter 类名过滤器t
     */
    public void addFilter(ClassNameFilter classNameFilter) {
        writeLock.lock();
        try {
            if (filters == null) {
                filters = new ArrayList<ClassNameFilter>();
            }
            filters.add(classNameFilter);
            // 从小到大，先进优先
            Collections.sort(filters, new Comparator<ClassNameFilter>() {
                public int compare(ClassNameFilter o1, ClassNameFilter o2) {
                    return o1.order() - o2.order();
                }
            });
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 删除类名过滤器
     * 
     * @param classNameFilter 类名过滤器
     */
    public void removeFilter(ClassNameFilter classNameFilter) {
        writeLock.lock();
        try {
            if (filters != null) {
                filters.remove(classNameFilter);
                // 删除不用重新排序
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 决定类名
     *
     * @param className 类名
     * @return 过滤器执行后的类名
     */
    public String resolve(String className) throws IOException {
        readLock.lock();
        try {
            if (filters == null || filters.isEmpty()) {
                return className;
            }
            String cls = className;
            for (ClassNameFilter filter : filters) {
                cls = filter.resolve(cls);
            }
            return cls;
        } finally {
            readLock.unlock();
        }
    }
}
