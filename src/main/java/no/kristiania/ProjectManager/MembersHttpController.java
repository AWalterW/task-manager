package no.kristiania.ProjectManager;

import com.google.gson.Gson;
import no.kristiania.http.HttpController;
import no.kristiania.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.HttpServer.decodeValue;

public class MembersHttpController implements HttpController {

    private final MemberDAO memberDAO;

    public MembersHttpController(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    @Override
    public void handle(String requestAction, String requestPath, Map<String, String> query, String requestBody, OutputStream outputStream) throws IOException{

        try {


            if (requestAction.equals("PUT")){
                query = HttpServer.parseQueryString(requestBody);
                Member member = new Member();
                if(query.containsKey("name") && query.containsKey("email")) {
                    member.setName(decodeValue(query.get("name")));
                    member.setEmail(decodeValue(query.get("email")));
                    memberDAO.insert(member);
                    outputStream.write(("HTTP/1.1 200 OK\r\n" +
                            "Connection: close\r\n" +
                            "\r\n").getBytes());
                    return;
                } else {
                    outputStream.write(("HTTP/1.1 400 BAD REQUEST\r\n" +
                            "Connection: close\r\n" +
                            "\r\n").getBytes());
                    return;
                }
            }

            if (requestAction.equals("POST")){
                query = HttpServer.parseQueryString(requestBody);
                String id = query.get("id");
                String name = decodeValue(query.get("name"));
                String email = decodeValue(query.get("email"));

                memberDAO.edit(Integer.parseInt(id), name, email);
                outputStream.write(("HTTP/1.1 200 OK\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
                return;
            }

            if (requestAction.equals("DELETE")){
                query = HttpServer.parseQueryString(requestBody);
                String id = decodeValue(query.get("id"));
                memberDAO.delete(Integer.parseInt(id));
                outputStream.write(("HTTP/1.1 200 OK\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
                return;
            }


            String body = "";

            if(query != null) {
                if(query.containsKey("id")) {

                    body = getOneBody(query.get("id"));
                } else if(query.containsKey("projectid")) {
                    body = getExceptBody(query.get("projectid"));
                } else {
                    body = getBody();
                }
            } else {
                body = getBody();
            }


            int contentLength = body.getBytes("UTF-8").length;
            String contentType = "application/json";
            outputStream.write(("HTTP/1.0 200 OK\r\n" + "Content-length: " + contentLength +  "\r\n" +
                    "Content-type: " + contentType + "\r\n" + "Connection: close\r\n"+ "\r\n" + body).getBytes("UTF-8"));
            outputStream.flush();
        } catch (SQLException e) {
            logger.error("While handling requests {}", requestPath, e);
            String message = e.toString();
            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-type: text/plain\r\n" +
                    "Content-Length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" ).getBytes());
        }
    }

    public String getOneBody(String idString) throws SQLException {
        int id = Integer.parseInt(idString);
        var member = memberDAO.retrieve(id);
        return new Gson().toJson(member);
    }

    public String getExceptBody(String idString) throws SQLException {
        int projectId = Integer.parseInt(idString);
        var members = memberDAO.listAllExcept(projectId);
        return new Gson().toJson(members);
    }

    public String getBody() throws SQLException {
        var members = memberDAO.listAll();
        return new Gson().toJson(members);
    }

}


