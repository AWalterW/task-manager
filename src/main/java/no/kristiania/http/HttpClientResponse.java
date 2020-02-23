package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpClientResponse extends HttpMessage {

    private final String statusLine;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpClientResponse(InputStream inputStream) throws IOException {

        this.statusLine = readLine(inputStream);
        String headerLine;

        headers = readHeaders(inputStream);

        body = readBody(headers, inputStream);
    }

    public int getStatusCode(){
        return Integer.parseInt(statusLine.split(" ")[1]);
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("content-length"));
    }

    public String getBody() {
        return body;
    }


    public String getHeader(String key) {
        return headers.get(key.toLowerCase());
    }
}
