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

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass06;

/**
 * @see Sample_06_Constructor_Interceptor_Scope_Limitation
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_06_Constructor_Interceptor_Group_Limitation_IT {
    
    @Test
    public void test() throws Exception {
        // Invoke 0-arg constructor. It calls 1-arg constructor.
        TargetClass06 target = new TargetClass06();
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Constructor<?> targetConstructor0 = TargetClass06.class.getConstructor();
        Constructor<?> targetConstructor1 = TargetClass06.class.getConstructor(int.class);

        // Unlike actual constructor invocation order, 1-arg constructor trace comes first. 
        verifier.verifyTrace(event("PluginExample", targetConstructor1));
        verifier.verifyTrace(event("PluginExample", targetConstructor0));
        
        verifier.verifyTraceCount(0);
    }
}
