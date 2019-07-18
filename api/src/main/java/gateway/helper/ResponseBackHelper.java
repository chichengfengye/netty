package gateway.helper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ResponseBackHelper {

    public static String sendRequest(HttpMethod httpMethod, String url, FullHttpRequest request) throws Exception {
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

    public static HttpResponse sendRequest2(HttpMethod httpMethod, String url, FullHttpRequest request) throws Exception {
        HttpResponse response;
        Request okRequest;

        OkHttpClient client = new OkHttpClient();

        if (httpMethod.equals(HttpMethod.GET)) {
            okRequest = new Request.Builder()
                    .url(url)
//                        .get(null)
                    .build();
        } else if (httpMethod.equals(HttpMethod.POST)) {
            HttpHeaders reqHeaders = request.headers();
            ByteBuf byteContent = request.content();
            String reqBody = byteContent.isReadable() ? byteContent.toString(CharsetUtil.UTF_8) : "";

            String[] nameAndValues = new String[reqHeaders.size() * 2];
            int i = 0;
            for (Map.Entry<String, String> reqHeader : reqHeaders) {
                nameAndValues[i] = reqHeader.getKey();
                nameAndValues[i + 1] = reqHeader.getValue();
                i += 2;
            }

            RequestBody okBody = RequestBody.create(
                    MediaType.get(reqHeaders.get("content-type") + "; charset=utf-8"),
                    reqBody);

            okRequest = new Request.Builder()
                    .url(url)
                    .headers(Headers.of(nameAndValues))
                    .post(okBody)
                    .build();

        } else {
            return httpJsonResponse(Unpooled.copiedBuffer("just support GET|POST method", CharsetUtil.UTF_8));
        }

        Response okResponse = client.newCall(okRequest).execute();
        response = convertResponse(okResponse);

        return response;
    }

    private static HttpResponse convertResponse(Response okResponse) throws IOException {
        String contentStr = okResponse.body().string();
        contentStr = contentStr == null ? "null" : contentStr;
        ByteBuf content = Unpooled.copiedBuffer(contentStr, CharsetUtil.UTF_8);
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        Headers headers = okResponse.headers();
        if (headers != null) {
            Map<String, List<String>> map = headers.toMultimap();
            for (Map.Entry<String, List<String>> stringListEntry : map.entrySet()) {
                System.out.println(stringListEntry.getKey() + ":" + stringListEntry.getValue());
                response.headers().set(stringListEntry.getKey(), stringListEntry.getValue());
            }
        }

        return response;
    }

    /**
     * 返回json格式数据
     *
     * @param content
     * @return
     */
    public static HttpResponse httpJsonResponse(ByteBuf content) {
        if (content == null) {
            content = Unpooled.copiedBuffer("null", CharsetUtil.UTF_8);
        }
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        return response;
    }
}
