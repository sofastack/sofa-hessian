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

import com.alipay.hessian.generic.exception.ConvertException;
import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericClass;
import com.alipay.hessian.generic.model.GenericCollection;
import com.alipay.hessian.generic.model.GenericMap;
import com.alipay.hessian.generic.model.GenericObject;
import com.caucho.hessian.io.ByteHandle;
import com.caucho.hessian.io.CalendarHandle;
import com.caucho.hessian.io.FloatHandle;
import com.caucho.hessian.io.ShortHandle;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import static com.alipay.hessian.generic.io.GenericDeserializer.ARRAY_PREFIX;

/**
 * 
 * @author <a href="mailto:caojie.cj@antfin.com">Jie Cao</a>
 * @since 5.3.0
 */
public class GenericUtils {

    private static final Logger                                log      = Logger
                                                                            .getLogger(GenericUtils.class
                                                                                .getName());

    private static Unsafe                                      unsafe;

    /**
     * 自定义类型处理器
     */
    private static final CopyOnWriteArrayList<CustomConverter> HANDLERS = new CopyOnWriteArrayList<CustomConverter>();

    static {
        try {
            Class<?> unsafe = Class.forName("sun.misc.Unsafe");
            Field theUnsafe = null;
            for (Field field : unsafe.getDeclaredFields()) {
                if (field.getName().equals("theUnsafe"))
                    theUnsafe = field;
            }

            if (theUnsafe != null) {
                theUnsafe.setAccessible(true);
                GenericUtils.unsafe = (Unsafe) theUnsafe.get(null);
            }
        } catch (Throwable e) {
            log.warning(e.toString());
        }
    }

    /**
     * 增加自定义 CustomConverter
     *
     * @param customConverter
     */
    public static void addCustomConverter(CustomConverter customConverter) {
        HANDLERS.add(customConverter);
    }

    /**
     * 删除自定义 CustomConverter
     *
     * @param customConverter
     */
    public static void removeCustomConverter(CustomConverter customConverter) {
        HANDLERS.remove(customConverter);
    }

    /**
     * 将 GenericObject 转换为具体对象
     *
     * @param genericObject 待转换的GenericObject
     * @return 转换后结果
     */
    public static <T> T convertToObject(Object genericObject) {

        try {
            return (T) innerToConvertObject(genericObject, new IdentityHashMap<Object, Object>());
        } catch (Throwable t) {
            throw new ConvertException(t);
        }
    }

    private static Object innerToConvertObject(Object value, Map<Object, Object> map)
        throws Exception {

        // 判null
        if (value == null) {
            return null;
        }

        // 值为GenericObject类型
        if (value.getClass() == GenericObject.class) {
            GenericObject genericObject = (GenericObject) value;
            return doConvertToObject(genericObject, map);
        }

        // 值为GenericCollection类型
        if (value.getClass() == GenericCollection.class) {
            GenericCollection collection = (GenericCollection) value;
            return doConvertToCollection(collection, map);
        }

        // 值为GenericMap类型
        if (value.getClass() == GenericMap.class) {
            GenericMap genericMap = (GenericMap) value;
            return doConvertToMap(genericMap, map);
        }

        // 值为GenericArray类型
        if (value.getClass() == GenericArray.class) {
            GenericArray genericArray = (GenericArray) value;
            return doConvertToArray(genericArray, map);
        }

        // 值为GenericClass类型
        if (value.getClass() == GenericClass.class) {
            GenericClass genericClass = (GenericClass) value;
            return doConvertToClass(genericClass, map);
        }

        // 说明是jdk类, 处理集合类,将集合类中结果转换
        Object obj = handleCollectionOrMapToObject(value, map);
        return obj;
    }

