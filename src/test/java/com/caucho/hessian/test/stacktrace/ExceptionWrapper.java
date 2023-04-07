/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.test.stacktrace;

import java.io.Serializable;

/**
 *
 * @author junyuan
 * @version ExceptionWrapper.java, v 0.1 2023年04月04日 15:40 junyuan Exp $
 */
public class ExceptionWrapper implements Serializable {
    private static final long serialVersionUID = 4065571790594438646L;

    Throwable t;

    /**
     * Getter method for property <tt>t</tt>.
     *
     * @return property value of t
     */
    public Throwable getT() {
        return t;
    }

    /**
     * Setter method for property <tt>t</tt>.
     *
     * @param t  value to be assigned to property t
     */
    public void setT(Throwable t) {
        this.t = t;
    }
}