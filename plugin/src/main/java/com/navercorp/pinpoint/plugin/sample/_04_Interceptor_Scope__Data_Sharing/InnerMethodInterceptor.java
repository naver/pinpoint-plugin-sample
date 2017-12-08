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
package com.navercorp.pinpoint.plugin.sample._04_Interceptor_Scope__Data_Sharing;

import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor0;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.InterceptorScope;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.InterceptorScopeInvocation;

/**
 * This interceptor uses {@link InterceptorScopeInvocation} to receive <tt>MyAttachment</tt> for a flag to see if the
 * current transaction is being traced. If so, the return value is then set to the attachment so that it can be passed
 * on to {@link OuterMethodInterceptor}.
 */
public class InnerMethodInterceptor implements AroundInterceptor0 {
    private final InterceptorScope scope;

    // An interceptor receives an InterceptorScope through its constructor
    public InnerMethodInterceptor(InterceptorScope scope) {
        this.scope = scope;
    }

    @Override
    public void before(Object target) {

    }

    @Override
    public void after(Object target, Object result, Throwable throwable) {
        MyAttachment attachment = (MyAttachment)scope.getCurrentInvocation().getAttachment();
        
        if (attachment.isTrace()) {
            attachment.setValue(result);
        }
    }
}
