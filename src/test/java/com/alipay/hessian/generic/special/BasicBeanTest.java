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

import com.alipay.hessian.generic.bean.BasicBean;
import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.GenericUtils;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author xuanbei
 * @since 2017/01/04
 */
public class BasicBeanTest {

    @Test
    public void testAll() throws Exception {
        BasicBean bb = new BasicBean((short) 12, new Short((short) 32), 21, new Integer(43),
            (byte) 12, new Byte((byte) 13), 1274646l, 873763l, (float) 1456.9877,
            (float) 1456.9877, 82837.93883, 82837.88, true, false);
        Object go = GenericUtils.convertToGenericObject(bb);
        assertGenericBasicBean(go);
        BasicBean basicBean = GenericUtils.convertToObject(go);
        assertEquals(basicBean, bb);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output hout = new Hessian2Output(bout);
        hout.setSerializerFactory(new GenericSerializerFactory());
        hout.writeObject(go);
        hout.close();

        byte[] body = bout.toByteArray();
        ByteArrayInputStream bin = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new GenericSerializerFactory());

        Object o1 = hin.readObject();
        basicBean = GenericUtils.convertToObject(o1);
        assertEquals(basicBean, bb);

        body = bout.toByteArray();
        bin = new ByteArrayInputStream(body, 0, body.length);
        hin = new Hessian2Input(bin);
        hin.setSerializerFactory(new SerializerFactory());
        Object o2 = hin.readObject();
        assertEquals(o2, bb);
        go = GenericUtils.convertToGenericObject(o2);
        assertGenericBasicBean(go);
        basicBean = GenericUtils.convertToObject(go);
        assertEquals(basicBean, bb);
    }

    private void assertGenericBasicBean(Object go) {
        assertEquals(go.getClass(), GenericObject.class);
        GenericObject genericObject = (GenericObject) go;
        assertEquals(BasicBean.class.getName(), genericObject.getType());
        BasicBean bb = new BasicBean((short) 12, new Short((short) 32), 21, new Integer(43),
            (byte) 12, new Byte((byte) 13), 1274646l, 873763l, (float) 1456.9877,
            (float) 1456.9877, 82837.93883, 82837.88, true, false);
        assertEquals(bb.getB(), genericObject.getField("b"));
        assertEquals(bb.getBb(), genericObject.getField("bb"));
        assertEquals(bb.getS(), genericObject.getField("s"));
        assertEquals(bb.getSs(), genericObject.getField("ss"));
        assertEquals(bb.getF(), genericObject.getField("f"));
        assertEquals(bb.getFf(), genericObject.getField("ff"));
        assertEquals(bb.getD(), genericObject.getField("d"));
        assertEquals(bb.getDd(), genericObject.getField("dd"));
        assertEquals(bb.getL(), genericObject.getField("l"));
        assertEquals(bb.getLl(), genericObject.getField("ll"));
        assertEquals(bb.getI(), genericObject.getField("i"));
        assertEquals(bb.getIi(), genericObject.getField("ii"));
        assertEquals(bb.isBo(), true);
        assertEquals(bb.getBbo(), false);
    }
}
