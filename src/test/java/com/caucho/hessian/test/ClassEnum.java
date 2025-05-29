/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package com.caucho.hessian.test;

/**
 * @author lianglipeng.llp@alibaba-inc.com
 * @version $Id: ClassEnum.java, v 0.1 2025年05月29日 15:46 立蓬 Exp $
 */
public enum ClassEnum {
    A{
        @Override
        public void test() {
            System.out.println("A");
        }
    },
    B{
        @Override
        public void test() {
            System.out.println("B");
        }
    };
    public abstract void test();
}