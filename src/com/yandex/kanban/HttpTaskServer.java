package com.yandex.kanban;

import com.sun.net.httpserver.HttpServer;
import com.yandex.kanban.api.*;
import com.yandex.kanban.service.FileBackedTaskManager;
import com.yandex.kanban.service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TaskHttpHandler(manager));
        server.createContext("/epics", new EpicHttpHandler(manager));
        server.createContext("/subtasks", new SubtaskHttpHandler(manager));
        server.createContext("/history", new HistoryHttpHandler(manager));
        server.createContext("/prioritized", new PrioritizedHttpHandler(manager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(FileBackedTaskManager.loadFromFile(new File(
                "src/com/yandex/kanban/file/manager.CSV"
        )));
        httpTaskServer.start();
    }

    public void start() throws IOException {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
