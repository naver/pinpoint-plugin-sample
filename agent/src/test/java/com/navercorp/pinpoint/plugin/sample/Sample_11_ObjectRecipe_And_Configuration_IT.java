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

import com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.Sample_11_Configuration_And_ObjectRecipe;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.plugin.test.Expectations;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointConfig;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass11;

/**
 * This test uses @PinpointConfig to specify configuration file. 
 * 
 * @see Sample_11_Configuration_And_ObjectRecipe
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@PinpointConfig("pinpoint-sample11.config")
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_11_ObjectRecipe_And_Configuration_IT {

    @Test
    public void test() throws Exception {
        String name = "Pinpoint Agent";

        TargetClass11 target = new TargetClass11();
        target.hello(name);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method hello = TargetClass11.class.getMethod("hello", String.class);
        verifier.verifyTrace(Expectations.event("PluginExample", hello, Expectations.args("Pinpo...")));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
