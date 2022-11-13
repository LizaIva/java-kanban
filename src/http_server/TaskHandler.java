package http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static http_server.HttpUtils.DEFAULT_CHARSET;
import static http_server.HttpUtils.gson;

public class TaskHandler implements HttpHandler {

    private final TaskManager manager;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        switch (method) {
            case "GET":
                httpExchange.getResponseHeaders().set("Content-Type", "application/json");
                if (uri.getQuery() == null) {
                    List<Task> tasks = manager.getTasks();
                    HttpUtils.writeResponse(httpExchange, tasks);
                } else if (uri.getQuery().contains("id")) {
                    String taskId = HttpUtils.findQueryParam(uri, "id");
                    if (taskId == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 400);
                        return;
                    }
                    Task task = manager.getTaskById(Integer.parseInt(taskId));
                    if (task == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 404);
                        return;
                    }

                    HttpUtils.writeResponse(httpExchange, task);
                } else {
                    HttpUtils.writeResponseWithStatus(httpExchange, 404);
                }
            case "POST":
                try (InputStreamReader is = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET)) {
                    Task newTask = gson.fromJson(is, Task.class);
                    Task task = manager.createTask(newTask.getName(), newTask.getDescription(), newTask.getDuration(), newTask.getStartTime());
                    HttpUtils.writeResponse(httpExchange, task);
                    return;
                }

            case "DELETE":
                httpExchange.getResponseHeaders().set("Content-Type", "application/json");
                if (uri.getQuery() == null) {
                    manager.removeAllTasks();
                    httpExchange.sendResponseHeaders(200, 0);
                    httpExchange.close();
                    return;
                } else if (uri.getQuery().contains("id")) {
                    String taskId = HttpUtils.findQueryParam(uri, "id");
                    if (taskId == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 400);
                        return;
                    }
                    if (manager.getTaskById(Integer.parseInt(taskId)) == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 404);
                        return;
                    }
                    manager.removeTaskById(Integer.parseInt(taskId));
                    httpExchange.sendResponseHeaders(200, 0);
                    httpExchange.close();
                } else {
                    HttpUtils.writeResponseWithStatus(httpExchange, 404);
                }
        }
    }
}