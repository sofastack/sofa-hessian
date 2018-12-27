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
package com.alipay.hessian.internal;

import com.alipay.hessian.NameBlackListFilter;

import java.util.Arrays;
import java.util.List;

/**
 * 内置黑名单列表过滤器
 *
 * @author <a href="mailto:zhanggeng.zg@antfin.com">zhanggeng</a>
 */
public class InternalNameBlackListFilter extends NameBlackListFilter {

    static final List<String> INTERNAL_BLACK_LIST = Arrays
                                                              .asList(
                                                                  "org.codehaus.groovy.runtime.MethodClosure",
                                                                  "clojure.core$constantly",
                                                                  "clojure.main$eval_opt",
                                                                  "com.alibaba.citrus.springext.support.parser.AbstractNamedProxyBeanDefinitionParser$ProxyTargetFactory",
                                                                  "com.alibaba.citrus.springext.support.parser.AbstractNamedProxyBeanDefinitionParser$ProxyTargetFactoryImpl",
                                                                  "com.alibaba.citrus.springext.util.SpringExtUtil.AbstractProxy",
                                                                  "com.alipay.custrelation.service.model.redress.Pair",
                                                                  "com.caucho.hessian.test.TestCons",
                                                                  "com.mchange.v2.c3p0.JndiRefForwardingDataSource",
                                                                  "com.mchange.v2.c3p0.WrapperConnectionPoolDataSource",
                                                                  "com.rometools.rome.feed.impl.EqualsBean",
                                                                  "com.rometools.rome.feed.impl.ToStringBean",
                                                                  "com.sun.jndi.rmi.registry.BindingEnumeration",
                                                                  "com.sun.jndi.toolkit.dir.LazySearchEnumerationImpl",
                                                                  "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
                                                                  "com.sun.rowset.JdbcRowSetImpl",
                                                                  "com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data",
                                                                  "java.rmi.server.UnicastRemoteObject",
                                                                  "java.security.SignedObject",
                                                                  "java.util.ServiceLoader$LazyIterator",
                                                                  "javax.imageio.ImageIO$ContainsFilter",
                                                                  "javax.imageio.spi.ServiceRegistry",
                                                                  "javax.management.BadAttributeValueExpException",
                                                                  "javax.naming.InitialContext",
                                                                  "javax.naming.spi.ObjectFactory",
                                                                  "javax.script.ScriptEngineManager",
                                                                  "javax.sound.sampled.AudioFormat$Encoding",
                                                                  "org.apache.carbondata.core.scan.expression.ExpressionResult",
                                                                  "org.apache.commons.dbcp.datasources.SharedPoolDataSource",
                                                                  "org.apache.ibatis.executor.loader.AbstractSerialStateHolder",
                                                                  "org.apache.ibatis.executor.loader.CglibSerialStateHolder",
                                                                  "org.apache.ibatis.executor.loader.JavassistSerialStateHolder",
                                                                  "org.apache.ibatis.executor.loader.cglib.CglibProxyFactory",
                                                                  "org.apache.ibatis.executor.loader.javassist.JavassistSerialStateHolder",
                                                                  "org.apache.tomcat.dbcp.dbcp.datasources.SharedPoolDataSource",
                                                                  "org.apache.wicket.util.upload.DiskFileItem",
                                                                  "org.apache.xalan.xsltc.trax.TemplatesImpl",
                                                                  "org.apache.xbean.naming.context.ContextUtil$ReadOnlyBinding",
                                                                  "org.apache.xpath.XPathContext",
                                                                  "org.eclipse.jetty.util.log.LoggerLog",
                                                                  "org.geotools.filter.ConstantExpression",
                                                                  "org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator$PartiallyComparableAdvisorHolder",
                                                                  "org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor",
                                                                  "org.springframework.beans.factory.BeanFactory",
                                                                  "org.springframework.beans.factory.config.PropertyPathFactoryBean",
                                                                  "org.springframework.beans.factory.support.DefaultListableBeanFactory",
                                                                  "org.springframework.jndi.support.SimpleJndiBeanFactory",
                                                                  "org.springframework.orm.jpa.AbstractEntityManagerFactoryBean",
                                                                  "org.springframework.transaction.jta.JtaTransactionManager",
                                                                  "org.yaml.snakeyaml.tokens.DirectiveToken",
                                                                  "sun.rmi.server.UnicastRef",
                                                                  "javax.management.ImmutableDescriptor",
                                                                  "org.springframework.jndi.JndiObjectTargetSource",
                                                                  "ch.qos.logback.core.db.JNDIConnectionSource",
                                                                  "java.beans.Expression",
                                                                  "javassist.bytecode",
                                                                  "org.apache.ibatis.javassist.bytecode",
                                                                  "org.springframework.beans.factory.config.MethodInvokingFactoryBean",
                                                                  "com.alibaba.druid.pool.DruidDataSource",
                                                                  "com.sun.org.apache.bcel.internal.util.ClassLoader",
                                                                  "com.alibaba.druid.stat.JdbcDataSourceStat",
                                                                  "org.apache.tomcat.dbcp.dbcp.BasicDataSource",
                                                                  "com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput",
                                                                  "javassist.tools.web.Viewer",
                                                                  "net.bytebuddy.dynamic.loading.ByteArrayClassLoader",
                                                                  "org.apache.commons.beanutils.BeanMap");

    /**
     * 构造函数
     */
    public InternalNameBlackListFilter() {
        super(INTERNAL_BLACK_LIST);
    }

    /**
     * 构造函数指定缓存大小
     *
     * @param maxCacheSize 最大缓存大小
     */
    public InternalNameBlackListFilter(int maxCacheSize) {
        super(INTERNAL_BLACK_LIST, maxCacheSize);
    }
}
