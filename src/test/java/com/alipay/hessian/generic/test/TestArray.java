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

import com.alipay.hessian.generic.bean.ArrayBean;
import com.alipay.hessian.generic.bean.SimplePerson;
import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.GenericUtils;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.alipay.hessian.generic.test.ComplexTestGO2GO.cmpGPersonEqualPerson;
import static org.junit.Assert.assertEquals;

/**
 * Created by qiwei.lqw on 2016/7/19.
 */
public class TestArray {
    private static ComplexDataGenerator dg = new ComplexDataGenerator();

    @Test
    public void testGenericObjectArray() throws IOException {
        GenericObject[] gpArr = dg.generateGenericObjectArray();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(gpArr);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Object[] pArr = (Object[]) hin.readObject();

        assertEquals(gpArr.length, pArr.length);
        cmpGPersonEqualPerson(gpArr[0], (Person) pArr[0]);
        cmpGPersonEqualPerson(gpArr[1], (Person) pArr[1]);
        cmpGPersonEqualPerson(gpArr[2], (Person) pArr[2]);
    }

    @Test
    public void testGenericArray() throws IOException {
        GenericArray ga = dg.generateGenericArray();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(ga);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        Person[] pArr = (Person[]) hin.readObject();

        assertEquals(ga.getLength(), pArr.length);
        cmpGPersonEqualPerson((GenericObject) ga.get(0), (Person) pArr[0]);
        cmpGPersonEqualPerson((GenericObject) ga.get(1), (Person) pArr[1]);
        cmpGPersonEqualPerson((GenericObject) ga.get(2), (Person) pArr[2]);

    }

    @Test
    public void testGenericArrayDes() throws IOException {
        GenericArray ga = dg.generateGenericArray();
        ComplexDataGenerator dg = new ComplexDataGenerator();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(ga);
        hout.flush();
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        GenericArray pArr = (GenericArray) hin.readObject();

        assertEquals(ga.getLength(), pArr.getLength());
        assertEquals("[" + Person.class.getName(), pArr.getType());
        cmpGPersonEqualPerson((GenericObject) pArr.get(0), (Person) dg.generatePerson_4());
        cmpGPersonEqualPerson((GenericObject) pArr.get(1), (Person) dg.generatePerson_5());
        cmpGPersonEqualPerson((GenericObject) pArr.get(2), (Person) dg.generatePerson_6());

    }

    @Test
    public void testArrayBean() throws IOException {
        ArrayBean ab1 = createArrayBean();

        GenericObject go = GenericUtils.convertToGenericObject(ab1);
        assertGenericArrayBean(go);

        ArrayBean ab2 = GenericUtils.convertToObject(go);
        assertArrayBean(ab2);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new SerializerFactory());
        hout.writeObject(ab1);
        hout.close();
        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        GenericObject o = (GenericObject) hin.readObject();
        assertGenericArrayBean(o);

