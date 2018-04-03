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

package com.caucho.hessian.server;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.services.server.AbstractSkeleton;
import com.caucho.services.server.ServiceContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proxy class for Hessian services.
 */
public class HessianSkeleton extends AbstractSkeleton {
  private static final Logger
    log = Logger.getLogger(HessianSkeleton.class.getName());
  
  private Object _service;
  
  /**
   * Create a new hessian skeleton.
   *
   * @param service the underlying service object.
   * @param apiClass the API interface
   */
  public HessianSkeleton(Object service, Class apiClass)
  {
    super(apiClass);

    if (service == null)
      service = this;

    _service = service;

    if (! apiClass.isAssignableFrom(service.getClass()))
      throw new IllegalArgumentException("Service " + service + " must be an instance of " + apiClass.getName());
  }

  /**
   * Invoke the object with the request from the input stream.
   *
   * @param in the Hessian input stream
   * @param out the Hessian output stream
   */
  public void invoke(AbstractHessianInput in, AbstractHessianOutput out)
    throws Throwable
  {
    ServiceContext context = ServiceContext.getContext();

    // backward compatibility for some frameworks that don't read
    // the call type first
    in.skipOptionalCall();
    
    String header;
    while ((header = in.readHeader()) != null) {
      Object value = in.readObject();

      context.addHeader(header, value);
    }

    String methodName = in.readMethod();
    Method method = getMethod(methodName);

    if (method != null) {
    }
    else if ("_hessian_getAttribute".equals(methodName)) {
      String attrName = in.readString();
      in.completeCall();

      String value = null;

      if ("java.api.class".equals(attrName))
	value = getAPIClassName();
      else if ("java.home.class".equals(attrName))
	value = getHomeClassName();
      else if ("java.object.class".equals(attrName))
	value = getObjectClassName();

      out.startReply();

      out.writeObject(value);

      out.completeReply();
      return;
    }
    else if (method == null) {
      out.startReply();
      out.writeFault("NoSuchMethodException",
		     "The service has no method named: " + in.getMethod(),
		     null);
      out.completeReply();
      return;
    }

    Class []args = method.getParameterTypes();
    Object []values = new Object[args.length];

    for (int i = 0; i < args.length; i++) {
      values[i] = in.readObject(args[i]);
    }

    Object result = null;
    
    try {
      result = method.invoke(_service, values);
    } catch (Throwable e) {
      if (e instanceof InvocationTargetException)
        e = ((InvocationTargetException) e).getTargetException();

      log.log(Level.WARNING, e.toString(), e);
      
      out.startReply();
      out.writeFault("ServiceException", e.getMessage(), e);
      out.completeReply();
      return;
    }

    // The complete call needs to be after the invoke to handle a
    // trailing InputStream
    in.completeCall();
    
    out.startReply();

    out.writeObject(result);
    
    out.completeReply();
    out.close();
  }
}
