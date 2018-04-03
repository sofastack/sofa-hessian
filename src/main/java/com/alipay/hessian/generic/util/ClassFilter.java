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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.alipay.hessian.generic.io.GenericDeserializer.ARRAY_PREFIX;

/**
 *
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public class ClassFilter {

    private final static Set<String> PKG_FILTER;
    private final static Set<String> NAME_FILTER;
    public final static String       CLASS_NAME = Class.class.getName();
    static {
        PKG_FILTER = new HashSet<String>();

        //these package names are found in the source codes of JDK
        PKG_FILTER.add("com.sun");
        PKG_FILTER.add("java");
        PKG_FILTER.add("javax");
        PKG_FILTER.add("org.ietf");
        PKG_FILTER.add("org.ogm");
        PKG_FILTER.add("org.w3c");
        PKG_FILTER.add("org.xml");
        PKG_FILTER.add("sunw.io");
        PKG_FILTER.add("sunw.util");

        //these key words are used in Hessian
        NAME_FILTER = new HashSet<String>();
        NAME_FILTER.add("boolean");
        NAME_FILTER.add(Boolean.class.getName());
        NAME_FILTER.add("byte");
        NAME_FILTER.add(Byte.class.getName());
        NAME_FILTER.add("short");
        NAME_FILTER.add(Short.class.getName());
        NAME_FILTER.add("int");
        NAME_FILTER.add(Integer.class.getName());
        NAME_FILTER.add("long");
        NAME_FILTER.add(Long.class.getName());
        NAME_FILTER.add("float");
        NAME_FILTER.add(Float.class.getName());
        NAME_FILTER.add("double");
        NAME_FILTER.add(Double.class.getName());
        NAME_FILTER.add("char");
        NAME_FILTER.add(Character.class.getName());
        NAME_FILTER.add("void");
        NAME_FILTER.add(Void.class.getName());
        NAME_FILTER.add("string");
        NAME_FILTER.add(String.class.getName());
        NAME_FILTER.add("object");
        NAME_FILTER.add("date");

    }

    /**
     * 在反序列化类时, 会调用此方法检测是否是jdk类型, 一共包含两个维度, 一个从包名检测, 一个从指定的特殊类型中查找
     *
     * @param type 类名称
     * @return true 表示是jdk类
     */
    public static boolean filter(String type) {

        return doFilter(type, false);
    }

    /**
     * 在反序列化类时, 会调用此方法检测是否是jdk类型, 一共包含两个维度, 一个从包名检测, 一个从指定的特殊类型中查找
     * 需要注意的是java.lang.Class排除在过滤列表之外
     *
     * @param type 类名称
     * @return true 表示是jdk类
     */
    public static boolean filterExcludeClass(String type) {

        return doFilter(type, true);
    }

    private static boolean doFilter(String type, boolean excludeClass) {

        // java.lang.Class 虽然在java.lang包, 但是在实例化时却可能实例化不存在的类
        if (excludeClass && CLASS_NAME.equals(type)) {
            return false;
        }

        if (NAME_FILTER.contains(type)) {
            return true;
        }

        int index = -1;
        while ((index = type.indexOf('.', index + 1)) != -1) {
            String pkg = type.substring(0, index);
            if (PKG_FILTER.contains(pkg))
                return true;
        }
        return false;
    }

    /**
     * 在反序列化数组时,会调用此方法检测是否是jdk类型, 包含一个维度, 从指定的特殊类型中查找
     * 不从包名检测的原因是, 数组类型是jdk类型, 但是存放的内容可能是子类, 子类反序列化为GenericObject无法放入数组
     * NAME_FILTER没有这个问题的原因是, 除了object和date外, 这几个类为final类,无法继承
     * type为date或者object时,返回false
     *
     * @param type 类名称
     * @return 是否被过滤
     */
    public static boolean nameFilter(String type) {

        if ("date".equals(type) || "object".equals(type)) {
            return false;
        }
        return NAME_FILTER.contains(type);
    }

    /**
     * 对于java.lang.Object/java.util.Date对象, hessian序列化时需要写入object/date,此处进行转换
     *
     * @param type 类名称
     * @return 转换结果
     */
    public static String encodeObjectAndDate(String type) {
        return type.replace(Object.class.getName(), "object").replace(Date.class.getName(), "date");
    }

    /**
     * 对于java.lang.Object/java.util.Date对象, hessian序列化的时候写入类型是object/date,此处进行解析
     *
     * @param type 类名称
     * @return 转换结果
     */
    public static String decodeObjectAndDate(String type) {
        return type.replace("object", Object.class.getName()).replace("date", Date.class.getName());
    }

    /**
     * 检测数组类型是否在过滤列表
     * @param clazz 类
     * @return 是否在过滤列表
     */
    public static boolean arrayFilter(Class clazz) {

        if (!clazz.isArray()) {
            return false;
        }

        String arrayType = getArrayComponentName(clazz);

        return nameFilter(arrayType);
    }

    /**
     * 检测数组类型是否在过滤列表
     * @param type 类名称
     * @return 是否在过滤列表
     */
    public static boolean arrayFilter(String type) {

        int lastIndex = type.lastIndexOf(ARRAY_PREFIX);
        if (lastIndex == -1) {
            return false;
        }

        String arrayType = type.substring(lastIndex + 1);

        return nameFilter(arrayType);
    }

    private static String getArrayComponentName(Class cl) {
        if (cl.isArray()) {
            return getArrayComponentName(cl.getComponentType());
        } else {
            return cl.getName();
        }
    }
}
