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
 * 4. The names "Burlap", "Resin", and "Caucho" must not be used to
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

import com.alipay.hessian.ClassNameResolver;
import com.alipay.hessian.ClassNameResolverBuilder;
import com.caucho.burlap.io.BurlapRemoteObject;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for returning serialization methods.
 */
public class SerializerFactory extends AbstractSerializerFactory
{
  private static final Logger log
    = Logger.getLogger(SerializerFactory.class.getName());

  protected static Deserializer OBJECT_DESERIALIZER
    = new BasicDeserializer(BasicDeserializer.OBJECT);
  
  protected static ConcurrentMap _staticSerializerMap;
  protected static ConcurrentMap _staticDeserializerMap;
  protected static ConcurrentMap _staticTypeMap;

  protected Serializer _defaultSerializer;

  // Additional factories
  protected ArrayList _factories = new ArrayList();
  
  protected CollectionSerializer _collectionSerializer;

  protected Deserializer _hashMapDeserializer;
  protected ConcurrentMap _cachedSerializerMap = new ConcurrentHashMap();
  protected ConcurrentMap _cachedDeserializerMap = new ConcurrentHashMap();
  protected ConcurrentMap _cachedTypeDeserializerMap = new ConcurrentHashMap();

  protected boolean _isAllowNonSerializable;
  
  protected ClassNameResolver classNameResolver = ClassNameResolverBuilder.buildDefault();

  /**
   * Set true if the collection serializer should send the java type.
   */
  public void setSendCollectionType(boolean isSendType)
  {
    if (_collectionSerializer == null)
      _collectionSerializer = new CollectionSerializer();

    _collectionSerializer.setSendJavaType(isSendType);
  }

  /**
   * Adds a factory.
   */
  public void addFactory(AbstractSerializerFactory factory)
  {
    _factories.add(factory);
  }

  /**
   * If true, non-serializable objects are allowed.
   */
  public void setAllowNonSerializable(boolean allow)
  {
    _isAllowNonSerializable = allow;
  }

  /**
   * If true, non-serializable objects are allowed.
   */
  public boolean isAllowNonSerializable()
  {
    return _isAllowNonSerializable;
  }

  /**
   * Get ClassNameResolver
   * 
   * @return ClassNameResolver
   */
  public ClassNameResolver getClassNameResolver() {
    return classNameResolver;
  }

  /**
   * Sets ClassNameResolver
   * 
   * @param classNameResolver ClassNameResolver
   */
  public void setClassNameResolver(ClassNameResolver classNameResolver) {
    this.classNameResolver = classNameResolver;
  }

  /**
   * Returns the serializer for a class.
   *
   * @param cl the class of the object that needs to be serialized.
   *
   * @return a serializer object for the serialization.
   */
  public Serializer getSerializer(Class cl)
    throws HessianProtocolException
  {
    Serializer serializer;

    serializer = (Serializer) _staticSerializerMap.get(cl);
    if (serializer != null)
      return serializer;

    serializer = (Serializer) _cachedSerializerMap.get(cl);
    if (serializer != null)
      return serializer;

    for (int i = 0;
	 serializer == null && _factories != null && i < _factories.size();
	 i++) {
      AbstractSerializerFactory factory;

      factory = (AbstractSerializerFactory) _factories.get(i);

      serializer = factory.getSerializer(cl);
    }

    if (serializer != null) {
    }

    else if (HessianRemoteObject.class.isAssignableFrom(cl))
      serializer = new RemoteSerializer();

    else if (BurlapRemoteObject.class.isAssignableFrom(cl))
      serializer = new RemoteSerializer();

    else if (Map.class.isAssignableFrom(cl))
      serializer = new MapSerializer();

    else if (Collection.class.isAssignableFrom(cl)) {
      if (_collectionSerializer == null) {
	_collectionSerializer = new CollectionSerializer();
      }

      serializer = _collectionSerializer;
    }

    else if (cl.isArray())
      serializer = new ArraySerializer();

    else if (Throwable.class.isAssignableFrom(cl))
      serializer = new ThrowableSerializer(cl);

    else if (InputStream.class.isAssignableFrom(cl))
      serializer = new InputStreamSerializer();

    else if (Iterator.class.isAssignableFrom(cl))
      serializer = IteratorSerializer.create();

    else if (Enumeration.class.isAssignableFrom(cl))
      serializer = EnumerationSerializer.create();
    
    else if (Calendar.class.isAssignableFrom(cl))
      serializer = CalendarSerializer.create();
    
    else if (Locale.class.isAssignableFrom(cl))
      serializer = LocaleSerializer.create();
    
    else if (Enum.class.isAssignableFrom(cl))
      serializer = new EnumSerializer(cl);

    if (serializer == null)
      serializer = getDefaultSerializer(cl);

    _cachedSerializerMap.put(cl, serializer);

    return serializer;
  }

