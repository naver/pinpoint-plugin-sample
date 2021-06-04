# Pinpoint Profiler Plugin Sample

You can extend Pinoint's profiling ability by writing a Pinpoint profiler plugin. This sample project shows how to write it. It consists of 7 modules:

* sample-pinpoint-plugin-target: target library
* sample-pinpoint-plugins: sample plugin
  * sample-pinpoint-plugin
* sample-pinpoint-plugins-it
  * sample-pinpoint-plugin-it
* sample-pinpoint-agent: agent distribution with sample plugin
* sample-pinpoint-agent-testweb
  * sample-pinpoint-plugin-testweb
* sample-pinpoint-collector
* sample-pinpoint-web

# Implementing a Profiler Plugin
A Pinpoint profiler plugin have to provide implementations of [ProfilerPlugin](https://github.com/naver/pinpoint/blob/master/bootstrap-core/src/main/java/com/navercorp/pinpoint/bootstrap/plugin/ProfilerPlugin.java) and [TraceMetadataProvider](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/TraceMetadataProvider.java)
`ProfilerPlugin` is used by Pinpoint Agent only while `TraceMetadataProvider` is used by Pinpoint Agent, Collector and Web.

Pinpoint loads these implementations by Java's [ServiceLoader](https://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html). So an plugin JAR must contains two provider-configuration files.

* META-INF/services/com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin
* META-INF/services/com.navercorp.pinpoint.common.trace.TraceMetadataProvider 

Each file should contains fully qualified names of the implementation classes.


### TraceMetadataProvider
A TraceMetadataProvider adds [ServiceType](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/ServiceType.java)s and [AnnotationKey](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/AnnotationKey.java)s to Pinpoint.

Both ServiceType and AnnotationKey's code value must be unique. If you're writing a private plugin, you can use code values reserved for private usage. Pinpoint will not assign these values to anything. Otherwise you have to contact Pinpoint dev team to allocate codes for the plugin. 

* ServiceType codes for private use
  * Server: 1900 ~ 1999
  * DB client: 2900 ~ 2999
  * Cache client: 8900 ~ 8999
  * RPC client: 9900 ~ 9999
  * Others: 7500 ~ 7999

* AnnotaionKey codes for private use
  * 900 ~ 999


### ProfilerPlugin
A ProfilerPlugin adds [TransformCallback](https://github.com/naver/pinpoint/blob/master/bootstrap-core/src/main/java/com/navercorp/pinpoint/bootstrap/instrument/transformer/TransformCallback.java)s to Pinpoint.

A TransformCallback transforms a target class by adding interceptors, getters and/or fields. You can find example codes in plugin-sample-plugin project.


# Integration Test
You can run plugin integration tests (`mvn integration-test`) with [PinointPluginTestSuite](https://github.com/naver/pinpoint/blob/master/test/src/main/java/com/navercorp/pinpoint/test/plugin/PinpointPluginTestSuite.java), a JUnit Runner. It downloads required dependencies from maven repositories and launch a new JVM with Pinpoint profiler agent and dependencies. On that JVM, JUnit tests are executed.

To run plugin integration test, it needs a complete agent distribution. That's why integration tests are in plugin-sample-agent module.

In test, you can use [PluginTestVerifier](https://github.com/naver/pinpoint/blob/master/bootstrap-core/src/main/java/com/navercorp/pinpoint/bootstrap/plugin/test/PluginTestVerifier.java) to check if traces are recorded correctly.


#### Test Dependency
PinointPluginTestSuite doesn't use the project's dependencies (configured at pom.xml). It uses dependencies listed by @Dependency. In this way, you can test multiple versions of the target library.

Dependencies are declared like this. You can specify versions or version ranges of a dependency.
```
@Dependency({"some.group:some-artifact:1.0", "another.group:another-artifact:2.1-RELEASE"})
@Dependency({"some.group:some-artifact:[1.0,)"})
@Dependency({"some.group:some-artifact:[1.0,1.9]"})
@Dependency({"some.group:some-artifact:[1.0],[2.1],[3.2])"})
```
PinointPluginTestSuite searches dependencies from local repository and maven central repository. You can add repositories by @Repository.

#### Jvm Version
You can specify the JVM version for a test by @JvmVersion.

#### Application Test
PinpointPluginTestSuite is not for an application which has to be launched by its own main class. You can extends [AbstractPinpointPluginTestSuite](https://github.com/naver/pinpoint/blob/master/test/src/main/java/com/navercorp/pinpoint/test/plugin/AbstractPinpointPluginTestSuite.java) and related types to test such applications. 

