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
package com.alipay.hessian.clhm;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by zhanggeng on 2017/8/4.
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class ConcurrentLinkedHashMapTest {
    @Test
    public void testAll() {
        ConcurrentLinkedHashMap<Integer, Integer> map = new ConcurrentLinkedHashMap.Builder<Integer, Integer>()
            .initialCapacity(1).maximumWeightedCapacity(3).build();

        map.put(1, 1);
        map.put(2, 2);
        // 12
        Assert.assertTrue(map.size() == 2);

        map.put(3, 3);
        map.put(4, 4);
        // 234
        Assert.assertTrue(map.size() == 3);
        Assert.assertTrue(map.get(1) == null);

        map.get(2);
        map.get(3);
        map.put(1, 1);
        // 132
        Assert.assertTrue(map.get(4) == null);
    }
}