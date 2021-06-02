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

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.instrument.MethodFilter;
import com.navercorp.pinpoint.bootstrap.plugin.test.Expectations;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.sample._08_Interceptor_Annotations.Sample_08_Interceptor_Annotations;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass07;

/**
 * Intercept public methods by using {@link MethodFilter}.
 * 
 * @see Sample_08_Interceptor_Annotations
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_07_Use_MethodFilter_To_Intercept_Multiple_Methods_IT {

    @Test
    public void test() throws Exception {
        TargetClass07 target = new TargetClass07();
        
        target.recordMe();
        target.recordMe(0);
        target.recordMe("maru");
        
        target.logMe();
        target.logMe("morae");
        
        Method recordMe0 = TargetClass07.class.getDeclaredMethod("recordMe");
        Method recordMe1 = TargetClass07.class.getDeclaredMethod("recordMe", int.class);
        Method recordMe2 = TargetClass07.class.getDeclaredMethod("recordMe", String.class);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        verifier.verifyTrace(Expectations.event("PluginExample", recordMe0));
        verifier.verifyTrace(Expectations.event("PluginExample", recordMe1));
        verifier.verifyTrace(Expectations.event("PluginExample", recordMe2));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
