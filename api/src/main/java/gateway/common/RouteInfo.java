package gateway.common;

import io.netty.handler.codec.http.HttpMethod;

public class RouteInfo {
    private HttpMethod httpMethod;
    private String uri;
    ProxiedService proxiedService;

    public RouteInfo(HttpMethod httpMethod, String uri, ProxiedService proxiedService) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.proxiedService = proxiedService;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ProxiedService getProxiedService() {
        return proxiedService;
    }

    public void setProxiedService(ProxiedService proxiedService) {
        this.proxiedService = proxiedService;
    }
}
