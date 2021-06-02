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

import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplate;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplateAware;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext;
import com.navercorp.pinpoint.plugin.sample._01_Injecting_BasicMethodInterceptor.Sample_01_Inject_BasicMethodInterceptor;
import com.navercorp.pinpoint.plugin.sample._02_Injecting_Custom_Interceptor.Sample_02_Inject_Custom_Interceptor;
import com.navercorp.pinpoint.plugin.sample._03_Interceptor_Scope__Prevent_Duplicated_Trace.Sample_03_Use_Interceptor_Scope_To_Prevent_Duplicated_Trace;
import com.navercorp.pinpoint.plugin.sample._04_Interceptor_Scope__Data_Sharing.Sample_04_Interceptors_In_A_Scope_Share_Value;
import com.navercorp.pinpoint.plugin.sample._05_Constructor_Interceptor.Sample_05_Constructor_Interceptor;
import com.navercorp.pinpoint.plugin.sample._06_Constructor_Interceptor_Scope_Limitation.Sample_06_Constructor_Interceptor_Scope_Limitation;
import com.navercorp.pinpoint.plugin.sample._07_MethodFIlter.Sample_07_Use_MethodFilter_To_Intercept_Multiple_Methods;
import com.navercorp.pinpoint.plugin.sample._08_Interceptor_Annotations.Sample_08_Interceptor_Annotations;
import com.navercorp.pinpoint.plugin.sample._09_Adding_Getter.Sample_09_Adding_Getter;
import com.navercorp.pinpoint.plugin.sample._10_Adding_Field.Sample_10_Adding_Field;
import com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.Sample_11_Configuration_And_ObjectRecipe;
import com.navercorp.pinpoint.plugin.sample._12_Asynchronous_Trace.Sample_12_Asynchronous_Trace;
import com.navercorp.pinpoint.plugin.sample._13_RPC_Client.Sample_13_RPC_Client;
import com.navercorp.pinpoint.plugin.sample._14_RPC_Server.Sample_14_RPC_Server;

/**
 * Any Pinpoint profiler plugin must implement ProfilerPlugin interface.
 * ProfilerPlugin declares only one method {@link #setup(ProfilerPluginSetupContext)}.
 * You should implement the method to do whatever you need to setup your plugin with the passed ProfilerPluginSetupContext object.
 * 
 * @author Jongho Moon
 */
public class SamplePlugin implements ProfilerPlugin, TransformTemplateAware {
    private TransformTemplate transformTemplate;
    
    @Override
    public void setup(ProfilerPluginSetupContext context) {
        SampleServerDetector sampleServerDetector = new SampleServerDetector();
        if (sampleServerDetector.detect()) {
            context.registerApplicationType(sampleServerDetector.getApplicationType());
        }
        addTransformers();
    }

    private void addTransformers() {
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass01", Sample_01_Inject_BasicMethodInterceptor.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass02", Sample_02_Inject_Custom_Interceptor.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass03", Sample_03_Use_Interceptor_Scope_To_Prevent_Duplicated_Trace.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass04", Sample_04_Interceptors_In_A_Scope_Share_Value.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass05", Sample_05_Constructor_Interceptor.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass06", Sample_06_Constructor_Interceptor_Scope_Limitation.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass07", Sample_07_Use_MethodFilter_To_Intercept_Multiple_Methods.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass08", Sample_08_Interceptor_Annotations.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass09", Sample_09_Adding_Getter.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass10_Producer", Sample_10_Adding_Field.Producer.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass10_Consumer", Sample_10_Adding_Field.Consumer.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass10_Message", Sample_10_Adding_Field.Message.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass11", Sample_11_Configuration_And_ObjectRecipe.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass12_AsyncInitiator", Sample_12_Asynchronous_Trace.AsyncInitiator.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass12_Future", Sample_12_Asynchronous_Trace.Future.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass12_Worker", Sample_12_Asynchronous_Trace.Worker.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass13_Client", Sample_13_RPC_Client.class);
        transformTemplate.transform("com.navercorp.plugin.sample.target.TargetClass14_Server", Sample_14_RPC_Server.class);
    }

    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        this.transformTemplate = transformTemplate;
    }
}
