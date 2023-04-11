/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.test.throwable;

import com.caucho.burlap.io.BurlapRemoteObject;
import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.ArraySerializer;
import com.caucho.hessian.io.CalendarSerializer;
import com.caucho.hessian.io.CollectionSerializer;
import com.caucho.hessian.io.EnumSerializer;
import com.caucho.hessian.io.EnumerationSerializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.HessianRemoteObject;
import com.caucho.hessian.io.InputStreamSerializer;
import com.caucho.hessian.io.IteratorSerializer;
import com.caucho.hessian.io.LocaleSerializer;
import com.caucho.hessian.io.MapSerializer;
import com.caucho.hessian.io.RemoteSerializer;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author junyuan
 * @version SerializeFactoryWithoutThrowable.java, v 0.1 2023年04月27日 19:14 junyuan Exp $
 */
public class SerializeFactoryWithoutThrowable extends SerializerFactory {

    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        Serializer serializer;

        serializer = (Serializer) _staticSerializerMap.get(cl);
        if (serializer != null)
            return serializer;

        //must before "else if (JavaSerializer.getWriteReplace(cl) != null)" or will be WriteReplace
//        if (isZoneId(cl)) {
//            serializer = ZoneIdSerializer.getInstance();
//            return serializer;
//        }

        serializer = (Serializer) _cachedSerializerMap.get(cl);
        if (serializer != null)
            return serializer;

        if (classNameResolver != null) {
            try {
                classNameResolver.resolve(cl.getName());
            } catch (Exception e) {
                throw new HessianProtocolException(e);
            }
        }

        for (int i = 0; serializer == null && _factories != null && i < _factories.size(); i++) {
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

}