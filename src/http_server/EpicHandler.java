package http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static http_server.HttpUtils.DEFAULT_CHARSET;
import static http_server.HttpUtils.gson;

public class EpicHandler implements HttpHandler {
    private final TaskManager manager;
    public EpicHandler(TaskManager manager) {
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
                    List<Epic> epics = manager.getEpics();
                    HttpUtils.writeResponse(httpExchange, epics);
                } else if (uri.getQuery().contains("id")) {
                    String epicId = HttpUtils.findQueryParam(uri, "id");
                    if (epicId == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 400);
                        return;
                    }
                    Epic epic = manager.getEpicById(Integer.parseInt(epicId));
                    if (epic == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 404);
                        return;
                    }

                    HttpUtils.writeResponse(httpExchange, epic);
                } else {
                    HttpUtils.writeResponseWithStatus(httpExchange, 404);
                    return;
                }
            case "POST":
                try (InputStreamReader is = new InputStreamReader(httpExchange.getRequestBody(), DEFAULT_CHARSET)) {
                    Epic newEpic = gson.fromJson(is, Epic.class);
                    Epic epic = manager.createEpic(newEpic.getName(), newEpic.getDescription());
                    HttpUtils.writeResponse(httpExchange, epic);
                    return;
                }

            case "DELETE":
                httpExchange.getResponseHeaders().set("Content-Type", "application/json");
                if (uri.getQuery() == null) {
                    manager.removeAllEpics();
                    httpExchange.sendResponseHeaders(200, 0);
                    httpExchange.close();
                } else if (uri.getQuery().contains("id")) {
                    String epicId = HttpUtils.findQueryParam(uri, "id");
                    if (epicId == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 400);
                        return;
                    }
                    if (manager.getEpicById(Integer.parseInt(epicId)) == null) {
                        HttpUtils.writeResponseWithStatus(httpExchange, 404);
                        return;
                    }
                    manager.removeEpicById(Integer.parseInt(epicId));
                    httpExchange.sendResponseHeaders(200, 0);
                    httpExchange.close();
                } else {
                    HttpUtils.writeResponseWithStatus(httpExchange, 404);
                }

        }
    }
}