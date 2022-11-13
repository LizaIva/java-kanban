package kvserver;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static http_server.HttpUtils.DEFAULT_CHARSET;

public class KVTaskClient {
    private final String url;
    private final String token;

    public KVTaskClient(String url) {
        this.url = url;

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        HttpRequest request = requestBuilder
                .GET()
                .uri(URI.create(url + "/register"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response = client.send(request, handler);
            token = response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String key, String json) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json, DEFAULT_CHARSET))
                .uri(URI.create(url + "/save/" + key + "/?API_TOKEN=" + token))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        try {
            client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key){
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        HttpRequest request = requestBuilder
                .GET()
                .uri(URI.create(url + "/load/" + key + "/?API_TOKEN=" + token))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "Application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
            return response.body();
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }
}
