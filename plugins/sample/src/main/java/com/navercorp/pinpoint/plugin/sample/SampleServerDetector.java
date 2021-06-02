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

import com.navercorp.pinpoint.bootstrap.resolver.condition.MainClassCondition;
import com.navercorp.pinpoint.common.trace.ServiceType;

/**
 * @author Jongho Moon
 *
 */
public final class SampleServerDetector {

    private static final String MAIN_CLASS = "com.navercorp.plugin.sample.target.TargetClass14_Server";

    public ServiceType getApplicationType() {
        return SamplePluginConstants.MY_RPC_SERVER_SERVICE_TYPE;
    }

    public boolean detect() {
        if (MainClassCondition.INSTANCE.check(MAIN_CLASS)) {
            return true;
        }
        return false;
    }
}