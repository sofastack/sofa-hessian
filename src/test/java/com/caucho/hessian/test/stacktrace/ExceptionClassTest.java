/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.test.stacktrace;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author junyuan
 * @version ExceptionClassTest.java, v 0.1 2023年04月04日 15:38 junyuan Exp $
 */
public class ExceptionClassTest {

    private static SerializerFactory     factory;
    private static ByteArrayOutputStream os;

    @BeforeClass
    public static void setUp() {
        factory = new SerializerFactory();
        os = new ByteArrayOutputStream();
    }
    
    @Test
    public void test_stacktrace() throws IOException {
        Throwable t = null;
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            t = e;
        }
        
        ExceptionWrapper w = new ExceptionWrapper();
        w.setT(t);

        Object result = doEncodeNDecode(w);

        
    }

    protected Object doEncodeNDecode(Object origin) throws IOException {
        os.reset();
        Hessian2Output output = new Hessian2Output(os);

        output.setSerializerFactory(factory);
        output.writeObject(origin);
        output.flush();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(factory);
        Object actual = input.readObject();
        return actual;
    }

}