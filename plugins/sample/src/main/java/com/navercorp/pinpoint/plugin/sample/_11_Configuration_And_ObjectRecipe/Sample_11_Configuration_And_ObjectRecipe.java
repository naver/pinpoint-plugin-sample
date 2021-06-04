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
package com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.config.ProfilerConfig;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.plugin.ObjectFactory;

/**
 * ProfilerPlugin and PinpointClassFileTransformer implementation classes are loaded by a plugin class loader whose
 * parent is the system class loader when Pinpoint agent is initialized. However, interceptor classes are loaded by the
 * class loader which loads the actual target class, and these target class loaders cannot see the plugin classloader.
 * <p>
 * Therefore for a class X in a plugin, X in ProfilerPlugin and PinpointClassFileTransformer implementations and X in
 * interceptor implementations are different. This makes it impossible for a transformer to pass an object whose type is defined in the plugin to a interceptor as
 * it's constructor argument.
 * <p>
 * To handle this problem, you can pass an {@link ObjectFactory} which describes how to create the argument.
 * <p>
 * Note that, for the same reason, you should avoid sharing values by static variables of classes defined in a plugin.
 * <p>
 * This sample also shows how to read configurations in pinpoint.config file via {@link ProfilerConfig}.
 */
public class Sample_11_Configuration_And_ObjectRecipe implements TransformCallback {

    @Override
    public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        InstrumentMethod targetMethod = target.getDeclaredMethod("hello", "java.lang.String");
        
        // Get ProfilerConfig
        ProfilerConfig config = instrumentor.getProfilerConfig();
        int maxLen = config.readInt("profiler.sample11.maxLen", 8);
        
        // If you pass StringTrimmer object directly like below, Pinpoint agent fails to create the interceptor instance. 
        // targetMethod.addInterceptor("com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.HelloInterceptor", new StringTrimmer(maxLen));
        
        ObjectFactory trimmerFactory = ObjectFactory.byConstructor("com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.StringTrimmer", maxLen);
        targetMethod.addInterceptor(HelloInterceptor.class, new Object[] { trimmerFactory });

        return target.toBytecode();
    }
}
