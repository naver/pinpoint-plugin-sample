package com.navercorp.pinpoint.collector;

import com.navercorp.pinpoint.common.server.util.ServerBootLogger;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.context.annotation.ImportResource;

@SpringBootConfiguration
@EnableAutoConfiguration
@ImportResource({ "classpath:applicationContext-collector.xml", "classpath:servlet-context-collector.xml"})
public class PinpointCollectorApp {
    private static final ServerBootLogger logger = ServerBootLogger.getLogger(PinpointCollectorApp.class);

    public static void main(String[] args) {
        try {
            CollectorStarter app = new CollectorStarter(PinpointCollectorApp.class);
            app.start(args);
        } catch (Exception exception) {
            logger.error("[PinpointCollectorApp] could not launch app.", exception);
        }
    }

}