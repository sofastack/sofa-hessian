/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author junyuan
 * @version ExtendSerializeFactory.java, v 0.1 2023年11月02日 18:22 junyuan Exp $
 */
public class ExtendSerializeFactory extends SerializerFactory {
    private static ConcurrentMap staticSerializerMap;
    private static ConcurrentMap staticDeserializerMap;
    private static ConcurrentMap staticTypeMap;

    protected Serializer                                                          defaultSerializer;

    // Additional factories
    protected ArrayList factories = new ArrayList();

    protected CollectionSerializer                                                collectionSerializer;

    private       Deserializer  hashMapDeserializer;
    private final ConcurrentMap cachedSerializerMap   = new ConcurrentHashMap();
    private final ConcurrentMap cachedDeserializerMap = new ConcurrentHashMap();
    private final ConcurrentMap<String, ConcurrentMap<ClassLoader, Deserializer>> cachedTypeDeserializerMap = new ConcurrentHashMap<String, ConcurrentMap<ClassLoader, Deserializer>>();

    static {
        staticSerializerMap = new ConcurrentHashMap();
        staticDeserializerMap = new ConcurrentHashMap();
        staticTypeMap = new ConcurrentHashMap();

        addToStaticMap(staticSerializerMap, staticDeserializerMap);
    }

    protected ConcurrentMap getSerStatic() {
        return staticSerializerMap;
    }

    protected ConcurrentMap getDesStatic() {
        return staticDeserializerMap;
    }

}