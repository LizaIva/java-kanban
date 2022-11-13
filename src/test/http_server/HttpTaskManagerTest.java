package test.http_server;

import http_server.HttpTaskServer;
import kvserver.KVServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class HttpTaskManagerTest {

    protected static KVServer kvServer;
    protected static HttpTaskServer httpTaskServer;

    @BeforeAll
    static void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterAll
    static void tearDown() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @AfterEach
    void cleanServer() throws IOException, InterruptedException {
        URI uri1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest.Builder requestBuilder1 = HttpRequest.newBuilder();

        HttpRequest request1 = requestBuilder1
                .DELETE()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client1.send(request1, handler1);

        URI uri2 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        HttpRequest request2 = requestBuilder2
                .DELETE()
                .uri(uri2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client2.send(request2, handler2);

    }
}
