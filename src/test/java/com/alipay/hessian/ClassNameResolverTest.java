/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.hessian;

import com.alipay.hessian.internal.InternalNameBlackListFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhanggeng on 2017/7/24.
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class ClassNameResolverTest {

    @Test
    public void test() throws Exception {
        ClassNameFilter blackListFilter = new InternalNameBlackListFilter();
        NameMappingFilter mappingFilter = new NameMappingFilter();
        mappingFilter.registerMapping("xx", "yy");
        mappingFilter.registerMapping("yy", "xx");

        ClassNameResolver resolver = new ClassNameResolver();
        resolver.addFilter(blackListFilter);
        resolver.addFilter(mappingFilter);

        boolean error = false;
        try {
            resolver.resolve("java.security.SignedObject");
        } catch (Exception e) {
            error = true;
        }
        Assert.assertTrue(error);

        Assert.assertEquals("yy", resolver.resolve("xx"));
        Assert.assertEquals("xx", resolver.resolve("yy"));
    }

    @Test
    public void addFilter() throws Exception {
        ClassNameResolver resolver = new ClassNameResolver();
        List<ClassNameFilter> old = resolver.filters;
        try {
            resolver.filters = null;

            resolver.addFilter(new ClassNameFilter() {
                public int order() {
                    return 1;
                }

                public String resolve(String className) {
                    return className;
                }
            });
            resolver.addFilter(new ClassNameFilter() {
                public int order() {
                    return 3;
                }

                public String resolve(String className) {
                    return className;
                }
            });
            Assert.assertTrue(resolver.filters.size() == 2);
        } finally {
            resolver.filters = old;
        }
    }

    @Test
    public void removeFilter() throws Exception {
        ClassNameResolver resolver = new ClassNameResolver();
        List<ClassNameFilter> old = resolver.filters;
        try {
            resolver.filters = null;
            ClassNameFilter filter1 = new ClassNameFilter() {
                public int order() {
                    return 1;
                }

                public String resolve(String className) {
                    return className;
                }
            };
            ClassNameFilter filter2 = new ClassNameFilter() {
                public int order() {
                    return 3;
                }

                public String resolve(String className) {
                    return className;
                }
            };

            resolver.addFilter(filter1);
            resolver.addFilter(filter2);
            Assert.assertTrue(resolver.filters.size() == 2);
            resolver.removeFilter(filter2);
            Assert.assertTrue(resolver.filters.size() == 1);
            resolver.removeFilter(filter1);
            Assert.assertTrue(resolver.filters.size() == 0);
        } finally {
            resolver.filters = old;
        }
    }

    @Test
    public void resolve() throws Exception {
        ClassNameResolver resolver = new ClassNameResolver();
        List<ClassNameFilter> old = resolver.filters;
        try {
            resolver.filters = null;
            ClassNameFilter filter1 = new ClassNameFilter() {
                public int order() {
                    return 1;
                }

                public String resolve(String className) {
                    return "a" + className;
                }
            };
            ClassNameFilter filter2 = new ClassNameFilter() {
                public int order() {
                    return 3;
                }

                public String resolve(String className) {
                    return className + "b";
                }
            };

            resolver.addFilter(filter1);
            resolver.addFilter(filter2);
            Assert.assertTrue(resolver.filters.size() == 2);

            Assert.assertEquals(resolver.resolve("xxx"), "axxxb");
        } finally {
            resolver.filters = old;
        }
    }

    @Test
    public void multipleThreadSet() {
        final ClassNameResolver resolver = new ClassNameResolver();
        resolver.addFilter(new ClassNameFilter() {
            public int order() {
                return 1;
            }

            public String resolve(String className) {
                return className;
            }
        });
        resolver.addFilter(new ClassNameFilter() {
            public int order() {
                return 3;
            }

            public String resolve(String className) {
                return className;
            }
        });
        Assert.assertTrue(resolver.filters.size() == 2);

        final AtomicBoolean run = new AtomicBoolean(true);
        final AtomicBoolean error = new AtomicBoolean(false); // 有没有发生异常，例如死锁等
        final CountDownLatch latch = new CountDownLatch(1); // 出现异常 跳出等待
        Thread readThread = new Thread(new Runnable() {
            public void run() {
                while (run.get()) {
                    try {
                        resolver.resolve("xxx");
                    } catch (Throwable e) {
                        e.printStackTrace();
                        error.compareAndSet(false, true);
                        run.compareAndSet(true, false);
                        latch.countDown();
                    }
                }
            }
        }, "Read");
        final ClassNameFilter filter = new ClassNameFilter() {
            public int order() {
                return 2;
            }

            public String resolve(String className) {
                return "z" + className;
            }
        };
        final AtomicInteger count = new AtomicInteger();
        Thread writeThread = new Thread(new Runnable() {
            public void run() {
                while (run.get()) {
                    try {
                        if (count.incrementAndGet() % 2 == 0) {
                            resolver.addFilter(filter);
                        } else {
                            resolver.removeFilter(filter);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        error.compareAndSet(false, true);
                        run.compareAndSet(true, false);
                        latch.countDown();
                    }
                }
            }
        }, "Write");
        writeThread.setDaemon(false);
        writeThread.start();
        readThread.setDaemon(true);
        readThread.start();

        // 正常跑5秒 或者出异常
        try {
            latch.await(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            run.set(false);
        }
        Assert.assertFalse(error.get());
    }
}