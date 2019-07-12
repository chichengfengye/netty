package gateway.handler;

import gateway.common.ProxiedService;

import java.util.Map;

public interface RouteHandler {
    /**
     * 获取路径路由映射表
     * @return map<fromUri , toUri>
     */
    Map<String, String> getRouteMap();

    /**
     * 获取被代理的服务路径
     *
     * @param registerUri
     * @return
     */
    String getProxiedUri(String registerUri);

    /**
     * 获取注册了的被代理的服务列表
     *
     * @return Map<fromUri, serviceInfo>
     */
    Map<String, ProxiedService> getProxiedServicesMap();

}
