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
package com.navercorp.pinpoint.plugin.sample._13_RPC_Client;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.Trace;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.context.TraceId;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor1;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;
import com.navercorp.plugin.sample.target.TargetClass13_Request;

/**
 * This interceptor shows how to records a RPC call and pass tracing data to the server.
 * 
 * @author Jongho Moon
 *
 */
public class SendRequestInterceptor implements AroundInterceptor1 {
    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;

    public SendRequestInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        this.descriptor = descriptor;
        this.traceContext = traceContext;
    }

    @Override
    public void before(Object target, Object arg0) {
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        TargetClass13_Request request = (TargetClass13_Request) arg0;

        if (trace.canSampled()) {
            SpanEventRecorder recorder = trace.traceBlockBegin();

            // RPC call trace have to be recorded with a service code in RPC client code range.
            recorder.recordServiceType(SamplePluginConstants.MY_RPC_CLIENT_SERVICE_TYPE);

            // You have to issue a TraceId the receiver of this request will use.
            TraceId nextId = trace.getTraceId().getNextTraceId();

            // Then record it as next span id.
            recorder.recordNextSpanId(nextId.getSpanId());

            // Finally, pass some tracing data to the server.
            // How to put them in a message is protocol specific.
            // This example assumes that the target protocol message can include any metadata (like HTTP headers).
            request.addMetadata(SamplePluginConstants.META_TRANSACTION_ID, nextId.getTransactionId());
            request.addMetadata(SamplePluginConstants.META_SPAN_ID, Long.toString(nextId.getSpanId()));
            request.addMetadata(SamplePluginConstants.META_PARENT_SPAN_ID, Long.toString(nextId.getParentSpanId()));
            request.addMetadata(SamplePluginConstants.META_PARENT_APPLICATION_TYPE, Short.toString(traceContext.getServerTypeCode()));
            request.addMetadata(SamplePluginConstants.META_PARENT_APPLICATION_NAME, traceContext.getApplicationName());
            request.addMetadata(SamplePluginConstants.META_FLAGS, Short.toString(nextId.getFlags()));
        } else {
            // If sampling this transaction is disabled, pass only that infomation to the server.  
            request.addMetadata(SamplePluginConstants.META_DO_NOT_TRACE, "1");
        }
    }

    @Override
    public void after(Object target, Object arg0, Object result, Throwable throwable) {
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        try {
            SpanEventRecorder recorder = trace.currentSpanEventRecorder();

            recorder.recordApi(descriptor);

            if (throwable == null) {
                // RPC client have to record end point (server address)
                String serverAddress = ((ServerAddressGetter) target)._$PREFIX$_getServerAddress();
                int port = ((ServerPortGetter) target)._$PREFIX$_getServerPort();
                recorder.recordEndPoint(serverAddress + ":" + port);

                TargetClass13_Request request = (TargetClass13_Request) arg0;
                // Optionally, record the destination id (logical name of server. e.g. DB name)
                recorder.recordDestinationId(request.getNamespace());
                recorder.recordAttribute(SamplePluginConstants.MY_RPC_PROCEDURE_ANNOTATION_KEY, request.getProcedure());
                recorder.recordAttribute(SamplePluginConstants.MY_RPC_ARGUMENT_ANNOTATION_KEY, request.getArgument());
                recorder.recordAttribute(SamplePluginConstants.MY_RPC_RESULT_ANNOTATION_KEY, result);
            } else {
                recorder.recordException(throwable);
            }
        } finally {
            trace.traceBlockEnd();
        }
    }
}
