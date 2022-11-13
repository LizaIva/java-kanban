package test.http_server;

import com.google.gson.reflect.TypeToken;
import http_server.HttpTaskServer;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static http_server.HttpUtils.DEFAULT_CHARSET;
import static http_server.HttpUtils.gson;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerRestoreTest extends HttpTaskManagerTest {

    @Override
    void cleanServer() {

    }

    @Test
    void restoreTest() throws IOException, InterruptedException {
        URI uri1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest.Builder requestBuilder1 = HttpRequest.newBuilder();

        Task task1 = new Task();
        task1.setName("Task1");
        task1.setDescription("Task1 Description");
        task1.setDuration(Duration.ofMinutes(10));
        task1.setStartTime(LocalDateTime.of(2022, 10, 12, 14, 0));

        HttpRequest request1 = requestBuilder1
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response1 = client1.send(request1, handler1);
        Task createdTask1 = gson.fromJson(response1.body(), Task.class);


        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        Task task2 = new Task();
        task2.setName("Task2");
        task2.setDescription("Task2 Description");
        task2.setDuration(Duration.ofMinutes(15));
        task2.setStartTime(LocalDateTime.of(2016, 10, 12, 14, 0));

        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        Task createdTask2 = gson.fromJson(response2.body(), Task.class);

        HttpRequest.Builder requestBuilder3 = HttpRequest.newBuilder();
        Task task3 = new Task();
        task3.setName("Task3");
        task3.setDescription("Task3 Description");
        task3.setDuration(Duration.ofMinutes(15));
        task3.setStartTime(LocalDateTime.of(2018, 10, 12, 14, 0));

        HttpRequest request3 = requestBuilder3
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task3), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client3 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler3 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response3 = client3.send(request3, handler3);
        Task createdTask3 = gson.fromJson(response3.body(), Task.class);


        URI uriGetTask2 = URI.create("http://localhost:8080/tasks/task?id=" + createdTask2.getId());
        HttpRequest request4 = HttpRequest.newBuilder()
                .GET()
                .uri(uriGetTask2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client4 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler4 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response4 = client4.send(request4, handler4);


        URI uriGetTask1 = URI.create("http://localhost:8080/tasks/task?id=" + createdTask1.getId());
        HttpRequest request5 = HttpRequest.newBuilder()
                .GET()
                .uri(uriGetTask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client5 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler5 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response5 = client5.send(request5, handler5);

        URI uriEpic = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder4 = HttpRequest.newBuilder();

        Epic epic1 = new Epic();
        epic1.setName("Epic1");
        epic1.setDescription("Epic1 Description");


        HttpRequest request6 = requestBuilder4
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1), DEFAULT_CHARSET))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client6 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler6 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response6 = client6.send(request6, handler6);
        Epic createdEpic1 = gson.fromJson(response6.body(), Epic.class);

        URI uriSubtask1 = URI.create("http://localhost:8080/tasks/subtask");

        HttpRequest.Builder requestBuilder5 = HttpRequest.newBuilder();
        Subtask subtask1 = new Subtask();
        subtask1.setEpicId(createdEpic1.getId());
        subtask1.setName("Subtask1");
        subtask1.setDescription("Subtask1 Description");
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.of(2010, 10, 12, 14, 0));

        HttpRequest request7 = requestBuilder5
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1), DEFAULT_CHARSET))
                .uri(uriSubtask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client7 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler7 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response7 = client7.send(request7, handler7);

        Subtask createdSubtask1 = gson.fromJson(response2.body(), Subtask.class);

        URI uriGetEpic1 = URI.create("http://localhost:8080/tasks/epic?id=" + createdEpic1.getId());
        HttpRequest request8 = HttpRequest.newBuilder()
                .GET()
                .uri(uriGetEpic1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client8 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler8 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response8 = client8.send(request8, handler8);


        URI uriHistory = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request9 = HttpRequest.newBuilder()
                .GET()
                .uri(uriHistory)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client9 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler9 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response9 = client9.send(request9, handler9);
        List<Task> createdTasks = gson.fromJson(response9.body(), new TypeToken<>() {
        });
        assertEquals(3, createdTasks.size(), "Не произошло добавление в историю менеджера задач");

        httpTaskServer.stop();

        httpTaskServer = new HttpTaskServer();

        httpTaskServer.start();


        HttpRequest request10 = HttpRequest.newBuilder()
                .GET()
                .uri(uriHistory)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client10 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler10 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response10 = client10.send(request10, handler10);
        List<Task> createdTasks1 = gson.fromJson(response10.body(), new TypeToken<>() {
        });
        assertEquals(3, createdTasks1.size(), "Не произошло добавление в историю менеджера задач");

    }
}
