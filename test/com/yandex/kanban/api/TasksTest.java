package com.yandex.kanban.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.kanban.HttpTaskServer;
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

public class TasksTest {
    private HttpTaskServer taskServer;
    private HttpClient client;
    private HttpRequest request;
    private URI uri;
    private final String tasksUri = "http://localhost:8080/tasks";
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
    public void createTask() throws IOException, InterruptedException {
        Task task = new Task("testName", "testDescription");
        Task taskTime = new Task("testName1", "testDescription", Duration.ofMinutes(10)
                , LocalDateTime.now());
        uri = URI.create(tasksUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strTask = gson.toJson(task);
        String strTaskTime = gson.toJson(taskTime);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strTask))
                .build();
        Assertions.assertTrue(manager.getTasks().isEmpty());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(manager.getTasks().isEmpty());
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response1.statusCode());
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strTaskTime))
                .build();
        HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
        Assertions.assertEquals(2, manager.getTasks().size());
        HttpResponse<String> response3 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(406, response3.statusCode());
        Assertions.assertEquals(2, manager.getTasks().size());
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {
        Task task = new Task("testName", "testDescription");
        Task taskTime = new Task("testName1", "testDescription", Duration.ofMinutes(10)
                , LocalDateTime.now());
        uri = URI.create(tasksUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strTask = gson.toJson(task);
        String strTaskTime = gson.toJson(taskTime);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strTask))
                .build();
        HttpRequest getRequest = requestBuilder
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("[]", response.body());
        Assertions.assertTrue(manager.getTasks().isEmpty());
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response1 = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getTasks()), response1.body());
        Assertions.assertEquals(1, manager.getTasks().size());
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strTaskTime))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(gson.toJson(manager.getTasks()), response2.body());
        Assertions.assertEquals(2, manager.getTasks().size());
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        Task task = new Task("testName", "testDescription");
        uri = URI.create(tasksUri);
        URI getUri = URI.create(tasksUri + "/1");
        URI getUriError = URI.create(tasksUri + "/2");
        URI getUriError1 = URI.create(tasksUri + "/2test");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strTask = gson.toJson(task);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strTask))
                .build();
        HttpRequest getRequest = requestBuilder
                .uri(getUri)
                .GET()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(manager.getTaskById(1)), response.body());
        getRequest = requestBuilder
                .uri(getUriError)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response2.statusCode());
        getRequest = requestBuilder
                .uri(getUriError1)
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response3.statusCode());
    }

    @Test
    public void updateTask() throws IOException, InterruptedException {
        Task task = new Task("testName", "testDescription");
        Task taskTime = new Task("testName1", "testDescription", Duration.ofMinutes(10)
                , LocalDateTime.now());
        uri = URI.create(tasksUri);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strTask = gson.toJson(task);
        String strTaskTime = gson.toJson(taskTime);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strTask))
                .build();
        URI uri1 = URI.create(tasksUri + "/1");
        HttpRequest putRequest = requestBuilder
                .uri(uri1)
                .PUT(HttpRequest.BodyPublishers.ofString(strTaskTime))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Задача обновлена!", response.body());
        URI uri2 = URI.create(tasksUri + "/2");
        putRequest = requestBuilder
                .uri(uri2)
                .PUT(HttpRequest.BodyPublishers.ofString(strTaskTime))
                .build();
        HttpResponse<String> response1 = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response1.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Задача с id: 2 не существует!\"}}", response1.body());
        URI uri3 = URI.create(tasksUri + "/2test");
        putRequest = requestBuilder
                .uri(uri3)
                .PUT(HttpRequest.BodyPublishers.ofString(strTaskTime))
                .build();
        HttpResponse<String> response2 = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}", response2.body());
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("testName", "testDescription");
        uri = URI.create(tasksUri);
        URI deleteUri = URI.create(tasksUri + "/1");
        URI errUri = URI.create(tasksUri + "/2");
        URI errUri2 = URI.create(tasksUri + "/test");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String strTask = gson.toJson(task);
        request = requestBuilder
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(strTask))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest deleteRequest = requestBuilder
                .uri(deleteUri)
                .DELETE()
                .build();
        Assertions.assertEquals(1, manager.getTasks().size());
        HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("Задача удалена!", response.body());
        Assertions.assertTrue(manager.getTasks().isEmpty());
        deleteRequest = requestBuilder
                .uri(errUri)
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response2.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Задача с id: 2 не существует!\"}}", response2.body());
        deleteRequest = requestBuilder
                .uri(errUri2)
                .DELETE()
                .build();
        HttpResponse<String> response3 = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(400, response3.statusCode());
        Assertions.assertEquals("{\"error\":{\"message\":\"Некорректный запрос!\"}}", response3.body());

    }
}
