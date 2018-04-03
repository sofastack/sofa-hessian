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

import com.alipay.hessian.NameBlackListFilter;

import java.util.Arrays;
import java.util.List;

/**
 * 内置黑名单列表过滤器
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class InternalNameBlackListFilter extends NameBlackListFilter {

    private static final List<String> INTERNAL_BLACK_LIST = Arrays.asList(
            "bsh",
            "com.mchange",
            "com.sun.",
            "java.lang.Thread",
            "java.net.Socket",
            "java.rmi",
            "javax.xml",
            "org.apache.bcel",
            "org.apache.commons.beanutils",
            "org.apache.commons.collections.Transformer",
            "org.apache.commons.collections.functors",
            "org.apache.commons.collections4.comparators",
            "org.apache.commons.fileupload",
            "org.apache.myfaces.context.servlet",
            "org.apache.tomcat",
            "org.apache.wicket.util",
            "org.codehaus.groovy.runtime",
            "org.hibernate",
            "org.jboss",
            "org.mozilla.javascript",
            "org.python.core",
            "org.springframework",
            "javax.imageio.",
            "jdk.nashorn.internal.objects.NativeString",
            "org.apache.commons.collections4.functors.InvokerTransformer",
            "org.apache.commons.collections4.functors.ChainedTransformer",
            "org.apache.commons.collections4.functors.ConstantTransformer",
            "org.apache.commons.collections4.functors.InstantiateTransformer",
            "org.apache.myfaces.el.CompositeELResolver",
            "org.apache.myfaces.el.unified.FacesELContext",
            "org.apache.myfaces.view.facelets.el.ValueExpressionMethodExpression",
            "java.util.PriorityQueue",
            "java.lang.reflect.Proxy",
            "javax.management.MBeanServerInvocationHandler",
            "javax.management.openmbean.CompositeDataInvocationHandler",
            "java.beans.EventHandler",
            "java.util.Comparator",
            "org.reflections.Reflections",
            "net.sf.json.JSONObject");

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
}
