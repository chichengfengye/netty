package gateway.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gateway.common.Constant;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;

public class ParamDecodeHelper {
    public static JSONObject decodeParams(FullHttpRequest request) {
        JSONObject result = new JSONObject();
        HttpMethod httpMethod = request.method();
        String contentType = request.headers().get(Constant.CONTENT_TYPE);
        String reqBody = request.content().toString(CharsetUtil.UTF_8);

        if (httpMethod.equals(HttpMethod.GET)) {
            result = decodeUrlEncodedParam(reqBody);

        } else if (httpMethod.equals(HttpMethod.POST)) {
            if (contentType.equals(Constant.CONTENT_TYPE_URLENCODED)) {
                result = decodeUrlEncodedParam(reqBody);
            } else if (contentType.equals(Constant.CONTENT_TYPE_JSON)) {
                result = JSON.parseObject(reqBody);
                System.out.println(result);
            }
        }

        return result;
    }

    @Deprecated
    public static Object getReqParam(String key, FullHttpRequest request) {
        JSONObject jsonObject = decodeParams(request);
        return jsonObject.get(key);
    }

    private static JSONObject decodeUrlEncodedParam(String content) {
        JSONObject jsonObject = new JSONObject();
        QueryStringDecoder queryDecoder = new QueryStringDecoder(content, false);
        Map<String, List<String>> uriAttributes = queryDecoder.parameters();
        for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
            for (String attrVal : attr.getValue()) {
                System.out.println(attr.getKey() + "=" + attrVal);
                jsonObject.put(attr.getKey(), attrVal);
            }
        }
        return jsonObject;

    }

}
