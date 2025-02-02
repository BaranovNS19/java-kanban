package com.yandex.kanban.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.yandex.kanban.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    protected TaskManager manager;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }


    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, 0);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendTextCreated(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(201, 0);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, 0);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected String generateErrorMessage(String text) {
        return "{\"error\":{\"message\":\"" + text + "\"}}";
    }

    protected int getIdFromUrl(URI uri) {
        String[] pathSplit = uri.getPath().split("/");
        if (pathSplit[2] != null && pathSplit[2].matches("\\d+")) {
            return Integer.parseInt(pathSplit[2]);
        }
        return 0;
    }

    protected TaskEndpoints getEndpoint(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        if ((method.equals("GET") && pathSplit.length == 2) && !pathSplit[1].equals("history")
                && !pathSplit[1].equals("prioritized")) {
            return TaskEndpoints.GET_TASKS;
        } else if (method.equals("GET") && pathSplit.length == 3) {
            return TaskEndpoints.GET_TASK_BY_ID;
        } else if (method.equals("POST") && pathSplit.length == 2) {
            return TaskEndpoints.POST_TASK;
        } else if (method.equals("PUT") && pathSplit.length == 3) {
            return TaskEndpoints.PUT_TASK;
        } else if (method.equals("DELETE") && pathSplit.length == 3) {
            return TaskEndpoints.DELETE_TASK_BY_ID;
        } else if (method.equals("GET") && pathSplit.length == 4 && pathSplit[3].equals("subtasks")) {
            return TaskEndpoints.GET_SUBTASKS_FOR_EPIC;
        } else if (method.equals("GET") && pathSplit[1].equals("history")) {
            return TaskEndpoints.GET_HISTORY;
        } else if (pathSplit[1].equals("prioritized") && method.equals("GET")) {
            return TaskEndpoints.GET_PRIORITIZED;
        } else {
            return TaskEndpoints.UNKNOWN;
        }
    }
}
