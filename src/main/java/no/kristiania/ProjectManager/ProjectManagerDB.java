package no.kristiania.ProjectManager;

import no.kristiania.http.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ProjectManagerDB {

    private HttpServer server;


    public ProjectManagerDB(int port) throws IOException {

        Properties properties = new Properties();
        try(FileReader fileReader = new FileReader("ProjectManager.properties")){
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        server = new HttpServer(port);
        server.setAssetRoot("public");
        server.addController("/api/members", new MembersHttpController(new MemberDAO(dataSource)));
        server.addController("/api/projects", new ProjectHttpController(new ProjectDAO(dataSource)));
        server.addController("/api/projectmembers", new ProjectMemberHTTPController(new ProjectMemberDAO(dataSource)));
    }

    public static void main(String[] args) throws IOException {
        new ProjectManagerDB(8080).start();
    }

    private void start(){
        server.start();
    }
}