        bout = new ByteArrayOutputStream();
        hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(go);
        hout.close();
        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());

        ArrayBean a = (ArrayBean) hin.readObject();
        assertArrayBean(a);
    }

    private void assertGenericArrayBean(GenericObject genericObject) {
        assertEquals(ArrayBean.class.getName(), genericObject.getType());
        assertEquals(int[].class.getName(), genericObject.getField("int1").getClass().getName());
        assertEquals(3, ((int[]) genericObject.getField("int1")).length);
        assertEquals(1, ((int[]) genericObject.getField("int1"))[0]);
        assertEquals(2, ((int[]) genericObject.getField("int1"))[1]);
        assertEquals(3, ((int[]) genericObject.getField("int1"))[2]);

        assertEquals(int[][].class.getName(), genericObject.getField("int2").getClass().getName());
        assertEquals(2, ((int[][]) genericObject.getField("int2")).length);
        assertEquals(1, ((int[][]) genericObject.getField("int2"))[0][0]);
        assertEquals(2, ((int[][]) genericObject.getField("int2"))[0][1]);
        assertEquals(3, ((int[][]) genericObject.getField("int2"))[0][2]);
        assertEquals(7, ((int[][]) genericObject.getField("int2"))[1][0]);
        assertEquals(2, ((int[][]) genericObject.getField("int2"))[1].length);
        assertEquals(8, ((int[][]) genericObject.getField("int2"))[1][1]);

        assertEquals(String[].class.getName(), genericObject.getField("str1").getClass().getName());
        assertEquals(3, ((String[]) genericObject.getField("str1")).length);
        assertEquals("11", ((String[]) genericObject.getField("str1"))[0]);
        assertEquals("22", ((String[]) genericObject.getField("str1"))[1]);
        assertEquals(null, ((String[]) genericObject.getField("str1"))[2]);

        assertEquals(String[][].class.getName(), genericObject.getField("str2").getClass()
            .getName());
        assertEquals(2, ((String[][]) genericObject.getField("str2")).length);
        assertEquals("33", ((String[][]) genericObject.getField("str2"))[0][0]);
        assertEquals("44", ((String[][]) genericObject.getField("str2"))[0][1]);
        assertEquals(null, ((String[][]) genericObject.getField("str2"))[0][2]);
        assertEquals("11", ((String[][]) genericObject.getField("str2"))[1][0]);

        assertEquals(String[][][].class.getName(), genericObject.getField("str3").getClass()
            .getName());
        assertEquals(2, ((String[][][]) genericObject.getField("str3")).length);
        assertEquals("33", ((String[][][]) genericObject.getField("str3"))[1][0][0]);
        assertEquals("44", ((String[][][]) genericObject.getField("str3"))[1][0][1]);
        assertEquals(null, ((String[][][]) genericObject.getField("str3"))[1][0][2]);
        assertEquals("11", ((String[][][]) genericObject.getField("str3"))[1][1][0]);

        assertEquals(GenericArray.class.getName(), genericObject.getField("simplePerson1")
            .getClass().getName());
        GenericObject go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "wang");
        go.putField("job", "coder");
        assertEquals(go, ((GenericArray) genericObject.getField("simplePerson1")).getObjects()[0]);
        assertEquals(1,
            ((GenericArray) genericObject.getField("simplePerson1")).getObjects().length);

        assertEquals(GenericArray.class.getName(), genericObject.getField("simplePerson2")
            .getClass().getName());
        assertEquals(2,
            ((GenericArray) genericObject.getField("simplePerson2")).getObjects().length);
        assertEquals(GenericArray.class,
            ((GenericArray) genericObject.getField("simplePerson2")).getObjects()[1].getClass());
        assertEquals(null, ((GenericArray) ((GenericArray) genericObject.getField("simplePerson2"))
            .getObjects()[1]).getObjects()[0]);
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "li");
        go.putField("job", "coder");
        assertEquals(go, ((GenericArray) ((GenericArray) genericObject.getField("simplePerson2"))
            .getObjects()[0]).getObjects()[0]);

        assertEquals(GenericArray.class.getName(), genericObject.getField("simplePerson3")
            .getClass().getName());
        assertEquals(2,
            ((GenericArray) genericObject.getField("simplePerson3")).getObjects().length);
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "zhao");
        go.putField("job", "coder");
        assertEquals(go, ((GenericArray) ((GenericArray) ((GenericArray) genericObject
            .getField("simplePerson3")).getObjects()[1]).getObjects()[0]).getObjects()[0]);

        assertEquals(GenericArray.class.getName(), genericObject.getField("obj1").getClass()
            .getName());
        assertEquals(1, ((GenericArray) genericObject.getField("obj1")).getObjects()[0]);
        assertEquals("11", ((GenericArray) genericObject.getField("obj1")).getObjects()[1]);
        assertEquals(null, ((GenericArray) genericObject.getField("obj1")).getObjects()[2]);

        assertEquals(GenericArray.class.getName(), genericObject.getField("obj2").getClass()
            .getName());
        assertEquals("22",
            ((GenericArray) ((GenericArray) genericObject.getField("obj2")).getObjects()[1])
                .getObjects()[0]);
        assertEquals(2,
            ((GenericArray) ((GenericArray) genericObject.getField("obj2")).getObjects()[1])
                .getObjects()[1]);
        go = new GenericObject(SimplePerson.class.getName());
        go.putField("name", "qian");
        go.putField("job", "coder");
        assertEquals(go,
            ((GenericArray) ((GenericArray) genericObject.getField("obj2")).getObjects()[1])
                .getObjects()[2]);

        assertEquals(GenericArray.class.getName(), genericObject.getField("obj3").getClass()
            .getName());
        assertEquals(go,
            ((GenericArray) ((GenericArray) ((GenericArray) genericObject.getField("obj3"))
                .getObjects()[0]).getObjects()[1]).getObjects()[2]);
        assertEquals("11",
            ((GenericArray) ((GenericArray) ((GenericArray) genericObject.getField("obj3"))
                .getObjects()[0]).getObjects()[0]).getObjects()[0]);
        assertEquals(1,
            ((GenericArray) ((GenericArray) ((GenericArray) genericObject.getField("obj3"))
                .getObjects()[0]).getObjects()[0]).getObjects()[1]);
        assertEquals(null,
            ((GenericArray) ((GenericArray) ((GenericArray) genericObject.getField("obj3"))
                .getObjects()[0]).getObjects()[0]).getObjects()[2]);
        assertEquals("22",
            ((GenericArray) ((GenericArray) ((GenericArray) genericObject.getField("obj3"))
                .getObjects()[0]).getObjects()[1]).getObjects()[0]);
        assertEquals(2,
            ((GenericArray) ((GenericArray) ((GenericArray) genericObject.getField("obj3"))
                .getObjects()[0]).getObjects()[1]).getObjects()[1]);
        assertEquals(null,
            ((GenericArray) ((GenericArray) ((GenericArray) genericObject.getField("obj3"))
                .getObjects()[1]).getObjects()[0]).getObjects()[0]);
    }

    private void assertArrayBean(ArrayBean arrayBean) {
        assertEquals(1, arrayBean.getInt1()[0]);
        assertEquals(2, arrayBean.getInt1()[1]);
        assertEquals(3, arrayBean.getInt1()[2]);
        assertEquals(3, arrayBean.getInt1().length);

        assertEquals(2, arrayBean.getInt2().length);
        assertEquals(1, arrayBean.getInt2()[0][0]);
        assertEquals(2, arrayBean.getInt2()[0][1]);
        assertEquals(3, arrayBean.getInt2()[0][2]);
        assertEquals(7, arrayBean.getInt2()[1][0]);
        assertEquals(8, arrayBean.getInt2()[1][1]);

        assertEquals("11", arrayBean.getStr2()[1][0]);
        assertEquals("33", arrayBean.getStr2()[0][0]);
        assertEquals("44", arrayBean.getStr2()[0][1]);
        assertEquals(null, arrayBean.getStr2()[0][2]);

        assertEquals(2, arrayBean.getStr3().length);
        assertEquals("33", arrayBean.getStr3()[1][0][0]);
        assertEquals("44", arrayBean.getStr3()[1][0][1]);
        assertEquals(null, arrayBean.getStr3()[1][0][2]);
        assertEquals("11", arrayBean.getStr3()[1][1][0]);

        assertEquals(new SimplePerson("wang", "coder"), arrayBean.getSimplePerson1()[0]);
        assertEquals(new SimplePerson("li", "coder"), arrayBean.getSimplePerson2()[0][0]);
        assertEquals(null, arrayBean.getSimplePerson2()[1][0]);
        assertEquals(null, arrayBean.getSimplePerson3()[0][1][0]);
        assertEquals(new SimplePerson("li", "coder"), arrayBean.getSimplePerson3()[0][0][0]);
        assertEquals(new SimplePerson("zhao", "coder"), arrayBean.getSimplePerson3()[1][0][0]);

        assertEquals(1, arrayBean.getObj1()[0]);
        assertEquals("11", arrayBean.getObj1()[1]);
        assertEquals(null, arrayBean.getObj1()[2]);
        assertEquals("11", arrayBean.getObj2()[0][0]);
        assertEquals(1, arrayBean.getObj2()[0][1]);
        assertEquals(null, arrayBean.getObj2()[0][2]);
        assertEquals("22", arrayBean.getObj2()[1][0]);
        assertEquals(2, arrayBean.getObj2()[1][1]);
        assertEquals(new SimplePerson("qian", "coder"), arrayBean.getObj2()[1][2]);

        assertEquals("11", arrayBean.getObj3()[0][0][0]);
        assertEquals(1, arrayBean.getObj3()[0][0][1]);
        assertEquals(null, arrayBean.getObj3()[0][0][2]);

        assertEquals("22", arrayBean.getObj3()[0][1][0]);
        assertEquals(2, arrayBean.getObj3()[0][1][1]);
        assertEquals(new SimplePerson("qian", "coder"), arrayBean.getObj3()[0][1][2]);
        assertEquals(null, arrayBean.getObj3()[1][0][0]);
        assertEquals(1, arrayBean.getObj3()[1][0][1]);
    }

    private ArrayBean createArrayBean() {
        ArrayBean arrayBean = new ArrayBean();

        arrayBean.setInt1(new int[] { 1, 2, 3 });
        arrayBean.setInt2(new int[][] { { 1, 2, 3 }, { 7, 8 } });

        arrayBean.setStr1(new String[] { "11", "22", null });
        arrayBean.setStr2(new String[][] { { "33", "44", null }, { "11" } });
        arrayBean
            .setStr3(new String[][][] { { { null, "11" } }, { { "33", "44", null }, { "11" } } });

        arrayBean.setSimplePerson1(new SimplePerson[] { new SimplePerson("wang", "coder") });
        arrayBean.setSimplePerson2(new SimplePerson[][] { { new SimplePerson("li", "coder") },
                { null } });
        arrayBean.setSimplePerson3(new SimplePerson[][][] {
                { { new SimplePerson("li", "coder") }, { null } },
                { { new SimplePerson("zhao", "coder") } } });

        arrayBean.setObj1(new Object[] { 1, "11", null });
        arrayBean.setObj2(new Object[][] { { "11", 1, null },
                { "22", 2, new SimplePerson("qian", "coder") } });
        arrayBean.setObj3(new Object[][][] {
                { { "11", 1, null }, { "22", 2, new SimplePerson("qian", "coder") } },
                { { null, 1 } } });

        return arrayBean;
    }

}
