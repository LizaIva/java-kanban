package test.http_server;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;

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

public class HttpTaskManagerForSubtaskTest extends HttpTaskManagerTest {

    @Test
    void postSubtaskTest() throws IOException, InterruptedException {
        URI uriEpic = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder1 = HttpRequest.newBuilder();

        Epic epic1 = new Epic();
        epic1.setName("Epic1");
        epic1.setDescription("Epic1 Description");


        HttpRequest request1 = requestBuilder1
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1), DEFAULT_CHARSET))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        Epic createdEpic = gson.fromJson(response1.body(), Epic.class);

        URI uriSubtask1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        Subtask subtask1 = new Subtask();
        subtask1.setEpicId(createdEpic.getId());
        subtask1.setName("Subtask1");
        subtask1.setDescription("Subtask1 Description");
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.of(2010, 10, 12, 14, 0));

        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1), DEFAULT_CHARSET))
                .uri(uriSubtask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response2 = client2.send(request2, handler2);
        Subtask createdSubtask = gson.fromJson(response2.body(), Subtask.class);
        assertEquals(subtask1.getName(), createdSubtask.getName(), "Неверное имя сабтаска.");
        assertEquals(subtask1.getDescription(), createdSubtask.getDescription(), "Неверное описание сабтаска.");
        assertEquals(subtask1.getDuration(), createdSubtask.getDuration(), "Неверная продолжительность сабтаска.");
        assertEquals(subtask1.getStartTime(), createdSubtask.getStartTime(), "Неверное стартовое время сабтаска.");
        assertEquals(subtask1.getEpicId(), createdSubtask.getEpicId(), "Неверное id эпика у  сабтаска.");
    }


    @Test
    void getSubtaskWithoutIdAndWithIdAndWithWrongIdTest() throws IOException, InterruptedException {

        URI uriEpic = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder1 = HttpRequest.newBuilder();

        Epic epic1 = new Epic();
        epic1.setName("Epic1");
        epic1.setDescription("Epic1 Description");


        HttpRequest request1 = requestBuilder1
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1), DEFAULT_CHARSET))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response1 = client1.send(request1, handler1);
        Epic createdEpic1 = gson.fromJson(response1.body(), Epic.class);

        URI uriSubtask1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        Subtask subtask1 = new Subtask();
        subtask1.setEpicId(createdEpic1.getId());
        subtask1.setName("Subtask1");
        subtask1.setDescription("Subtask1 Description");
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.of(2010, 10, 12, 14, 0));

        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1), DEFAULT_CHARSET))
                .uri(uriSubtask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);

        Subtask createdSubtask1 = gson.fromJson(response2.body(), Subtask.class);


        HttpRequest.Builder requestBuilder3 = HttpRequest.newBuilder();
        Subtask subtask2 = new Subtask();
        subtask2.setEpicId(epic1.getId());
        subtask2.setName("Subtask2");
        subtask2.setDescription("Subtask2 Description");
        subtask2.setDuration(Duration.ofMinutes(10));
        subtask2.setStartTime(LocalDateTime.of(2008, 10, 12, 14, 0));

        HttpRequest request3 = requestBuilder3
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2), DEFAULT_CHARSET))
                .uri(uriSubtask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client3 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler3 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response3 = client2.send(request3, handler3);

        URI uriIdEpic = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + createdEpic1.getId());
        HttpRequest request4 = HttpRequest.newBuilder()
                .GET()
                .uri(uriIdEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client4 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler4 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response4 = client4.send(request4, handler4);
        List<Subtask> createdSubtasks = gson.fromJson(response4.body(), new TypeToken<>() {
        });
        int count = 0;
        for (Subtask createdSubtask : createdSubtasks) {
            if (count == 0) {
                assertEquals(subtask1.getName(), createdSubtask.getName(), "Неверное имя сабтаска.");
                assertEquals(subtask1.getDescription(), createdSubtask.getDescription(), "Неверное описание сабтаска.");
                assertEquals(subtask1.getDuration(), createdSubtask.getDuration(), "Неверная продолжительность сабтаска.");
                assertEquals(subtask1.getStartTime(), createdSubtask.getStartTime(), "Неверное стартовое время сабтаска.");
                assertEquals(subtask1.getEpicId(), createdSubtask.getEpicId(), "Неверное id эпика у  сабтаска.");
            } else if (count == 1) {
                assertEquals(subtask2.getName(), createdSubtask.getName(), "Неверное имя сабтаска.");
                assertEquals(subtask2.getDescription(), createdSubtask.getDescription(), "Неверное описание сабтаска.");
                assertEquals(subtask2.getDuration(), createdSubtask.getDuration(), "Неверная продолжительность сабтаска.");
                assertEquals(subtask2.getStartTime(), createdSubtask.getStartTime(), "Неверное стартовое время сабтаска.");
                assertEquals(subtask2.getEpicId(), createdSubtask.getEpicId(), "Неверное id эпика у  сабтаска.");
            }
            count++;
        }

        URI uriIdEpicAndSubtask = URI.create("http://localhost:8080/tasks/subtask/?epicId=" + createdEpic1.getId()
                + "&subtaskId=" + createdSubtask1.getId());

        HttpRequest request5 = HttpRequest.newBuilder()
                .GET()
                .uri(uriIdEpicAndSubtask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client5 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler5 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response5 = client5.send(request5, handler5);
        Subtask createdSubtask = gson.fromJson(response5.body(), Subtask.class);
        assertEquals(subtask1.getName(), createdSubtask.getName(), "Неверное имя сабтаска.");
        assertEquals(subtask1.getDescription(), createdSubtask.getDescription(), "Неверное описание сабтаска.");
        assertEquals(subtask1.getDuration(), createdSubtask.getDuration(), "Неверная продолжительность сабтаска.");
        assertEquals(subtask1.getStartTime(), createdSubtask.getStartTime(), "Неверное стартовое время сабтаска.");
        assertEquals(subtask1.getEpicId(), createdSubtask.getEpicId(), "Неверное id эпика у  сабтаска.");


        URI uriEpicWrongId = URI.create("http://localhost:8080/tasks/subtask/epic/?id=122331");
        HttpRequest request6 = HttpRequest.newBuilder()
                .GET()
                .uri(uriEpicWrongId)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client6 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler6 = HttpResponse.BodyHandlers.ofString();


        HttpResponse<String> response6 = client6.send(request6, handler6);
        int status = response6.statusCode();
        assertEquals(404, status, "Выдан несуществующий сабтаск");


        URI uriEpicWrongIdAndWrongIdSubtask = URI.create("http://localhost:8080/tasks/subtask/?epicId=76858"
                + "&subtaskId=98756796");
        HttpRequest request7 = HttpRequest.newBuilder()
                .GET()
                .uri(uriEpicWrongIdAndWrongIdSubtask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client7 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler7 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response7 = client7.send(request7, handler7);
        int status1 = response7.statusCode();
        assertEquals(404, status1, "Выдан несуществующий сабтаск");

    }

    @Test
    void deleteSubtaskWithEpicIdAndSubtaskId() throws IOException, InterruptedException {
        URI uriEpic = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder1 = HttpRequest.newBuilder();

        Epic epic1 = new Epic();
        epic1.setName("Epic1");
        epic1.setDescription("Epic1 Description");


        HttpRequest request1 = requestBuilder1
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1), DEFAULT_CHARSET))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        Epic createdEpic1 = gson.fromJson(response1.body(), Epic.class);

        URI uriSubtask1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        Subtask subtask1 = new Subtask();
        subtask1.setEpicId(createdEpic1.getId());
        subtask1.setName("Subtask1");
        subtask1.setDescription("Subtask1 Description");
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.of(2010, 10, 12, 14, 0));

        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1), DEFAULT_CHARSET))
                .uri(uriSubtask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response2 = client2.send(request2, handler2);

        HttpRequest.Builder requestBuilder3 = HttpRequest.newBuilder();
        Subtask subtask2 = new Subtask();
        subtask2.setEpicId(createdEpic1.getId());
        subtask2.setName("Subtask2");
        subtask2.setDescription("Subtask2 Description");
        subtask2.setDuration(Duration.ofMinutes(10));
        subtask2.setStartTime(LocalDateTime.of(2008, 10, 12, 14, 0));

        HttpRequest request3 = requestBuilder3
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2), DEFAULT_CHARSET))
                .uri(uriSubtask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client3 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler3 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response3 = client3.send(request3, handler3);
        Subtask createdSubtask2 = gson.fromJson(response3.body(), Subtask.class);

        URI uriIdEpic = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + createdEpic1.getId());
        HttpRequest request4 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriIdEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client4 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler4 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response4 = client4.send(request4, handler4);
        int status = response4.statusCode();
        assertEquals(404, status, "Выдан несуществующий сабтаск");

        URI uriIdEpicAndSubtask = URI.create("http://localhost:8080/tasks/subtask/?epicId=" + createdEpic1.getId()
                + "&subtaskId=" + createdSubtask2.getId());
        HttpRequest request5 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriIdEpicAndSubtask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client5 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler5 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response5 = client5.send(request5, handler5);

        HttpRequest request6 = HttpRequest.newBuilder()
                .GET()
                .uri(uriIdEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client6 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler6 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response6 = client6.send(request6, handler6);
        List<Subtask> createdSubtasks = gson.fromJson(response6.body(), new TypeToken<>() {
        });
        assertEquals(1, createdSubtasks.size(), "Произошло неверное удаление сабтасков.");
        for (Subtask createdSubtask : createdSubtasks) {
            assertEquals(subtask1.getName(), createdSubtask.getName(), "Неверное имя сабтаска.");
            assertEquals(subtask1.getDescription(), createdSubtask.getDescription(), "Неверное описание сабтаска.");
            assertEquals(subtask1.getDuration(), createdSubtask.getDuration(), "Неверная продолжительность сабтаска.");
            assertEquals(subtask1.getStartTime(), createdSubtask.getStartTime(), "Неверное стартовое время сабтаска.");
            assertEquals(subtask1.getEpicId(), createdSubtask.getEpicId(), "Неверное id эпика у сабтаска.");
        }


    }

    @Test
    void deleteAllSubtaskWithEpicIdTest() throws IOException, InterruptedException {
        URI uriEpic = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest.Builder requestBuilder1 = HttpRequest.newBuilder();

        Epic epic1 = new Epic();
        epic1.setName("Epic1");
        epic1.setDescription("Epic1 Description");


        HttpRequest request1 = requestBuilder1
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic1), DEFAULT_CHARSET))
                .uri(uriEpic)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        Epic createdEpic1 = gson.fromJson(response1.body(), Epic.class);

        URI uriSubtask1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();

        Subtask subtask1 = new Subtask();
        subtask1.setEpicId(epic1.getId());
        subtask1.setName("Subtask1");
        subtask1.setDescription("Subtask1 Description");
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.of(2010, 10, 12, 14, 0));

        HttpRequest request2 = requestBuilder2
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1), DEFAULT_CHARSET))
                .uri(uriSubtask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response2 = client2.send(request2, handler2);

        HttpRequest.Builder requestBuilder3 = HttpRequest.newBuilder();
        Subtask subtask2 = new Subtask();
        subtask2.setEpicId(epic1.getId());
        subtask2.setName("Subtask2");
        subtask2.setDescription("Subtask2 Description");
        subtask2.setDuration(Duration.ofMinutes(10));
        subtask2.setStartTime(LocalDateTime.of(2008, 10, 12, 14, 0));

        HttpRequest request3 = requestBuilder3
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2), DEFAULT_CHARSET))
                .uri(uriSubtask1)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "Application/json")
                .build();

        HttpClient client3 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler3 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response3 = client3.send(request3, handler3);
        Subtask createdSubtask2 = gson.fromJson(response3.body(), Subtask.class);


        URI uriWrongEpicId = URI.create("http://localhost:8080/tasks/subtask/?epicId=88776789");
        HttpRequest request4 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriWrongEpicId)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();


        HttpClient client4 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler4 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response4 = client4.send(request4, handler4);
        int status = response4.statusCode();
        assertEquals(404, status, "Произошло удаление сабтасков по несуществующему id эпика");

        URI uriEpicIdAndWrongSubtaskId = URI.create("http://localhost:8080/tasks/subtask/?epicId=" + createdEpic1.getId()
                + "&subtaskId=45968765");
        HttpRequest request5 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriEpicIdAndWrongSubtaskId)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();


        HttpClient client5 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler5 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response5 = client5.send(request5, handler5);
        int status1 = response5.statusCode();
        assertEquals(404, status1, "Произошло удаление сабтасков по несуществующему id эпика");

        URI uriIdEpicWithoutSubtask = URI.create("http://localhost:8080/tasks/subtask/?epicId=" + createdEpic1.getId());
        HttpRequest request6 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uriIdEpicWithoutSubtask)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();


        HttpClient client6 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler6 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response6 = client6.send(request6, handler6);
        int status2 = response6.statusCode();
        assertEquals(200, status2, "Не произошло удаление всех сабтасков");
    }
}
