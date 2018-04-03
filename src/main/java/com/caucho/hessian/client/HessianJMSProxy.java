/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2004 Caucho Technology, Inc.  All rights reserved.
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
 * @author Emil Ong
 */

package com.caucho.hessian.client;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.jms.util.BytesMessageOutputStream;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

/**
 * Proxy implementation for Hessian clients using JMS.  Applications will 
 * generally use HessianProxyFactory to create proxy clients.
 */
public class HessianJMSProxy implements InvocationHandler {
  protected static Logger log
    = Logger.getLogger(HessianJMSProxy.class.getName());

  private HessianProxyFactory _factory;

  private MessageProducer _producer;
  private Session _jmsSession;
  private Connection _jmsConnection;
  private String _outboundName;
 
  HessianJMSProxy(HessianProxyFactory factory, 
                  String outboundName, String connectionFactoryName)
    throws NamingException, JMSException
  {
    _factory = factory;
    _outboundName = outboundName;

    Context context = (Context) new InitialContext().lookup("java:comp/env");

    ConnectionFactory connectionFactory = 
      (ConnectionFactory) context.lookup(connectionFactoryName);

    Destination outboundDestination = 
      (Destination) context.lookup(outboundName);

    _jmsConnection = connectionFactory.createConnection();
    _jmsSession = 
        _jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    _producer = _jmsSession.createProducer(outboundDestination);
  }

  public String getOutboundName()
  {
    return _outboundName;
  }

  /**
   * Handles the object invocation.
   *
   * @param proxy the proxy object to invoke
   * @param method the method to call
   * @param args the arguments to the proxy object
   */
  public Object invoke(Object proxy, Method method, Object []args)
    throws Throwable
  {
    String methodName = method.getName();
    Class []params = method.getParameterTypes();

    // equals and hashCode are special cased
    if (methodName.equals("equals") &&
        params.length == 1 && params[0].equals(Object.class)) {
      Object value = args[0];
      if (value == null || ! Proxy.isProxyClass(value.getClass()))
        return new Boolean(false);

      InvocationHandler handler = Proxy.getInvocationHandler(value);

      if (! (handler instanceof HessianJMSProxy))
        return new Boolean(false);

      String otherOutboundName = ((HessianJMSProxy) handler).getOutboundName();

      return new Boolean(_outboundName.equals(otherOutboundName));
    }
    else if (methodName.equals("hashCode") && params.length == 0)
      return new Integer(_outboundName.hashCode());
    else if (methodName.equals("getHessianType"))
      return proxy.getClass().getInterfaces()[0].getName();
    else if (methodName.equals("getHessianURL"))
      return _outboundName;
    else if (methodName.equals("toString") && params.length == 0)
      return "[HessianJMSProxy " + _outboundName + "]";

    try {
      if (! _factory.isOverloadEnabled()) {
      }
      else if (args != null)
        methodName = methodName + "__" + args.length;
      else
        methodName = methodName + "__0";

      sendRequest(methodName, args);

      return null;
    } catch (Exception e) {
      throw new HessianRuntimeException(e);
    }
  }

  private void sendRequest(String methodName, Object []args)
    throws JMSException, IOException
  {
    BytesMessage message = _jmsSession.createBytesMessage();

    BytesMessageOutputStream os = new BytesMessageOutputStream(message);

    AbstractHessianOutput out = _factory.getHessianOutput(os);

    out.call(methodName, args);
    out.flush();

    _producer.send(message);
  }
}
