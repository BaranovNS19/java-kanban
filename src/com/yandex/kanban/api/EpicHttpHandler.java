package com.yandex.kanban.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.kanban.model.Epic;
import com.yandex.kanban.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicHttpHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        int id;

        switch (getEndpoint(exchange)) {
            case GET_TASKS:
                sendText(exchange, gson.toJson(manager.getEpics()));
                System.out.println(manager.getEpics());
                break;
            case GET_TASK_BY_ID:
                id = getIdFromUrl(exchange.getRequestURI());
                if (id != 0) {
                    if (manager.getEpicById(id) != null) {
                        sendText(exchange, gson.toJson(manager.getEpicById(id)));
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Эпик с id: " + id + " не существует!"));
                    }
                } else {
                    sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                }
                break;
            case POST_TASK:
                if (!requestBody.isEmpty()) {
                    Epic epic = gson.fromJson(requestBody, Epic.class);
                    if (manager.addEpic(epic) != 0) {
                        sendTextCreated(exchange, gson.toJson(epic));
                    } else {
                        sendText(exchange, generateErrorMessage("Эпик с таким названием уже существует!"),
                                400);
                    }
                }
                break;
            case PUT_TASK:
                id = getIdFromUrl(exchange.getRequestURI());
                if ((id != 0) && (!requestBody.isEmpty())) {
                    if (manager.updateEpic(id, gson.fromJson(requestBody, Epic.class))) {
                        sendText(exchange, "Эпик обновлен!");
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Эпик с id: " + id + " не существует!"));
                    }
                } else {
                    sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                }
                break;
            case DELETE_TASK_BY_ID:
                id = getIdFromUrl(exchange.getRequestURI());
                if (id != 0) {
                    if (manager.removeEpicById(id)) {
                        sendText(exchange, "Эпик удален!");
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Эпик с id: " + id + " не существует!"));
                    }
                } else {
                    sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                }
                break;
            case GET_SUBTASKS_FOR_EPIC:
                System.out.println(exchange.getRequestURI());
                id = getIdFromUrl(exchange.getRequestURI());
                System.out.println(id);
                if (id != 0) {
                    if (manager.getEpicById(id) != null) {
                        sendText(exchange, gson.toJson(manager.getSubtaskByEpic(manager.getEpicById(id))));
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Эпик с id: " + id + " не существует!"));
                    }
                } else {
                    sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                }
                break;
            case UNKNOWN:
                sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);


        }
    }
}

