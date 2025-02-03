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

public class PrioritizedTest {
    private HttpTaskServer taskServer;
    private HttpClient client;
    private HttpRequest request;
    private URI uri;
    private final String epicsUri = "http://localhost:8080/epics";
    private final String subtasksUri = "http://localhost:8080/subtasks";
    private final String tasksUri = "http://localhost:8080/tasks";
    private final String prioritizedUri = "http://localhost:8080/prioritized";
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
    public void getPrioritized() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask2 = new Subtask("subtaskTest2", "descriptionTest", Duration.ofMinutes(10),
                LocalDateTime.now(), 1);
        String strSubtask = gson.toJson(subtask2);
        uri = URI.create(subtasksUri);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strSubtask))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskTime = new Task("testName1", "testDescription", Duration.ofMinutes(10)
                , LocalDateTime.now().plusMonths(1));
        uri = URI.create(tasksUri);
        String strTask = gson.toJson(taskTime);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strTask))
                .build();
        Assertions.assertTrue(manager.getTasks().isEmpty());
        client.send(request, HttpResponse.BodyHandlers.ofString());
        uri = URI.create(prioritizedUri);
        request = requestBuilder
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getPrioritizedTasks()), response.body());
    }
}
