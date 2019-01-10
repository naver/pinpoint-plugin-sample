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
package com.navercorp.pinpoint.plugin.sample._10_Adding_Field;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.plugin.sample.target.TargetClass10_Consumer;
import com.navercorp.plugin.sample.target.TargetClass10_Message;
import com.navercorp.plugin.sample.target.TargetClass10_Producer;

/**
 * You can add a field to a class to attach some trace values.
 * 
 * In this sample, we are going to to trace {@link TargetClass10_Consumer#consume(TargetClass10_Message)} with the
 * producer name. But since we can not retrieve the producer name in that method, we intercept
 * {@link TargetClass10_Producer#produce()} to inject producer name into the returning {@link TargetClass10_Message}
 * instance.
 */
public class Sample_10_Adding_Field {

    public static class Producer implements TransformCallback {
        
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);

            target.getDeclaredMethod("produce").addInterceptor(ProducerInterceptor.class);
    
            return target.toBytecode();
        }
    }
    
    public static class Consumer implements TransformCallback {
        
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);

            target.getDeclaredMethod("consume", "com.navercorp.plugin.sample.target.TargetClass10_Message").addInterceptor(ConsumerInterceptor.class);
    
            return target.toBytecode();
        }
    }
    
    public static class Message implements TransformCallback {
        
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);

            // Add field to the message class. Note that you don't need to provide the field name. 
            target.addField(ProducerNameAccessor.class);

            return target.toBytecode();
        }
    }
}
