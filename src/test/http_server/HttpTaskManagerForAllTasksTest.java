package test.http_server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
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

public class HttpTaskManagerForAllTasksTest extends HttpTaskManagerTest {

    @Test
    void getAllTasksTest() throws IOException, InterruptedException {
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


        URI uriAllTasks = URI.create("http://localhost:8080/tasks");
        HttpRequest request4 = HttpRequest.newBuilder()
                .GET()
                .uri(uriAllTasks)
                .version(HttpClient.Version.HTTP_1_1) //
                .header("Accept", "text/html")
                .build();

        HttpClient client4 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler4 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response4 = client4.send(request4, handler4);
        List<Task> createdTasks = gson.fromJson(response4.body(), new TypeToken<>() {
        });
        int count = 0;
        for (Task createdTask : createdTasks) {
            if (count == 0) {
                assertEquals(createdTask2.getName(), createdTask.getName(), "Неверное имя задачи.");
                assertEquals(createdTask2.getDescription(), createdTask.getDescription(), "Неверное описание задачи.");
                assertEquals(createdTask2.getDuration(), createdTask.getDuration(), "Неверная продолжительность задачи.");
                assertEquals(createdTask2.getStartTime(), createdTask.getStartTime(), "Неверное стартовое время задачи.");
            } else if (count == 1) {
                assertEquals(createdTask3.getName(), createdTask.getName(), "Неверное имя задачи.");
                assertEquals(createdTask3.getDescription(), createdTask.getDescription(), "Неверное описание задачи.");
                assertEquals(createdTask3.getDuration(), createdTask.getDuration(), "Неверная продолжительность задачи.");
                assertEquals(createdTask3.getStartTime(), createdTask.getStartTime(), "Неверное стартовое время задачи.");
            } else if (count == 2) {
                assertEquals(createdTask1.getName(), createdTask.getName(), "Неверное имя задачи.");
                assertEquals(createdTask1.getDescription(), createdTask.getDescription(), "Неверное описание задачи.");
                assertEquals(createdTask1.getDuration(), createdTask.getDuration(), "Неверная продолжительность задачи.");
                assertEquals(createdTask1.getStartTime(), createdTask.getStartTime(), "Неверное стартовое время задачи.");
            }
            count++;
        }
    }
}

