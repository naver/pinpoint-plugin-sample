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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.plugin.test.Expectations;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass12_AsyncInitiator;
import com.navercorp.plugin.sample.target.TargetClass12_Future;
import com.navercorp.plugin.sample.target.TargetClass12_Worker;

/**
 *  
 * 
 * @see Sample_12_Asynchronous_Trace_IT
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_12_Asynchronous_Trace_IT {

    @Test
    public void test() throws Exception {
        TargetClass12_AsyncInitiator initiator = new TargetClass12_AsyncInitiator();
        TargetClass12_Future future = initiator.asyncHello("Pinpoint");
        String result = future.get();
        
        Assert.assertEquals("Hello Pinpoint", result);
        
        
        
        Method hello = TargetClass12_AsyncInitiator.class.getMethod("asyncHello", String.class);
        Method run = TargetClass12_Worker.class.getMethod("run");
        Method get = TargetClass12_Future.class.getMethod("get");

        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();

        verifier.verifyTrace(Expectations.async(
                Expectations.event("PluginExample", hello, Expectations.args("Pinpoint")),
                Expectations.event("ASYNC", "Asynchronous Invocation"),
                Expectations.event("PluginExample", run)));
        
        verifier.verifyTrace(Expectations.event("PluginExample", get));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
