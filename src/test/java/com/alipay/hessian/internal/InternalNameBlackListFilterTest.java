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

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by zhanggeng on 2017/8/5.
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class InternalNameBlackListFilterTest {

    @Test
    public void testAll() {
        InternalNameBlackListFilter filter = new InternalNameBlackListFilter(3);
        String className = null;
        boolean pass = true;
        try {
            filter.resolve("java.util.ServiceLoader$LazyIterator.xxx");
        } catch (Exception e) {
            pass = false;
        }
        Assert.assertFalse(pass);

        pass = true;
        try {
            className = filter.resolve("com.alipay.xx");
        } catch (Exception e) {
            pass = false;
        }
        Assert.assertTrue(pass);
        Assert.assertEquals(className, "com.alipay.xx");
    }

    @Test
    public void readBlackList() {

        InternalNameBlackListFilter filter = new InternalNameBlackListFilter(3);
        List<String> result = filter.readBlackList("test.blacklist");
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("aa", result.get(0));
        Assert.assertEquals("bb", result.get(1));

    }
}