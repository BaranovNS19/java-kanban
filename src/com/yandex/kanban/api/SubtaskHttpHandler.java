package com.yandex.kanban.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.kanban.model.Subtask;
import com.yandex.kanban.service.Check;
import com.yandex.kanban.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtaskHttpHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        int id;

        switch (getEndpoint(exchange)) {
            case GET_TASKS:
                sendText(exchange, gson.toJson(manager.getSubtasks()));
                System.out.println(manager.getSubtasks());
                break;
            case GET_TASK_BY_ID:
                id = getIdFromUrl(exchange.getRequestURI());
                if (id != 0) {
                    if (manager.getSubtaskById(id) != null) {
                        sendText(exchange, gson.toJson(manager.getSubtaskById(id)));
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Подзадача с id: " + id + " не существует!"));
                    }
                } else {
                    sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                }
                break;
            case POST_TASK:
                if (!requestBody.isEmpty()) {
                    Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                    if (manager.addSubtasks(subtask, manager.getEpicById(subtask.getEpicId())) != 0) {
                        sendTextCreated(exchange, gson.toJson(subtask));
                    } else {
                        if (!Check.checkStartTimeIntersection(manager.getCalendar(), subtask)) {
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
                    Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                    if (manager.updateSubtask(id, subtask, manager.getEpicById(subtask.getEpicId()))) {
                        sendText(exchange, "Подзадача обновлена!");
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Подзадача с id: " + id + " не существует!"));
                    }
                } else {
                    sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                }
                break;
            case DELETE_TASK_BY_ID:
                id = getIdFromUrl(exchange.getRequestURI());
                if (id != 0) {
                    if (manager.removeSubtasksById(id)) {
                        sendText(exchange, "Подзадача удалена!");
                    } else {
                        sendNotFound(exchange, generateErrorMessage("Подзадача с id: " + id + " не существует!"));
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

