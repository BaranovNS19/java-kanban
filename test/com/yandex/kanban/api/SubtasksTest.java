package com.yandex.kanban.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.kanban.HttpTaskServer;
import com.yandex.kanban.model.Epic;
import com.yandex.kanban.model.Subtask;
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

public class SubtasksTest {
    private HttpTaskServer taskServer;
    private HttpClient client;
    private HttpRequest request;
    private URI uri;
    private final String epicsUri = "http://localhost:8080/epics";
    private final String subtasksUri = "http://localhost:8080/subtasks";
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
    public void createSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("subtaskTest", "descriptionTest", 1);
        Subtask subtask2 = new Subtask("subtaskTest2", "descriptionTest", Duration.ofMinutes(10),
                LocalDateTime.now(), 1);
        String strSubtask = gson.toJson(subtask);
        String strSubtaskTime = gson.toJson(subtask2);
        uri = URI.create(subtasksUri);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strSubtask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(manager.getSubtasks().isEmpty());
        HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response2.statusCode());
        Assertions.assertEquals(1, manager.getSubtasks().size());
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strSubtaskTime))
                .build();
        HttpResponse<String> response3 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response3.statusCode());
        Assertions.assertEquals(2, manager.getSubtasks().size());
        HttpResponse<String> response4 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(406, response4.statusCode());
        Assertions.assertEquals(2, manager.getSubtasks().size());
    }

    @Test
    public void getAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("subtaskTest", "descriptionTest", 1);
        String strSubtask = gson.toJson(subtask);
        uri = URI.create(subtasksUri);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strSubtask))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = requestBuilder
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getSubtasks()), response.body());
    }

    @Test
    public void getSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("subtaskTest", "descriptionTest", 1);
        String strSubtask = gson.toJson(subtask);
        uri = URI.create(subtasksUri);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strSubtask))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        uri = URI.create(subtasksUri + "/2");
        request = requestBuilder
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getSubtaskById(2)), response.body());
        uri = URI.create(subtasksUri + "/3");
        request = requestBuilder
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Подзадача с id: 3 не существует!\"}}"
                , response2.body());
        uri = URI.create(subtasksUri + "/test");
        request = requestBuilder
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response3.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}"
                , response3.body());
    }

    @Test
    public void deleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("subtaskTest", "descriptionTest", 1);
        String strSubtask = gson.toJson(subtask);
        uri = URI.create(subtasksUri);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strSubtask))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        uri = URI.create(subtasksUri + "/3");
        request = requestBuilder
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Подзадача с id: 3 не существует!\"}}"
                , response.body());
        uri = URI.create(subtasksUri + "/test");
        request = requestBuilder
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}"
                , response2.body());
        uri = URI.create(subtasksUri + "/2");
        request = requestBuilder
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response3 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response3.statusCode());
        Assertions.assertEquals("Подзадача удалена!"
                , response3.body());
    }

    @Test
    public void updateSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("subtaskTest", "descriptionTest", 1);
        Subtask updateSubtask = new Subtask("updateTest", "updateDescription", 1);
        String strSubtask = gson.toJson(subtask);
        String strUpdateSubtask = gson.toJson(updateSubtask);
        uri = URI.create(subtasksUri);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strSubtask))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        uri = URI.create(subtasksUri + "/3");
        request = requestBuilder
                .uri(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(strUpdateSubtask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Подзадача с id: 3 не существует!\"}}"
                , response.body());
        uri = URI.create(subtasksUri + "/test");
        request = requestBuilder
                .uri(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(strUpdateSubtask))
                .build();
        HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}", response2.body());
        uri = URI.create(subtasksUri + "/2");
        request = requestBuilder
                .uri(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(strUpdateSubtask))
                .build();
        HttpResponse<String> response3 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response3.statusCode());
        Assertions.assertEquals("Подзадача обновлена!", response3.body());
    }
}
