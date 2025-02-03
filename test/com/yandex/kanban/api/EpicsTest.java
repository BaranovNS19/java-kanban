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

public class EpicsTest {
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
    public void createEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        Assertions.assertTrue(manager.getEpics().isEmpty());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(manager.getEpics().isEmpty());
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response1.statusCode());
    }

    @Test
    public void getAllEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        Assertions.assertTrue(manager.getEpics().isEmpty());
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest getRequest = requestBuilder
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getEpics()), response.body());
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        URI getUri = URI.create(epicsUri + "/1");
        URI errUri = URI.create(epicsUri + "/2");
        URI errUri2 = URI.create(epicsUri + "/test");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest getRequest = requestBuilder
                .GET()
                .uri(getUri)
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getEpicById(1)), response.body());
        getRequest = requestBuilder
                .GET()
                .uri(errUri)
                .build();
        HttpResponse<String> response2 = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Эпик с id: 2 не существует!\"}}", response2.body());
        getRequest = requestBuilder
                .GET()
                .uri(errUri2)
                .build();
        HttpResponse<String> response3 = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response3.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}", response3.body());
    }

    @Test
    public void deleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        URI deleteUri = URI.create(epicsUri + "/1");
        URI errUri = URI.create(epicsUri + "/2");
        URI errUri2 = URI.create(epicsUri + "/test");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest deleteRequest = requestBuilder
                .DELETE()
                .uri(errUri)
                .build();
        HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Эпик с id: 2 не существует!\"}}", response.body());
        deleteRequest = requestBuilder
                .DELETE()
                .uri(errUri2)
                .build();
        HttpResponse<String> response2 = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}", response2.body());
        deleteRequest = requestBuilder
                .DELETE()
                .uri(deleteUri)
                .build();
        HttpResponse<String> response3 = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response3.statusCode());
        Assertions.assertEquals("Эпик удален!", response3.body());
    }

    @Test
    public void updateEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        Epic updateEpic = new Epic("updateName", "updateDescription");
        uri = URI.create(epicsUri);
        URI updateUri = URI.create(epicsUri + "/1");
        URI errUri = URI.create(epicsUri + "/2");
        URI errUri2 = URI.create(epicsUri + "/test");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        String strUpdateEpic = gson.toJson(updateEpic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest updateRequest = requestBuilder
                .PUT(HttpRequest.BodyPublishers.ofString(strUpdateEpic))
                .uri(errUri)
                .build();
        HttpResponse<String> response = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Эпик с id: 2 не существует!\"}}", response.body());
        updateRequest = requestBuilder
                .PUT(HttpRequest.BodyPublishers.ofString(strUpdateEpic))
                .uri(errUri2)
                .build();
        HttpResponse<String> response2 = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}", response2.body());
        updateRequest = requestBuilder
                .PUT(HttpRequest.BodyPublishers.ofString(strUpdateEpic))
                .uri(updateUri)
                .build();
        HttpResponse<String> response3 = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response3.statusCode());
        Assertions.assertEquals("Эпик обновлен!", response3.body());
    }

    @Test
    public void getSubtasksByEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("testName", "testDescription");
        uri = URI.create(epicsUri);
        URI uriCreateSubtask = URI.create(subtasksUri);
        URI getSubtask = URI.create(epicsUri + "/1/subtasks");
        URI errUri = URI.create(epicsUri + "/2/subtasks");
        URI errUri2 = URI.create(epicsUri + "/test/subtasks");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strEpics = gson.toJson(epic);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strEpics))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = new Subtask("subtaskTest", "descriptionTest", 1);
        String strSubtask = gson.toJson(subtask);
        HttpRequest subtaskRequest = requestBuilder
                .uri(uriCreateSubtask)
                .POST(HttpRequest.BodyPublishers.ofString(strSubtask))
                .build();
        HttpResponse<String> response = client.send(subtaskRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        HttpRequest getSubtaskRequest = requestBuilder
                .uri(getSubtask)
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(getSubtaskRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getSubtaskByEpic(manager.getEpicById(1))), response1.body());
        getSubtaskRequest = requestBuilder
                .uri(errUri)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(getSubtaskRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Эпик с id: 2 не существует!\"}}", response2.body());
        getSubtaskRequest = requestBuilder
                .uri(errUri2)
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(getSubtaskRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response3.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}", response3.body());
    }
}
