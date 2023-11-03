/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author junyuan
 * @version TestExtendSerializeFactory.java, v 0.1 2023年11月02日 18:22 junyuan Exp $
 */
public class TestExtendSerializeFactory {

    @Test
    public void test_ExtendSerializeFactory() {
        ExtendSerializeFactory factory = new ExtendSerializeFactory();

        ConcurrentMap desMap = factory.getDesStatic();
        Assert.assertTrue(desMap.size() > 0);
    }
}