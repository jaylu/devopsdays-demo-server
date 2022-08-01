# devopsdays-demo-server

A demo project for 2022 DevOpsDays Guangzhou.

## Run from local

Basically what you need is run `RunLocalServer.java`

### If dependency [cranker-connector](https://github.com/hsbc/cranker-connector) is missing

```xml
<dependency>
    <groupId>com.hsbc.cranker</groupId>
    <artifactId>cranker-connector</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Up to 2022-08-02, the **cranker-connector** haven't been published to central maven repository. To use it, you may need to clone and build it from local first. below is the steps

1. `git clone https://github.com/hsbc/cranker-connector.git`
2. `cd cranker-connector && mvn clean install`

After that, the demo project should be able to be built and run.

### If you are connecting to cranker with `wss://`, but server is given invalid cert e.g. self-signed certificate

```java
// you may need a trust all host cert http client
.withHttpClient(CrankerConnectorBuilder.createHttpClient(true).build())

// or disable ssl host name verification globally
System.getProperties().setProperty("jdk.internal.httpclient.disableHostnameVerification", "true")
```

In production, please DO NOT ignore the ssl host name verification, your server should provide a valid cert.
