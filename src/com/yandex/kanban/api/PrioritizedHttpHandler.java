package com.yandex.kanban.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.kanban.service.TaskManager;

import java.io.IOException;

public class PrioritizedHttpHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (getEndpoint(exchange)) {
            case GET_PRIORITIZED:
                sendText(exchange, gson.toJson(manager.getPrioritizedTasks()));
                break;
            case UNKNOWN:
                sendText(exchange, generateErrorMessage("Некорректный запрос!"), 400);
                break;
        }

    }
}

