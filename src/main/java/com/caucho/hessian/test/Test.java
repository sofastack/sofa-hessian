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
    void nullCall();

    /**
     * Hello, World.
     */
    String hello();

    /**
     * Subtraction
     */
    int subtract(int a, int b);

    /**
     * Echos the object to the server.
     * <pre>
     */
    Object echo(Object value);

    /**
     * Throws an application fault.
     */
    void fault()
        throws IOException;
}