  /**
   * Returns the default serializer for a class that isn't matched
   * directly.  Application can override this method to produce
   * bean-style serialization instead of field serialization.
   *
   * @param cl the class of the object that needs to be serialized.
   *
   * @return a serializer object for the serialization.
   */
  protected Serializer getDefaultSerializer(Class cl)
  {
    if (_defaultSerializer != null)
      return _defaultSerializer;

    if (! Serializable.class.isAssignableFrom(cl)
	&& ! _isAllowNonSerializable) {
      throw new IllegalStateException("Serialized class " + cl.getName() + " must implement java.io.Serializable");
    }

    return new JavaSerializer(cl);
  }
  
  /**
   * Returns the deserializer for a class.
   *
   * @param cl the class of the object that needs to be deserialized.
   *
   * @return a deserializer object for the serialization.
   */
  public Deserializer getDeserializer(Class cl)
    throws HessianProtocolException
  {
    Deserializer deserializer;

    deserializer = (Deserializer) _staticDeserializerMap.get(cl);
    if (deserializer != null)
      return deserializer;

    deserializer = (Deserializer) _cachedDeserializerMap.get(cl);

    if (deserializer != null)
      return deserializer;

    for (int i = 0;
	 deserializer == null && _factories != null && i < _factories.size();
	 i++) {
      AbstractSerializerFactory factory;
      factory = (AbstractSerializerFactory) _factories.get(i);

      deserializer = factory.getDeserializer(cl);
    }

    if (deserializer != null) {
    }

    else if (Collection.class.isAssignableFrom(cl))
      deserializer = new CollectionDeserializer(cl);

    else if (Map.class.isAssignableFrom(cl))
      deserializer = new MapDeserializer(cl);
    
    else if (cl.isInterface())
      deserializer = OBJECT_DESERIALIZER;

    else if (cl.isArray())
      deserializer = new ArrayDeserializer(getDeserializer(cl.getComponentType()));

    else if (Enumeration.class.isAssignableFrom(cl))
      deserializer = EnumerationDeserializer.create();

    else if (Enum.class.isAssignableFrom(cl))
      deserializer = new EnumDeserializer(cl);
    
    else
      deserializer = getDefaultDeserializer(cl);

    _cachedDeserializerMap.put(cl, deserializer);

    return deserializer;
  }

  /**
   * Returns the default serializer for a class that isn't matched
   * directly.  Application can override this method to produce
   * bean-style serialization instead of field serialization.
   *
   * @param cl the class of the object that needs to be serialized.
   *
   * @return a serializer object for the serialization.
   */
  protected Deserializer getDefaultDeserializer(Class cl)
  {
    return new JavaDeserializer(cl);
  }

  /**
   * Reads the object as a list.
   */
  public Object readList(AbstractHessianInput in, int length, String type)
    throws HessianProtocolException, IOException
  {
    Deserializer deserializer = getDeserializer(type);

    if (deserializer != null)
      return deserializer.readList(in, length);
    else
      return new CollectionDeserializer(ArrayList.class).readList(in, length);
  }
  