    private static Object doConvertToObject(GenericObject genericObject, Map<Object, Object> map)
        throws Exception {

        // 如果map中缓存转换结果,直接返回
        Object object = map.get(genericObject);
        if (object != null) {
            return object;
        }

        // 检测 genericObject 是否是 CalendarHandle 类型
        if (CalendarHandle.class.getName().equals(genericObject.getType())) {
            return handleCalendarHandle(genericObject, map);
        }

        // 检测是否是Enum类型, 如果是, 根据name域得到对象实例
        Class clazz = loadClassFromTCCL(genericObject.getType());
        if (Enum.class.isAssignableFrom(clazz)) {
            Method valueOfMethod = clazz.getMethod("valueOf", new Class[] { Class.class,
                    String.class });
            return valueOfMethod.invoke(null, clazz, genericObject.getField("name"));
        }

        // 查找特殊处理
        for (CustomConverter converter : HANDLERS) {
            if (converter.interestClass().isAssignableFrom(clazz)) {
                return converter.convertToObject(clazz, genericObject);
            }
        }

        // 初始化对象,并放入map
        object = instantiate(clazz);
        map.put(genericObject, object);

        // field设值
        List<Field> fieldsList = getClassFields(clazz);
        for (Field field : fieldsList) {

            Object value = genericObject.getField(field.getName());

            if (value == null) {
                if (genericObject.hasField(field.getName())) {
                    setFieldValue(object, field, null);
                }
                continue;
            }

            Object result = innerToConvertObject(value, map);
            setFieldValue(object, field, result);
        }

        return object;
    }

    private static void setFieldValue(Object object, Field field, Object value)
        throws IllegalAccessException {
        if (value != null) {
            Class fieldType = field.getType();
            Class valueClass = value.getClass();

            // byte 域写入的可能是 int类型, setField 时需要进行转换
            if (fieldType == byte.class || fieldType == Byte.class) {
                if (valueClass == Integer.class) {
                    field.set(object, ((Integer) value).byteValue());
                    return;
                } else if (valueClass == ByteHandle.class) {
                    field.set(object, ((ByteHandle) value).getValue());
                    return;
                }
            }

            // short 域写入的可能是 int类型, setField 时需要强转
            if (fieldType == short.class || fieldType == Short.class) {
                if (valueClass == Integer.class) {
                    field.set(object, ((Integer) value).shortValue());
                    return;
                } else if (valueClass == ShortHandle.class) {
                    field.set(object, ((ShortHandle) value).getValue());
                    return;
                }
            }

            // float 域写入的可能是 double类型, setField 时需要进行转换
            if (fieldType == float.class || fieldType == Float.class) {
                if (valueClass == Double.class) {
                    field.set(object, ((Double) value).floatValue());
                    return;
                } else if (valueClass == FloatHandle.class) {
                    field.set(object, ((FloatHandle) value).getValue());
                    return;
                }
            }
        }
        // 默认情况直接设置
        field.set(object, value);
    }

    private static Object handleCalendarHandle(GenericObject genericObject, Map<Object, Object> map)
        throws InstantiationException,
        IllegalAccessException,
        ClassNotFoundException {
        Object type = genericObject.getField("type");
        Date date = (Date) genericObject.getField("date");

        Calendar cal;
        if (type == null) {
            cal = new GregorianCalendar();
        } else {
            if (type.getClass() == Class.class) {
                Class clazz = (Class) type;
                cal = (Calendar) clazz.newInstance();
            } else if (type.getClass() == GenericClass.class) {
                GenericClass gc = (GenericClass) type;
                Class clazz = (Class) doConvertToClass(gc, map);
                cal = (Calendar) clazz.newInstance();
            } else {
                throw new IllegalArgumentException("type需要是Class或者GenericClass类型");
            }
        }

        map.put(genericObject, cal);
        cal.setTimeInMillis(date.getTime());
        return cal;
    }

