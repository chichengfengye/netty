package gateway.handler;

import gateway.ProxyServer;
import gateway.common.ProxiedService;
import gateway.common.User;

/**
 * 认证 与 鉴权
 *
 */
public interface AuthHandler {

    /**
     * 用户是否存在 是否有效
     * @Params:
     *  userId : 用户名，用户主键，用户key 等等能够用来唯一标识用户的属性值
     *
     * @return
     */
//    User authUser(String userId);

    /**
     * 判断用户是否被允许访问该接口
     *
     * @param proxyService : 被代理的服务
     * @param key ： 用户傳來的key
     * @return
     */
    boolean isAccessible(ProxiedService proxyService, String key);

}