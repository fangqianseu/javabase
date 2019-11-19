/*
Date: 04/27,2019, 09:16

httpjson 的 request 请求
*/
package netty.protocol.http.json.codec;

import io.netty.handler.codec.http.FullHttpRequest;

public class HttpJsonRequest {
    private FullHttpRequest request;
    private Object body;

    public HttpJsonRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