    private static Object doConvertToCollection(GenericCollection genericCollection,
                                                Map<Object, Object> map) throws Exception {

        // 如果map中缓存转换结果,直接返回
        Object object = map.get(genericCollection);
        if (object != null) {
            return object;
        }

        // 检测 GenericCollection 是否封装 Collection 实例
        Class clazz = loadClassFromTCCL(genericCollection.getType());
        if (!Collection.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("GenericCollection实例未封装Collection实例.");
        }

        // 初始化Collection对象,并放入map
        Collection result = (Collection) instantiate(clazz);
        map.put(genericCollection, result);

        // 填充Collection对象
        Collection values = genericCollection.getCollection();
        for (Object value : values) {
            result.add(innerToConvertObject(value, map));
        }

        return result;
    }

    private static Object doConvertToMap(GenericMap genericMap, Map<Object, Object> map)
        throws Exception {

        // 如果map中缓存转换结果,直接返回
        Object object = map.get(genericMap);
        if (object != null) {
            return object;
        }

        // 检测 GenericMap 是否封装 Map 实例
        Class clazz = loadClassFromTCCL(genericMap.getType());
        if (!Map.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("GenericMap实例未封装Map实例.");
        }

        // 初始化对象,并放入map
        Map result = (Map) instantiate(clazz);
        map.put(genericMap, result);

        // 填充map对象
        Iterator iter = genericMap.getMap().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            result.put(innerToConvertObject(entry.getKey(), map),
                innerToConvertObject(entry.getValue(), map));
        }

