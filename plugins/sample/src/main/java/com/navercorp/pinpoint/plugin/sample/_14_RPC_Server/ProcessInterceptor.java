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
package com.navercorp.pinpoint.plugin.sample._14_RPC_Server;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanId;
import com.navercorp.pinpoint.bootstrap.context.SpanRecorder;
import com.navercorp.pinpoint.bootstrap.context.Trace;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.context.TraceId;
import com.navercorp.pinpoint.bootstrap.interceptor.SpanSimpleAroundInterceptor;
import com.navercorp.pinpoint.bootstrap.util.NumberUtils;
import com.navercorp.pinpoint.common.trace.ServiceType;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;
import com.navercorp.plugin.sample.target.TargetClass14_Request;
import com.navercorp.plugin.sample.target.TargetClass14_Server;

/**
 * You'd better extend {@link SpanSimpleAroundInterceptor} to write a server application interceptor.
 * 
 * @author Jongho Moon
 */
public class ProcessInterceptor extends SpanSimpleAroundInterceptor {
    public ProcessInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        super(traceContext, descriptor, ProcessInterceptor.class);
    }
    

    /**
     * In this method, you have to check if the current request contains following informations:
     * 
     * 1. Marker that indicates this transaction must not be traced
     * 2. Data required to continue tracing a transaction. transaction id, paraent id and so on. 
     * 
     * Then you have to create appropriate Trace object.
     */
    @Override
    protected Trace createTrace(Object target, Object[] args) {
        TargetClass14_Request request = (TargetClass14_Request)args[0];
        
        // If this transaction is not traceable, mark as disabled.
        if (request.getMetadata(SamplePluginConstants.META_DO_NOT_TRACE) != null) {
            return traceContext.disableSampling();
        }
        
        String transactionId = request.getMetadata(SamplePluginConstants.META_TRANSACTION_ID);

        // If there's no trasanction id, a new trasaction begins here. 
        if (transactionId == null) {
            return traceContext.newTraceObject();
        }

        // otherwise, continue tracing with given data.
        long parentSpanID = NumberUtils.parseLong(request.getMetadata(SamplePluginConstants.META_PARENT_SPAN_ID), SpanId.NULL);
        long spanID = NumberUtils.parseLong(request.getMetadata(SamplePluginConstants.META_SPAN_ID), SpanId.NULL);
        short flags = NumberUtils.parseShort(request.getMetadata(SamplePluginConstants.META_FLAGS), (short) 0);
        TraceId traceId = traceContext.createTraceId(transactionId, parentSpanID, spanID, flags);

        return traceContext.continueTraceObject(traceId);
    }
    
    
    @Override
    protected void doInBeforeTrace(SpanRecorder recorder, Object target, Object[] args) {
        TargetClass14_Server server = (TargetClass14_Server)target;
        TargetClass14_Request request = (TargetClass14_Request)args[0];
        
        // You have to record a service type within Server range. 
        recorder.recordServiceType(SamplePluginConstants.MY_RPC_SERVER_SERVICE_TYPE);
        
        // Record rpc name, client address, server address.
        recorder.recordRpcName(request.getProcedure());
        recorder.recordEndPoint(server.getAddress());
        recorder.recordRemoteAddress(request.getClientAddress());

        // If this transaction did not begin here, record parent(client who sent this request) information 
        if (!recorder.isRoot()) {
            String parentApplicationName = request.getMetadata(SamplePluginConstants.META_PARENT_APPLICATION_NAME);
            
            if (parentApplicationName != null) {
                short parentApplicationType = NumberUtils.parseShort(request.getMetadata(SamplePluginConstants.META_PARENT_APPLICATION_TYPE), ServiceType.UNDEFINED.getCode());
                recorder.recordParentApplication(parentApplicationName, parentApplicationType);
                
                String serverHostName = request.getServerHostName();
                
                if (serverHostName != null) {
                    recorder.recordAcceptorHost(serverHostName);
                } else {
                    recorder.recordAcceptorHost(server.getAddress());
                }
            }
        }
    }

    @Override
    protected void doInAfterTrace(SpanRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        TargetClass14_Request request = (TargetClass14_Request)args[0];

        recorder.recordApi(methodDescriptor);
        recorder.recordAttribute(SamplePluginConstants.MY_RPC_ARGUMENT_ANNOTATION_KEY, request.getArgument());
        
        if (throwable == null) {
            recorder.recordAttribute(SamplePluginConstants.MY_RPC_RESULT_ANNOTATION_KEY, result);
        } else {
            recorder.recordException(throwable);
        }
    }
}
