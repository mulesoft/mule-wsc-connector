package org.mule.extension.ws.api.transport;

import java.io.InputStream;
import java.util.Map;

public class HttpRequestResponse {

    private final InputStream content;
    private final Map<String, String> httpHeaders;
    private final Map<String, String> statusLine;

    public HttpRequestResponse(InputStream content, Map<String, String> httpHeaders, Map<String, String> statusLine){
        this.content = content;
        this.httpHeaders = httpHeaders;
        this.statusLine = statusLine;
    }

    public InputStream getContent() {
        return this.content;
    }

    public Map<String, String> getHttpHeaders(){
        return this.httpHeaders;
    }

    public Map<String, String> getStatusLine(){
        return this.statusLine;
    }
}
