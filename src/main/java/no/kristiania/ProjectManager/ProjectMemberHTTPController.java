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

public class ProjectMemberHTTPController implements HttpController {

    private final ProjectMemberDAO projectMemberDAO;
    public ProjectMemberHTTPController(ProjectMemberDAO projectMemberDAO) {this.projectMemberDAO = projectMemberDAO;}

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);


    @Override
    public void handle(String requestAction, String requestPath, Map<String, String> query, String requestBody, OutputStream outputStream) throws IOException {

        try {
            if (requestAction.equals("PUT")){
                query = HttpServer.parseQueryString(requestBody);
                ProjectMember projectMember = new ProjectMember();
                projectMember.setMemberId(Integer.parseInt(query.get("memberId")));
                projectMember.setProjectId(Integer.parseInt(query.get("projectId")));

                projectMemberDAO.insert(projectMember);
                outputStream.write(("HTTP/1.1 200 OK\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
                return;
            }

            if (requestAction.equals("DELETE")){

                query = HttpServer.parseQueryString(requestBody);

                if(query.containsKey("memberId") && query.containsKey("projectId")) {
                    projectMemberDAO.delete(query.get("memberId"), query.get("projectId"));
                } else if(query.containsKey("memberId")) {
                    projectMemberDAO.delete(query.get("memberId"), "");
                } else if(query.containsKey("projectId")) {
                    projectMemberDAO.delete("", query.get("projectId"));
                }

                outputStream.write(("HTTP/1.1 200 OK\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
                return;
            }

            String body = "";


                if(query.containsKey("memberId") && query.containsKey("projectId")) {
                    body = getBody(query.get("memberId"), query.get("projectId"));
                } else if(query.containsKey("memberId")) {
                    body = getBody(query.get("memberId"), "");
                } else if(query.containsKey("projectId")) {
                    body = getBody("", query.get("projectId"));
                } else {
                    body = getBody("", "");
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

    private String getBody(String memberId, String projectId) throws SQLException {

        var projectmembers = projectMemberDAO.listAll(memberId, projectId);

        return new Gson().toJson(projectmembers);
    }

}
