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
package com.caucho.hessian.test;

/**
 * @author lianglipeng.llp@alibaba-inc.com
 * @version $Id: ClassEnum.java, v 0.1 2025年05月29日 15:46 立蓬 Exp $
 */
public enum ClassEnum {
    A {
        @Override
        public void test() {
            System.out.println("A");
        }
    },
    B {
        @Override
        public void test() {
            System.out.println("B");
        }
    };
    public abstract void test();
}