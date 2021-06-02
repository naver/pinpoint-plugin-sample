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
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass13_Client;
import com.navercorp.plugin.sample.target.TargetClass13_Request;

/**
 *  
 * 
 * @see Sample_13_RPC_Client_IT
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:" + SampleTestConstants.VERSION})
public class Sample_13_RPC_Client_IT {

    @Test
    public void test() throws Exception {
        TargetClass13_Client client = new TargetClass13_Client("1.2.3.4", 5678);
        TargetClass13_Request request = new TargetClass13_Request("sample", "hello", "pinpoint");
        
        client.sendRequest(request);
        
        Method sendRequest = TargetClass13_Client.class.getMethod("sendRequest", TargetClass13_Request.class);

        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();

        verifier.verifyTrace(event("SAMPLE_CLIENT", sendRequest, null, "1.2.3.4:5678", "sample", annotation("MY_PROCEDURE", "hello"), annotation("MY_ARGUMENT", "pinpoint"), annotation("MY_RESULT", "SUCCESS")));

        verifier.verifyTraceCount(0);
    }
}
