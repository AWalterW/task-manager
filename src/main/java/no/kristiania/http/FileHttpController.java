package no.kristiania.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class FileHttpController implements HttpController {

    private HttpServer httpServer;

    public FileHttpController(HttpServer httpServer) {
        this.httpServer = httpServer;
    }
    private static final Logger logger = LoggerFactory.getLogger(FileHttpController.class);
    @Override
    public void handle(String requestAction, String requestPath, Map<String, String> query, String body, OutputStream outputStream) throws IOException {

        File file = new File(httpServer.getAssetRoot() + requestPath);


        logger.debug("Requesting file {}", file);

        if(file.isDirectory()) {
            file = new File(file, "index.html");
        }

        if(file.exists()) {
            long length = file.length();
            String contentType = getFiletype(file.getName());
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + length + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n").getBytes());
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                fileInputStream.transferTo(outputStream);
            }
        } else {
            outputStream.write(("HTTP/1.1 404 NOT FOUND\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: 9" + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    "Not found").getBytes());
        }
    }

    private static String getFiletype(String filename) {
        String fileType = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            fileType = filename.substring(i+1);
        }

        String MIMEType = "";
        switch (fileType) {
            case "html":
                MIMEType = "text/html";
                break;
            case "css":
                MIMEType = "text/css";
                break;
            case "js":
                MIMEType = "text/javascript";
                break;
            default:
                MIMEType = "text/plain";
        }
        return MIMEType;
    }
}
