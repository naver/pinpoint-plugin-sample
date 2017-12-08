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
package com.navercorp.pinpoint.plugin.sample._06_Constructor_Interceptor_Scope_Limitation;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.InterceptorScope;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

import static com.navercorp.pinpoint.common.util.VarArgs.va;

/**
 * Constructor interceptors can be scoped as well. But there is a limitation.
 * <p>
 * Java enforces that the first operation of a constructor must be invocation of a super constructor or an overloaded
 * constructor so any injected code (including interceptor methods) is executed after this. Therefore method invocation
 * order for constructor interceptors is different from that of a normal method.
 * <p>
 * If method A calls method B, the interceptors are invoked in this order :
 * <ol>
 * <li><tt>A_interceptor.before();</tt></li>
 * <li><tt>B_interceptor.before();</tt></li>
 * <li><tt>B_interceptor.after();</tt></li>
 * <li><tt>A_interceptor.after();</tt></li>
 * </ol>
 *
 * But if A and B are constructors and A calls B, the interceptors are executed in this order :
 * <ol>
 * <li><tt>B_interceptor.before();</tt></li>
 * <li><tt>B_interceptor.after();</tt></li>
 * <li><tt>A_interceptor.before();</tt></li>
 * <li><tt>A_interceptor.after();</tt></li>
 * </ol>
 *
 * This also affects the execution policy of scoped interceptors. For methods, if A and B are in the same scope with
 * execution policy BOUNDARY, B's interceptor is not executed. If A and B were constructors however, both A and B's
 * interceptors are executed as when B's interceptor is executed, it is not with the scope of A's interceptor.
 */
public class Sample_06_Constructor_Interceptor_Scope_Limitation implements TransformCallback {

    @Override
    public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        InterceptorScope scope = instrumentor.getInterceptorScope("SAMPLE_SCOPE");

        InstrumentMethod targetConstructorA = target.getConstructor();
        targetConstructorA.addScopedInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", va(SamplePluginConstants.MY_SERVICE_TYPE), scope);

        InstrumentMethod targetConstructorB = target.getConstructor("int");
        targetConstructorB.addScopedInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", va(SamplePluginConstants.MY_SERVICE_TYPE), scope);

        return target.toBytecode();
    }

}
