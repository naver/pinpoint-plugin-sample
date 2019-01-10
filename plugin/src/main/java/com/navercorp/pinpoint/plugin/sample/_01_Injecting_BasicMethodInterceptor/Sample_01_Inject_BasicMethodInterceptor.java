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
package com.navercorp.pinpoint.plugin.sample._01_Injecting_BasicMethodInterceptor;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

import static com.navercorp.pinpoint.common.util.VarArgs.va;

/**
 * Pinpiont provides {@link BasicMethodInterceptor} which records method execution time and exception.
 * This example shows how to inject it to a method. It also shows how to pass constructor arguments to the interceptor.
 */
public class Sample_01_Inject_BasicMethodInterceptor implements TransformCallback {

    @Override
    public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        // 1. Get InstrumentClass of the target class
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        
        // 2. Get InstrumentMethod of the target method.
        InstrumentMethod targetMethod = target.getDeclaredMethod("targetMethod", "java.lang.String");
        
        // 3. Add interceptor. The first argument is FQN of the interceptor class, followed by arguments for the interceptor's constructor.
        targetMethod.addInterceptor(BasicMethodInterceptor.class, va(SamplePluginConstants.MY_SERVICE_TYPE));
        
        // 4. Return resulting byte code.
        return target.toBytecode();
    }

}
