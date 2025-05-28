/*
 *
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
 *
 */
package io.cloudapp.starter.refresh;

import cn.hutool.core.bean.BeanUtil;
import io.cloudapp.starter.base.properties.RefreshableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.Type;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.ClassLoaderAwareGeneratorStrategy;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.*;
import org.springframework.cglib.transform.ClassEmitterTransformer;
import org.springframework.cglib.transform.TransformingClassGenerator;
import org.springframework.lang.Nullable;
import org.springframework.objenesis.SpringObjenesis;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class RefreshableProxyFactory {

    private static final Logger log = LoggerFactory.getLogger(RefreshableProxyFactory.class);

    /*
     * The Spring Objectnesis used to create the proxied objects.
     */
    private static final SpringObjenesis objenesis = new SpringObjenesis();

    /*
     * This method is used by the RefreshableComponent to create a proxy object
     * that implements the RefreshableProxy interface.
     */
    private static final String PROXY_METHOD_NAME = "refreshTarget";

    /*
     * This method is used by an external object to invoke the getTarget method.
     */
    private static final String GET_TARGET_METHOD_NAME = "getTarget";
    
    /*
     * This method is used by an external object to invoke the setRefreshableProxyTarget method.
     */
    private static final Method UPDATE_TARGET_METHOD;

    /*
     * This method is used by an external object to invoke the getTarget method.
     */
    private static final Method GET_TARGET_METHOD;
    
    static {
        try {
            UPDATE_TARGET_METHOD = TargetRefreshable.class.getMethod(PROXY_METHOD_NAME, RefreshableProperties.class);
            GET_TARGET_METHOD = TargetRefreshable.class.getMethod(GET_TARGET_METHOD_NAME);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ObjectLoader implements  LazyLoader {
        private final Object target;

        public ObjectLoader(Object target) {
            this.target = target;
        }


        @Override
        public Object loadObject() {
            return target;
        }
    }
    
    public static class RefreshableProxy<T, P extends RefreshableProperties> implements
            TargetRefreshable<P>, MethodInterceptor , CallbackFilter {
        private T target;

        private final Function<P, T> creator;

        private final Consumer<T> preRefreshCallback;

        private RefreshableProxy(Function<P, T> func, P properties) {
            this(func, properties, null);
        }

        private RefreshableProxy(Function<P, T> func,
                                 P properties,
                                 Consumer<T> preRefreshCallback) {
            this.creator = func;
            this.target = func.apply(properties);
            this.preRefreshCallback = preRefreshCallback;
        }

        @Override
        public void refreshTarget(P properties) {

            if (preRefreshCallback != null) {
                try {
                    preRefreshCallback.accept(target);
                } catch (Throwable t) {
                    log.error("Error while pre refreshing target, ignore the target refreshing", t);
                    return;
                }
            }

            this.target = creator.apply(properties);
        }

        @Override
        public Object getTarget() {
            return target;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
                throws Throwable {
            if (method.getName().equals(PROXY_METHOD_NAME)) {
                this.refreshTarget((P) objects[0]);
                return null;
            }

            if (method.getName().equals(GET_TARGET_METHOD_NAME)) {
                return target;
            }

            return methodProxy.invoke(target, objects);
        }



        @SuppressWarnings("unchecked")
        public T createProxy() {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(target.getClass());
            // enhancer.setCallback(this);
            enhancer.setUseFactory(true);
            enhancer.setInterfaces(new Class[] { TargetRefreshable.class });

            enhancer.setCallbackFilter(this);
            enhancer.setCallbackTypes(new Class[]{
                    this.getClass(),
                    ObjectLoader.class,
                    NoOp.class
            });

            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);

            ClassLoader classLoader = BeanFactory.class.getClassLoader();
            enhancer.setClassLoader(classLoader);
            enhancer.setStrategy(new BeanFactoryAwareGeneratorStrategy(classLoader));


            Class<T> clazz = (Class<T>) enhancer.createClass();
            Enhancer.registerStaticCallbacks(clazz, new Callback[]{
                    this,
                    new ObjectLoader(target),
                    null
            });
            
            T result = objenesis.newInstance(clazz);
            
            Field[] fields = target.getClass().getFields();
            try {
                Arrays.stream(fields).filter(
                        field -> !Modifier.isFinal(field.getModifiers())
                ).forEach(field -> {
                
                });
                for (Field field : fields) {
                    if(Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    BeanUtil.setFieldValue(result, field.getName(), field.get(target));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            return result;
        }

        @Override
        public int accept(Method method) {
            return 0;
        }

    }
    
    public static <P extends RefreshableProperties>  void updateProxyTarget(Object proxy, P target)
            throws InvocationTargetException, IllegalAccessException {
        UPDATE_TARGET_METHOD.invoke(proxy, target);
    }
    
    public static <T, P extends RefreshableProperties> T create(Function<P, T> func, P properties) {
        return new RefreshableProxy<>(func, properties).createProxy();
    }

    public static <T, P extends RefreshableProperties> T create(Function<P, T> func,
                                                                P properties,
                                                                Consumer<T> callback) {
        return new RefreshableProxy<>(func, properties, callback).createProxy();
    }


    private static class BeanFactoryAwareGeneratorStrategy extends
            ClassLoaderAwareGeneratorStrategy {

        private static final String BEAN_FACTORY_FIELD = "$$beanFactory";

        public BeanFactoryAwareGeneratorStrategy(@Nullable ClassLoader classLoader) {
            super(classLoader);
        }

        @Override
        protected ClassGenerator transform(ClassGenerator cg) {
            ClassEmitterTransformer transformer = new ClassEmitterTransformer() {
                @Override
                public void end_class() {
                    declare_field(Constants.ACC_PUBLIC, BEAN_FACTORY_FIELD, Type.getType(BeanFactory.class), null);
                    super.end_class();
                }
            };

            return new TransformingClassGenerator(cg, transformer);
        }

    }


}
