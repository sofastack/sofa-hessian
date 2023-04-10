/*
 * Copyright (c) 2001-2004 Caucho Technology, Inc.  All rights reserved.
 *
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Caucho Technology (http://www.caucho.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Hessian", "Resin", and "Caucho" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@caucho.com.
 *
 * 5. Products derived from this software may not be called "Resin"
 *    nor may "Resin" appear in their names without prior written
 *    permission of Caucho Technology.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL CAUCHO TECHNOLOGY OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Scott Ferguson
 */

package com.caucho.hessian.io;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deserializing a JDK 1.4 StackTraceElement
 * @author pangu
 */
public class StackTraceElementDeserializer extends AbstractDeserializer {
    protected static final Logger          log     = Logger.getLogger(StackTraceElementSerializer.class.getName());

    private Map<String, Field>             _fields = new HashMap<String, Field>();

    private Constructor<StackTraceElement> _constructor;

    @Override
    public Class getType() {
        return StackTraceElement.class;
    }

    public StackTraceElementDeserializer() {
        Class<StackTraceElement> clazz = StackTraceElement.class;
        Field[] originFields = clazz.getDeclaredFields();
        for (int i = 0; i < originFields.length; i++) {
            Field field = originFields[i];

            if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if ("declaringClassObject".equals(field.getName()) || "format".equals(field.getName())) {
                continue;
            }
            _fields.put(field.getName(), field);
        }

        try {
            if (_fields.size() == 7) {
                // available since java 9
                _constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class, String.class,
                    String.class, String.class, int.class);
            } else {
                // default, only read class, method, file and line
                _constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class, int.class);
            }
        } catch (Exception e) {
            log.log(Level.FINE, e.toString(), e);
        }

    }

    @Override
    public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException {
        try {
            int ref = in.addRef(_constructor.newInstance("", "", "", 0));
            Map<String, Object> fieldValueMap = new HashMap<String, Object>();

            for (int i = 0; i < fieldNames.length; i++) {
                String name = fieldNames[i];
                Field field = _fields.get(name);

                if (String.class.equals(field.getType())) {
                    fieldValueMap.put(name, in.readString());
                } else if (int.class.equals(field.getType())) {
                    fieldValueMap.put(name, in.readInt());
                }
            }

            StackTraceElement obj;
            if (fieldNames.length == 7) {
                obj = _constructor.newInstance(
                    fieldValueMap.get("classLoaderName"), fieldValueMap.get("moduleName"),
                    fieldValueMap.get("moduleVersion"), fieldValueMap.get("declaringClass"),
                    fieldValueMap.get("methodName"), fieldValueMap.get("fileName"),
                    fieldValueMap.get("lineNumber"));
            } else {
                obj = _constructor.newInstance(
                    fieldValueMap.get("declaringClass"), fieldValueMap.get("methodName"),
                    fieldValueMap.get("fileName"), fieldValueMap.get("lineNumber"));
            }

            in.setRef(ref, obj);

            return obj;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(StackTraceElement.class.getName() + ":" + e, e);
        }
    }
}
