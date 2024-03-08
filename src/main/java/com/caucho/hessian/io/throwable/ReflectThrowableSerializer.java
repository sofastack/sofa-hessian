/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io.throwable;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.JavaSerializer;

import java.io.IOException;

/**
 * use under jdk < 17
 * @author junyuan
 * @version ReflectThrowableSerializer.java, v 0.1 2023年05月06日 10:46 junyuan Exp $
 */
public class ReflectThrowableSerializer extends JavaSerializer {
    public ReflectThrowableSerializer(Class cl) {
        super(cl);
    }

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        // 如果需要反射操作获取 stack trace, 这里需要先 get 一下
        ((Throwable) obj).getStackTrace();
        super.writeObject(obj, out);
    }
}
