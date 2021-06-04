/**
 * Copyright 2014 https://github.com/lioolli/pinpoint-plugin-samplehttps://github.com/lioolli/pinpoint-plugin-sampleNAVER Corp.
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

import com.navercorp.pinpoint.bootstrap.plugin.test.Expectations;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.sample._03_Interceptor_Scope__Prevent_Duplicated_Trace.Sample_03_Use_Interceptor_Scope_To_Prevent_Duplicated_Trace;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass03;

/**
 * Both {@link TargetClass03#invoke()} and {@link TargetClass03#invoke(int)} have interceptors injected and are traced.
 * <p>
 * But these interceptors share the same interceptor scope and their execution policies are set to BOUNDARY telling them
 * to <em>execute only when no other interceptor in the same scope is active</em>.
 * <p>
 * So in {@link #testA()}, only {@link TargetClass03#invoke()} is recorded and in {@link #testB()}, only
 * {@link TargetClass03#invoke(int)} is recorded.
 * 
 * @see Sample_03_Use_Interceptor_Scope_To_Prevent_Duplicated_Trace
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_03_Use_Interceptor_Group_To_Prevent_Duplicated_Trace_IT {

    @Test
    public void testA() throws Exception {
        TargetClass03 target = new TargetClass03();
        target.invoke();
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method targetMethod = TargetClass03.class.getMethod("invoke");
        verifier.verifyTrace(Expectations.event("PluginExample", targetMethod));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
    
    @Test
    public void testB() throws Exception {
        TargetClass03 target = new TargetClass03();
        target.invoke(3);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method targetMethod = TargetClass03.class.getMethod("invoke", int.class);
        verifier.verifyTrace(Expectations.event("PluginExample", targetMethod));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
