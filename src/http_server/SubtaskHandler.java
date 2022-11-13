package http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static http_server.HttpUtils.DEFAULT_CHARSET;
import static http_server.HttpUtils.gson;

public class SubtaskHandler implements HttpHandler {

    private final TaskManager manager;

    public SubtaskHandler(TaskManager manager) {
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
                    HttpUtils.writeResponseWithStatus(httpExchange, 404);
                    return;
                    // /tasks/subtask/epic/?id=123412
                } else if (uri.getPath().contains("epic") && uri.getQuery().contains("id")) {
                    String epicId = HttpUtils.findQueryParam(uri, "id");
                    if (epicId == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 400);
                        return;
                    }
                    List<Subtask> subtasks = manager.getSubtasks(Integer.parseInt(epicId));

                    if (subtasks == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 404);
                        return;
                    }

                    HttpUtils.writeResponse(httpExchange, subtasks);

                } else if (uri.getQuery().contains("epicId") && uri.getQuery().contains("subtaskId")) {
                    String subtaskId = HttpUtils.findQueryParam(uri, "subtaskId");
                    String epicId = HttpUtils.findQueryParam(uri, "epicId");
                    if (epicId == null || subtaskId == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 400);
                        return;
                    }
                    Subtask subtask = manager.getSubtaskByIdEpic(Integer.parseInt(epicId), Integer.parseInt(subtaskId));
                    if (subtask == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 404);
                        return;
                    }
                    HttpUtils.writeResponse(httpExchange, subtask);
                } else {
                    HttpUtils.writeResponseWithStatus(httpExchange, 404);
                    return;
                }

            case "POST":
                try (InputStreamReader is = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET)) {
                    Subtask newSubtask = gson.fromJson(is, Subtask.class);
                    Subtask subtask = manager.createSubtask(newSubtask.getEpicId(), newSubtask.getName(), newSubtask.getDescription(), newSubtask.getDuration(), newSubtask.getStartTime());
                    HttpUtils.writeResponse(httpExchange, subtask);
                    return;
                }

            case "DELETE":
                httpExchange.getResponseHeaders().set("Content-Type", "application/json");
                if (uri.getQuery() == null) {
                    HttpUtils.writeResponseWithStatus(httpExchange, 404);
                } else if (uri.getQuery().contains("epicId") && !uri.getQuery().contains("subtaskId")) {
                    String epicId = HttpUtils.findQueryParam(uri, "epicId");
                    if (epicId == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 400);
                        return;
                    }
                    if (manager.getEpicById(Integer.parseInt(epicId)) == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 404);
                        return;
                    }
                    manager.removeAllEpicSubtasks(Integer.parseInt(epicId));
                    HttpUtils.writeResponseWithStatus(httpExchange, 200);
                } else if (uri.getQuery().contains("epicId") && uri.getQuery().contains("subtaskId")) {
                    String subtaskId = HttpUtils.findQueryParam(uri, "subtaskId");
                    String epicId = HttpUtils.findQueryParam(uri, "epicId");
                    if (epicId == null || subtaskId == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 400);
                        return;
                    }
                    if (manager.getSubtaskByIdEpic(Integer.parseInt(epicId), Integer.parseInt(subtaskId)) == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 404);
                        return;
                    }
                    manager.removeSubtaskByIdEpic(Integer.parseInt(epicId), Integer.parseInt(subtaskId));
                    HttpUtils.writeResponseWithStatus(httpExchange, 200);
                } else {
                    HttpUtils.writeResponseWithStatus(httpExchange, 404);
                }
        }

    }
}