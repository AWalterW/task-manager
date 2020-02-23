package no.kristiania.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpServerTest {

    private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpServer(0);
        server.start();
    }

    @Test
    void shouldReturnStatusCode200() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo");
        assertEquals(200, client.execute("GET").getStatusCode());
    }

    @Test
    void shouldReturnStatusCode401() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?status=401");
        assertEquals(401, client.execute("GET").getStatusCode());
    }

    @Test
    void shouldReturnBody() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?body=HelloWorld!");
        assertEquals("HelloWorld!", client.execute("GET").getBody());
    }

    @Test
    void shouldReturnFileFromDisk() throws IOException {
        Files.writeString(Paths.get("target/mytestfile.txt"), "Hello Kristiania");
        server.setAssetRoot("target");
        HttpClient httpClient = new HttpClient("localhost", server.getPort(),"/mytestfile.txt");
        HttpClientResponse response = httpClient.execute("GET");
        assertEquals("Hello Kristiania", response.getBody());

    }

    @Test
    void shouldParsePostParameters() throws IOException {
        String formBody = "content-type=text/html&body=foobar";
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?" + formBody);
        client.setRequestHeader("content-type", "application/x-www-form-urlencoded");
        client.setBody(formBody);
        HttpClientResponse response = client.execute("GET");
        assertThat(response.getHeader("content-type")).isEqualTo("text/html");
        assertThat(response.getBody()).isEqualTo("foobar");
    }

    @Test
    void shouldReadFile() throws IOException{
        server.setAssetRoot("target/");
        String fileContent = "some random string ";
        Files.writeString(Paths.get("target", "somefile.txt"), fileContent);

        HttpClientResponse response = executeLocalRequest("somefile.txt");
        assertThat(response.getBody()).isEqualTo("some random string " );
    }

    private HttpClientResponse executeLocalRequest(String s) throws IOException{
        HttpClient client = new HttpClient("localhost", server.getPort(), s);
        return client.execute("GET");
    }

}