package test.http_server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import task.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static http_server.HttpUtils.DEFAULT_CHARSET;
import static http_server.HttpUtils.gson;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerForEpicTest extends HttpTaskManagerTest {

    @Test
    void postEpicTest() throws IOException, InterruptedException {
        URI uri1 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        Epic epic = new Epic();
        epic.setName("Epic1");
        epic.setDescription("Epic1 Description");

        HttpRequest request1 = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        Epic createdEpic = gson.fromJson(response1.body(), Epic.class);
        assertEquals(epic.getName(), createdEpic.getName(), "Неверное имя эпика.");
        assertEquals(epic.getDescription(), createdEpic.getDescription(), "Неверное описание эпика.");
    }

    @Test
    void getEpicWithoutIdAndWithIdAndWithWrongIdTest() throws IOException, InterruptedException {

        URI uri1 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder1 = HttpRequest.newBuilder();

        Epic epic1 = new Epic();
        epic1.setName("Epic1");
        epic1.setDescription("Epic1 Description");


        HttpRequest request1 = requestBuilder1
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response1 = client1.send(request1, handler1);


        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        Epic epic2 = new Epic();
        epic2.setName("Epic2");
        epic2.setDescription("Epic2 Description");


        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response2 = client2.send(request2, handler2);
        Epic createdEpic2 = gson.fromJson(response2.body(), Epic.class);


        HttpRequest.Builder requestBuilder3 = HttpRequest.newBuilder();

        Epic epic3 = new Epic();
        epic3.setName("Epic3");
        epic3.setDescription("Epic3 Description");

        HttpRequest request3 = requestBuilder3
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic3), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client3 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler3 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response3 = client3.send(request3, handler3);


        HttpRequest request4 = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1) //
                .header("Accept", "text/html")
                .build();

        HttpClient client4 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler4 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response4 = client4.send(request4, handler4);
        List<Epic> createdEpics = gson.fromJson(response4.body(), new TypeToken<>() {
        });
        int count = 0;
        for (Epic createdEpic : createdEpics) {
            if (count == 0) {
                assertEquals(epic1.getName(), createdEpic.getName(), "Неверное имя эпика.");
                assertEquals(epic1.getDescription(), createdEpic.getDescription(), "Неверное описание эпика.");
            } else if (count == 1) {
                assertEquals(epic2.getName(), createdEpic.getName(), "Неверное имя эпика.");
                assertEquals(epic2.getDescription(), createdEpic.getDescription(), "Неверное описание эпика.");
            } else if (count == 2) {
                assertEquals(epic3.getName(), createdEpic.getName(), "Неверное имя эпика.");
                assertEquals(epic3.getDescription(), createdEpic.getDescription(), "Неверное описание эпика.");
            }
            count++;
        }


        URI uri2 = URI.create("http://localhost:8080/tasks/epic?id=" + createdEpic2.getId());

        HttpRequest request5 = HttpRequest.newBuilder()
                .GET()
                .uri(uri2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client5 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler5 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response5 = client5.send(request5, handler5);
        Epic createdEpic = gson.fromJson(response5.body(), Epic.class);
        assertEquals(createdEpic2.getName(), createdEpic.getName(), "Неверное имя эпика.");
        assertEquals(createdEpic2.getDescription(), createdEpic.getDescription(), "Неверное описание эпика.");


        URI uri3 = URI.create("http://localhost:8080/tasks/epic?id=23446");

        HttpRequest request6 = HttpRequest.newBuilder()
                .GET()
                .uri(uri3)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client6 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler6 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response6 = client6.send(request6, handler6);
        int status = response6.statusCode();
        assertEquals(404, status, "Выполнен поиск эпика, которого не существует");
    }

    @Test
    void deleteAllEpicAndWithEpicIdAndWithWrongEpicIdTest() throws IOException, InterruptedException {

        URI uri1 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder1 = HttpRequest.newBuilder();

        Epic epic1 = new Epic();
        epic1.setName("epic1");
        epic1.setDescription("epic1 Description");

        HttpRequest request1 = requestBuilder1
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response1 = client1.send(request1, handler1);
        } catch (IOException | InterruptedException e) {
        }

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        Epic epic2 = new Epic();
        epic2.setName("epic2");
        epic2.setDescription("epic2 Description");

        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response2 = client2.send(request2, handler2);
        } catch (IOException | InterruptedException e) {
        }


        HttpRequest.Builder requestBuilder3 = HttpRequest.newBuilder();

        Epic epic3 = new Epic();
        epic3.setName("Task3");
        epic3.setDescription("Task3 Description");

        HttpRequest request3 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic3), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client3 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler3 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response3 = client3.send(request3, handler3);
        } catch (IOException | InterruptedException e) {
        }

        HttpRequest request4 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1) //
                .header("Accept", "text/html")
                .build();

        HttpClient client4 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler4 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response4 = client4.send(request4, handler4);
        } catch (IOException | InterruptedException e) {
        }

        HttpRequest request5 = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1) //
                .header("Accept", "text/html")
                .build();

        HttpClient client5 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler5 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response4 = client5.send(request5, handler5);
            List<Epic> createdEpics = gson.fromJson(response4.body(), new TypeToken<>() {
            });
            assertEquals(0, createdEpics.size(), "Удалены не все эпики");
        } catch (IOException | InterruptedException e) {
        }


        try {
            HttpResponse<String> response1 = client1.send(request1, handler1);
            Epic createdEpic1 = gson.fromJson(response1.body(), Epic.class);
            HttpResponse<String> response2 = client1.send(request2, handler2);
            Epic createdEpic2 = gson.fromJson(response2.body(), Epic.class);
            HttpResponse<String> response3 = client1.send(request3, handler3);
            Epic createdEpic3 = gson.fromJson(response3.body(), Epic.class);

            URI uri2 = URI.create("http://localhost:8080/tasks/epic?id=" + createdEpic1.getId());

            HttpRequest request6 = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(uri2)
                    .version(HttpClient.Version.HTTP_1_1) //
                    .header("Accept", "text/html")
                    .build();

            HttpClient client6 = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler6 = HttpResponse.BodyHandlers.ofString();

            try {
                HttpResponse<String> response6 = client6.send(request6, handler6);
            } catch (IOException | InterruptedException e) {
            }

            URI uri3 = URI.create("http://localhost:8080/tasks/epic?id=" + createdEpic2.getId());

            HttpRequest request7 = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(uri3)
                    .version(HttpClient.Version.HTTP_1_1) //
                    .header("Accept", "text/html")
                    .build();

            HttpClient client7 = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler7 = HttpResponse.BodyHandlers.ofString();

            try {
                HttpResponse<String> response7 = client7.send(request7, handler7);
            } catch (IOException | InterruptedException e) {
            }

        } catch (IOException | InterruptedException e) {
        }


        HttpRequest request8 = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client8 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler8 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response8 = client8.send(request8, handler8);
            List<Epic> createdEpics = gson.fromJson(response8.body(), new TypeToken<>() {
            });
            assertEquals(1, createdEpics.size(), "Эпик не был удален");
            for (Epic createdEpic : createdEpics) {
                assertEquals(epic3.getName(), createdEpic.getName(), "Произошло удаление эпика с неверным id");
                assertEquals(epic3.getDescription(), createdEpic.getDescription(), "Произошло удаление эпика с неверным id");
            }
        } catch (IOException | InterruptedException e) {
        }


        URI uri4 = URI.create("http://localhost:8080/tasks/epic?id=123123432112343");

        HttpRequest request9 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri4)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client9 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler9 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response9 = client9.send(request9, handler9);
            int status = response9.statusCode();
            assertEquals(400, status, "Удален несуществующий эпик");
        } catch (IOException | InterruptedException e) {
        }


        HttpRequest request10 = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client10 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler10 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response10 = client10.send(request10, handler10);
            List<Epic> createdEpics = gson.fromJson(response10.body(), new TypeToken<>() {
            });
            assertEquals(1, createdEpics.size(), "Произошло удаление задачи с несуществующим id");
        } catch (IOException | InterruptedException e) {
        }
    }


}
