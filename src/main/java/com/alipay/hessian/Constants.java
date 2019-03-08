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

/**
 * @author qilong.zql
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class Constants {
    /**
     * enable blacklist of serializer, default is true
     */
    public static final String SERIALIZE_BLACKLIST_ENABLE         = "serialize.blacklist.enable";
    /**
     * default value of SERIALIZE_BLACKLIST_ENABLE
     */
    public static final String DEFAULT_SERIALIZE_BLACKLIST_ENABLE = "true";
    /**
     * the custom blacklist file path of serializer, default is DEFAULT_SERIALIZE_BLACK_LIST
     *
     * @see #DEFAULT_SERIALIZE_BLACK_LIST
     */
    public static final String SERIALIZE_BLACKLIST_FILE           = "serialize.blacklist.file";
    /**
     * default value of SERIALIZE_BLACKLIST_FILE
     */
    public static final String DEFAULT_SERIALIZE_BLACK_LIST       = "security/serialize.blacklist";
    /**
     * whether to create ContextSerializeFactory of parent classloader, default is true
     *
     * @since 4.0.3
     */
    public final static String HESSIAN_PARENT_CONTEXT_CREATE      = "hessian.parent.context.create";
}
