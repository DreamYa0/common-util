package com.g7.framwork.common.util.bean;

import java.io.Serializable;

public class URLData implements Serializable {

    private static final long serialVersionUID = -5898321902042606342L;
    private static final String PATH_PREFIX = "/router";
    private static final String SEPARATOR = "/";
    private final String protocol;
    private final String host;
    private final String owner;
    private final int port;
    private final String serviceName;
    private final String version;
    private final String group;
    private final String methodName;
    private final Object[] types;
    private final String[] names;
    private final String simpleServiceName;
    private final String application;
    private final String invokeURL;

    /**
     * 测试专用
     */
    protected URLData() {
        this.types = null;
        this.names = null;
        this.host = "127.0.0.1";
        this.protocol = "test";
        this.port = 8080;
        this.serviceName = "test";
        this.owner = "tester";
        this.group = "test_group";
        this.version = "0.0.1";
        this.methodName = "test_method";
        this.simpleServiceName = "simple_test";
        this.application = "app_test";
        this.invokeURL = null;
    }

    public URLData(String protocol,
                   String host,
                   String applicaiton,
                   int port,
                   String[] names,
                   Object[] types,
                   String version,
                   String group,
                   String serviceName,
                   String methodName,
                   String owner) {

        this.serviceName = serviceName;
        this.protocol = protocol;
        this.host = host;
        this.port = (Math.max(port, 0));
        this.owner = owner;
        this.version = version;
        this.group = group;
        this.methodName = methodName;
        this.names = names;
        this.types = types;
        this.simpleServiceName = getSimpleServiceName(serviceName);
        this.application = applicaiton;
        this.invokeURL = null;
    }

    public URLData(String protocol,
                   String host,
                   int port,
                   String application,
                   String serviceName,
                   String methodName,
                   String owner,
                   String version,
                   String invokeURL) {

        this.serviceName = serviceName;
        this.protocol = protocol;
        this.owner = owner;
        this.version = version;
        this.methodName = methodName;
        this.simpleServiceName = getSimpleServiceName(serviceName);
        this.invokeURL = invokeURL;
        this.types = null;
        this.names = null;
        this.host = host;
        this.port = port;
        this.application = application;
        this.group = null;


    }

    private String getSimpleServiceName(String serviceName) {
        return serviceName.substring(serviceName.lastIndexOf(".") + 1);

    }

    public String getAddress() {
        return port <= 0 ? host : host + ":" + port;
    }

    public String generateURL() {
        return PATH_PREFIX + SEPARATOR + application + SEPARATOR +
                serviceName + SEPARATOR +
                methodName + SEPARATOR +
                host + ":" + port;
    }

    public String getSimpleServiceName() {
        return simpleServiceName;
    }

    public String getServiceNodePath() {
        StringBuffer sb = new StringBuffer(PATH_PREFIX + SEPARATOR);
        sb.append(application).append(SEPARATOR)
                .append(serviceName);
        return sb.toString();
    }

    public String getMethodNodePath() {
        StringBuffer sb = new StringBuffer(PATH_PREFIX + SEPARATOR);
        sb.append(application).append(SEPARATOR)
                .append(serviceName).append(SEPARATOR)
                .append(methodName);
        return sb.toString();
    }

    public String getApplication() {
        return application;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getOwner() {
        return owner;
    }

    public String getVersion() {
        return version;
    }

    public String getGroup() {
        return group;
    }

    public Object[] getTypes() {
        return types;
    }

    public String[] getNames() {
        return names;
    }

    public String getInvokeURL() {
        return invokeURL;
    }
}
