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

import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.common.trace.AnnotationKeyProvider;
import com.navercorp.pinpoint.common.trace.ServiceType;
import com.navercorp.pinpoint.common.trace.ServiceTypeProvider;

/**
 * @author Jongho Moon
 *
 */
public interface SamplePluginConstants {

    ServiceType MY_SERVICE_TYPE = ServiceTypeProvider.getByName("PluginExample");
    AnnotationKey ANNOTATION_KEY_MY_VALUE = AnnotationKeyProvider.getByCode(998);
    
    ServiceType MY_RPC_SERVER_SERVICE_TYPE = ServiceTypeProvider.getByName("SAMPLE_SERVER");
    
    ServiceType MY_RPC_CLIENT_SERVICE_TYPE = ServiceTypeProvider.getByName("SAMPLE_CLIENT");
    AnnotationKey MY_RPC_ARGUMENT_ANNOTATION_KEY = AnnotationKeyProvider.getByCode(995);
    AnnotationKey MY_RPC_PROCEDURE_ANNOTATION_KEY = AnnotationKeyProvider.getByCode(996);
    AnnotationKey MY_RPC_RESULT_ANNOTATION_KEY = AnnotationKeyProvider.getByCode(997);

    String META_DO_NOT_TRACE = "_SAMPLE_DO_NOT_TRACE";
    String META_TRANSACTION_ID = "_SAMPLE_TRANSACTION_ID";
    String META_SPAN_ID = "_SAMPLE_SPAN_ID";
    String META_PARENT_SPAN_ID = "_SAMPLE_PARENT_SPAN_ID";
    String META_PARENT_APPLICATION_NAME = "_SAMPLE_PARENT_APPLICATION_NAME";
    String META_PARENT_APPLICATION_TYPE = "_SAMPLE_PARENT_APPLICATION_TYPE";
    String META_FLAGS = "_SAMPLE_FLAGS";
    
}
