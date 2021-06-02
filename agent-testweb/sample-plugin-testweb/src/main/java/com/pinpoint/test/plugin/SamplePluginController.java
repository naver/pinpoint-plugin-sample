/*
 * Copyright 2020 NAVER Corp.
 *
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

package com.pinpoint.test.plugin;

import com.navercorp.plugin.sample.target.TargetClass01;
import com.navercorp.plugin.sample.target.TargetClass02;
import com.navercorp.plugin.sample.target.TargetClass03;
import com.navercorp.plugin.sample.target.TargetClass04;
import com.navercorp.plugin.sample.target.TargetClass05;
import com.navercorp.plugin.sample.target.TargetClass06;
import com.navercorp.plugin.sample.target.TargetClass07;
import com.navercorp.plugin.sample.target.TargetClass09;
import com.navercorp.plugin.sample.target.TargetClass10_Consumer;
import com.navercorp.plugin.sample.target.TargetClass10_Producer;
import com.navercorp.plugin.sample.target.TargetClass11;
import com.navercorp.plugin.sample.target.TargetClass12_AsyncInitiator;
import com.navercorp.plugin.sample.target.TargetClass12_Future;
import com.navercorp.plugin.sample.target.TargetClass13_Client;
import com.navercorp.plugin.sample.target.TargetClass13_Request;
import com.navercorp.plugin.sample.target.TargetClass14_Request;
import com.navercorp.plugin.sample.target.TargetClass14_Server;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SamplePluginController {
    private static final String OK = "OK";

    @RequestMapping(value = "/sample/01", method = RequestMethod.GET)
    @ResponseBody
    public String sample01() {
        String name = "Sample01";
        TargetClass01 target = new TargetClass01();

        return target.targetMethod(name);
    }

    @RequestMapping(value = "/sample/02", method = RequestMethod.GET)
    @ResponseBody
    public String sample02() {
        String name = "Sample02";
        TargetClass02 target = new TargetClass02();

        return target.targetMethod(name);
    }

    @RequestMapping(value = "/sample/03A", method = RequestMethod.GET)
    @ResponseBody
    public String sample03A() {
        TargetClass03 target = new TargetClass03();
        target.invoke();

        return OK;
    }

    @RequestMapping(value = "/sample/03B", method = RequestMethod.GET)
    @ResponseBody
    public String sample03B() {
        TargetClass03 target = new TargetClass03();
        target.invoke(3);

        return OK;
    }

    @RequestMapping(value = "/sample/04", method = RequestMethod.GET)
    @ResponseBody
    public String sample04() {
        String name = "FOOBAR";
        int length = name.length();

        TargetClass04 target = new TargetClass04();
        target.outerMethod(name);

        return OK;
    }

    @RequestMapping(value = "/sample/05", method = RequestMethod.GET)
    @ResponseBody
    public String sample05() {
        String name = "Pinpoint";

        TargetClass05 target = new TargetClass05(name);
        return target.getName();
    }

    @RequestMapping(value = "/sample/06", method = RequestMethod.GET)
    @ResponseBody
    public String sample06() {
        // Invoke 0-arg constructor. It calls 1-arg constructor.
        TargetClass06 target0 = new TargetClass06();
        TargetClass06 target1 = new TargetClass06(1);

        return OK;
    }

    @RequestMapping(value = "/sample/07", method = RequestMethod.GET)
    @ResponseBody
    public String sample07() {
        TargetClass07 target = new TargetClass07();
        target.recordMe();
        target.recordMe(0);
        target.recordMe("maru");
        target.logMe();
        target.logMe("morae");

        return OK;
    }

    @RequestMapping(value = "/sample/08", method = RequestMethod.GET)
    @ResponseBody
    public String sample08() {
        // Invoke 0-arg constructor. It calls 1-arg constructor.
        TargetClass06 target0 = new TargetClass06();
        TargetClass06 target1 = new TargetClass06(1);

        return OK;
    }

    @RequestMapping(value = "/sample/09", method = RequestMethod.GET)
    @ResponseBody
    public String sample09() {
        String name = "Pinpoint";
        TargetClass09 target = new TargetClass09(name);
        target.targetMethod();

        return OK;
    }

    @RequestMapping(value = "/sample/10", method = RequestMethod.GET)
    @ResponseBody
    public String sample10() {
        String name = "Pinpoint";
        TargetClass10_Producer producer = new TargetClass10_Producer(name);
        TargetClass10_Consumer consumer = new TargetClass10_Consumer();
        consumer.consume(producer.produce());

        return OK;
    }

    @RequestMapping(value = "/sample/11", method = RequestMethod.GET)
    @ResponseBody
    public String sample11() {
        String name = "Pinpoint Agent";
        TargetClass11 target = new TargetClass11();
        target.hello(name);

        return OK;
    }

    @RequestMapping(value = "/sample/12", method = RequestMethod.GET)
    @ResponseBody
    public String sample12() throws InterruptedException {
        TargetClass12_AsyncInitiator initiator = new TargetClass12_AsyncInitiator();
        TargetClass12_Future future = initiator.asyncHello("Pinpoint");
        String result = future.get();

        return result;
    }

    @RequestMapping(value = "/sample/13", method = RequestMethod.GET)
    @ResponseBody
    public String sample13() {
        TargetClass13_Client client = new TargetClass13_Client("1.2.3.4", 5678);
        TargetClass13_Request request = new TargetClass13_Request("sample", "hello", "pinpoint");

        return client.sendRequest(request);
    }

    @RequestMapping(value = "/sample/14", method = RequestMethod.GET)
    @ResponseBody
    public String sample14() {
        Map<String, String> metadatas = new HashMap<String, String>();
        metadatas.put("_SAMPLE_TRASACTION_ID", "frontend.agent^1234567890^123");
        metadatas.put("_SAMPLE_SPAN_ID", "9876543210");
        metadatas.put("_SAMPLE_PARENT_SPAN_ID", "1357913579");
        metadatas.put("_SAMPLE_PARENT_APPLICATION_NAME", "sample.client");
        metadatas.put("_SAMPLE_PARENT_APPLICATION_TYPE", "1000");
        metadatas.put("_SAMPLE_FLAGS", "0");

        TargetClass14_Server server = new TargetClass14_Server("1.2.3.4");
        TargetClass14_Request request = new TargetClass14_Request("5.6.7.8", "sample.pinpoint.navercorp.com", "hello", "pinpoint", metadatas);
        server.process(request);

        return OK;
    }
}
