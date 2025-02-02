package com.yandex.kanban.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.kanban.HttpTaskServer;
import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.model.Task;
import com.yandex.kanban.service.InMemoryTaskManager;
import com.yandex.kanban.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryTest {
    private HttpTaskServer taskServer;
    private HttpClient client;
    private HttpRequest request;
    private URI uri;
    private final String historyUri = "http://localhost:8080/history";
    private Gson gson;
    private TaskManager manager;

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        client = HttpClient.newHttpClient();
        taskServer.start();
    }

    @AfterEach
    public void stopServer() {
        taskServer.stop();
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        Task task = new Task("taskName", "description");
        manager.addTask(task);
        Epic epic = new Epic("epicName", "description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("subtaskName", "description", epic.getId());
        manager.addSubtasks(subtask, epic);
        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());
        uri = URI.create(historyUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        request = requestBuilder
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getInMemoryHistoryManager().getHistory()), response.body());
    }
}
