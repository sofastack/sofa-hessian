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
package com.caucho.hessian.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Hessian2InputTest {

    SerializerFactory serializerFactory = new SerializerFactory();

    int[]             sizeList          = new int[] {
                                        0, 2, 16, 512, 1024,
                                        0x8000, 0x8000 + 1,
                                        64 * 1024, 64 * 1024 * 1024
                                        };

    @Test
    public void testSerialize() {
        for (int i = 0; i < sizeList.length; i++) {
            byte[] originBytes = generateLargeByteArray(sizeList[i]);
            byte[] encodeBytes = hessianEncodeByte(originBytes);
            byte[] decodeBytes = hessianDecodeByte(encodeBytes);
            Assert.assertArrayEquals(originBytes, decodeBytes);
        }
    }

    @Test
    public void testSerializeTime() {
        byte[] originBytes = generateLargeByteArray(1024 * 1024);
        byte[] encodeBytes = hessianEncodeByte(originBytes);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            hessianDecodeByte(encodeBytes);
        }
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start));
    }

    private static byte[] generateLargeByteArray(int size) {
        byte[] largeArray = new byte[size];
        for (int i = 0; i < size; i++) {
            largeArray[i] = (byte) (i % 256);
        }
        return largeArray;
    }

    private byte[] hessianEncodeByte(byte[] bytes) {
        serializerFactory.setAllowNonSerializable(true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(serializerFactory);
        try {
            output.writeObject(bytes);
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return os.toByteArray();
    }

    private byte[] hessianDecodeByte(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Hessian2Input input = new Hessian2Input(bis);
        input.setSerializerFactory(serializerFactory);
        byte[] decodeObject;
        try {
            decodeObject = (byte[]) input.readObject();
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return decodeObject;
    }

}