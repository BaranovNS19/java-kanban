package com.yandex.kanban.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.kanban.model.Task;
import com.yandex.kanban.service.Check;
import com.yandex.kanban.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHttpHandler extends BaseHttpHandler implements HttpHandler {


    public TaskHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        int id;

        switch (getEndpoint(exchange)) {
            case GET_TASKS:
                sendText(exchange, gson.toJson(manager.getTasks()));
                System.out.println(manager.getTasks());
                break;
            case GET_TASK_BY_ID:
                id = getIdFromUrl(exchange.getRequestURI());
                if (id != 0) {
                    if (manager.getTaskById(id) != null) {
                        sendText(exchange, gson.toJson(manager.getTaskById(id)));
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Задача с id: " + id + " не существует!"));
                    }
                } else {
                    sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                }
                break;
            case POST_TASK:
                if (!requestBody.isEmpty()) {
                    Task task = gson.fromJson(requestBody, Task.class);
                    if (manager.addTask(task) != 0) {
                        sendTextCreated(exchange, gson.toJson(task));
                    } else {
                        if (!Check.checkStartTimeIntersection(manager.getCalendar(), task)) {
                            sendHasInteractions(exchange, generateErrorMessage("Задачи пересекаются по времени!"));
                        } else {
                            sendText(exchange, generateErrorMessage("Задача с таким названием уже существует!"),
                                    400);
                        }
                    }

                }
                break;
            case PUT_TASK:
                id = getIdFromUrl(exchange.getRequestURI());
                if ((id != 0) && (!requestBody.isEmpty())) {
                    if (manager.updateTask(id, gson.fromJson(requestBody, Task.class))) {
                        sendText(exchange, "Задача обновлена!");
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Задача с id: " + id + " не существует!"));
                    }
                } else {
                    sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                }
                break;
            case DELETE_TASK_BY_ID:
                id = getIdFromUrl(exchange.getRequestURI());
                if (id != 0) {
                    if (manager.removeTaskById(id)) {
                        sendText(exchange, "Задача удалена!");
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Задача с id: " + id + " не существует!"));
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

