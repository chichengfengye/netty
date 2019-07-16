package gateway.common;

import java.util.List;

/**
 * 被代理的服务（接口）
 */
public class ProxiedService {
    private String proxiedUrl;
    private String exposeUrl;
    private List<String> keys;

    public String getProxiedUrl() {
        return proxiedUrl;
    }

    public void setProxiedUrl(String proxiedUrl) {
        this.proxiedUrl = proxiedUrl;
    }

    public String getExposeUrl() {
        return exposeUrl;
    }

    public void setExposeUrl(String exposeUrl) {
        this.exposeUrl = exposeUrl;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
