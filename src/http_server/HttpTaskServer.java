package http_server;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager manager;

    public HttpTaskServer() throws IOException {
        manager = Managers.loadFromKVServer("http://localhost:8078");

        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks/task", new TaskHandler(manager));

        httpServer.createContext("/tasks/epic", new EpicHandler(manager));

        httpServer.createContext("/tasks/subtask", new SubtaskHandler(manager));

        httpServer.createContext("/tasks", new TasksHandler(manager));

        httpServer.createContext("/tasks/history", new HistoryHandler(manager));

    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }
}
