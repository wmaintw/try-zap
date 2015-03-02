# Integrate ZAP in CI

The idea is to integrate ZAP Proxy into CI server, let it scan or analysis security vulnerabilities while running regular functional test.

### Quick Start

You just need several steps to benefit from ZAP Proxy.

**step 1, Apply zap gradle plugin**

```gradle
apply plugin: "zap-plugin"

// TODO: Add zap plugin repo url
```

**step 2, Config gradle 'test' task**

```gradle
test {
    systemProperty 'zap.proxy', System.properties['zap.proxy']
}
```

**step 3, Setup proxy setting for WebDriver**

```java
public WebDriver setupDriverProxy() {
    DesiredCapabilities cap = new DesiredCapabilities();

    String zapProxySetting = getProperty(ZAP_PROXY_KEY);
    if (StringUtils.isNotBlank(zapProxySetting)) {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(zapProxySetting)
                .setFtpProxy(zapProxySetting)
                .setSslProxy(zapProxySetting);
        cap.setCapability(PROXY, proxy);
    }

    return new FirefoxDriver(cap);
}
```

**step 4, Run gradle task**

```sh
$ gradle zapStart build -Dzap.proxy=localhost:7070
```

The `zapStart` task will launch ZAP proxy, and the 'build' task will run functional tests, after that some security alerts will be generated by ZAP proxy, you can run following gradle task to generate a report based on these alerts:

```gradle
gradle zapReport
```

**step 5, Stop zap proxy**

This task will stop ZAP proxy

```gradle
gradle zapStop
```

### Report
The security report will be generated under **zap-report** folder. There are 2 report files:

* zap-alerts-{timestamp}.html
* zap-alerts-{timestamp}.json