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

import com.alipay.hessian.generic.io.GenericObjectSerializer;
import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.GenericUtils;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.Assert.*;

/**
 * @author xuanbei
 * @since 17/11/7
 */
public class PersonTest {

    static Field WRITE_DEFINITION_EVERYTIME_FIELD;
    static {
        try {
            WRITE_DEFINITION_EVERYTIME_FIELD = GenericObjectSerializer.class
                .getDeclaredField("WRITE_DEFINITION_EVERYTIME");
            WRITE_DEFINITION_EVERYTIME_FIELD.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(WRITE_DEFINITION_EVERYTIME_FIELD,
                WRITE_DEFINITION_EVERYTIME_FIELD.getModifiers() & ~Modifier.FINAL);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Test
    public void testWriteDefinitionWithSameDefinition() throws Exception {
        setWriteDefinitionEverytimeField(true);
        testSameDefinition();
    }

    @Test
    public void testNotWriteDefinitionWithSameDefinition() throws Exception {
        setWriteDefinitionEverytimeField(false);
        testSameDefinition();
    }

    @Test
    public void testWriteDefinitionWithDiffDefinition() throws Exception {
        setWriteDefinitionEverytimeField(true);
        testDiffDefinition();
    }

    @Test
    public void testNotWriteDefinitionWithDiffDefinition() throws Exception {
        boolean hasException = false;
        try {
            setWriteDefinitionEverytimeField(false);
            testDiffDefinition();
        } catch (Throwable t) {
            hasException = true;
        }
        assertTrue(hasException);
    }

    public void testSameDefinition() throws Exception {
        GenericObject go1 = new GenericObject(Person.class.getName());
        go1.putField("name", "zhangsan");
        go1.putField("age", 12);
        go1.putField("gender", null);
        go1.putField("scores", new HashMap<String, Integer>());
        go1.putField("friend", null);
        go1.putField("pet", GenericUtils.convertToGenericObject(new Pet()));
        go1.putField("mapValue", null);
        go1.putField("listValue", new ArrayList<Object>());

        GenericObject go2 = new GenericObject(Person.class.getName());
        go2.putField("name", "lisi");
        go2.putField("age", 21);
        go2.putField("gender", "Femal");
        go2.putField("scores", new HashMap<String, Integer>());
        go2.putField("friend", GenericUtils.convertToGenericObject(new Person()));
        go2.putField("pet", null);
        go2.putField("mapValue", null);
        go2.putField("listValue", null);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(go1);
        hout.writeObject(go2);

        hout.close();

        byte[] body = bout.toByteArray();

        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Person p1 = (Person) hin.readObject();
        assertEquals("zhangsan", p1.getName());
        assertEquals(12, p1.getAge());
        assertEquals(null, p1.getGender());
        assertEquals(0, p1.getScores().size());
        assertEquals(null, p1.getFriend());
        assertNotNull(p1.getPet());
        assertEquals(null, p1.getMapValue());
        assertEquals(0, p1.getListValue().size());

        Person p2 = (Person) hin.readObject();
        assertEquals("lisi", p2.getName());
        assertEquals(21, p2.getAge());
        assertEquals("Femal", p2.getGender());
        assertEquals(0, p2.getScores().size());
        assertNotNull(p2.getFriend());
        assertEquals(null, p2.getPet());
        assertEquals(null, p2.getMapValue());
        assertEquals(null, p2.getListValue());
    }

    public void testDiffDefinition() throws Exception {
        try {
            GenericObject go1 = new GenericObject(Person.class.getName());
            go1.putField("name", "zhangsan");
            //        go1.putField("age", 12);
            go1.putField("gender", null);
            go1.putField("scores", new HashMap<String, Integer>());
            go1.putField("friend", null);
            go1.putField("pet", GenericUtils.convertToGenericObject(new Pet()));
            //        go1.putField("mapValue", null);
            go1.putField("listValue", new ArrayList<Object>());

            GenericObject go2 = new GenericObject(Person.class.getName());
            go2.putField("name", "lisi");
            go2.putField("age", 21);
            //        go2.putField("gender", "Femal");
            go2.putField("scores", new HashMap<String, Integer>());
            go2.putField("friend", GenericUtils.convertToGenericObject(new Person()));
            //        go2.putField("pet", null);
            go2.putField("mapValue", null);
            go2.putField("listValue", null);

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Hessian2Output hout = new Hessian2Output(bout);
            hout.setSerializerFactory(new GenericSerializerFactory());

            hout.writeObject(go1);
            hout.writeObject(go2);

            hout.close();

            byte[] body = bout.toByteArray();

            ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
            Hessian2Input hin = new Hessian2Input(bin);
            hin.setSerializerFactory(new SerializerFactory());

            Person p1 = (Person) hin.readObject();
            assertEquals("zhangsan", p1.getName());
            //        assertEquals(12, p1.getAge());
            assertEquals(null, p1.getGender());
            assertEquals(0, p1.getScores().size());
            assertEquals(null, p1.getFriend());
            assertNotNull(p1.getPet());
            //        assertEquals(null, p1.getMapValue());
            assertEquals(0, p1.getListValue().size());

            Person p2 = (Person) hin.readObject();
            assertEquals("lisi", p2.getName());
            assertEquals(21, p2.getAge());
            //        assertEquals("Femal", p2.getGender());
            assertEquals(0, p2.getScores().size());
            assertNotNull(p2.getFriend());
            //        assertEquals(null, p2.getPet());
            assertEquals(null, p2.getMapValue());
            assertEquals(null, p2.getListValue());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName() + " current WriteDefinitionEverytimeField to "
                    + WRITE_DEFINITION_EVERYTIME_FIELD.get(null));
            throw e;
        }
        
    }
    
    private void setWriteDefinitionEverytimeField(boolean write){
        try {
            System.out.println(Thread.currentThread().getName() + " setWriteDefinitionEverytimeField to " + write);
            WRITE_DEFINITION_EVERYTIME_FIELD.set(GenericObjectSerializer.class, write);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    } 
    
}