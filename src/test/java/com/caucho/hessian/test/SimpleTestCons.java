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

import java.util.HashMap;

/**
 * Cons-cell for testing
 */
public class SimpleTestCons implements java.io.Serializable {
    private Object _first;
    private Object _rest;

    public SimpleTestCons() {
    }

    public SimpleTestCons(Object first) {
        _first = first;
    }

    public SimpleTestCons(Object first, Object rest) {
        _first = first;
        _rest = rest;
    }

    public Object getFirst() {
        return _first;
    }

    public void setFirst(Object first) {
        _first = first;
    }

    public Object getRest() {
        return _rest;
    }

    public void setRest(Object rest) {
        _rest = rest;
    }

    public boolean equals(Object o) {
        return toString().equals(o.toString());
    }

    public String toString() {
        return toString(new HashMap());
    }

    public String toString(HashMap map) {
        Object ref = map.get(this);

        if (ref != null)
            return "#" + ref;

        map.put(this, map.size());

        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("[");

        if (_first instanceof SimpleTestCons)
            sb.append(((SimpleTestCons) _first).toString(map));
        else
            sb.append(_first);

        sb.append(",");

        if (_rest instanceof SimpleTestCons)
            sb.append(((SimpleTestCons) _rest).toString(map));
        else
            sb.append(_rest);

        sb.append("[");

        return sb.toString();
    }
}
