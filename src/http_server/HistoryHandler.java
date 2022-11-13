package http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HistoryHandler implements HttpHandler {

    private final TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();

        if (method.equals("GET")) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            List<Task> historyTasks = manager.getHistory();
            HttpUtils.writeResponse(httpExchange, historyTasks);
        } else {
            HttpUtils.writeResponseWithStatus(httpExchange, 404);
            httpExchange.close();
        }
    }
}
