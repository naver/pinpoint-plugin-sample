/**
 * Copyright 2014 NAVER Corp.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.pinpoint.plugin.sample._08_Interceptor_Annotations;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.*;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.Scope;

/**
 * There are annotations for interceptor class to declare : interceptor target, and interceptor scope.
 * <p>
 * To specify a target, use {@link TargetMethod}, {@link TargetConstructor}, {@link TargetFilter}.<br/>
 * To specify multiple targets, use {@link TargetMethods}, {@link TargetConstructors}.<br/>
 * Note that these are effective only when the interceptor is injected by one of {@link InstrumentClass}'s
 * <tt>addInterceptor()</tt> or <tt>addScopedInterceptor()</tt> method.
 * <p>
 * To specify interceptor scope, use {@link Scope}.
 * If you pass other InterceptorScope through addScopedInterceptor(), the annotation will be ignored.
 * 
 * @see AnnotatedInterceptor
 * @deprecated use the normal, non-annotated interceptors due to
 *      <a href="https://github.com/naver/pinpoint/issues/2862">https://github.com/naver/pinpoint/issues/2862</a>
 */
@Deprecated
public class Sample_08_Interceptor_Annotations implements TransformCallback {

    @Override
    public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        
        target.addInterceptor("com.navercorp.pinpoint.plugin.sample._08_Interceptor_Annotations.AnnotatedInterceptor");

        return target.toBytecode();
    }
}
