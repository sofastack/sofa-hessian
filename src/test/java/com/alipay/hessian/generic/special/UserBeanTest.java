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
package com.alipay.hessian.generic.special;

import com.alipay.hessian.generic.util.GenericUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserBeanTest {

    @Test
    public void testAll() throws Exception {
        UserBean userBean = new UserBean();
        userBean.setName(null);
        Object go = GenericUtils.convertToGenericObject(userBean);
        UserBean newUserBean = GenericUtils.convertToObject(go);
        assertEquals(newUserBean.getName(), null);
    }

    private class UserBean {
        private String  name = "zhangsan";
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
