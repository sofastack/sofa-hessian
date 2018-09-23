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

import com.caucho.hessian.server.HessianServlet;

import java.io.CharArrayWriter;
import java.io.IOException;

/**
 * The test service is a Hessian 2.0 protocol test for developers of
 * Hessian 2.0 clients.  For a new client the recommended order is:
 *
 * <ul>
 * <li>methodNull
 * <li>methodHello
 * </ul>
 */
public class TestHessianServlet
                               extends HessianServlet
                                                     implements Test
{
    private ThreadLocal<CharArrayWriter> _threadWriter = new ThreadLocal<CharArrayWriter>();

    /**
     * Does nothing.
     */
    public void nullCall()
    {
    }

    /**
     * Hello, World.
     */
    public String hello()
    {
        return "Hello, World";
    }

    /**
     * Subtraction
     */
    public int subtract(int a, int b)
    {
        return a - b;
    }

    /**
     * Echos the object to the server.
     * <pre>
     */
    public Object echo(Object value)
    {
        return value;
    }

    /**
     * Throws an application fault.
     */
    public void fault()
        throws IOException
    {
        throw new NullPointerException("sample exception");
    }
}
