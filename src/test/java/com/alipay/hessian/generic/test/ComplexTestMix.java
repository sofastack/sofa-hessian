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

import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.core.response.SofaResponse;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.alipay.hessian.generic.test.ComplexTestGO2GO.cmpGPersonEqualPerson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Created by qiwei.lqw on 2016/7/8.
 */
public class ComplexTestMix {
    private static ComplexDataGenerator dg = new ComplexDataGenerator();

    //check deserialize same byte stream with both SerializeFactory and GenericSerializerFactory
    // Not Supported
    // @Test
    public void testGOGO2GOO() throws IOException {
        Object obj1 = dg.generateMixGenericObject();
        Object obj2 = dg.generateMixGenericObject();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());

        hout.writeObject(obj1);
        hout.writeObject(obj2);

        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);

        hin.setSerializerFactory(new SerializerFactory());
        obj1 = hin.readObject();

        hin.setSerializerFactory(new GenericSerializerFactory());
        obj2 = hin.readObject();

        hin.close();

        Object[][] arr = (Object[][]) obj1;
        Object[][] garr = (Object[][]) obj2;

        assertEquals(garr.length, 3);
        assertEquals(garr[0].length, 3);
        assertEquals(arr.length, 3);
        assertEquals(arr[0].length, 3);

        assertSame(garr, garr[2][0]);
        assertSame(arr, arr[2][0]);
        cmpGPersonEqualPerson((GenericObject) garr[2][1], (Person) arr[2][1]);
        cmpGPersonEqualPerson((GenericObject) garr[2][2], (Person) arr[2][2]);

        Object[] garr1 = (Object[]) garr[1][0];
        Map gmap1 = (Map) garr[1][1];
        List glist1 = (List) garr[1][2];

        Object[] arr1 = (Object[]) arr[1][0];
        Map map1 = (Map) arr[1][1];
        List list1 = (List) arr[1][2];

        //check the circle reference for generic  hessian
        Object[] garr2 = (Object[]) garr1[2];
        assertSame(garr2[2], gmap1);
        Map gmap2 = (Map) gmap1.get(3);
        assertSame(gmap2.get("www"), glist1);
        List glist2 = (List) glist1.get(2);
        assertSame(glist2.get(2), garr1);

        //check the circle reference for hessian
        Object[] arr2 = (Object[]) arr1[2];
        assertSame(arr2[2], map1);
        Map map2 = (Map) map1.get(3);
        assertSame(map2.get("www"), list1);
        List list2 = (List) list1.get(2);
        assertSame(list2.get(2), arr1);

        cmpGPersonEqualPerson((GenericObject) garr1[0], (Person) arr1[0]);
        //check the same object for generic hessian
        assertSame(garr1[0], garr2[0]);
        assertSame(garr1[0], gmap1.get(1));
        assertSame(garr1[0], gmap2.get("lll"));
        assertSame(garr1[0], glist1.get(0));
        assertSame(garr1[0], glist2.get(0));
        //check the same object for hessian
        assertSame(arr1[0], arr2[0]);
        assertSame(arr1[0], map1.get(1));
        assertSame(arr1[0], map2.get("lll"));
        assertSame(arr1[0], list1.get(0));
        assertSame(arr1[0], list2.get(0));

        cmpGPersonEqualPerson((GenericObject) garr1[1], (Person) arr1[1]);
        //check the same object for generic hessian
        assertSame(garr1[1], garr2[1]);
        assertSame(garr1[1], gmap1.get(2));
        assertSame(garr1[1], gmap2.get("qqq"));
        assertSame(garr1[1], glist1.get(1));
        assertSame(garr1[1], glist2.get(1));
        //check the same object for hessian
        assertSame(arr1[1], arr2[1]);
        assertSame(arr1[1], map1.get(2));
        assertSame(arr1[1], map2.get("qqq"));
        assertSame(arr1[1], list1.get(1));
        assertSame(arr1[1], list2.get(1));
    }

    @Test
    public void testSofaRequest() throws IOException {
        SofaRequest request = dg.generateSofaRequest();

        // serialization uses GenericHessian
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(byteArray);
        output.setSerializerFactory(new GenericSerializerFactory());
        output.writeObject(request);

        final Object[] args = request.getMethodArgs();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                output.writeObject(args[i]);
            }
        }
        output.close();
        byteArray.close();

        // deserialization uses Hessian
        byte[] body = byteArray.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);

        hin.setSerializerFactory(new SerializerFactory());

        SofaRequest deRequest = (SofaRequest) hin.readObject();
        String[] sig = deRequest.getMethodArgSigs();
        Class<?>[] classSig = new Class[sig.length];

        final Object[] deArgs = new Object[sig.length];
        for (int i = 0; i < deRequest.getMethodArgSigs().length; ++i) {
            deArgs[i] = hin.readObject(classSig[i]);
        }
        deRequest.setMethodArgs(deArgs);

        bin.close();
        hin.close();

        assertEquals(request.getTargetServiceUniqueName(), deRequest.getTargetServiceUniqueName());
        assertEquals(request.getMethodName(), deRequest.getMethodName());
        assertEquals(request.getTargetAppName(), deRequest.getTargetAppName());
        assertEquals(request.getRequestProps(), deRequest.getRequestProps());

        // 1st argument is a Person, and 2nd argument is an int
        cmpGPersonEqualPerson((GenericObject) request.getMethodArgs()[0],
            (Person) deRequest.getMethodArgs()[0]);
        assertEquals(request.getMethodArgs()[1], deRequest.getMethodArgs()[1]);

    }

    @Test
    public void testSofaResponse() throws IOException {
        SofaResponse response = dg.generateSofaResponse();

        // serialization uses Hessian
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(byteArray);
        output.setSerializerFactory(new SerializerFactory());
        output.writeObject(response);

        output.close();
        byteArray.close();

        byte[] body = byteArray.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);

        hin.setSerializerFactory(new GenericSerializerFactory());

        GenericObject deResponse = (GenericObject) hin.readObject();

        bin.close();
        hin.close();

        assertEquals(response.getErrorMsg(), deResponse.getField("errorMsg"));
        assertEquals(response.isError(), deResponse.getField("isError"));
        cmpGPersonEqualPerson((GenericObject) deResponse.getField("appResponse"),
            (Person) response.getAppResponse());

    }
}
