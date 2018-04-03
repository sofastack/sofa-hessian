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

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author xuanbei
 * @since 2016/12/31
 */
public class SpecialBean implements Serializable {
    Calendar calendar;

    Class    clazz;

    public SpecialBean(Calendar calendar, Class clazz) {
        this.calendar = calendar;
        this.clazz = clazz;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SpecialBean that = (SpecialBean) o;

        if (calendar != null ? !calendar.equals(that.calendar) : that.calendar != null)
            return false;
        return clazz != null ? clazz.equals(that.clazz) : that.clazz == null;

    }

    @Override
    public int hashCode() {
        int result = calendar != null ? calendar.hashCode() : 0;
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        return result;
    }
}
