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
package com.navercorp.pinpoint.plugin.sample._02_Injecting_Custom_Interceptor;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.HelloInterceptor;

/**
 * This sample shows you how to inject your very own, custom interceptor.
 * See {@link HelloInterceptor}
 * to learn how to write an interceptor.
 */
public class Sample_02_Inject_Custom_Interceptor implements TransformCallback {

    @Override
    public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        InstrumentMethod targetMethod = target.getDeclaredMethod("targetMethod", "java.lang.String");

        targetMethod.addInterceptor("com.navercorp.pinpoint.plugin.sample._02_Injecting_Custom_Interceptor.RecordArgsAndReturnValueInterceptor");
        
        return target.toBytecode();
    }

}
