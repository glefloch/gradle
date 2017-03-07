/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.tasks.compile;

import org.gradle.api.tasks.compile.CompileOptions;
import org.gradle.internal.Factory;
import org.gradle.util.DeprecationLogger;

public abstract class AbstractJavaCompileSpecFactory<T extends JavaCompileSpec> implements Factory<T> {
    private final CompileOptions compileOptions;

    public AbstractJavaCompileSpecFactory(CompileOptions compileOptions) {
        this.compileOptions = compileOptions;
    }

    @Override
    public T create() {
        if (compileOptions.isFork()) {
            if (getExecutable() != null || compileOptions.getForkOptions().getJavaHome() != null) {
                return getCommandLineSpec();
            } else {
                return getForkingSpec();
            }
        } else {
            return getDefaultSpec();
        }
    }

    private String getExecutable() {
        return DeprecationLogger.whileDisabled(new Factory<String>() {
            @Override
            @SuppressWarnings("deprecation")
            public String create() {
                return compileOptions.getForkOptions().getExecutable();
            }
        });
    }

    abstract protected T getCommandLineSpec();

    abstract protected T getForkingSpec();

    abstract protected T getDefaultSpec();
}
