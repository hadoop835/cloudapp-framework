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

package io.cloudapp.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public abstract class BaseModel {

    /** Base builder which builds arguments. */
    public abstract static class Builder
            <B extends Builder<B, A>, A extends BaseModel> {

        protected Collection<Consumer<A>> operations;

        protected abstract void validate(A args);

        public Builder() {
            this.operations = new ArrayList<>();
        }

        @SuppressWarnings("unchecked")
        // safe as B will always be the builder of the current args class
        private A newInstance() {
            try {
                return newInstance0();
            } catch (Throwable e) {
                // Args class must have no argument constructor with at least
                // protected access.
                throw new RuntimeException(e);
            }
        }

        private A newInstance0() throws Throwable {
            Class<?> clazz = this.getClass().getEnclosingClass();
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 0) {
                    //noinspection unchecked
                    return (A) constructor.newInstance();
                }
            }

            throw new RuntimeException(clazz +
                    " should at least have 1 'no argument constructor'");
        }

        /** Creates derived Args class with each attribute populated. */
        public A build() throws IllegalArgumentException {
            A obj = newInstance();

            operations.forEach(oper -> oper.accept(obj));
            validate(obj);

            return obj;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }

        if (!(o instanceof BaseModel)) {
            return false;
        }

        BaseModel baseArgs = (BaseModel) o;

        return o.hashCode() == baseArgs.hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

