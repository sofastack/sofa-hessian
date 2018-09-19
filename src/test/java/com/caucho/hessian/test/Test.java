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

import java.io.IOException;

/**
 * The Test service is a quick sanity check service.  Developers of a
 * new Hessian implementation can use this service as an initial test.
 */
public interface Test {
    /**
     * Does nothing.
     */
    public void nullCall();

    /**
     * Hello, World.
     */
    public String hello();

    /**
     * Subtraction
     */
    public int subtract(int a, int b);

    /**
     * Echos the object to the server.
     * <pre>
     */
    public Object echo(Object value);

    /**
     * Throws an application fault.
     */
    public void fault()
        throws IOException;
}
