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

import com.navercorp.pinpoint.pluginit.utils.PluginITConstants;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.plugin.test.Expectations;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass01;

/**
 * @see Sample_01_Inject_BasicMethodInterceptor
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_01_Injecting_BasicMethodInterceptor_IT {

    @Test
    public void test() throws Exception {
        String name = "Pinpoint";

        TargetClass01 target = new TargetClass01();
        target.targetMethod(name);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method targetMethod = TargetClass01.class.getMethod("targetMethod", String.class);
        verifier.verifyTrace(Expectations.event("PluginExample", targetMethod));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
