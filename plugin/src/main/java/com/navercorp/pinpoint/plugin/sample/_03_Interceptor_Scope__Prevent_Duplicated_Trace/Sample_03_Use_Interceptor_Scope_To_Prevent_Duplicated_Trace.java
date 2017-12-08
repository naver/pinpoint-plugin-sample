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
package com.navercorp.pinpoint.plugin.sample._03_Interceptor_Scope__Prevent_Duplicated_Trace;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.ExecutionPolicy;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.InterceptorScope;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

import static com.navercorp.pinpoint.common.util.VarArgs.va;

/**
 * {@link com.navercorp.plugin.sample.target.TargetClass03 TargetClass03} has 2 overloaded methods and we want to trace
 * them both.<br/>
 * Suppose that we have simply added <tt>BasicMethodInterceptor</tt> to both of them like before. When we call
 * <tt>invoke()</tt>, the traced call stack will be 11 levels deep; 1 for the first <tt>invoke()</tt> call,
 * and 10 for recursive calls of <tt>invoke(int)</tt>.
 * <p>
 * While this could be what you wanted, what if you simply wanted to know that any of the overloaded method have been
 * called, and not care about the overloaded and recursive calls polluting the call stack? Scoped interceptors are
 * perfect for this kind of situations.
 * <p>
 * Interceptors can be associated with an {@link InterceptorScope} and also specify an {@link ExecutionPolicy}.
 * <ul>
 * <li>ALWAYS: execute the interceptor no matter other interceptors in the same scope are active or not.</li>
 * <li>BOUNDARY: execute the interceptor only if no other interceptors in the same scope are active. (default)</li>
 * <li>INTERNAL: execute the interceptor only if at least one interceptor in the same scope is active.</li>
 * </ul>
 * Scoped interceptors that share the same scope will only run it's <tt>before()</tt> and <tt>after()</tt> methods if
 * they satisfy what is specified in it's execution policy. (An interceptor scope is active after the interceptor's
 * <tt>before()</tt> method is executed but before <tt>after()</tt> is called.)
 * <p>
 * For the example above, associating the same scope for both interceptors and specifying BOUNDARY as their execution
 * policy, overloaded call of <tt>invoke(int)</tt> will not be traced as it will be inside the scope of
 * <tt>invoke()</tt>, and recursive calls of <tt>invoke(int)</tt> will not be traced as they will all be inside the
 * scope of itself.
 */
public class Sample_03_Use_Interceptor_Scope_To_Prevent_Duplicated_Trace implements TransformCallback {

    @Override
    public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        
        // Get the scope object from Instrumentor
        InterceptorScope scope = instrumentor.getInterceptorScope("SAMPLE_SCOPE");

        // Add scoped interceptor with execution policy set to BOUNDARY (default)
        InstrumentMethod targetMethodA = target.getDeclaredMethod("invoke");
        targetMethodA.addScopedInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", va(SamplePluginConstants.MY_SERVICE_TYPE), scope);
        
        // Add scoped interceptor with execution policy set to BOUNDARY (default)
        InstrumentMethod targetMethodB = target.getDeclaredMethod("invoke", "int");
        targetMethodB.addScopedInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", va(SamplePluginConstants.MY_SERVICE_TYPE), scope);
        
        return target.toBytecode();
    }
}
