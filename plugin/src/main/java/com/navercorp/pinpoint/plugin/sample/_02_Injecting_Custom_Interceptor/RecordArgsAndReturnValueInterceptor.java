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
package com.navercorp.pinpoint.plugin.sample._02_Injecting_Custom_Interceptor;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.Trace;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor0;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor1;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor2;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor3;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor4;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor5;
import com.navercorp.pinpoint.bootstrap.interceptor.SpanEventSimpleAroundInterceptorForPlugin;
import com.navercorp.pinpoint.bootstrap.interceptor.StaticAroundInterceptor;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;
import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

/**
 * <strong>It is strongly recommended that you extend SpanEventSimpleAroundInterceptorForPlugin.</strong>
 * It contains most of the boiler plate code for implementing a basic interceptor.
 * <p>
 * This interceptor shows how to record a method invocation with it's arguments and return value.
 * <p>
 * An interceptor have to implement one of following interfaces:
 * <ul>
 * <li>{@link AroundInterceptor}</li>
 * <li>{@link AroundInterceptor0}</li>
 * <li>{@link AroundInterceptor1}</li>
 * <li>{@link AroundInterceptor2}</li>
 * <li>{@link AroundInterceptor3}</li>
 * <li>{@link AroundInterceptor4}</li>
 * <li>{@link AroundInterceptor5}</li>
 * <li>{@link StaticAroundInterceptor}</li>
 * </ul>
 * Differences between these interfaces are, number of arguments the intercepter receives and at which point the target
 * method is intercepted.
 * <p>
 * This sample interceptor implements AroundInterceptor1 which intercepts before and after the target method execution,
 * and receives one argument of the method.
 *
 * @see SpanEventSimpleAroundInterceptorForPlugin
 */
public class RecordArgsAndReturnValueInterceptor implements AroundInterceptor1 {
    // You have to use PLogger for logging because you don't know which logging library the target application uses. 
    private final PLogger logger = PLoggerFactory.getLogger(getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;

    // An interceptor receives Pinpoint objects as constructor arguments. 
    public RecordArgsAndReturnValueInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        this.traceContext = traceContext;
        this.descriptor = descriptor;
    }
    
    @Override
    public void before(Object target, Object arg0) {
        if (isDebug) {
            logger.beforeInterceptor(target, new Object[] { arg0 } );
        }

        // 1. Get Trace. It's null when current transaction is not being profiled.
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        // 2. Begin a trace block.
        trace.traceBlockBegin();
    }

    @Override
    public void after(Object target, Object arg0, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, new Object[] { arg0 });
        }

        // 1. Get Trace.
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        try {
            // 2. Get current span event recorder
            SpanEventRecorder recorder = trace.currentSpanEventRecorder();

            // 3. Record service type
            recorder.recordServiceType(SamplePluginConstants.MY_SERVICE_TYPE);
            
            // 4. record method signature and arguments 
            recorder.recordApi(descriptor, new Object[] { arg0 });
            
            // 5. record exception if any.
            recorder.recordException(throwable);
            
            // 6. Trace doesn't provide a method to record return value. You have to record it as an attribute.
            recorder.recordAttribute(AnnotationKey.RETURN_DATA, result);
        } finally {
            // 7. End trace block.
            trace.traceBlockEnd();
        }
    }
}
