package gateway.handler;

import gateway.common.ProxiedService;

import java.util.Map;

public interface RouteHandler {

    /**
     * 获取被代理的服务
     *
     * @param registerUri
     * @return
     */
    ProxiedService getProxiedUri(String registerUri);


}
