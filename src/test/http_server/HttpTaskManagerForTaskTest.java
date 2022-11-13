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

public class HttpTaskManagerForTaskTest extends HttpTaskManagerTest {

    @Test
    void postTaskTest() throws IOException, InterruptedException {
        URI uri1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        Task task = new Task();
        task.setName("Task1");
        task.setDescription("Task1 Description");
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(LocalDateTime.of(2022, 10, 12, 14, 0));

        HttpRequest request1 = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task), DEFAULT_CHARSET))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        Task createdTask = gson.fromJson(response1.body(), Task.class);
        assertEquals(task.getName(), createdTask.getName(), "Неверное имя задачи.");
        assertEquals(task.getDescription(), createdTask.getDescription(), "Неверное описание задачи.");
        assertEquals(task.getDuration(), createdTask.getDuration(), "Неверная продолжительность задачи.");
        assertEquals(task.getStartTime(), createdTask.getStartTime(), "Неверное стартовое время задачи.");

    }

    @Test
    void getTaskWithoutIdAndWithIdAndWithWrongIdTest() {

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
        try {
            HttpResponse<String> response1 = client1.send(request1, handler1);
        } catch (IOException | InterruptedException e) {
        }

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        Task task2 = new Task();
        task2.setName("Task2");
        task2.setDescription("Task2 Description");
        task2.setDuration(Duration.ofMinutes(15));
        task2.setStartTime(LocalDateTime.of(2018, 10, 12, 14, 0));

        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2), DEFAULT_CHARSET))
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

        Task task3 = new Task();
        task3.setName("Task3");
        task3.setDescription("Task3 Description");
        task3.setDuration(Duration.ofMinutes(15));
        task3.setStartTime(LocalDateTime.of(2016, 10, 12, 14, 0));

        HttpRequest request3 = requestBuilder3
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task3), DEFAULT_CHARSET))
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
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client4 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler4 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response4 = client4.send(request4, handler4);
            List<Task> createdTasks = gson.fromJson(response4.body(), new TypeToken<>() {
            });
            int count = 0;
            for (Task createdTask : createdTasks) {
                if (count == 0) {
                    assertEquals(task1.getName(), createdTask.getName(), "Неверное имя задачи.");
                    assertEquals(task1.getDescription(), createdTask.getDescription(), "Неверное описание задачи.");
                    assertEquals(task1.getDuration(), createdTask.getDuration(), "Неверная продолжительность задачи.");
                    assertEquals(task1.getStartTime(), createdTask.getStartTime(), "Неверное стартовое время задачи.");
                } else if (count == 1) {
                    assertEquals(task2.getName(), createdTask.getName(), "Неверное имя задачи.");
                    assertEquals(task2.getDescription(), createdTask.getDescription(), "Неверное описание задачи.");
                    assertEquals(task2.getDuration(), createdTask.getDuration(), "Неверная продолжительность задачи.");
                    assertEquals(task2.getStartTime(), createdTask.getStartTime(), "Неверное стартовое время задачи.");
                } else if (count == 2) {
                    assertEquals(task3.getName(), createdTask.getName(), "Неверное имя задачи.");
                    assertEquals(task3.getDescription(), createdTask.getDescription(), "Неверное описание задачи.");
                    assertEquals(task3.getDuration(), createdTask.getDuration(), "Неверная продолжительность задачи.");
                    assertEquals(task3.getStartTime(), createdTask.getStartTime(), "Неверное стартовое время задачи.");
                }
                count++;
            }
        } catch (IOException | InterruptedException e) {
        }

        URI uri2 = URI.create("http://localhost:8080/tasks/task?id=1");

        HttpRequest request5 = HttpRequest.newBuilder()
                .GET()
                .uri(uri2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client5 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler5 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response5 = client5.send(request5, handler5);
            Task createdTask = gson.fromJson(response5.body(), Task.class);
            assertEquals(task2.getName(), createdTask.getName(), "Неверное имя задачи.");
            assertEquals(task2.getDescription(), createdTask.getDescription(), "Неверное описание задачи.");
            assertEquals(task2.getDuration(), createdTask.getDuration(), "Неверная продолжительность задачи.");
            assertEquals(task2.getStartTime(), createdTask.getStartTime(), "Неверное стартовое время задачи.");
        } catch (IOException | InterruptedException e) {
        }

        URI uri3 = URI.create("http://localhost:8080/tasks/task?id=6");

        HttpRequest request6 = HttpRequest.newBuilder()
                .GET()
                .uri(uri3)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client6 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler6 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response6 = client6.send(request6, handler6);
            int status = response6.statusCode();
            assertEquals(404, status, "Выполнен поиск задачи, которой не существует");
        } catch (IOException | InterruptedException e) {
        }

    }

    @Test
    void deleteAllTaskAndWithTaskIdAndWithWrongTaskIdTest() {

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
        try {
            HttpResponse<String> response1 = client1.send(request1, handler1);
        } catch (IOException | InterruptedException e) {
        }

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        Task task2 = new Task();
        task2.setName("Task2");
        task2.setDescription("Task2 Description");
        task2.setDuration(Duration.ofMinutes(15));
        task2.setStartTime(LocalDateTime.of(2018, 10, 12, 14, 0));

        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2), DEFAULT_CHARSET))
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

        Task task3 = new Task();
        task3.setName("Task3");
        task3.setDescription("Task3 Description");
        task3.setDuration(Duration.ofMinutes(15));
        task3.setStartTime(LocalDateTime.of(2016, 10, 12, 14, 0));

        HttpRequest request3 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task3), DEFAULT_CHARSET))
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
                .version(HttpClient.Version.HTTP_1_1)
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
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client5 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler5 = HttpResponse.BodyHandlers.ofString();

        try {
            HttpResponse<String> response4 = client5.send(request5, handler5);
            List<Task> createdTasks = gson.fromJson(response4.body(), new TypeToken<>() {
            });
            assertEquals(0, createdTasks.size(), "Удалены не все задачи");
        } catch (IOException | InterruptedException e) {
        }


        try {
            HttpResponse<String> response1 = client1.send(request1, handler1);
            Task createdTask1 = gson.fromJson(response1.body(), Task.class);
            HttpResponse<String> response2 = client1.send(request2, handler2);
            Task createdTask2 = gson.fromJson(response2.body(), Task.class);
            HttpResponse<String> response3 = client1.send(request3, handler3);
            Task createdTask3 = gson.fromJson(response3.body(), Task.class);

            URI uri2 = URI.create("http://localhost:8080/tasks/task?id=" + createdTask1.getId());

            HttpRequest request6 = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(uri2)
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "text/html")
                    .build();

            HttpClient client6 = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler6 = HttpResponse.BodyHandlers.ofString();

            try {
                HttpResponse<String> response6 = client6.send(request6, handler6);
            } catch (IOException | InterruptedException e) {
            }

            URI uri3 = URI.create("http://localhost:8080/tasks/task?id=" + createdTask2.getId());

            HttpRequest request7 = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(uri3)
                    .version(HttpClient.Version.HTTP_1_1)
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
            List<Task> createdTasks = gson.fromJson(response8.body(), new TypeToken<>() {
            });
            assertEquals(1, createdTasks.size(), "Задача не была удалена");
            for (Task createdTask : createdTasks) {
                assertEquals(task3.getName(), createdTask.getName(), "Произошло удаление задачи с неверным id");
                assertEquals(task3.getDescription(), createdTask.getDescription(), "Произошло удаление задачи с неверным id");
                assertEquals(task3.getDuration(), createdTask.getDuration(), "Произошло удаление задачи с неверным id");
                assertEquals(task3.getStartTime(), createdTask.getStartTime(), "Произошло удаление задачи с неверным id");
            }
        } catch (IOException | InterruptedException e) {
        }


        URI uri4 = URI.create("http://localhost:8080/tasks/task?id=123123432112343");

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
            assertEquals(400, status, "Удалена несуществующая задача");
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
            List<Task> createdTasks = gson.fromJson(response10.body(), new TypeToken<>() {
            });
            assertEquals(1, createdTasks.size(), "Произошло удаление задачи с несуществующим id");
        } catch (IOException | InterruptedException e) {
        }
    }
}
