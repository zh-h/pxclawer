package me.zohar.pxclawer;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ResourceFilter extends HttpFiltersSourceAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class);

    private static final String trustPath = "/trust";

    private static final String pemFile = "littleproxy-mitm.pem";

    /***
     * While request /trust return pem file
     *
     * @param ctx
     * @param uri match your setting trust uri,such "/uri"
     * @return httpResponse contain written content
     */
    public HttpResponse serverPemFile(ChannelHandlerContext ctx, String uri) {
        if (uri.equals(trustPath)) {
            LOGGER.info("Serve trust file");
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(pemFile));
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                ByteBuf byteBuf = Unpooled.wrappedBuffer(sb.toString().getBytes("UTF-8"));
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
                HttpHeaders.setContentLength(response, byteBuf.readableBytes());
                HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, "application/x-x509-ca-cert");
                fileInputStream.close();
                return response;
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return null;
    }


    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        return new HttpFiltersAdapter(originalRequest) {
            @Override
            public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                if (httpObject instanceof HttpRequest) {
                    HttpRequest httpRequest = (HttpRequest) httpObject;
                    HttpHeaders headers = httpRequest.headers();
                    String uri = httpRequest.getUri();
                    HttpMethod method = httpRequest.getMethod();
                    Map<String, String> headersMap = new HashMap<>();
                    headers.forEach((e) -> headersMap.put(e.getKey(), e.getValue()));
                    Gson gson = new Gson();
                    LOGGER.info("Request: {} {}", method.name(), uri);
                    LOGGER.info(gson.toJson(headersMap));
                    return serverPemFile(ctx, uri);
                }
                return null;
            }

            @Override
            public HttpObject serverToProxyResponse(HttpObject httpObject) {
                LOGGER.info("Response");
                String contentType = null;
                String contentEncoding = null;
                // 一个请求会有多个响应，不知道如何把请求和响应封装在一起
                // 请求响应过程是乱序的
                if (httpObject instanceof HttpResponse) {
                    HttpResponse httpResponse = (HttpResponse) httpObject;
                    contentType = HttpHeaders.getHeader(httpResponse, HttpHeaders.Names.CONTENT_TYPE);
                    contentEncoding = HttpHeaders.getHeader(httpResponse, HttpHeaders.Names.CONTENT_ENCODING);
                    LOGGER.info("Content: {} {}", contentType, contentEncoding);
                    Map<String, String> headersMap = new HashMap<>();
                    httpResponse.headers().forEach((e) -> headersMap.put(e.getKey(), e.getValue()));
                    Gson gson = new Gson();
                    LOGGER.info(gson.toJson(headersMap));
                }

                if (httpObject instanceof HttpContent) {
                    HttpContent httpContent = (HttpContent) httpObject;
                    ByteBuf bufferedContent = httpContent.content();
                    if (contentType == null || contentType.startsWith("text") ||
                            contentType.startsWith("application")) {
                        byte[] binaryContent = new byte[bufferedContent.readableBytes()];

                        bufferedContent.markReaderIndex();
                        bufferedContent.readBytes(binaryContent);
                        bufferedContent.resetReaderIndex();

                        String content;  // 此处乱码
                        if (contentEncoding != null && contentEncoding.equals("gzip")) {
                            content = StringUtil.gzipUncompressToString(binaryContent);
                        } else {
                            content = new String(binaryContent, CharsetUtil.UTF_8);
                        }
                        LOGGER.info(content);
                    }

                    if (httpContent instanceof LastHttpContent) {
                        LastHttpContent lastContent = (LastHttpContent) httpContent;
                    }
                }
                return httpObject;
            }
        };
    }
}
