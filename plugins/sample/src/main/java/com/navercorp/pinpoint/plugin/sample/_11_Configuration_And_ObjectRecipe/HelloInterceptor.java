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
package com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.Trace;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor1;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

/**
 */
public class HelloInterceptor implements AroundInterceptor1 {
    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;
    private final StringTrimmer trimmer;

    public HelloInterceptor(TraceContext traceContext, MethodDescriptor descriptor, StringTrimmer trimmer) {
        this.traceContext = traceContext;
        this.descriptor = descriptor;
        this.trimmer = trimmer;
    }
    
    @Override
    public void before(Object target, Object arg0) {
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        trace.traceBlockBegin();
    }

    @Override
    public void after(Object target, Object arg0, Object result, Throwable throwable) {
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        try {
            String stringArg = trimmer.trim((String)arg0);
                    
            SpanEventRecorder recorder = trace.currentSpanEventRecorder();
            recorder.recordServiceType(SamplePluginConstants.MY_SERVICE_TYPE);
            recorder.recordApi(descriptor, new Object[] { stringArg });
            recorder.recordException(throwable);
        } finally {
            trace.traceBlockEnd();
        }
    }
}