  /**
   * Reads the object as a map.
   */
  public Object readMap(AbstractHessianInput in, String type)
    throws HessianProtocolException, IOException
  {
    Deserializer deserializer = getDeserializer(type);

    if (deserializer != null)
      return deserializer.readMap(in);
    else if (_hashMapDeserializer != null)
      return _hashMapDeserializer.readMap(in);
    else {
      _hashMapDeserializer = new MapDeserializer(HashMap.class);
      
      return _hashMapDeserializer.readMap(in);
    }
  }
  
  /**
   * Reads the object as a map.
   */
  public Object readObject(AbstractHessianInput in,
			   String type,
			   String []fieldNames)
    throws HessianProtocolException, IOException
  {
    Deserializer deserializer = getDeserializer(type);

    if (deserializer != null)
      return deserializer.readObject(in, fieldNames);
    else if (_hashMapDeserializer != null)
      return _hashMapDeserializer.readObject(in, fieldNames);
    else {
      _hashMapDeserializer = new MapDeserializer(HashMap.class);
      
      return _hashMapDeserializer.readObject(in, fieldNames);
    }
  }

  /**
   * Reads the object as a map.
   */
  public Deserializer getObjectDeserializer(String type, Class cl)
    throws HessianProtocolException
  {
    Deserializer reader = getObjectDeserializer(type);
    
    if (cl == null
	|| cl == reader.getType()
	|| cl.isAssignableFrom(reader.getType())) {
      return reader;
    }

    if (log.isLoggable(Level.FINE))
      log.fine("hessian: expected '" + cl.getName() + "' at '" + type + "'");
    
    return getDeserializer(cl);
  }
  
  /**
   * Reads the object as a map.
   */
  public Deserializer getObjectDeserializer(String type)
    throws HessianProtocolException
  {
    Deserializer deserializer = getDeserializer(type);

    if (deserializer != null)
      return deserializer;
    else if (_hashMapDeserializer != null)
      return _hashMapDeserializer;
    else {
      _hashMapDeserializer = new MapDeserializer(HashMap.class);
      
      return _hashMapDeserializer;
    }
  }

  /**
   * Returns a deserializer based on a string type.
   */
  public Deserializer getDeserializer(String type)
    throws HessianProtocolException
  {
    if (type == null || type.equals(""))
      return null;

    Deserializer deserializer;

    deserializer = (Deserializer) _cachedTypeDeserializerMap.get(type);

    if (deserializer != null)
      return deserializer;


    deserializer = (Deserializer) _staticTypeMap.get(type);
    if (deserializer != null)
      return deserializer;

    if (type.startsWith("[")) {
      Deserializer subDeserializer = getDeserializer(type.substring(1));
      deserializer = new ArrayDeserializer(subDeserializer);
    }
    else {
      try {
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
      
	Class cl = Class.forName(type, false, loader);

	deserializer = getDeserializer(cl);
      } catch (Exception e) {
	log.log(Level.FINER, e.toString(), e);
      }
    }

    if (deserializer != null) { 
      _cachedTypeDeserializerMap.put(type, deserializer);
    }

    return deserializer;
  }

  protected static void addBasic(Class cl, String typeName, int type)
  {
    _staticSerializerMap.put(cl, new BasicSerializer(type));

    Deserializer deserializer = new BasicDeserializer(type);
    _staticDeserializerMap.put(cl, deserializer);
    _staticTypeMap.put(typeName, deserializer);
  }

