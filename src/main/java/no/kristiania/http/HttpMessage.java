package no.kristiania.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpMessage {

   static String readLine(InputStream inputStream) throws IOException{
       StringBuilder statusLine = new StringBuilder();
       int c;
       while ((c = inputStream.read()) != - 1){
           if (c == '\r'){
               inputStream.read();
               break;
           }
           statusLine.append((char) c);
       }
       return statusLine.toString();
   }

   static Map<String, String> readHeaders(InputStream inputStream) throws IOException{
       Map<String, String> headers = new HashMap<>();
       String headerLine;
       while (!(headerLine = readLine(inputStream)).isBlank()){
           int colonPos = headerLine.indexOf(':');
           headers.put(headerLine.substring(0, colonPos).trim().toLowerCase(),
                   headerLine.substring(colonPos + 1).trim());
       }
       return headers;
   }

    static String readBody(Map<String, String> headers, InputStream inputStream) throws IOException {
        if (headers.containsKey("content-length")){
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < Integer.parseInt(headers.get("content-length")); i++) {
                body.append((char)inputStream.read());
            }
            return body.toString();
        } else{
            return null;
        }
    }
}
