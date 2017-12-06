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
package com.navercorp.pinpoint.plugin.sample._12_Asynchronous_Trace;

import com.navercorp.pinpoint.bootstrap.context.AsyncContext;
import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.interceptor.AsyncContextSpanEventSimpleAroundInterceptor;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

/**
 * This interceptor intercepts run() method, which handles the async task.
 * 
 * Pinpoint provides {@link AsyncContextSpanEventSimpleAroundInterceptor} which handles all the chores related to
 * continuing traces beyond thread boundaries.
 * <br/>
 * {@link AsyncContextSpanEventSimpleAroundInterceptor} retrieves the {@link AsyncContext} from the target (this) object
 * via {@link com.navercorp.pinpoint.bootstrap.async.AsyncContextAccessor AsyncContextAccessor}.
 * Therefore the target class <strong>must</strong> be transformed to be an instance of <tt>AsyncContextAccessor</tt>
 * and have the <tt>AsyncContext</tt> field.
 * <br/>
 * In this sample, {@link WorkerConstructorInterceptor} sets the <tt>AsyncContext</tt> to <tt>this</tt> object.
 * 
 * @author Jongho Moon
 */
public class WorkerRunInterceptor extends AsyncContextSpanEventSimpleAroundInterceptor {

    public WorkerRunInterceptor(TraceContext traceContext, MethodDescriptor methodDescriptor) {
        super(traceContext, methodDescriptor);
    }

    @Override
    protected void doInBeforeTrace(SpanEventRecorder recorder, AsyncContext asyncContext, Object target, Object[] args) {
        // do nothing
    }

    @Override
    protected void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        recorder.recordServiceType(SamplePluginConstants.MY_SERVICE_TYPE);
        recorder.recordApi(methodDescriptor);
        recorder.recordException(throwable);
    }
}