  static {
    _staticSerializerMap = new ConcurrentHashMap();
    _staticDeserializerMap = new ConcurrentHashMap();
    _staticTypeMap = new ConcurrentHashMap();

    addBasic(void.class, "void", BasicSerializer.NULL);
    
    addBasic(Boolean.class, "boolean", BasicSerializer.BOOLEAN);
    addBasic(Byte.class, "byte", BasicSerializer.BYTE);
    addBasic(Short.class, "short", BasicSerializer.SHORT);
    addBasic(Integer.class, "int", BasicSerializer.INTEGER);
    addBasic(Long.class, "long", BasicSerializer.LONG);
    addBasic(Float.class, "float", BasicSerializer.FLOAT);
    addBasic(Double.class, "double", BasicSerializer.DOUBLE);
    addBasic(Character.class, "char", BasicSerializer.CHARACTER_OBJECT);
    addBasic(String.class, "string", BasicSerializer.STRING);
    addBasic(Object.class, "object", BasicSerializer.OBJECT);
    addBasic(java.util.Date.class, "date", BasicSerializer.DATE);

    addBasic(boolean.class, "boolean", BasicSerializer.BOOLEAN);
    addBasic(byte.class, "byte", BasicSerializer.BYTE);
    addBasic(short.class, "short", BasicSerializer.SHORT);
    addBasic(int.class, "int", BasicSerializer.INTEGER);
    addBasic(long.class, "long", BasicSerializer.LONG);
    addBasic(float.class, "float", BasicSerializer.FLOAT);
    addBasic(double.class, "double", BasicSerializer.DOUBLE);
    addBasic(char.class, "char", BasicSerializer.CHARACTER);
    
    addBasic(boolean[].class, "[boolean", BasicSerializer.BOOLEAN_ARRAY);
    addBasic(byte[].class, "[byte", BasicSerializer.BYTE_ARRAY);
    addBasic(short[].class, "[short", BasicSerializer.SHORT_ARRAY);
    addBasic(int[].class, "[int", BasicSerializer.INTEGER_ARRAY);
    addBasic(long[].class, "[long", BasicSerializer.LONG_ARRAY);
    addBasic(float[].class, "[float", BasicSerializer.FLOAT_ARRAY);
    addBasic(double[].class, "[double", BasicSerializer.DOUBLE_ARRAY);
    addBasic(char[].class, "[char", BasicSerializer.CHARACTER_ARRAY);
    addBasic(String[].class, "[string", BasicSerializer.STRING_ARRAY);
    addBasic(Object[].class, "[object", BasicSerializer.OBJECT_ARRAY);

    _staticSerializerMap.put(Class.class, new ClassSerializer());
    _staticDeserializerMap.put(Class.class, new ClassDeserializer());

    _staticDeserializerMap.put(Number.class, new BasicDeserializer(BasicSerializer.NUMBER));
    
    _staticSerializerMap.put(BigDecimal.class, new StringValueSerializer());
    try {
      _staticDeserializerMap.put(BigDecimal.class,
			   new StringValueDeserializer(BigDecimal.class));
    } catch (Throwable e) {
    }
    
    _staticSerializerMap.put(File.class, new StringValueSerializer());
    try {
      _staticDeserializerMap.put(File.class,
			   new StringValueDeserializer(File.class));
    } catch (Throwable e) {
    }
    
    _staticSerializerMap.put(java.sql.Date.class, new SqlDateSerializer());
    _staticSerializerMap.put(java.sql.Time.class, new SqlDateSerializer());
    _staticSerializerMap.put(java.sql.Timestamp.class, new SqlDateSerializer());
    
    _staticSerializerMap.put(java.io.InputStream.class,
			     new InputStreamSerializer());
    _staticDeserializerMap.put(java.io.InputStream.class,
			       new InputStreamDeserializer());

    try {
      _staticDeserializerMap.put(java.sql.Date.class,
			  new SqlDateDeserializer(java.sql.Date.class));
      _staticDeserializerMap.put(java.sql.Time.class,
			  new SqlDateDeserializer(java.sql.Time.class));
      _staticDeserializerMap.put(java.sql.Timestamp.class,
			  new SqlDateDeserializer(java.sql.Timestamp.class));
    } catch (Throwable e) {
      e.printStackTrace();
    }

    try {
      Class stackTrace = Class.forName("java.lang.StackTraceElement");
      
      _staticDeserializerMap.put(stackTrace, new StackTraceElementDeserializer());
    } catch (Throwable e) {
    }
  }
}
