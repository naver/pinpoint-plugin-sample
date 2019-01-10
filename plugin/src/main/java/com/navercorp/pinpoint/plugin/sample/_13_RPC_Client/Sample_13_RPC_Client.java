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
package com.navercorp.pinpoint.plugin.sample._13_RPC_Client;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;

/**
 * To trace a transaction across nodes, you have to attach some tracing data to RPC calls. This example shows how.
 * <p>
 * Target classes are just for this example. They are not real RPC client.
 */
public class Sample_13_RPC_Client implements TransformCallback {

    @Override
    public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);

        target.addGetter(ServerAddressGetter.class, "serverAddress");
        target.addGetter(ServerPortGetter.class, "serverPort");
        target.getDeclaredMethod("sendRequest", "com.navercorp.plugin.sample.target.TargetClass13_Request").addInterceptor(SendRequestInterceptor.class);

        return target.toBytecode();
    }
}
