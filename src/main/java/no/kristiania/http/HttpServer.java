package no.kristiania.http;


import com.google.gson.internal.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static no.kristiania.http.HttpMessage.readHeaders;

public class HttpServer {

    private ServerSocket serverSocket;
    private String assetRoot;

    private HttpController defaultController = new FileHttpController(this);

    private Map<String, HttpController> controllers = new HashMap<>();

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        controllers.put("/echo", new EchoHttpController());
    }

    public static void main(String[] args) throws IOException {
        new HttpServer(8080).start();
    }

    public void start() {
        new Thread(() -> run()).start();
    }

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public void run() {

        System.out.println("Server running on localhost: " + getPort());
        boolean keepRunning = true;

        while(keepRunning) {

            try {
                Socket socket = serverSocket.accept();

                String requestLine = HttpMessage.readLine(socket.getInputStream() );
                if (requestLine.isBlank()) continue;

                Map<String, String> headers = readHeaders(socket.getInputStream());
                String body = HttpMessage.readBody(headers, socket.getInputStream());
                

                String requestAction = requestLine.split(" ")[0];
                String requestTarget = requestLine.split(" ")[1];

                int questionPos = requestTarget.indexOf('?');
                String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);
                Map<String, String> query = getQueryParameters(requestTarget);

                controllers
                        .getOrDefault(requestPath, defaultController)
                        .handle(requestAction, requestPath, query, body, socket.getOutputStream());
                logger.info("Handling request {} {} ", requestAction, requestPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> getQueryParameters(String requestLine) {
        String requestTarget;
        if(requestLine.split(" ").length > 1) {
            requestTarget = requestLine.split(" ")[1];
        } else if(requestLine.split(" ").length > 0) {
            requestTarget = requestLine.split(" ")[0];
        } else {
            requestTarget = "/";
        }
        int questionPos = requestTarget.indexOf('?');
        if (questionPos != -1){
            String queryString = requestTarget.substring(questionPos + 1);
            if(queryString.length() > 0) {


                return parseQueryString(queryString);
            } else {
                return new StringMap<>();
            }

            }
        return new HashMap<>();
    }

    public static Map<String, String> parseQueryString(String queryString) {


        if(queryString != null) {
            Map<String, String> parameters = new HashMap<>();
            for (String parameter : queryString.split("&")) {

                int equalsPos = parameter.indexOf('=');
                String parameterName = parameter.substring(0, equalsPos);
                String parameterValue = parameter.substring(equalsPos + 1);

                parameters.put(parameterName, parameterValue);
            }
            return parameters;
        } else {
            return null;
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setAssetRoot(String assetRoot) {
        this.assetRoot = assetRoot;
    }

    public String getAssetRoot() {
        return assetRoot;
    }

    public void addController(String requestPath, HttpController controller){
        controllers.put(requestPath, controller);
    }

    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public static String encodeValue(String value) {
        try {
            return  URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
