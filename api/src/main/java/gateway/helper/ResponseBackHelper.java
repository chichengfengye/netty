package gateway.helper;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import okhttp3.*;

import java.io.IOException;

public class ResponseBackHelper {

    public static String sendRequest(HttpMethod httpMethod, String url, FullHttpRequest request) throws Exception{
        String result = "just support GET|POST method";
        OkHttpClient client = new OkHttpClient();
        Request okRequest = null;
        RequestBody body;
        String reqBody = "";

        if (httpMethod.equals(HttpMethod.GET)) {
            okRequest = new Request.Builder()
                    .url(url)
//                        .get(null)
                    .build();
        } else if (httpMethod.equals(HttpMethod.POST)) {
            HttpHeaders headers = request.headers();
            MediaType mediaType;
            ByteBuf byteContent = request.content();
            if (byteContent.isReadable()) {
                reqBody = byteContent.toString(CharsetUtil.UTF_8);
            }

            String contentType = headers.get("content-type");
            mediaType = MediaType.get(contentType + "; charset=utf-8");

            body = RequestBody.create(mediaType, reqBody);
            okRequest = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

        } else {
            return "";
        }

        try (Response response = client.newCall(okRequest).execute()) {
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 返回json格式数据
     * @param content
     * @return
     */
    public static HttpResponse httpJsonResponse(ByteBuf content) {
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        return response;
    }
}
