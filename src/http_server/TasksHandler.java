package http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

public class TasksHandler implements HttpHandler {

    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();

        if (method.equals("GET")) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            Set<Task> allTasks = manager.getPrioritizedTasks();
            HttpUtils.writeResponse(httpExchange, allTasks);
        } else {
            HttpUtils.writeResponseWithStatus(httpExchange, 404);
            httpExchange.close();
        }
    }
}