        return result;
    }

    private static Object doConvertToArray(GenericArray genericArray, Map<Object, Object> map)
        throws Exception {

        // 如果map中缓存转换结果,直接返回
        Object object = map.get(genericArray);
        if (object != null) {
            return object;
        }

        // 初始化数组对象,并放入map
        Class clazz = loadClassFromTCCL(genericArray.getComponentType());
        Object[] objects = genericArray.getObjects();
        Object result = Array.newInstance(clazz, objects.length);
        map.put(genericArray, result);

        // 填充数组对象
        for (int i = 0; i < objects.length; i++) {
            Array.set(result, i, innerToConvertObject(objects[i], map));
        }

        return result;
    }

    private static Object doConvertToClass(GenericClass genericClass, Map<Object, Object> map)
        throws ClassNotFoundException {
        // 如果map中缓存转换结果,直接返回
        Object object = map.get(genericClass);
        if (object != null) {
            return object;
        }

        Object obj = loadClassFromTCCL(genericClass.getClazzName());
        map.put(genericClass, obj);
        return obj;
    }

    /**
     * 将对象转换为GenericObject
     *
     * @param object 待转换的对象
     * @return 转换后的结果
     */
    public static <T> T convertToGenericObject(Object object) {

        try {
            return (T) innerConvertToGenericObject(object, new IdentityHashMap<Object, Object>());
        } catch (Throwable t) {
            throw new ConvertException(t);
        }
    }

    private static Object innerConvertToGenericObject(Object value, Map<Object, Object> map)
        throws Exception {
        // 判null
        if (value == null) {
            return null;
        }

        // 查找特殊处理
        Class clazz = value.getClass();
        for (CustomConverter converter : HANDLERS) {
            if (converter.interestClass().isAssignableFrom(clazz)) {
                GenericObject genericObject = converter.convertToGenericObject(clazz, value);
                if (genericObject != null) {
                    map.put(value, genericObject);
                    return genericObject;
                }
            }
        }

        // 如果是jdk内部类, 不转换为Generic类型
        if (ClassFilter.filterExcludeClass(clazz.getName())) {
            Object result = handleCollectionOrMapToGenericObjec(value, map);
            return result;
        }

        // 如果clazz是数组, 且在过滤列表, 直接返回
        if (clazz.isArray() && ClassFilter.arrayFilter(clazz)) {
            return value;
        }

        // 值为Collection类型, 转化为GenericCollection
        if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) value;
            return doConvertToGenericCollection(collection, map);
        }

        // 值为Map类型, 转化为GenericMap
        if (Map.class.isAssignableFrom(clazz)) {
            Map m = (Map) value;
            return doConvertToGenericMap(m, map);
        }

        // 值为数组类型, 转化为GenericArray
        if (clazz.isArray()) {
            // 判断是否是基础类型的数组, 如果是, 直接返回
            if (ClassFilter.arrayFilter(clazz)) {
                return value;
            }

            return doConvertToGenericArray(value, map);
        }

        // 处理value为Class类型情况
        if (clazz == Class.class) {
            Class clazzValue = (Class) value;

            // 如果value代表的Class属于jdk类, 直接返回value
            if (ClassFilter.filter(clazzValue.getName())) {
                return value;
            }

            return doConvertToGenericClass(clazzValue, map);
        }

        // 值为Enumeration类型, 转换为ArrayList返回
        if (Enumeration.class.isAssignableFrom(clazz)) {
            ArrayList list = new ArrayList();
            Enumeration iter = (Enumeration) value;
            while (iter.hasMoreElements()) {
                list.add(iter.nextElement());
            }
            return list;
        }

        // 如果clazz 是自定义Calendar 实现类
        if (Calendar.class.isAssignableFrom(clazz)) {
            Calendar calendar = (Calendar) value;
            return convertCalendarToGenericObject(calendar, map);
        }

        // 默认转换为GenericObject
        return doConvertToGenericObjec(value, map);
    }

    private static Object doConvertToGenericObjec(Object object, Map<Object, Object> map)
        throws Exception {

        // 如果map中缓存转换结果,直接返回
        Object cachedObject = map.get(object);
        if (cachedObject != null) {
            return cachedObject;
        }

        // 初始化GenericObject对象,并放入map
        Class clazz = object.getClass();
        GenericObject genericObject = new GenericObject(clazz.getName());
        map.put(object, genericObject);

        // 检测是否是枚举类型, 并且设置isEnum值
        boolean isEnum = false;
        if (Enum.class.isAssignableFrom(clazz)) {
            isEnum = true;
        }

        // 处理普通对象, 填充GenericObject
        List<Field> fieldsList = getClassFields(clazz);
        for (Field field : fieldsList) {

            Object obj = field.get(object);

            // 如果类型为枚举类型但是域的名字不是name时, 直接跳过
            if ((isEnum && !field.getName().equals("name"))) {
                continue;
            }

            genericObject.putField(field.getName(), innerConvertToGenericObject(obj, map));
        }

        return genericObject;
    }

    private static Object doConvertToGenericCollection(Collection collection,
                                                       Map<Object, Object> map) throws Exception {

        // 如果map中缓存转换结果,直接返回
        Object cachedObject = map.get(collection);
        if (cachedObject != null) {
            return cachedObject;
        }

        // 构造GenericCollection对象,并放入map
        GenericCollection genericCollection = new GenericCollection(collection.getClass().getName());
        map.put(collection, genericCollection);

        // 构造默认Collection,并填充数据
        List defaultCollection = new ArrayList();
        for (Object obj : collection) {
            defaultCollection.add(innerConvertToGenericObject(obj, map));
        }

        genericCollection.setCollection(defaultCollection);

        return genericCollection;
    }

    private static Object doConvertToGenericMap(Map valueMap, Map<Object, Object> map)
        throws Exception {

        // 如果map中缓存转换结果,直接返回
        Object cachedObject = map.get(valueMap);
        if (cachedObject != null) {
            return cachedObject;
        }

        // 构造GenericMap,并放入map
        GenericMap genericMap = new GenericMap(valueMap.getClass().getName());
        map.put(valueMap, genericMap);

        // 构造默认Map, 并填充数据
        Map defaultMap = new HashMap();
        Iterator iter = valueMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            defaultMap.put(innerConvertToGenericObject(entry.getKey(), map),
                innerConvertToGenericObject(entry.getValue(), map));
        }

        genericMap.setMap(defaultMap);

        return genericMap;
    }

    private static Object doConvertToGenericArray(Object array, Map<Object, Object> map)
        throws Exception {

        // 检测是否是数组对象
        Class clazz = array.getClass();
        if (!clazz.isArray()) {
            throw new IllegalArgumentException("参数必须要是一个数组类型");
        }

        // 如果map中缓存转换结果,直接返回
        Object cachedObject = map.get(array);
        if (cachedObject != null) {
            return cachedObject;
        }

        // 构造GenericArray,并放入map
        GenericArray genericArray = new GenericArray(convertHessianFormat(clazz.getComponentType()
            .getName()));
        map.put(array, genericArray);

        // 构造数组对象,并填充数据
        int length = Array.getLength(array);
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++) {
            Array.set(objects, i, innerConvertToGenericObject(Array.get(array, i), map));
        }

        genericArray.setObjects(objects);

        return genericArray;
    }

    private static Object doConvertToGenericClass(Class clazzValue, Map<Object, Object> map) {

        // 如果map中缓存转换结果,直接返回
        Object cachedObject = map.get(clazzValue);
        if (cachedObject != null) {
            return cachedObject;
        }

        GenericClass genericClass = new GenericClass(clazzValue.getName());
        map.put(clazzValue, genericClass);
        return genericClass;
    }

    private static Object convertCalendarToGenericObject(Calendar calendar, Map<Object, Object> map) {

        // 如果map中缓存转换结果,直接返回
        Object cachedObject = map.get(calendar);
        if (cachedObject != null) {
            return cachedObject;
        }

        GenericObject go = new GenericObject(CalendarHandle.class.getName());
        map.put(calendar, go);
        go.putField("type", doConvertToGenericClass(calendar.getClass(), map));
        go.putField("date", new Date(calendar.getTimeInMillis()));

        return go;
    }

    private static List<Field> getClassFields(Class clazz) {
        List<Field> fieldsList = new ArrayList<Field>();

        for (; clazz != null; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];

                if (Modifier.isTransient(field.getModifiers())
                    || Modifier.isStatic(field.getModifiers()))
                    continue;

                field.setAccessible(true);

                fieldsList.add(field);
            }
        }

        return fieldsList;
    }

    private static Object instantiate(Class clazz) throws Exception {

        try {
            Class type = clazz;
            Constructor constructor = null;
            Object[] constructorArgs = null;

            Constructor[] constructors = clazz.getDeclaredConstructors();
            long bestCost = Long.MAX_VALUE;

            for (int i = 0; i < constructors.length; i++) {
                Class[] param = constructors[i].getParameterTypes();
                long cost = 0;

                for (int j = 0; j < param.length; j++) {
                    cost = 4 * cost;

                    if (Object.class.equals(param[j]))
                        cost += 1;
                    else if (String.class.equals(param[j]))
                        cost += 2;
                    else if (int.class.equals(param[j]))
                        cost += 3;
                    else if (long.class.equals(param[j]))
                        cost += 4;
                    else if (param[j].isPrimitive())
                        cost += 5;
                    else
                        cost += 6;
                }

                if (cost < 0 || cost > (1 << 48))
                    cost = 1 << 48;

                cost += param.length << 48;

                if (cost < bestCost) {
                    constructor = constructors[i];
                    bestCost = cost;
                }
            }

            if (constructor != null) {
                constructor.setAccessible(true);
                Class[] params = constructor.getParameterTypes();
                constructorArgs = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    constructorArgs[i] = getParamArg(params[i]);
                }
            }

            if (constructor != null)
                return constructor.newInstance(constructorArgs);
            else
                return type.newInstance();
        } catch (Exception e) {
            // 构造函数抛出异常,使用unsafe进行初始化
            return unsafe.allocateInstance(clazz);
        }
    }

    private static Object getParamArg(Class cl) {
        if (!cl.isPrimitive())
            return null;
        else if (boolean.class.equals(cl))
            return Boolean.FALSE;
        else if (byte.class.equals(cl))
            return new Byte((byte) 0);
        else if (short.class.equals(cl))
            return new Short((short) 0);
        else if (char.class.equals(cl))
            return new Character((char) 0);
        else if (int.class.equals(cl))
            return Integer.valueOf(0);
        else if (long.class.equals(cl))
            return Long.valueOf(0);
        else if (float.class.equals(cl))
            return Float.valueOf(0);
        else if (double.class.equals(cl))
            return Double.valueOf(0);
        else
            throw new UnsupportedOperationException();
    }

    private static Object handleCollectionOrMapToObject(Object value, Map<Object, Object> map)
        throws Exception {

        // 1. 判null
        if (value == null) {
            return null;
        }

        // 2. 查看缓存的转换记录是否存在转换历史
        if (map.get(value) != null) {
            return map.get(value);
        }

        // 3. 处理Collection实现类情况
        if (Collection.class.isAssignableFrom(value.getClass())) {

            Collection values = (Collection) value;
            Collection result = (Collection) instantiate(value.getClass());
            map.put(value, result);

            for (Object obj : values) {
                result.add(innerToConvertObject(obj, map));
            }

            return result;
        }

        // 4. 处理Map实现类情况
        if (Map.class.isAssignableFrom(value.getClass())) {

            Map<Object, Object> valueMap = (Map<Object, Object>) value;
            Map result = (Map) instantiate(value.getClass());
            map.put(value, result);

            Iterator iter = valueMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                result.put(innerToConvertObject(entry.getKey(), map),
                    innerToConvertObject(entry.getValue(), map));
            }

            return result;
        }

        return value;
    }

    private static Object handleCollectionOrMapToGenericObjec(Object value, Map<Object, Object> map)
        throws Exception {

        // 1. 判null
        if (value == null) {
            return null;
        }

        // 2. 查看缓存的转换记录是否存在转换历史
        if (map.get(value) != null) {
            return map.get(value);
        }

        // 3. 处理Collection实现类情况
        if (Collection.class.isAssignableFrom(value.getClass())) {

            Collection values = (Collection) value;
            Collection result = (Collection) instantiate(value.getClass());
            map.put(value, result);

            for (Object obj : values) {
                result.add(innerConvertToGenericObject(obj, map));
            }

            return result;
        }

        // 4. 处理Map实现类情况
        if (value != null && Map.class.isAssignableFrom(value.getClass())) {

            Map<Object, Object> valueMap = (Map<Object, Object>) value;
            Map result = (Map) instantiate(value.getClass());
            map.put(value, result);

            Iterator iter = valueMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                result.put(innerConvertToGenericObject(entry.getKey(), map),
                    innerConvertToGenericObject(entry.getValue(), map));
            }

            return result;
        }

        return value;
    }

    /**
     * 对于Object[]数组, hessian传入的componentType为[java.lang.Object, 但是Class.forName 需要的字符串是 [Ljava.lang.Object;
     * 这里为了实现兼容, 需要把 [Ljava.lang.Object 转为 [java.lang.Object
     *
     * @return
     */
    private static String convertHessianFormat(String componentType) {

        if (componentType == null) {
            throw new IllegalArgumentException("参数不能为null.");
        }

        int lastIndex = componentType.lastIndexOf(ARRAY_PREFIX);
        if (lastIndex != -1) {

            if (componentType.charAt(componentType.length() - 1) != ';'
                && componentType.charAt(lastIndex + 1) != 'L') {
                throw new IllegalArgumentException("非法数组格式");
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lastIndex + 1; i++) {
                sb.append(ARRAY_PREFIX);
            }

            sb.append(componentType.substring(lastIndex + 2, componentType.length() - 1));
            return sb.toString();
        }

        return componentType;
    }

    private static Class loadClassFromTCCL(String clazzName) throws ClassNotFoundException {
        return Class.forName(clazzName, true, Thread.currentThread().getContextClassLoader());
    }
}
