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
package com.alipay.hessian.generic.test;

import com.alipay.hessian.generic.bean.SimplePerson;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.GenericUtils;
import org.junit.Test;

import java.util.TreeSet;

import static org.junit.Assert.assertTrue;

/**
 * @author xuanbei
 * @since 17/11/13
 */
public class GenericObjectCompareToTest {
    @Test
    public void testTreeSet() throws Exception {
        TreeSet<GenericObject> treeSet = new TreeSet<GenericObject>();
        SimplePerson person = new SimplePerson();
        person.setJob("coder");
        treeSet.add((GenericObject) GenericUtils.convertToGenericObject(person));

        person = new SimplePerson();
        person.setName("zhangsan");
        treeSet.add((GenericObject) GenericUtils.convertToGenericObject(person));

        assertTrue(treeSet.size() == 2);

        person = new SimplePerson();
        person.setName("lisi");
        treeSet.add((GenericObject) GenericUtils.convertToGenericObject(person));

        person = new SimplePerson();
        person.setName("zhangsan");
        treeSet.add((GenericObject) GenericUtils.convertToGenericObject(person));

        assertTrue(treeSet.size() == 3);

        person = new SimplePerson();
        person.setName("lisi");
        person.setJob("coder");
        treeSet.add((GenericObject) GenericUtils.convertToGenericObject(person));
        assertTrue(treeSet.size() == 4);

        person = new SimplePerson();
        person.setName("lisi");
        person.setJob("coder");
        treeSet.add((GenericObject) GenericUtils.convertToGenericObject(person));
        assertTrue(treeSet.size() == 4);
    }
}
