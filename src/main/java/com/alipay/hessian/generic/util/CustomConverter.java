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
package com.alipay.hessian.generic.util;

import com.alipay.hessian.generic.model.GenericObject;

/**
 * Created by zhanggeng on 2017/7/28.
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public interface CustomConverter {

    /**
     * 关注的类名
     *
     * @return 关注的类名
     */
    public Class interestClass();

    /**
     * 泛化对象转普通对象
     *
     * @param clazz         类
     * @param genericObject 泛化对象
     * @return 普通对象
     */
    public Object convertToObject(Class clazz, GenericObject genericObject);

    /**
     * 普通对象转泛化对象
     *
     * @param clazz  类
     * @param object 普通对象
     * @return 泛化对象
     */
    public GenericObject convertToGenericObject(Class clazz, Object object);
}
