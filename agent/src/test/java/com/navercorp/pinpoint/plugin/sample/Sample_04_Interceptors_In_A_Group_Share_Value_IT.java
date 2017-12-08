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

import com.navercorp.pinpoint.bootstrap.interceptor.scope.InterceptorScopeInvocation;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.sample._04_Interceptor_Scope__Data_Sharing.Sample_04_Interceptors_In_A_Scope_Share_Value;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass04;

/**
 *  We want to trace {@link TargetClass04#outerMethod(java.lang.String)} when it's argument starts with "FOO". 
 *  In addition, we want to record the return value of {@link TargetClass04#innerMethod(java.lang.String)}, which is
 *  invoked by <tt>outerMethod()</tt> but don't want to record the invocation of <tt>innerMethod()</tt>.
 *  <p>
 *  We can do this by sharing data between the two interceptors through {@link InterceptorScopeInvocation}. When an
 *  <tt>InterceptorScopeInvocation</tt> is active, interceptors can attach objects to it and share them with other
 *  interceptors within the same scope.<br/>
 *  <tt>InterceptorScopeInvocation</tt> is activated for a scope when the <tt>before()</tt> method of any interceptors
 *  in the scope is invoked, but before the <tt>after()</tt> method is invoked.
 *  
 *  @see Sample_04_Interceptors_In_A_Scope_Share_Value
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_04_Interceptors_In_A_Group_Share_Value_IT {

    @Test
    public void testTrace() throws Exception {
        String name = "FOOBAR";
        int length = name.length();

        TargetClass04 target = new TargetClass04();
        target.outerMethod(name);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method targetMethod = TargetClass04.class.getMethod("outerMethod", String.class);
        verifier.verifyTrace(event("PluginExample", targetMethod, args(name)[0], annotation("MyValue", length)));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
