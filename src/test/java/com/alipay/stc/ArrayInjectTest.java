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
package com.alipay.stc;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 通过数组等其他方式绕过安全黑名单的case
 */
public class ArrayInjectTest {

    @Test
    public void testHessian2Array() throws IOException {
        SerializerFactory factory = new SerializerFactory();
        String s = "567400075b6f626a6563746e025674001e5b636f6d2e73756e2e726f777365742e4a646263526f77536574496d706c6e014fad786f6d2e73756e2e726f777365742e4a646263526f77536574496d706cac07636f6d6d616e640355524c0a64617461536f757263650a726f77536574547970650b73686f7744656c657465640c717565727954696d656f7574076d6178526f77730c6d61784669656c6453697a650b636f6e63757272656e637908726561644f6e6c791065736361706550726f63657373696e670969736f6c6174696f6e08666574636844697209666574636853697a6504636f6e6e02707302727306726f77734d44057265734d440d694d61746368436f6c756d6e730f7374724d61746368436f6c756d6e730c62696e61727953747265616d0d756e69636f646553747265616d0b617363696953747265616d0a6368617253747265616d036d6170096c697374656e65727306706172616d736f904e4e4ecbec46909090cbf0545492cbe8904e4e4e4e4e567400106a6176612e7574696c2e566563746f726e0a8f8f8f8f8f8f8f8f8f8f7a76929a4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e7692904d7400136a6176612e7574696c2e486173687461626c657a7a4a027a";
        byte[] bs = hex2byte(s.getBytes());
        Assert.assertTrue(s.equalsIgnoreCase(byte2hex(bs)));

        ByteArrayInputStream input = new ByteArrayInputStream(bs, 0, bs.length);
        Hessian2Input hin = new Hessian2Input(input);
        hin.setSerializerFactory(factory);

        try {
            hin.readObject();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testHessian1Array() throws IOException {
        SerializerFactory factory = new SerializerFactory();
        String s = "567400075b6f626a6563746e025674001e5b636f6d2e73756e2e726f777365742e4a646263526f77536574496d706c6e014fad786f6d2e73756e2e726f777365742e4a646263526f77536574496d706cac07636f6d6d616e640355524c0a64617461536f757263650a726f77536574547970650b73686f7744656c657465640c717565727954696d656f7574076d6178526f77730c6d61784669656c6453697a650b636f6e63757272656e637908726561644f6e6c791065736361706550726f63657373696e670969736f6c6174696f6e08666574636844697209666574636853697a6504636f6e6e02707302727306726f77734d44057265734d440d694d61746368436f6c756d6e730f7374724d61746368436f6c756d6e730c62696e61727953747265616d0d756e69636f646553747265616d0b617363696953747265616d0a6368617253747265616d036d6170096c697374656e65727306706172616d736f904e4e4ecbec46909090cbf0545492cbe8904e4e4e4e4e567400106a6176612e7574696c2e566563746f726e0a8f8f8f8f8f8f8f8f8f8f7a76929a4e4e4e4e4e4e4e4e4e4e4e4e4e4e4e7692904d7400136a6176612e7574696c2e486173687461626c657a7a4a027a";
        byte[] bs = hex2byte(s.getBytes());
        Assert.assertTrue(s.equalsIgnoreCase(byte2hex(bs)));

        ByteArrayInputStream input = new ByteArrayInputStream(bs, 0, bs.length);
        HessianInput hin = new HessianInput(input);
        hin.setSerializerFactory(factory);

        try {
            hin.readObject();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IOException);
        }
    }

    private String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    /**
     * 十六转byte
     *
     * @param b
     * @return
     */
    private byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}
