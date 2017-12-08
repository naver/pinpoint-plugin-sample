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
package com.navercorp.pinpoint.plugin.sample;

import static com.navercorp.pinpoint.bootstrap.plugin.test.Expectations.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.sample._10_Adding_Field.Sample_10_Adding_Field;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass10_Consumer;
import com.navercorp.plugin.sample.target.TargetClass10_Message;
import com.navercorp.plugin.sample.target.TargetClass10_Producer;

/**
 * We want to trace {@link TargetClass10_Consumer#consume(TargetClass10_Message)} with producer name.
 * But we can not retrieve the producer name in the method.
 * So we intercept {@link TargetClass10_Producer#produce()} to inject producer name into the returning
 * {@link TargetClass10_Message} instance.
 * 
 * @see Sample_10_Adding_Field
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_10_Adding_Field_IT {

    @Test
    public void test() throws Exception {
        String name = "Pinpoint";
        
        TargetClass10_Producer producer = new TargetClass10_Producer(name);
        TargetClass10_Consumer consumer = new TargetClass10_Consumer();
        
        consumer.consume(producer.produce());


        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method targetMethod = TargetClass10_Consumer.class.getMethod("consume", TargetClass10_Message.class);
        
        verifier.verifyTrace(event("PluginExample", targetMethod, annotation("MyValue", name)));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
