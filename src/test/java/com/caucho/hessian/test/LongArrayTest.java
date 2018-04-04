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
package com.caucho.hessian.test;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by qiwei.lqw on 2016/8/24.
 */
public class LongArrayTest {

    @Test
    public void oneArray() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output h2out = new Hessian2Output(bout);

        Map map = new HashMap();
        map.put("1", new long[] { 1L, 2L });

        h2out.writeObject(map);

        h2out.flush();
        byte[] body = bout.toByteArray();

        ByteArrayInputStream input = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(input);

        Map copy = (Map) hin.readObject();

        long[] a1 = (long[]) map.get("1");
        long[] a2 = (long[]) copy.get("1");
        assertEquals(a1.length, a2.length);
        for (int i = 0; i < a1.length; ++i)
            assertEquals(a1[i], a2[i]);
    }

    @Test
    public void twoArray() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output h2out = new Hessian2Output(bout);

        Map map = new HashMap();
        map.put("1", new long[] { 1L, 2L });
        map.put("2", new long[] { 3L, 4L });

        h2out.writeObject(map);

        h2out.flush();
        byte[] body = bout.toByteArray();

        ByteArrayInputStream input = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(input);

        Map copy = (Map) hin.readObject();

        long[] a1 = (long[]) map.get("1");
        long[] a2 = (long[]) copy.get("1");
        assertEquals(a1.length, a2.length);
        for (int i = 0; i < a1.length; ++i)
            assertEquals(a1[i], a2[i]);

        a1 = (long[]) map.get("2");
        a2 = (long[]) copy.get("2");
        assertEquals(a1.length, a2.length);
        for (int i = 0; i < a1.length; ++i)
            assertEquals(a1[i], a2[i]);
    }

    @Test
    public void threeArray() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output h2out = new Hessian2Output(bout);

        Map map = new HashMap();
        map.put("1", new long[] { 1L, 2L });
        map.put("2", new long[] { 3L, 4L, 5L });
        map.put("3", new long[] { 3L, 4L, 5L, 6L });

        h2out.writeObject(map);

        h2out.flush();
        byte[] body = bout.toByteArray();

        ByteArrayInputStream input = new ByteArrayInputStream(body, 0, body.length);
        Hessian2Input hin = new Hessian2Input(input);

        Map copy = (Map) hin.readObject();

        long[] a1 = (long[]) map.get("1");
        long[] a2 = (long[]) copy.get("1");
        assertEquals(a1.length, a2.length);
        for (int i = 0; i < a1.length; ++i)
            assertEquals(a1[i], a2[i]);

        a1 = (long[]) map.get("2");
        a2 = (long[]) copy.get("2");
        assertEquals(a1.length, a2.length);
        for (int i = 0; i < a1.length; ++i)
            assertEquals(a1[i], a2[i]);

        a1 = (long[]) map.get("3");
        a2 = (long[]) copy.get("3");
        assertEquals(a1.length, a2.length);
        for (int i = 0; i < a1.length; ++i)
            assertEquals(a1[i], a2[i]);
    }
}